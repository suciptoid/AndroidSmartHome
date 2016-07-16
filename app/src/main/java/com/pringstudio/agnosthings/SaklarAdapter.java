package com.pringstudio.agnosthings;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pringstudio.agnosthings.model.Saklar;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by sucipto on 4/25/16.
 */
public class SaklarAdapter extends RecyclerView.Adapter<SaklarAdapter.SaklarHolder> {

    private List<Saklar> saklarList;

    // Realm
    Realm realm;

    // Context
    Context context;

    // Listener
    OnSaklarItemClickListener saklarItemClickListener;

    public class SaklarHolder extends RecyclerView.ViewHolder {
        public TextView saklarName;
        public SwitchCompat saklarSwitch;
        public ImageView saklarIcon;

        public SaklarHolder(View view) {
            super(view);

            saklarName = (TextView) view.findViewById(R.id.saklar_text);
            saklarSwitch = (SwitchCompat) view.findViewById(R.id.saklar_switch);
            saklarIcon = (ImageView) view.findViewById(R.id.saklar_icon);

            // Click Listener
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (saklarItemClickListener != null) {
                        saklarItemClickListener.onClick(v, position);
                    }
                }
            });
        }
    }

    public SaklarAdapter(List<Saklar> saklarList) {
        this.saklarList = saklarList;


    }

    @Override
    public SaklarHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_recycler_saklar, parent, false);

        // Init Realm
        RealmConfiguration config = new RealmConfiguration.Builder(context)
                .name("agnosthings.realm")
                .schemaVersion(1)
                .build();

        realm = Realm.getInstance(config);

        return new SaklarHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SaklarHolder holder, final int position) {

        final Saklar saklar = saklarList.get(position);

        // Saklar Switch Listenner
        CompoundButton.OnCheckedChangeListener toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                AgnosthingsApi api = new AgnosthingsApi(context);

                realm.beginTransaction();
                if (isChecked) {
                    saklar.setValue(1);
                    holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_green);
                } else {
                    saklar.setValue(0);
                    holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_grey);
                }
                realm.commitTransaction();

                // Push updated data saklar
                api.pushDataSaklar();
            }
        };

        holder.saklarName.setText(saklar.getName());

        // Disable Listener sementara
        holder.saklarSwitch.setOnCheckedChangeListener(null);

        // Set Saklar dari Object / Database
        if (saklar.getValue() == 1) {
            holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_green);
            holder.saklarSwitch.setChecked(true);
        } else if (saklar.getValue() == 0) {
            holder.saklarIcon.setImageResource(R.drawable.ic_lightbulb_outline_grey);
            holder.saklarSwitch.setChecked(false);
        }

        holder.saklarSwitch.setOnCheckedChangeListener(toggleListener);

    }

    @Override
    public int getItemCount() {
        return saklarList.size();
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
