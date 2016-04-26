package com.pringstudio.agnosthings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pringstudio.agnosthings.model.Saklar;
import com.pringstudio.agnosthings.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sucipto on 4/14/16.
 */
public class FragmentSaklar extends Fragment{

    // Main View
    View mainView;

    // RecyclerView Saklar
    private RecyclerView recyclerView;

    // Data adapter recyclerview
    SaklarAdapter saklarAdapter;

    // Data Saklar
    List<Saklar> saklarList = new ArrayList<>();

    // Empty Constructor
    public FragmentSaklar(){
        // Nothing
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_saklar, container, false);

        // Init Saklar RecyclerView
        setupSaklarRecycler();

        return mainView;
    }

    private void setupSaklarRecycler(){
        // Init The View
        recyclerView = (RecyclerView) mainView.findViewById(R.id.saklar_recycler);

        // Set Adapter
        saklarAdapter = new SaklarAdapter(saklarList);
        recyclerView.setAdapter(saklarAdapter);

        // Set Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        // Item Decorator / Divider on list
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        // Populate data
        getDataSaklar();

    }

    /**
     * Get Data Saklar dari API
     */

    private void getDataSaklar(){

        // Clear Item
        saklarList.clear();

        Saklar saklar = new Saklar("Teras");
        saklar.setValue(1);
        saklarList.add(saklar);

        Saklar saklar1 = new Saklar("Dapur");
        saklarList.add(saklar1);

        Saklar saklar2 = new Saklar("Taman");
        saklar2.setValue(1);
        saklarList.add(saklar2);

        // Notify the adapter
        saklarAdapter.notifyDataSetChanged();
    }
}
