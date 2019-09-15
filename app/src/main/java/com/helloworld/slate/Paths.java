package com.helloworld.slate;

import android.graphics.Path;

public class Paths {

    public int color;
    public int strokeWidth;
    public Path path;

    public Paths(int color,int strokeWidth, Path path) {
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.path = path;
    }
}
