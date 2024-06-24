package com.example.wifianalyzer;


import android.animation.LayoutTransition;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ThirdFragment extends Fragment {

    TextView detailsText;
    LinearLayout layout;
    CardView exp;
    TextView detailsText1;
    LinearLayout layout1;
    CardView exp1;
    TextView detailsText2;
    LinearLayout layout2;
    CardView exp2;
    TextView detailsText3;
    LinearLayout layout3;
    CardView exp3;
    TextView detailsText4;
    LinearLayout layout4;
    CardView exp4;
    TextView detailsText5;
    LinearLayout layout5;
    CardView exp5;
    TextView detailsText6;
    LinearLayout layout6;
    CardView exp6;
    TextView detailsText7;
    LinearLayout layout7;
    CardView exp7;
    TextView detailsText8;
    LinearLayout layout8;
    CardView exp8;
    TextView detailsText9;
    LinearLayout layout9;
    CardView exp9;
    TextView detailsText10;
    LinearLayout layout10;
    CardView exp10;
    TextView detailsText11;
    LinearLayout layout11;
    CardView exp11;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_third, container, false);




        exp = view.findViewById(R.id.expand);
        detailsText = view.findViewById(R.id.details);
        layout = view.findViewById(R.id.layout);
        layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout, new AutoTransition());
                detailsText.setVisibility(ve);
            }
        });
        exp1 = view.findViewById(R.id.expand1);
        detailsText1 = view.findViewById(R.id.details1);
        layout1 = view.findViewById(R.id.layout1);
        layout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout1, new AutoTransition());
                detailsText1.setVisibility(ve);
            }
        });
        exp2 = view.findViewById(R.id.expand2);
        detailsText2 = view.findViewById(R.id.details2);
        layout2 = view.findViewById(R.id.layout2);
        layout2.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText2.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout2, new AutoTransition());
                detailsText2.setVisibility(ve);
            }
        });
        exp3 = view.findViewById(R.id.expand3);
        detailsText3 = view.findViewById(R.id.details3);
        layout3 = view.findViewById(R.id.layout3);
        layout3.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText3.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout3, new AutoTransition());
                detailsText3.setVisibility(ve);
            }
        });
        exp4 = view.findViewById(R.id.expand4);
        detailsText4 = view.findViewById(R.id.details4);
        layout4 = view.findViewById(R.id.layout4);
        layout4.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText4.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout4, new AutoTransition());
                detailsText4.setVisibility(ve);
            }
        });
        exp5 = view.findViewById(R.id.expand5);
        detailsText5 = view.findViewById(R.id.details5);
        layout5 = view.findViewById(R.id.layout5);
        layout5.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText5.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout5, new AutoTransition());
                detailsText5.setVisibility(ve);
            }
        });
        exp6 = view.findViewById(R.id.expand6);
        detailsText6 = view.findViewById(R.id.details6);
        layout6 = view.findViewById(R.id.layout6);
        layout6.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText6.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout6, new AutoTransition());
                detailsText6.setVisibility(ve);
            }
        });
        exp7 = view.findViewById(R.id.expand7);
        detailsText7 = view.findViewById(R.id.details7);
        layout7 = view.findViewById(R.id.layout7);
        layout7.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText7.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout7, new AutoTransition());
                detailsText7.setVisibility(ve);
            }
        });
        exp8 = view.findViewById(R.id.expand8);
        detailsText8 = view.findViewById(R.id.details8);
        layout8 = view.findViewById(R.id.layout8);
        layout8.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText8.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout8, new AutoTransition());
                detailsText8.setVisibility(ve);
            }
        });
        exp9 = view.findViewById(R.id.expand9);
        detailsText9 = view.findViewById(R.id.details9);
        layout9 = view.findViewById(R.id.layout9);
        layout9.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText9.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout9, new AutoTransition());
                detailsText9.setVisibility(ve);
            }
        });
        exp10 = view.findViewById(R.id.expand10);
        detailsText10 = view.findViewById(R.id.details10);
        layout10 = view.findViewById(R.id.layout10);
        layout10.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText10.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout10, new AutoTransition());
                detailsText10.setVisibility(ve);
            }
        });
        exp11 = view.findViewById(R.id.expand11);
        detailsText11 = view.findViewById(R.id.details11);
        layout11 = view.findViewById(R.id.layout11);
        layout11.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        exp11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ve = (detailsText11.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;

                TransitionManager.beginDelayedTransition(layout11, new AutoTransition());
                detailsText11.setVisibility(ve);
            }
        });

        return view;
    }
}