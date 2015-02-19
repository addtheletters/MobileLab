package com.atl.onceupon;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private Calendar calTarget;
    private JSONObject current;

    private Calendar baseCalendar;

    private int recordIndex;
    private JSONObject records;

    private ArrayList<Integer> seenTargets;

    private Toast currentToast;
    private Button btnSubmit;
    private TextView viewQuestion;
    private TextView viewFeedback1;
    private TextView viewFeedback2;
    private TextView viewFeedback3;
    private DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            records = new JSONObject(loadRawResource(R.raw.history));
        }
        catch(JSONException e){
            System.out.println("failed to initialize json, " + e);
        }

        calTarget = new GregorianCalendar();

        baseCalendar = new GregorianCalendar();

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this);

        viewQuestion = (TextView) findViewById(R.id.view_question);
        viewFeedback1 = (TextView) findViewById(R.id.view_feedback1);
        viewFeedback2 = (TextView) findViewById(R.id.view_feedback2);
        viewFeedback3 = (TextView) findViewById(R.id.view_feedback3);

        picker = (DatePicker) findViewById(R.id.edit_date);

        this.picker.init(baseCalendar.get(Calendar.YEAR), baseCalendar.get(Calendar.MONTH), baseCalendar.get(Calendar.DAY_OF_MONTH)-1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
            }
        });
       // picker.setMinDate(Long.MIN_VALUE);

        try {
            newTarget();
        }
        catch (JSONException e){
            System.out.println("failed to interpret json, " + e);
        }
        //showQuestion();

    }

    protected JSONObject getCurrentEvent() throws JSONException{
        return records.getJSONArray("events").getJSONObject(recordIndex);
    }

    public void newTarget() throws JSONException{
        recordIndex = newTargetIndex();
        seenTargets.add(recordIndex);

        current = getCurrentEvent();
        showQuestion(current.getString("name"));
        setCalendarTo(current.getJSONObject("date"));
        //calTarget.set(1, 1, 1);
    }

    protected void setCalendarTo(JSONObject json_date) throws JSONException{
        calTarget.set( json_date.getInt("year"), json_date.getInt("month"), json_date.getInt("day") );
    }

    protected int newTargetIndex() throws JSONException{
        if(seenTargets == null){
            seenTargets = new ArrayList<Integer>();
        }
        int totalEvents = records.getJSONArray("events").length();
        if(seenTargets.size() >= totalEvents){
            seenTargets.clear();
        }
        ArrayList<Integer> possible = new ArrayList<Integer>();
        for(int i = 0; i < totalEvents; i++){
            if(!seenTargets.contains(i)){
                possible.add(i);
            }
        }
        return possible.get(((int)Math.random() * possible.size()));
    }

    public void showQuestion(String question){
        try{
            viewQuestion.setText(question);
        }catch (Exception e){
            System.out.println("failed to show question");
        }
    }

    public void showFeedback(String feedback1, String feedback2, String feedback3){
        try{
            viewFeedback1.setText(feedback1);
            viewFeedback2.setText(feedback2);
            viewFeedback3.setText(feedback3);
        }catch (Exception e){
            System.out.println("failed to show feedback");
        }
    }

    public void showAToast(String st) { //"Toast toast" is declared in the class
        try {
            currentToast.getView().isShown();     // true if visible
            currentToast.setText(st);
        } catch (Exception e) {         // invisible if exception
            currentToast = Toast.makeText(MainActivity.this, st, Toast.LENGTH_LONG);
            //System.out.println("toast made.");
        }
        currentToast.show();  //finally display it
    }

    public String loadRawResource(int id){
        InputStream is = getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            is.close();
        }catch (Exception e){
            System.out.println("loadRawResource: Something went wrong.");
        }
        return writer.toString();
    }

    protected GregorianCalendar getPickedDate(){
        return new GregorianCalendar( picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
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

    @Override
    public void onClick(View v) {
        System.out.println("button pushed");
        Calendar calPicked = getPickedDate();
        int far_off = Math.abs((int) dayDifference(calPicked, calTarget));
        showAToast(far_off + " days off");
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("MMM dd, yyyy");
            showFeedback(current.getString("name"), dateformat.format(calTarget.getTime()),  current.getString("detail"));
            newTarget();
        }
        catch(JSONException e){
            System.out.println("Failed to interpret json.");
        }
    }

    public long dayDifference(Calendar day1, Calendar day2){
        return (day1.getTimeInMillis() - day2.getTimeInMillis()) / (24 * 60 * 60 * 1000);
    }
}
