package com.pringstudio.agnosthings;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.pringstudio.agnosthings.model.Saklar;

import java.util.List;

/**
 * Created by sucipto on 4/25/16.
 */
public class SaklarAdapter extends RecyclerView.Adapter<SaklarAdapter.SaklarHolder> {

    private List<Saklar> saklarList;

    public class SaklarHolder extends RecyclerView.ViewHolder {
        public TextView saklarName;
        public SwitchCompat saklarSwitch;
        public ImageView saklarIcon;

        public SaklarHolder(View view){
            super(view);

            saklarName = (TextView) view.findViewById(R.id.saklar_text);
            saklarSwitch = (SwitchCompat) view.findViewById(R.id.saklar_switch);
            saklarIcon = (ImageView) view.findViewById(R.id.saklar_icon);
        }
    }

    public SaklarAdapter(List<Saklar> saklarList){
        this.saklarList = saklarList;
    }

    @Override
    public SaklarHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_saklar,parent,false);

        return new SaklarHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SaklarHolder holder, final int position) {

        Saklar saklar = saklarList.get(position);

        holder.saklarName.setText(saklar.getName());

        // Set Saklar dari Object / Database
        if(saklar.getValue() == 1){
            holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_green);
            holder.saklarSwitch.setChecked(true);
        }else if(saklar.getValue() == 0){
            holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_grey);
            holder.saklarSwitch.setChecked(false);
        }


        // Saklar Switch Listenner
        holder.saklarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("SAKLAR","Pos: "+position+" buttonView.isPressed()"+buttonView.isPressed());
                Log.d("SAKLAR","Pos: "+position+" buttonView = isCheccked? "+isChecked);

                if(isChecked){
                    holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_green);
                }else {
                    holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_grey);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return saklarList.size();
    }
}
