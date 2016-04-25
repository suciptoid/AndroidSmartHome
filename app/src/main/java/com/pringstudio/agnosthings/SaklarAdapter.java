package com.pringstudio.agnosthings;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        public SaklarHolder(View view){
            super(view);

            saklarName = (TextView) view.findViewById(R.id.saklar_text);
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
    public void onBindViewHolder(SaklarHolder holder, int position) {

        Saklar saklar = saklarList.get(position);

        holder.saklarName.setText(saklar.getName());

    }

    @Override
    public int getItemCount() {
        return saklarList.size();
    }
}
