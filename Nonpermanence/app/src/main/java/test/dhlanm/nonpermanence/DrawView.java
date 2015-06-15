package test.dhlanm.nonpermanence;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dhlanm on 6/3/2015.
 */
public class DrawView extends View{
    private Paint currentPaint = new Paint();
    private Path currentPath = new Path();
    private int paintColor;
    private float brushSize;//, lastBrushSize;

   // private Bitmap canvasBitmap;

//    private DrawView drawView;

    private ArrayList<Stroke> strokes = new ArrayList<Stroke>();


    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        resetBrush();

    }

    public ArrayList<Stroke> getStrokes(){
        return strokes;
    }

    public void setStrokes(List<Map<String, String>> fbstrokes) {
        //Log.i("SEG", "setStrokes");
        Log.i("SEG", strokes.toString());
        ArrayList<Stroke> lst = new ArrayList<>();
        for(int i = 0; i < fbstrokes.size(); i++){
            lst.add( fromFBStroke(fbstrokes.get(i)) );
        }
        strokes=lst;
        Log.i("SEG", strokes.toString());
        //Log.i("SEG", "wat");
        invalidate();
    }

    public static Stroke fromFBStroke(Map<String, String> fbstroke){
        Gson gson = new Gson();
        Path    tpa = gson.fromJson(fbstroke.get("path"),   Path.class);
        Paint   tpt = gson.fromJson(fbstroke.get("paint"),  Paint.class);
        return new Stroke(tpa, tpt);
    }

    public void resetBrush(){
        currentPaint.setAntiAlias(true);
        //currentPaint.setStrokeWidth(5f);
        currentPaint.setColor(Color.BLACK);
        currentPaint.setStyle(Paint.Style.STROKE);
        currentPaint.setStrokeJoin(Paint.Join.ROUND);
        currentPaint.setStrokeCap(Paint.Cap.ROUND);
        setBrushSize(20);
    }

    public void setBrushSize(float newSize){
        // this function's adjustment makes the brush smaller than if setStrokeWidth were called with the same param
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize = pixelAmount;
        currentPaint.setStrokeWidth(brushSize);
    }
    /*
    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }
    public float getLastBrushSize(){
        return lastBrushSize;
    }
    */

    public void setColor(String newColor){
        invalidate();
        paintColor = Color.parseColor(newColor);
        currentPaint.setColor(paintColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        //canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        // might need something here
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // this is currently very slow once a large number of paths exist.
        // the canvas saves strokes into a bitmap image
        // so it should be unnecessary.
        for(int i = 0; i < strokes.size(); i++){
            // TODO test removing this line
            canvas.drawPath(strokes.get(i).getPath(), strokes.get(i).getPaint());
        }

        canvas.drawPath(currentPath, currentPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set a new starting point
                currentPath.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                // Connect the points
                currentPath.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:
                // End stroke
                strokes.add(new Stroke(currentPath, new Paint(currentPaint))); // store stroke
                currentPath = new Path();
            default:
                return false;
        }

        invalidate();
        return true;
    }

}
