package com.example.archy.livewell;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Archy on 3/21/15.
 */
public class AdviceDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advice_dialog_frag, container);
        getDialog().setTitle("Some Advice");
        return view;
    }
}
