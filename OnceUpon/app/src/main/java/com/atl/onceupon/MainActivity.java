package com.atl.onceupon;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText editText;
    private Toast currentToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addKeyListener();

    }

    public void addKeyListener() {

        // get edittext component
        editText = (EditText) findViewById(R.id.edit_message);
        // add a keylistener to keep track user input
        editText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                // if keydown and "enter" is pressed
                if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // display a floating message
                    Toast.makeText(MainActivity.this,
                            editText.getText(), Toast.LENGTH_LONG).show();
                    return true;

                } else if ((event.getAction() == KeyEvent.ACTION_DOWN)
                        && (keyCode != KeyEvent.KEYCODE_ENTER)) {

                    // display a floating message
                    //showAToast("wow");
                    return true;
                }

                return false;
            }
        });
    }

    public void showAToast (String st){ //"Toast toast" is declared in the class
        try{ currentToast.getView().isShown();     // true if visible
            currentToast.setText(st);
        } catch (Exception e) {         // invisible if exception
            currentToast= Toast.makeText(MainActivity.this, st, Toast.LENGTH_LONG);
        }
        currentToast.show();  //finally display it
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
}
