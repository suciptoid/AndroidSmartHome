package com.pringstudio.agnosthings;

import android.animation.ValueAnimator;
import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import org.w3c.dom.Text;

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
    Menu mainMenu;

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
        super.onCreateView(inflater,container,savedInstanceState);
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
        mainMenu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_home_refresh:
                updateChart();
                //startAnimateRefreshMenu(true);
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
                chartSuhu.animateY(1000);
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
                chartPDAM.animateY(1000);
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

        final ProgressBar progressBarListrik = (ProgressBar) mainView.findViewById(R.id.progressListrik);
        final ProgressBar progressBarLPG = (ProgressBar) mainView.findViewById(R.id.progressLpg);
        final TextView listrikUpdated = (TextView) mainView.findViewById(R.id.update_listrik_text);
        final TextView listrikProgress = (TextView) mainView.findViewById(R.id.progress_listrik_text);
        final int maxValue = 200;


        // Reset Progressbar
        //progressBar.setProgress(0);
        ProgressBarAnimation animation = new ProgressBarAnimation(progressBarListrik,progressBarListrik.getProgress(),0);
        animation.setDuration(500);
        progressBarListrik.setAnimation(animation);

        startCountAnimation(listrikProgress,500,(progressBarListrik.getProgress()*100)/maxValue,0);

        listrikUpdated.setText("Loading...");
        listrikProgress.setText("0%");

        api.getDataListrik();
        api.setListrikLoadedListener(new AgnosthingsApi.ListrikLoadedListener() {
            @Override
            public void onDataLoaded(String data) {
                String kwh = data.split(",")[1];
                String lastDate = data.split(",")[2];

                // Date formater
                SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
                Date parsed = new Date();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
                dateFormat.setTimeZone(TimeZone.getDefault());

                // end date formater

                try {
                    parsed = sourceFormat.parse(lastDate);
                }catch (Exception e){
                    e.printStackTrace();
                }

                String newDate = dateFormat.format(parsed);

                String prosentase = String.valueOf((Integer.valueOf(kwh)*100)/maxValue);


                listrikUpdated.setText("Update : \n"+newDate);
                listrikProgress.setText(prosentase+"%");
                startCountAnimation(listrikProgress,1000,0,Integer.valueOf(prosentase));

                // Test progressbar

                progressBarListrik.setMax(maxValue);
                progressBarListrik.setProgress(Integer.valueOf(kwh));
                ProgressBarAnimation animation = new ProgressBarAnimation(progressBarListrik,0,Integer.valueOf(kwh));
                animation.setDuration(1000);
                progressBarListrik.setAnimation(animation);


                //startAnimateRefreshMenu(false);
                Log.d("Listrik","Loaded , Data : "+kwh+", date: "+newDate);
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
        //TODO: Progressbar GAS LPG
        ProgressBarAnimation animation2 = new ProgressBarAnimation(progressBarLPG,0,progressBarLPG.getProgress());
        animation2.setDuration(1500);
        progressBarLPG.setAnimation(animation2);
    }

    private void startCountAnimation(TextView textProgress, int duration, int from, int to) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(from, to);
        animator.setDuration(duration);
        final TextView textView = textProgress;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText("" + (int) animation.getAnimatedValue()+"%");
            }
        });
        animator.start();
    }

    private void startAnimateRefreshMenu(boolean start){
        //TODO: Gak bisa berenti muter, gak di pake dulu lah
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView iv = (ImageView)inflater.inflate(R.layout.iv_refresh, null);
        Animation rotation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_refresh);
        rotation.setRepeatCount(Animation.INFINITE);
        iv.startAnimation(rotation);
        //item.setActionView(iv);
        if(start){
            mainMenu.findItem(R.id.menu_home_refresh).setActionView(iv);
        }else {
            mainMenu.findItem(R.id.menu_home_refresh).setIcon(R.drawable.ic_cached_grey_200_36dp);
        }
    }


}
