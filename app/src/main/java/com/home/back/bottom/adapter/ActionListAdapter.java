package com.home.back.bottom.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.R;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;
import com.home.back.bottom.util.Util_Share;

import java.util.ArrayList;
import java.util.List;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.MyViewHolder> {

    Inter_OnItemClickListener onItemClickListener;
    private List<Action> list = new ArrayList<>();
    private Context context;


    public ActionListAdapter(Context context, List<Action> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tv_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.rd_button.setChecked(false);
        holder.txt_tvbranname.setText(list.get(position).name());
        holder.ll_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClickLister(v, position,holder.rd_button);
            }
        });
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
        RadioButton rd_button;
        LinearLayout ll_list;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_tvbranname = itemView.findViewById(R.id.txt_tvbranname);
            rd_button = itemView.findViewById(R.id.rd_button);
            ll_list = itemView.findViewById(R.id.ll_list);
        }
    }

}
