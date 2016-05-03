package com.pringstudio.agnosthings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pringstudio.agnosthings.model.Saklar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sucipto on 5/3/16.
 */
public class SaklarHistoryAdapter extends RecyclerView.Adapter<SaklarHistoryAdapter.SaklarHistoryHolder> {

    private List<Map<String,String>> historyData;

    // Realm
    private Realm realm;

    // Context
    private Context context;

    // Listener
    private OnSaklarItemClickListener saklarItemClickListener;

    public class SaklarHistoryHolder extends RecyclerView.ViewHolder {
        public TextView historyDate;
        public TextView historyClock;
        public ImageView historyImage;

        public SaklarHistoryHolder(View view) {
            super(view);

            historyDate = (TextView) view.findViewById(R.id.history_date);
            historyClock = (TextView) view.findViewById(R.id.history_clock);
            historyImage = (ImageView) view.findViewById(R.id.history_image);

        }


    }

    public SaklarHistoryAdapter(List<Map<String,String>> data) {
        this.historyData = data;
    }

    @Override
    public SaklarHistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_recycler_history, parent, false);


        return new SaklarHistoryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SaklarHistoryHolder holder, final int position) {

        Map<String,String> history = historyData.get(position);
        Log.d("Adapter","Data : "+history.toString());

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        Date parsed = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        dateFormat.setTimeZone(TimeZone.getDefault());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getDefault());



        try{
            parsed = sourceFormat.parse(history.get("date"));
        }catch (Exception e){
            Log.e("HistoryAdapter","Error parsing date");
            e.printStackTrace();
        }

        String tanggal = dateFormat.format(parsed);
        String jam = timeFormat.format(parsed);
        Log.d("HistoryAdapter",tanggal);

        holder.historyDate.setText(tanggal);
        holder.historyClock.setText(jam);
        if(history.get("value") == "1" | Integer.valueOf(history.get("value")) == 1){
            holder.historyImage.setImageResource(R.drawable.ic_lightbulb_outline_green);
        }else{
            holder.historyImage.setImageResource(R.drawable.ic_lightbulb_outline_grey);
        }

    }

    @Override
    public int getItemCount() {
        return historyData.size();
    }

    // Listener interface
    public interface OnSaklarItemClickListener {
        public void onClick(View v, int pos);
    }

    // Listenner setter
    public void setOnSaklarItemClickListener(OnSaklarItemClickListener listener) {
        this.saklarItemClickListener = listener;
    }

}
