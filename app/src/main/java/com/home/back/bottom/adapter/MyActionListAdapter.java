package com.home.back.bottom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.R;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MyActionListAdapter extends RecyclerView.Adapter<MyActionListAdapter.MyViewHolder> {

    Inter_OnItemClickListener onItemClickListener;
    private List<Action> list = new ArrayList<>();
    private Context context;


    public MyActionListAdapter(Context context, List<Action> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_action, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Action c = list.get(position);
        holder.txt_tvbranname.setText(c.name());
        holder.imgChecked.setVisibility(View.GONE);

    }

    public void setOnItemClickListener(Inter_OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_tvbranname;
        ImageView imgChecked;


        public MyViewHolder(View itemView) {
            super(itemView);
            txt_tvbranname = itemView.findViewById(R.id.tvActionTitle);
            imgChecked = itemView.findViewById(R.id.imgCheckAction);

        }
    }

}
