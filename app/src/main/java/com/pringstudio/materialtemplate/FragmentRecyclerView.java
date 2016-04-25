package com.pringstudio.materialtemplate;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sucipto on 4/14/16.
 */
public class FragmentRecyclerView extends Fragment{

    View mainView;

    public FragmentRecyclerView(){
        // Nothing
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        return mainView;
    }
}
