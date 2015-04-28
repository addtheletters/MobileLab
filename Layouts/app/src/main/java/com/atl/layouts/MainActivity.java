package com.atl.layouts;

import android.app.Activity;
import android.app.FragmentTransaction;
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
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.Map;


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


    public boolean loaded = false;
    public boolean refreshRejected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("oncreate called");

        try {
            allStoryData = loadStoryData(false);
        }
        catch(Exception e){
            System.out.println("failed to initialize json, " + e);
        }

        Firebase.setAndroidContext(this);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        //allStoryData = mNavigationDrawerFragment.frag_storyData; // fragment loads it

        mTitle = getTitle();


        // Set up the drawer.
        /*  NavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), this);
*/

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

    public void refreshNavDrawer(){
        // replace existing nav drawer with new one based on changes to allStoryData
        NavigationDrawerFragment newNav = NavigationDrawerFragment.newInstance(allStoryData);
        android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.navigation_drawer, newNav);
        trans.addToBackStack(null);
        trans.commit();

        // Set up the drawer.
        newNav.setUp(
                newNav.getId(),
                (DrawerLayout) findViewById(R.id.drawer_layout), this);

        mNavigationDrawerFragment = newNav;
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

    private JSONObject loadStoryJSON() {
        JSONObject ret = new JSONObject();
        try {
            ret = new JSONObject(RawResourceUtil.loadRawResource(this, R.raw.stories));

        } catch (Exception e) {
            System.out.println("Load failed. " + e);
        }
        return ret;
    }

    private JSONObject loadStoryData(boolean useFirebase) throws JSONException, IOException{
        if(useFirebase){
            Firebase ref = new Firebase(getString(R.string.firebase_url));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    allStoryData = new JSONObject((Map<String, Object>)snapshot.getValue());
                    refreshNavDrawer();
                    if(!loaded){
                        loaded = true;
                    }
                    System.out.println("Retrieved data is " + allStoryData);
                }

                @Override
                public void onCancelled(FirebaseError error) {
                    System.out.println("Firebase read failed " + error.getMessage());
                }
            });
            return new JSONObject(RawResourceUtil.loadRawResource(this, R.raw.placeholder));
        }
        else{
            loaded = true;
            return new JSONObject(RawResourceUtil.loadRawResource(this, R.raw.stories));
        }
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
