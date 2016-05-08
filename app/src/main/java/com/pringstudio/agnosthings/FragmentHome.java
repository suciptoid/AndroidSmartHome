package com.pringstudio.agnosthings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by sucipto on 4/13/16.
 */
public class FragmentHome extends Fragment {

    View mainView;
    LineChart chartSuhu, chartPDAM;

    // Empty Constructor
    public FragmentHome(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_home, container, false);

        // Setup chart
        setupChart();

        // Setup listener
        setupListener();

        return mainView;
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

        // Dummy data chart suhu
        ArrayList<Entry> entrySuhu = new ArrayList<>();
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

        ArrayList<String> labelSuhu = new ArrayList<>();
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

        chartSuhu.setData(dataSuhu);
        chartSuhu.animateY(2000);

        // Dummy data chart PDAM
        // Dummy data chart suhu
        ArrayList<Entry> entryPDAM = new ArrayList<>();
        entryPDAM.add(new Entry(320,0));
        entryPDAM.add(new Entry(350,1));
        entryPDAM.add(new Entry(410,2));
        entryPDAM.add(new Entry(440,3));
        entryPDAM.add(new Entry(470,4));
        entryPDAM.add(new Entry(520,5));
        entryPDAM.add(new Entry(550,6));


        ArrayList<String> labelPDAM = new ArrayList<>();
        labelPDAM.add("Senin");
        labelPDAM.add("Selasa");
        labelPDAM.add("Rabu");
        labelPDAM.add("Kamis");
        labelPDAM.add("Jumat");
        labelPDAM.add("Sabut");
        labelPDAM.add("Minggu");


        LineDataSet dataSetPDAM = new LineDataSet(entryPDAM, "Meter Kibik");
        dataSetPDAM.setColor(Color.parseColor("#2196f3"));
        dataSetPDAM.setCircleColor(Color.parseColor("#ffcdd2"));
        dataSetPDAM.setCircleColorHole(Color.parseColor("#f44336"));

        LineData dataPDAM = new LineData(labelPDAM, dataSetPDAM);

        chartPDAM.setData(dataPDAM);
        chartPDAM.animateY(2000);

    }


}
