package com.atl.layouts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class PageSelectDialogFragment extends DialogFragment {

    PageSelectDialogListener mListener;

    public interface PageSelectDialogListener{
        public void onPageSelected(int page);
        public boolean validatePage(int page);
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View gotoView = inflater.inflate(R.layout.fragment_page_select_dialog, null);
        builder.setView(gotoView)
            .setTitle(R.string.goto_dialog_title)
            .setCancelable(true)
            .setPositiveButton(R.string.goto_dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        int page = Integer.parseInt(((EditText) gotoView.findViewById(R.id.goto_page_num_chooser)).getText().toString());
                        if( mListener.validatePage(page)){
                            mListener.onPageSelected(page);
                        }
                    }
                    catch (NumberFormatException e){
                        System.out.println("Failed to interpret as page number. " + e);
                    }
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
            mListener = (PageSelectDialogListener)activity;
        }
        catch(ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement PageSelectDialogListener");
        }
    }
}
