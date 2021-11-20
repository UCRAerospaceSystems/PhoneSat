package com.example.messingaround;

import android.app.Service;
import android.content.Context;
import android.hardware.usb.*;
import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class USBCommunication {
    private boolean heartbeatRunning = false;

    private UsbDevice obc_Found = null;
    private UsbDeviceConnection communicationLine = null;
    private UsbInterface usbInterface;
    public UsbEndpoint endpointIN, endpointOUT;

    /*Note: This sucks, but I'll have to hard code some unintuitive format for sending data through. Here's the format:

    byte[] data = {
        1 for sensor data, 0 for heartbeat
        0/1 averaged or not,
        Linear Accel.x, Linear Accel.y, Linear Accel.Z,
        Rotational x, y, z,
        Magnetic x, y, z,
        Barometer pressure,
    }

    Also need to find a spot for positional data. What would the obc need in terms of data? Lat. and Long. ? Or like speed? Things to ask, when it's not 4 AM

    */

    USBCommunication(Service container){
        UsbManager connectionMgr = (UsbManager) container.getSystemService(Context.USB_SERVICE);
        obc_Found = connectionMgr.getDeviceList().get("MSP"); //TODO: Figure out the name
        GetInterface(obc_Found);
        GetEndpoint(obc_Found);
        StartUSB(container);
    }

    public void beginHeartbeat(){
        if (!heartbeatRunning){
            Thread heartbeat = new Thread(heartbeatLoop);
            heartbeat.start();
        }
    }

    public void sendSensorData(boolean averaged, HashMap<String, Vector3> vectorInfo, Location position, float pressure) {
        byte[] data = {
                1,
                (byte) (averaged ? 1 : 0)
        };

        addFloatToByteArr(data, vectorInfo.get("Linear").getX());
        addFloatToByteArr(data, vectorInfo.get("Linear").getY());
        addFloatToByteArr(data, vectorInfo.get("Linear").getZ());

        addFloatToByteArr(data, vectorInfo.get("Rotational").getX());
        addFloatToByteArr(data, vectorInfo.get("Rotational").getY());
        addFloatToByteArr(data, vectorInfo.get("Rotational").getZ());

        addFloatToByteArr(data, vectorInfo.get("Magnetic").getX());
        addFloatToByteArr(data, vectorInfo.get("Magnetic").getY());
        addFloatToByteArr(data, vectorInfo.get("Magnetic").getZ());

        addFloatToByteArr(data, pressure);
        //TODO: Add position into this.... somehow

        communicationLine.bulkTransfer(endpointOUT, data, data.length, 1000);
    }

    //Convert Float values into Bytes
    private byte[] addFloatToByteArr(byte[] orig, float value) {
        int intBits =  Float.floatToIntBits(value);
        byte[] representation = { (byte) (intBits >> 24), (byte) (intBits >> 16), (byte) (intBits >> 8), (byte) (intBits) };

        int start = orig.length;
        orig = Arrays.copyOf(orig, start + representation.length); //btw, kinda bad practice to edit the input parameters.... but screw it lmao
        for (int i = start; i < orig.length; i++){
            orig[i] = representation[i - start];
        }

        return orig;
    }

    private final Runnable heartbeatLoop = new Runnable() {
        @Override
        public void run() {
            heartbeatRunning = true;

            while(obc_Found != null && communicationLine != null){
                //Wait, pause the heart
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }

                //Beat the heart
                byte[] str = {0};
                communicationLine.bulkTransfer(endpointOUT, str, str.length, 500);
            }

            heartbeatRunning = false;
        }
    };

    //As copied from Stack Overflow lmao (https://stackoverflow.com/questions/21808223/send-data-through-usb-from-android-app-to-pc/24290009)
    private void GetInterface(UsbDevice d) {
        //<String> listInterface = new ArrayList<String>();
        ArrayList<UsbInterface> listUsbInterface = new ArrayList<UsbInterface>();
        for(int i=0; i<d.getInterfaceCount(); i++){
            UsbInterface usbif = d.getInterface(i);
            //listInterface.add(usbif.toString());
            listUsbInterface.add(usbif);
        }

        if(d.getInterfaceCount() > 0)
        {
            usbInterface = listUsbInterface.get(1);
        }
        else usbInterface = null;
    }

    private void GetEndpoint(UsbDevice d) {
        int EndpointCount = usbInterface.getEndpointCount();
        /*ArrayList<String> listEndPoint = new ArrayList<String>();
        ArrayList<UsbEndpoint> listUsbEndpoint = new ArrayList<UsbEndpoint>();

        for(int i=0; i<usbInterface.getEndpointCount(); i++) {
            UsbEndpoint usbEP = usbInterface.getEndpoint(i);
            listEndPoint.add(usbEP.toString());
            listUsbEndpoint.add(usbEP);
        }*/

        if(EndpointCount > 0) {
            endpointIN = usbInterface.getEndpoint(0);
            endpointOUT = usbInterface.getEndpoint(1);
        }
        else {
            endpointIN = null;
            endpointOUT = null;
        }
    }

    private boolean StartUSB(Service container) {
        boolean result = false;
        UsbDevice deviceToRead = obc_Found;
        UsbManager manager = (UsbManager) container.getSystemService(Context.USB_SERVICE);

        boolean permitToRead = manager.hasPermission(deviceToRead);

        if(permitToRead) {
            result = OpenDevice(container, deviceToRead);
        }

        return result;
    }

    private boolean OpenDevice(Service container, UsbDevice device){

        boolean forceClaim = true;

        UsbManager manager = (UsbManager) container.getSystemService(Context.USB_SERVICE);
        communicationLine = manager.openDevice(device);

        if(communicationLine != null){
            communicationLine.claimInterface(usbInterface, forceClaim);
            return true;
        }

        return false;
    }
}
