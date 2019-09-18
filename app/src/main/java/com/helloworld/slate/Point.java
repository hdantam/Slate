package com.helloworld.slate;

/**
 * Holds the x and y values of a drawing path. This allows for other connected phones to emulate
 * the new line.
 */

public class Point {
    private float x;
    private float y;

    Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }
}
