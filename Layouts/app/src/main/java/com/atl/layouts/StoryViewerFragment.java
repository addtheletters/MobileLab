package com.atl.layouts;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StoryViewerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StoryViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryViewerFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STORY_DATA = "";

    private JSONObject storyData;

    private OnFragmentInteractionListener mListener;

    private TextView textView;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param storyData JSON object representing the story.
     * @return A new instance of fragment StoryViewerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StoryViewerFragment newInstance(JSONObject storyData) {
        StoryViewerFragment fragment = new StoryViewerFragment();
        Bundle args = new Bundle();
        args.putString(STORY_DATA, storyData.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public StoryViewerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                storyData = new JSONObject(getArguments().getString(STORY_DATA));
            } catch (JSONException e) {
                System.out.println("Failed to interpret json in StoryViewerFragment creation");
            }
        }
    }
    public String getPageText(int page) throws JSONException{
        return storyData.getJSONArray("pages").getJSONObject(page - 1).getString("content");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_story_viewer, container, false);

        // Inflate the layout for this fragment
        textView = (TextView)v.findViewById(R.id.text_story_viewer);
        System.out.println("TEXTVIEW STATUS: " + textView);
        try {
            textView.setText(getPageText(1));
        }
        catch(JSONException e){
            textView.setText("Page unavailable.");
        }

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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

}
