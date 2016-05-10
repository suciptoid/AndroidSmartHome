package com.pringstudio.agnosthings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pringstudio.agnosthings.view.ProgressBarAnimation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by sucipto on 4/13/16.
 */
public class FragmentHome extends Fragment {

    View mainView;
    LineChart chartSuhu, chartPDAM;

    AgnosthingsApi api;

    // Empty Constructor
    public FragmentHome(){
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_home, container, false);

        // Setup api
        api = new AgnosthingsApi(getContext());

        // Setup chart
        setupChart();

        // Setup listener
        setupListener();

        return mainView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_home_refresh:
                updateChart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupListener(){
        LinearLayout linearPulsa = (LinearLayout) mainView.findViewById(R.id.pulsaListrik);
        linearPulsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Pulsa","Clicked");
            }
        });
    }


    private void setupChart(){
        // Setup chart suhu
        chartSuhu = (LineChart) mainView.findViewById(R.id.home_chart_suhu);
        chartPDAM = (LineChart) mainView.findViewById(R.id.home_chart_pdam);
        chartSuhu.setDescription("");
        chartPDAM.setDescription("");



        updateChart();

    }

    private void updateChart(){

        // Log.d
        Log.d("updateChart()","Update chart Home fragment");

        // Dummy data chart suhu
        /*final ArrayList<Entry> entrySuhu = new ArrayList<>();
        entrySuhu.add(new Entry(39,0));
        entrySuhu.add(new Entry(30,1));
        entrySuhu.add(new Entry(35,2));
        entrySuhu.add(new Entry(36,3));
        entrySuhu.add(new Entry(25,4));
        entrySuhu.add(new Entry(24,5));
        entrySuhu.add(new Entry(35,6));
        entrySuhu.add(new Entry(25,7));
        entrySuhu.add(new Entry(26,8));
        entrySuhu.add(new Entry(35,9));

        final ArrayList<String> labelSuhu = new ArrayList<>();
        labelSuhu.add("06:00");
        labelSuhu.add("07:00");
        labelSuhu.add("08:00");
        labelSuhu.add("09:00");
        labelSuhu.add("10:00");
        labelSuhu.add("11:00");
        labelSuhu.add("12:00");
        labelSuhu.add("13:00");
        labelSuhu.add("14:00");
        labelSuhu.add("15:00");

        LineDataSet dataSetSuhu = new LineDataSet(entrySuhu, "Derajat celcius");
        dataSetSuhu.setColor(Color.parseColor("#009688"));
        dataSetSuhu.setCircleColor(Color.parseColor("#ffcdd2"));
        dataSetSuhu.setCircleColorHole(Color.parseColor("#f44336"));

        LineData dataSuhu = new LineData(labelSuhu, dataSetSuhu);

        chartSuhu.setData(dataSuhu);*/


        // Dummy data chart PDAM
        /*ArrayList<Entry> entryPDAM = new ArrayList<>();
        entryPDAM.add(new Entry(320,0));
        entryPDAM.add(new Entry(350,1));
        entryPDAM.add(new Entry(410,2));
        entryPDAM.add(new Entry(440,3));
        entryPDAM.add(new Entry(470,4));
        entryPDAM.add(new Entry(520,5));
        entryPDAM.add(new Entry(550,6));


        final ArrayList<String> labelPDAM = new ArrayList<>();
        labelPDAM.add("Senin");
        labelPDAM.add("Selasa");
        labelPDAM.add("Rabu");
        labelPDAM.add("Kamis");
        labelPDAM.add("Jumat");
        labelPDAM.add("Sabut");
        labelPDAM.add("Minggu");*/




        api.getDataSuhu();
        api.setSuhuLoadedListener(new AgnosthingsApi.SuhuLoadedListener() {
            @Override
            public void onDataLoaded(List<Map<String, String>> data) {

                ArrayList<Entry> entrySuhu = new ArrayList<>();
                ArrayList<String> labelSuhu = new ArrayList<>();

                entrySuhu.clear();
                labelSuhu.clear();

                // Date formater
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
                Date parsed = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                dateFormat.setTimeZone(TimeZone.getDefault());

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                timeFormat.setTimeZone(TimeZone.getDefault());
                // end date formater

                int x = 0;
                for(Map suhu : data){
                    try{
                        parsed = sourceFormat.parse(suhu.get("date").toString());
                    }catch (Exception e){
                        Log.e("setSuhuLoadedListener","Error parsing date");
                        e.printStackTrace();
                    }
                    entrySuhu.add(new Entry(Float.parseFloat(suhu.get("value").toString()),x));
                    labelSuhu.add(timeFormat.format(parsed));
                    x++;
                }

                LineDataSet dataSetSuhu = new LineDataSet(entrySuhu, "Derajat celcius");
                dataSetSuhu.setColor(Color.parseColor("#009688"));
                dataSetSuhu.setCircleColor(Color.parseColor("#ffcdd2"));
                dataSetSuhu.setCircleColorHole(Color.parseColor("#f44336"));

                LineData dataSuhu = new LineData(labelSuhu, dataSetSuhu);

                chartSuhu.setData(dataSuhu);

                // Update data
                chartSuhu.notifyDataSetChanged();

                // Animate
                chartSuhu.animateY(2000);
            }

            @Override
            public void onFail() {
                try{
                    Snackbar.make(getView(),"Refresh data gagal, coba lagi nanti", Snackbar.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // Get data PDAM

        api.getDataPDAM();
        api.setPDAMLoadedListener(new AgnosthingsApi.PDAMLoadedListener() {
            @Override
            public void onDataLoaded(List<Map<String, String>> data) {
                ArrayList<Entry> entryPDAM = new ArrayList<>();
                ArrayList<String> labelPDAM = new ArrayList<>();

                // Date formater
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
                Date parsed = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                dateFormat.setTimeZone(TimeZone.getDefault());

                SimpleDateFormat timeFormat = new SimpleDateFormat("DD");
                timeFormat.setTimeZone(TimeZone.getDefault());
                // end date formater

                int x = 0;
                for(Map pdam : data){
                    try{
                        parsed = sourceFormat.parse(pdam.get("date").toString());
                    }catch (Exception e){
                        Log.e("setSuhuLoadedListener","Error parsing date");
                        e.printStackTrace();
                    }
                    entryPDAM.add(new Entry(Float.parseFloat(pdam.get("value").toString()),x));
                    labelPDAM.add(timeFormat.format(parsed));
                    x++;
                }


                LineDataSet dataSetPDAM = new LineDataSet(entryPDAM, "Meter Kibik");
                dataSetPDAM.setColor(Color.parseColor("#2196f3"));
                dataSetPDAM.setCircleColor(Color.parseColor("#ffcdd2"));
                dataSetPDAM.setCircleColorHole(Color.parseColor("#f44336"));

                LineData dataPDAM = new LineData(labelPDAM, dataSetPDAM);

                chartPDAM.setData(dataPDAM);
                chartPDAM.animateY(2000);
                chartPDAM.notifyDataSetChanged();
            }

            @Override
            public void onFail() {
                try{
                    Snackbar.make(getView(),"Refresh data gagal, coba lagi nanti", Snackbar.LENGTH_LONG).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        // Test progressbar
        ProgressBar progressBar = (ProgressBar) mainView.findViewById(R.id.progressBar);
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBar,0,progressBar.getProgress());
        animation.setDuration(1500);
        progressBar.setAnimation(animation);
    }


}
