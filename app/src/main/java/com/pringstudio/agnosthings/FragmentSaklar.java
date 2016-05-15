package com.pringstudio.agnosthings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.pringstudio.agnosthings.model.Saklar;
import com.pringstudio.agnosthings.view.DividerItemDecoration;
import com.pringstudio.agnosthings.view.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by sucipto on 4/14/16.
 */
public class FragmentSaklar extends Fragment {

    // Main View
    View mainView;

    // RecyclerView Saklar
    private RecyclerView recyclerView;

    // Data adapter recyclerview
    SaklarAdapter saklarAdapter;

    // Data Saklar
    List<Saklar> saklarList = new ArrayList<>();

    // Realm
    Realm realm;

    // Realm saklar result
    RealmResults<Saklar> results;

    /**
     * =============================================================================================
     */

    // Empty Constructor
    public FragmentSaklar() {
        // Nothing
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init Realm
        RealmConfiguration config = new RealmConfiguration.Builder(getContext())
                .name("agnosthings.realm")
                .schemaVersion(1)
                .build();

        realm = Realm.getInstance(config);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_saklar, container, false);

        // Init Saklar RecyclerView
        setupSaklarRecycler();

        return mainView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_saklar, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_refresh:
                getDataSaklar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupSaklarRecycler() {
        // Init The View
        recyclerView = (RecyclerView) mainView.findViewById(R.id.saklar_recycler);

        // Set Adapter
        saklarAdapter = new SaklarAdapter(saklarList);
        recyclerView.setAdapter(saklarAdapter);

        // Set Layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        // Item Decorator / Divider on list
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

        // Add click listenner
        saklarAdapter.setOnSaklarItemClickListener(new SaklarAdapter.OnSaklarItemClickListener() {
            @Override
            public void onClick(View v, int pos) {
                Intent saklarHistory = new Intent(getContext(),SaklarHistory.class);
                Saklar item = saklarList.get(pos);
                saklarHistory.putExtra("saklarID",item.getId());
                startActivity(saklarHistory);
            }
        });

        // Populate data
        getDataSaklar();

    }

    /**
     * Get Data Saklar dari API
     */

    private void getDataSaklar() {

        // Clear Item
        saklarList.clear();

        // Api Class
        AgnosthingsApi agnosthingsApi = new AgnosthingsApi(getContext());

        // Update subtittle
        setSubTitle("Updating data ...");

        // Set Listenner
        agnosthingsApi.setSaklarValueUpdateListener(new AgnosthingsApi.SaklarValueUpdateListener() {
            @Override
            public void onValueLoaded() {
                results = realm.where(Saklar.class).findAll();
                saklarList.clear();
                saklarList.addAll(results);
                saklarAdapter.notifyDataSetChanged();
                setSubTitle("");
            }

            @Override
            public void onFail(){
                setSubTitle("Update data failed");
            }
        });

        // Retrieve value from server (Update value from online server)
        agnosthingsApi.retreiveSaklarValue();


        // Get The Data
        results = realm.where(Saklar.class).findAll();

        if (results.size() == 0) {

            realm.beginTransaction();

            Saklar saklar1 = realm.createObject(Saklar.class);

            saklar1.setId("lampu_kanan");
            saklar1.setName("Lampu Teras");
            saklar1.setValue(1);

            Saklar saklar2 = realm.createObject(Saklar.class);

            saklar2.setId("lampu_kiri");
            saklar2.setName("Lampu Dapur");
            saklar2.setValue(0);

            Saklar saklar3 = realm.createObject(Saklar.class);

            saklar3.setId("lampu_tengah");
            saklar3.setName("Lampu Kamar");
            saklar3.setValue(1);

            Saklar saklar4 = realm.createObject(Saklar.class);

            saklar4.setId("lampu_halaman");
            saklar4.setName("Lampu Taman");
            saklar4.setValue(1);

            Saklar saklar5 = realm.createObject(Saklar.class);

            saklar5.setId("lampu_jalan");
            saklar5.setName("Lampu Jalan");
            saklar5.setValue(1);

            realm.commitTransaction();

            results = realm.where(Saklar.class).findAll();

        }

        saklarList.addAll(results);

        // Notify the adapter
        saklarAdapter.notifyDataSetChanged();
    }

    private void setTitle(String title){
        try {
            ((MainActivity) getActivity())
                    .getSupportActionBar()
                    .setTitle(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSubTitle(String sub){
        try {
            ((MainActivity) getActivity())
                    .getSupportActionBar()
                    .setSubtitle(sub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
