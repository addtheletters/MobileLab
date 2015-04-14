package com.atl.layouts;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StoryViewerFragment extends Fragment {

    private static final String STORY_DATA = "storyData";
    private static final String STORY_INDEX = "storyIndex";

    private static final String PREF_KEY = "pagenum";

    public String prefs_name = "storyPagePrefs_";
    public SharedPreferences pagePrefs;

    private JSONObject storyData;

    private OnFragmentInteractionListener mListener;

    private TextView textView;
    private Button lastPageBtn;
    private Button nextPageBtn;
    private Button goToPageBtn;

    private int currentPage;

    public static StoryViewerFragment newInstance(JSONObject storyData, int storyIndex) {
        StoryViewerFragment fragment = new StoryViewerFragment();
        Bundle args = new Bundle();
        args.putString(STORY_DATA, storyData.toString());
        args.putInt(STORY_INDEX, storyIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public StoryViewerFragment() {
        // Required empty public constructor
    }

    public Spannable formatLine(String rawLine){
        return new SpannableString(rawLine + System.getProperty("line.separator"));
    }

    public Spannable getLineStarter(){
        Spannable lStart = new SpannableString("| ");
        lStart.setSpan(new ForegroundColorSpan(R.color.line_starter), 0, lStart.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return lStart;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                storyData = new JSONObject(getArguments().getString(STORY_DATA));
                System.out.println(storyData);
                prefs_name = prefs_name + storyData.get("id");
                pagePrefs = getActivity().getSharedPreferences(prefs_name, 0);

                currentPage = pagePrefs.getInt(PREF_KEY, 0);
                try {
                    JSONArray pages = storyData.getJSONArray("pages");
                    if (currentPage < 1 || currentPage > pages.length()) {
                        currentPage = 1;
                    }
                }
                catch(JSONException e){
                    currentPage = 1;
                    System.out.println("failure : onCreateView page parse " + e);
                }

            } catch (JSONException e) {
                System.out.println("Failed to interpret json in StoryViewerFragment creation; " + e);
            }
        }
    }
    public String getPageText(int page, JSONArray pages) throws JSONException{
        if(pages != null){
            return pages.getJSONObject(page - 1).getString("content");
        }
        else {
            return storyData.getJSONArray("pages").getJSONObject(page - 1).getString("content");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_story_viewer, container, false);

        // Inflate the layout for this fragment
        textView = (TextView)v.findViewById(R.id.text_story_viewer);
        textView.setMovementMethod(new ScrollingMovementMethod());
        //System.out.println("TEXTVIEW STATUS: " + textView);

        lastPageBtn = (Button) v.findViewById(R.id.btn_last_page);
        lastPageBtn.setOnClickListener(new LastPageListener());
        lastPageBtn.setOnLongClickListener(new GoToStartListener());
        nextPageBtn = (Button) v.findViewById(R.id.btn_next_page);
        nextPageBtn.setOnClickListener(new NextPageListener());
        nextPageBtn.setOnLongClickListener(new GoToEndListener());

        goToPageBtn = (Button) v.findViewById(R.id.btn_goto_page);
        goToPageBtn.setOnClickListener(new GoToPageListener());


        changePageTo(currentPage);

        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(STORY_INDEX));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public boolean pageNumberValid(int pageNum){
        System.out.println("Validating page " + pageNum);
        try {
            JSONArray pages = storyData.getJSONArray("pages");
            if (pageNum < 1 || pageNum > pages.length()) {
                return false;
            }
            return true;
        }
        catch(JSONException e){
            return false;
        }
    }

    public boolean changePageTo(int pageNum){
        System.out.println("Changing page to " + pageNum);

        try {
            JSONArray pages = storyData.getJSONArray("pages");
            if (pageNum < 1 || pageNum > pages.length()) {
                return false;
            }
            if (lastPageBtn.isEnabled()){
                if(pageNum == 1){
                    lastPageBtn.setEnabled(false);
                }
            }
            else{
                lastPageBtn.setEnabled(true);
            }
            if (nextPageBtn.isEnabled()){
                System.out.println("pn" + pageNum + " len" + pages.length());
                System.out.println("pn" + pageNum + " len" + pages.length());
                if(pageNum == pages.length()){
                    nextPageBtn.setEnabled(false);
                }
            }
            else{
                nextPageBtn.setEnabled(true);
            }
            goToPageBtn.setText(pages.getJSONObject(pageNum-1).getString("index"));
            setPageText(getPageText(pageNum, pages));
            currentPage = pageNum;
            pagePrefs.edit().putInt(PREF_KEY, pageNum).commit();
            return true;
        }
        catch(JSONException e){
            System.out.println("error changing page " + e);
            return false;
        }
    }

    public void setPageText( String pageText ){
        textView.scrollTo(0, 0);
        textView.setText("");
        String[] lines = pageText.split(System.getProperty("line.separator"));
        for(String line : lines){
            textView.append(getLineStarter());
            textView.append(formatLine(line));
        }
        //textView.setText(pageText);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class NextPageListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            changePageTo(currentPage + 1);
        }
    }

    public class LastPageListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            changePageTo(currentPage - 1);
        }
    }

    public class GoToPageListener implements View.OnClickListener{
        public void onClick(View v){
            // TODO show dialogue to switch page
            DialogFragment goToDialog = new PageSelectDialogFragment();
            goToDialog.show(getActivity().getSupportFragmentManager(), "PageSelectDialogFragment");
        }
    }

    public class GoToStartListener implements View.OnLongClickListener{
        public boolean onLongClick(View v){
            // TODO ask if sure wants to return to start of story
            // then
            changePageTo(1);
            return true;
        }
    }

    public class GoToEndListener implements View.OnLongClickListener{
        public boolean onLongClick(View v){
            // TODO ask if sure wants to go to end of story
            // then
            try {
                changePageTo(storyData.getJSONArray("pages").length());
            }
            catch(JSONException e){
                System.out.println("failure to change to end page, " + e);
            }
            return true;
        }
    }


}
