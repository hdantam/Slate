package com.helloworld.slate;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains an arraylist of points detailing a full drawn path. The points of a segment
 * are stored on Firestore.
 */

public class Segment {
    List<Point> points = new ArrayList<>();

    public void addPoint(int x, int y){
        points.add(new Point(x, y));
    }

    public List<Point> getPoints(){
        return points;
    }
}

