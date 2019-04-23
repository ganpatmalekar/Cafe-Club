package com.example.swapnil.coffeeshop.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.swapnil.coffeeshop.R;
import com.ms.square.android.expandabletextview.ExpandableTextView;

public class AboutUs extends Fragment {

    private ExpandableTextView expandableTextView;
    private TextView textAbout, textVision, textServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        expandableTextView = (ExpandableTextView)view.findViewById(R.id.expand_textView);
        expandableTextView.setText(getString(R.string.mission));

        textAbout = (TextView)view.findViewById(R.id.about);
        textAbout.setText(R.string.about);
        //textAbout.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);

        textVision = (TextView)view.findViewById(R.id.vision);
        textVision.setText(getString(R.string.vision));

        textServices = (TextView)view.findViewById(R.id.services);
        textServices.setText(getString(R.string.services));

        return view;
    }
}
