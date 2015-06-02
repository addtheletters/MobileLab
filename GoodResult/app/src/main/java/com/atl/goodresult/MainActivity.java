package com.atl.goodresult;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity implements PickSubredditDialogFragment.PickSubredditDialogListener{

    private String current_subreddit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null) {
            addFragment(current_subreddit);
        }
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

        if(id == R.id.pass_judgement){
            System.out.println("judge or be judged");

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void addFragment(String subreddit){
        System.out.println("attempted to add fragment " + subreddit);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragments_holder,
                        PostsFragment.newInstance(subreddit))
                .commit();
    }

    void swapFragment(String subreddit){
        //getActionBar().setTitle(subreddit);
        //getSupportActionBar().setTitle(subreddit);
        System.out.println("attempted to swap fragment " + subreddit);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments_holder,
                        PostsFragment.newInstance(subreddit))
                .addToBackStack(null)
                .commit();
    }

    void updateActionBarTitle(){
        // TODO
        // based on current_subreddit
    }

    @Override
    public void onSubredditSelected(String subreddit) {
        current_subreddit = subreddit;
        System.out.println("SWAP TO " + subreddit);
        swapFragment(subreddit);
    }
}
