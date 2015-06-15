package test.dhlanm.nonpermanence;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;

import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity{

    private static final String FIREBASE_URL = "https://shining-torch-306.firebaseio.com/nonpermanence";

    private ImageButton paintSelectBtn, brushSelectBtn;
    private static DrawView drawView;
    private float smallBrush, mediumBrush, largeBrush;
    private ImageButton currPaint;

    private FirebaseInterfacer fbi;

    private Activity selfReference(){
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // Firebase.setAndroidContext(this);

        if(savedInstanceState == null){
            // stuff in here doesn't happen on rotate
            fbi = new FirebaseInterfacer(FIREBASE_URL, this);

            smallBrush = getResources().getInteger(R.integer.small_size);
            mediumBrush = getResources().getInteger(R.integer.medium_size);
            largeBrush = getResources().getInteger(R.integer.large_size);
        }
        else{

        }

        brushSelectBtn = (ImageButton)findViewById(R.id.brush);
        brushSelectBtn.setOnClickListener(new BrushSizeDialogListener());

        drawView = (DrawView) findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        // need to test
        (findViewById(R.id.save)).setOnClickListener(testFBSave());
        (findViewById(R.id.TESTING_retrieve)).setOnClickListener(testFBRetrieve());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public View.OnClickListener getBrushSizeListener(final float size, final Dialog brushDialog){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(size);
                //drawView.setLastBrushSize(size);
                brushDialog.dismiss();
            }
        };
    }

    public View.OnClickListener testFBRetrieve(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fbi.retrieveTile(1, 1, 1);

            }
        };
    }

    public static void testSetStrokes(DataSnapshot dataSnapshot) {
        //Log.i("SEG", "testSetStrokes");
        drawView.setStrokes((List<Map<String, String>>) dataSnapshot.getValue());
    }

    public View.OnClickListener testFBSave(){
        return new View.OnClickListener(){
            @Override
            public void onClick(View v){
                fbi.saveTile(new Tile(new Point(1, 1), drawView.getStrokes()));
            }
        };
    }

    // lol for some reason doing a listener differently from the listeners for selecting a brush size
    private class BrushSizeDialogListener implements View.OnClickListener{
        public void onClick(View view){
            if(view.getId()==R.id.brush){
                final Dialog brushDialog = new Dialog(selfReference());
                brushDialog.setTitle("Brush size:");
                brushDialog.setContentView(R.layout.brush_chooser);

                ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(getBrushSizeListener(smallBrush, brushDialog));

                ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(getBrushSizeListener(mediumBrush, brushDialog));

                ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(getBrushSizeListener(largeBrush, brushDialog));

                brushDialog.show();
            }
        }
    }

    public void paintClicked(View view){
        if(view!=currPaint){
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton)view;
        }
    }


}
