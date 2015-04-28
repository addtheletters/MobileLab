package com.atl.goodresult;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Ben on 4/28/2015.
 */
public class PickSubredditDialogFragment extends DialogFragment {
    PickSubredditDialogListener listener;

    public interface PickSubredditDialogListener{
        public void onSubredditSelected(String subreddit);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        System.out.println("Creating dialog: calabunga");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View gotoView = inflater.inflate(R.layout.pick_subreddit_dialog_layout, null);
        builder.setView(gotoView)
                .setTitle(R.string.pick_subreddit)
                .setCancelable(true)
                .setPositiveButton("Yes.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sr = ((EditText) gotoView.findViewById(R.id.pick_subreddit_edittext)).getText().toString();
                        listener.onSubredditSelected(sr);
                    }
                });

        AlertDialog alert = builder.create();
        Button goBtn = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        return alert;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            listener = (PickSubredditDialogListener)activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement PickSubredditDialogListener");
        }
    }

}
