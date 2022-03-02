# The Computer Systems

The computer systems of the satellite consist of two parts.
The On Board Computer (OBC) which interfaces between the Android Phone and the rest of the Satellite, and the Android Phone which will serve as the Inertial Measurement Unit (IMU).

---

## The OBC
The OBC will handle the processing of radio signals to and from the home base as well as send data across the Radio Waves.

The OBC consists of:
1. USB Connection to the Phone
2. Radio Signal Module
   a. Packetizer
3. Heartbeat functionality

The USB Connection will allow the OBC to interface with the Android Phone, getting important IMU data to maneuver the satellite and any images the phone takes.
The Radio Signal Module will interface with the two antennas on R'Satellite and send data on regular pulses (as well as receive data).
The packetizer module will transform any received data into bytes that can be sent to the Phone and processed there.
The packetizer will also take data from the Phone and transform them into a packet in order to send more than engineering data across the waves.
The Heartbeat function is there to prevent any errors that could shut the Phone or Application on the Phone off.
Every couple of seconds, the function will send a pulse through the USB to ensure the application is properly running.
The phone needs to respond with a pulse back, otherwise after a set amount of time the Phone will be restarted by the OBC (restart not yet designed though).

---

## The Android Phone
The Android Phone will serve as the IMU of the entire Satellite and the only method to take pictures while in space.

The Android Phone Application will consist of:
1. Sensor Module
   a. Vector class
2. Camera Module
   a. Image Processor
   i. Image Cropping
   ii. Color Formatting
   b. Image Analyzer
   i. Image Compression
3. USB Connection to the OBC
4. Auto-start/Auto-reboot function

The Sensor Module is the section of the Application which will snoop through all the sensors of the Phone and return Linear/Rotational Velocity, Magnetometer data, and GPS information.
The Sensor Module must, of course, include a Vector class to manage the information measured by the sensors on the phone.
The Camera Module consists of itself, to take the picture, and then an Image Processor to format the image taken and ensure that the image/video is not too large.
Then the Image Analyzer will compress this image/video and send it through the USB connection to be packetized and sent back to Earth.
The USB Connection will simply connect the Android Phone to the OBC.
The Auto-start/Auto-reboot function will ensure the program automatically starts when the phone is restarted whether by itself or by the OBC.
