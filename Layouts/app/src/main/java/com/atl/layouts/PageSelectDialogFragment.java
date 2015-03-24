package com.atl.layouts;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PageSelectDialogFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.goto_dialog_title);

        return builder.create();
    }
}
