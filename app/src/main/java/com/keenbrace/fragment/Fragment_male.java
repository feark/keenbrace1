package com.keenbrace.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.keenbrace.R;

/**
 * Created by KeenBrace on 2016/12/5.
 */

public class Fragment_male extends Fragment{
    public Fragment_male() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tt3_male, container, false);

        return view;
    }
}
