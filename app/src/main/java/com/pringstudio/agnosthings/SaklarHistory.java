package com.pringstudio.agnosthings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.pringstudio.agnosthings.view.DividerItemDecoration;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaklarHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SaklarHistoryAdapter saklarHistoryAdapter;
    private TextView textHistory;

    private List<Map<String,String>> historyData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saklar_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHistoryData();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup View
        setupLayout();

        // Title
        getSupportActionBar().setTitle("History Saklar");
        getSupportActionBar().setSubtitle("Data terakhri 2016/05/04 20:09:05");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                // Untuk Kembali ke halaman berikutnya daripada kembali ke MainActivity
                // http://stackoverflow.com/a/33252156/3086112
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupLayout(){
        // Setup recyclerview
        // ------------------

        // Get the view
        recyclerView = (RecyclerView) findViewById(R.id.recycler_saklar_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        // Get adapter
        saklarHistoryAdapter = new SaklarHistoryAdapter(historyData);
        recyclerView.setAdapter(saklarHistoryAdapter);

        // SaklarHistory
        textHistory = (TextView) findViewById(R.id.history_text);

        getHistoryData();
    }

    private void getHistoryData(){
        textHistory.setText("Loading...");
        textHistory.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        AgnosthingsApi api = new AgnosthingsApi(this);

        api.getSaklarHistory(getIntent().getStringExtra("saklarID"));

        api.setSaklarHistoryLoadedListener(new AgnosthingsApi.SaklarHistoryLoadedListener() {
            @Override
            public void onDataLoaded(List<Map<String, String>> data) {
                if(data.size() == 0){
                    textHistory.setText("No data");
                }else{
                    textHistory.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    historyData.clear();
                    historyData.addAll(data);
                    saklarHistoryAdapter.notifyDataSetChanged();
                }

                Log.d("ListenerDarta","Data Loaded Data : "+data.toString());
                Log.d("ListenerDarta","Data Loaded HistoryList : "+historyData.toString());
            }
        });

        Log.d("History","Data loaded : "+historyData.toString());

        // Update ui
        saklarHistoryAdapter.notifyDataSetChanged();
    }
}
