package com.home.back.bottom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.R;
import com.home.back.bottom.interfaces.OnAppSelectedLis;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class AppsListAdapter extends RecyclerView.Adapter<AppsListAdapter.MyViewHolder> {

    OnAppSelectedLis onAppSelectedLis;
    private String[] list;
    private Context context;


    public AppsListAdapter(Context context, String[] list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_one_app, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.rd_button.setText(list[position]);
        holder.rd_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    onAppSelectedLis.appSelected(position);
                }
            }
        });
    }

    public void setOnItemClickListener(OnAppSelectedLis onAppSelectedLis1) {
        this.onAppSelectedLis = onAppSelectedLis1;
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton rd_button;

        public MyViewHolder(View itemView) {
            super(itemView);
            rd_button = itemView.findViewById(R.id.radioButton);
        }
    }

}
