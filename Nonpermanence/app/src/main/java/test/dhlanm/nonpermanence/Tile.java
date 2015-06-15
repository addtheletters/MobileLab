package test.dhlanm.nonpermanence;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 6/11/2015.
 */
public class Tile {
    private List<Stroke> strokes;
    private Point pos;

    public Tile(Point pos, List<Stroke> strokes){
        this.strokes = strokes;
        this.pos = pos;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public void setStrokes(List<Stroke> strokes) {
        this.strokes = strokes;
    }

    public Point getPos() {
        return pos;
    }

    public void setPos(Point pos) {
        this.pos = pos;
    }


}
