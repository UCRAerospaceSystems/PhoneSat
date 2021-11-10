package com.example.messingaround;

public class Vector3 {

    private float x;
    private float y;
    private float z;

    Vector3(){
        x = 0;
        y = 0;
        z = 0;
    }

    Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3(float[] values){
        this.set(values);
    }
    //Accessors
    float getX(){
        return this.x;
    }

    float getY(){
        return this.y;
    }

    float getZ(){
        return this.z;
    }

    float[] getRoundedVector(int accuracy){
        double scalar = accuracy * 10;
        float nX = (float) (Math.floor(this.x * scalar) / scalar);
        float nY = (float) (Math.floor(this.y * scalar) / scalar);
        float nZ = (float) (Math.floor(this.z * scalar) / scalar);

        return new float[]{nX, nY, nZ};
    }

    //Mutators
    void set(float[] values) {
        this.x = values[0];
        this.y = values[1];
        this.z = values[2];
    }

    void set(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    void add(float[] values){
        this.x += values[0];
        this.y += values[1];
        this.z += values[2];
    }

    void add(Vector3 vector){
        this.x += vector.getX();
        this.y += vector.getY();
        this.z += vector.getZ();
    }

    void add(float x, float y, float z){
        this.x += x;
        this.y += y;
        this.z += z;
    }

    void multiply(float scalar){
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    Vector3 divideUnchanged(float scalar){

        return new Vector3(
            this.x / scalar,
            this.y / scalar,
            this.z / scalar
        );
    }

    //Methods
    float dot(Vector3 rhs){
        return (this.x * rhs.x) + (this.y * rhs.y) + (this.z * rhs.z);
    }

    Vector3 cross(Vector3 rhs){ //Note that I am not entirely sure if this is correct
        float x = (this.y * rhs.z - this.z * rhs.y); //ad - bc
        float y = (this.x * rhs.z - this.z * rhs.x);
        float z = (this.x * rhs.y - this.y * rhs.x);

        return new Vector3(x, y, z);
    }
}
