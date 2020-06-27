package com.home.back.bottom.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.R;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyActionListAdapter extends RecyclerView.Adapter<MyActionListAdapter.MyViewHolder> {

    Inter_OnItemClickListener onItemClickListener;
    private List<Action> list = new ArrayList<>();
    private Context context;
    ArrayList<View> images = new ArrayList<>();
    int selected = -1;
    private static final String TAG = "MyActionListAdapter";


    public MyActionListAdapter(Context context, List<Action> listx) {
        this.context = context;
        this.list = listx;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_action, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Action c = list.get(position);
        holder.txt_tvbranname.setText(context.getString(c.getNameResId()));
        holder.imgChecked.setVisibility(View.GONE);
        images.add(holder.imgChecked);
        Log.e(TAG, "onBindViewHolder: " + c.name());

        if (c.isChecked()) {
            holder.imgChecked.setVisibility(View.VISIBLE);
            c.setChecked(false);
        } else {
            holder.imgChecked.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (selected != -1) {
                    for (View x : images) {
                        if (x != null) {
                            x.setVisibility(View.GONE);
                        }
                    }
                }
                v.setVisibility(View.VISIBLE);
                selected = position;*/
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
        ImageView imgChecked;


        public MyViewHolder(View itemView) {
            super(itemView);
            txt_tvbranname = itemView.findViewById(R.id.tvActionTitle);
            imgChecked = itemView.findViewById(R.id.imgCheckAction);

        }
    }

}
