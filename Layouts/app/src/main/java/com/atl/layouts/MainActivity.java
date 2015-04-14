package com.atl.layouts;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.atl.layouts.util.RawResourceUtil;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, StoryViewerFragment.OnFragmentInteractionListener, PageSelectDialogFragment.PageSelectDialogListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Fragment showing the stories.
     */
    private StoryViewerFragment viewerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    public JSONObject allStoryData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("oncreate called");

        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        //allStoryData = mNavigationDrawerFragment.frag_storyData; // fragment loads it

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        System.out.println("allStoryData is " + allStoryData);

        if(allStoryData == null){
            allStoryData = new JSONObject();
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        System.out.println("nav drawer selected");

        System.out.println("position is " + position);

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        JSONObject story;
        try{
            story = getStoryJSON(position);
        }
        catch(JSONException e){
            System.out.println("Failed to get json for story at " + position + "... " + e);
            story = new JSONObject();
        }

        viewerFragment = StoryViewerFragment.newInstance(story, position);

        fragmentManager.beginTransaction()
                .replace(R.id.container, viewerFragment)
                .commit();
    }

    public void onSectionAttached(int number) {
        try{
            mTitle = getStoryJSON(number).getString("title");
            //System.out.println("ADKHFLJDAFHLDAJFLDHFK");
        }catch(JSONException e){
            System.out.println("onSectionAttach, json obj get failed " + e);
            mTitle = "Unknown Title";
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public JSONObject getStoryJSON(int section) throws JSONException{
        //System.out.println("they say it is null:" + allStoryData);
        if(allStoryData == null){
            System.out.println("All story data is null. Blank object returned by getStoryJSON.");
            return new JSONObject();
        }
        return allStoryData.getJSONArray("stories").getJSONObject(section);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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


    public void onFragmentInteraction(Uri uri){
        return;
    }


    @Override
    public void onPageSelected(int page) {
        viewerFragment.changePageTo(page);
    }

    @Override
    public boolean validatePage(int page) {
        return viewerFragment.pageNumberValid(page);
    }
}
