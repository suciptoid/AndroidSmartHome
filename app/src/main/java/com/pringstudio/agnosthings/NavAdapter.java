package com.pringstudio.agnosthings;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sucipto <chip@sucipto.net> on 1/24/16.
 * Copyright (c) Chip Labs 2016
 */
public class NavAdapter extends RecyclerView.Adapter<NavAdapter.NavHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_HEADER = 0;

    private ArrayList<String> navTitles;
    private ArrayList<Integer> navIcons;
    private String navTitle;
    private String navDesc;
    private Context main_context;
    private static NavItemClickListener clickListener;

    // Selected
    private static int selectedPos = 1; // 0 adalah header

    public static class NavHolder extends RecyclerView.ViewHolder {
        int HolderId;

        TextView textView;
        ImageView imageView;
        TextView title;
        TextView description;
        Context context;

        public NavHolder(View view, int ViewType, Context c){
            super(view);

            context = c;

            if(ViewType == TYPE_ITEM){
                textView = (TextView) view.findViewById(R.id.nav_drawer_menu_text);
                imageView = (ImageView) view.findViewById(R.id.nav_drawer_menu_icon);
                HolderId = 1;
            }else{
                title = (TextView) view.findViewById(R.id.nav_drawer_header_title);
                description = (TextView) view.findViewById(R.id.nav_drawer_header_subtittle);
                HolderId = 0;
            }

            // Set Listener
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    clickListener.onItemClick(position, v);
                    // Update selected position
                    selectedPos = position;
                }
            });

            //Set item Clickable
            // http://stackoverflow.com/a/28454385/3086112
            view.setClickable(true);
        }

    }

    NavAdapter(ArrayList<String> Titles, ArrayList<Integer> Icons, String title, String description, Context cont){
        this.navTitles = Titles;
        this.navIcons = Icons;
        this.navTitle = title;
        this.navDesc = description;
        this.main_context = cont;

    }

    @Override
    public NavHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_item_row,parent,false);
            NavHolder navHolder = new NavHolder(v,viewType, main_context);
            return navHolder;
        }else if(viewType == TYPE_HEADER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_drawer_header,parent,false);
            NavHolder navHolder = new NavHolder(v,viewType, main_context);
            return navHolder;
        }else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return navTitles.size()+1;
    }

    @Override
    public void onBindViewHolder(NavHolder holder, final int position) {
        if(holder.HolderId == 1){
            // Menu item
            holder.textView.setText(navTitles.get(position-1));
            holder.imageView.setImageResource(navIcons.get(position-1));
            // Set selected item
            if(selectedPos == position){
                // Membuat selectable effect di menu item
                holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
            }else{
                // Mengembalikan selectableItemBackground
                // http://stackoverflow.com/a/31292299/3086112
                int[] attrs = new int[]{R.attr.selectableItemBackground};
                TypedArray typedArray = main_context.obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0,0);
                holder.itemView.setBackgroundResource(backgroundResource);
                typedArray.recycle();
            }
        }else{
            // Header
            holder.title.setText(navTitle);
            holder.description.setText(navDesc);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position)){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    private boolean isPositionHeader(int position){
        return position == 0;
    }

    public interface NavItemClickListener{
        public void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(NavItemClickListener myClickListener){
        clickListener = myClickListener;
    }
}
