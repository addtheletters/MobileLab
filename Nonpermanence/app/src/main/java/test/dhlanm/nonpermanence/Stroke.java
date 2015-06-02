package test.dhlanm.nonpermanence;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by Ben on 6/2/2015.
 */
public class Stroke {
    boolean black; // true = black, false = white
    int size;

    ArrayList<Point> points;

    public Stroke(boolean black, int s){
        this(black, s, new ArrayList<Point>());
    }

    public Stroke(boolean black, int s, ArrayList<Point> pts){
        this.black = black;
        this.size = s;
        this.points = pts;
    }

    public void addPoint(Point p){
        points.add(p);
    }

    

}
