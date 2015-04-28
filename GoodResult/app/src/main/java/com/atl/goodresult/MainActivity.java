package com.atl.goodresult;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements PickSubredditDialogFragment.PickSubredditDialogListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment("neutralpolitics");
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

        if(id == R.id.pick_subreddit){
            DialogFragment pick_sr_dialog = new PickSubredditDialogFragment();
            pick_sr_dialog.show(this.getSupportFragmentManager(), "PickSubredditDialogFragment");
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void addFragment(String subreddit){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_holder,
                        PostsFragment.newInstance(subreddit))
                .commit();
    }

    void swapFragment(String subreddit){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments_holder,
                        PostsFragment.newInstance(subreddit))
                .commit();
    }

    @Override
    public void onSubredditSelected(String subreddit) {
        swapFragment(subreddit);
    }
}
