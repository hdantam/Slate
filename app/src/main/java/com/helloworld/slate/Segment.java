package com.helloworld.slate;

import java.util.ArrayList;
import java.util.List;

public class Segment {
    List<Point> points = new ArrayList<>();

    public void addPoint(int x, int y){
        points.add(new Point(x, y));
    }

    public List<Point> getPoints(){
        return points;
    }
}

