package com.home.back.bottom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.home.back.bottom.IconsModel;
import com.home.back.bottom.R;
import com.home.back.bottom.util.Inter_OnItemClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyColorsListAdapter extends RecyclerView.Adapter<MyColorsListAdapter.MyViewHolder> {

    Inter_OnItemClickListener onItemClickListener;
    private List<IconsModel> list;
    private Context context;
    private static final String TAG = "MyColorsListAdapter";


    public MyColorsListAdapter(Context context, List<IconsModel> listx) {
        this.context = context;
        this.list = listx;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_button_color, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        IconsModel icon = list.get(position);
        holder.imgIcon.setImageResource(icon.getIconResId());

        if (icon.isChecked()) {
            holder.imgChecked.setVisibility(View.VISIBLE);
            icon.setChecked(false);
        } else holder.imgChecked.setVisibility(View.INVISIBLE);

    }

    public void setOnItemClickListener(Inter_OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon, imgChecked;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            imgChecked = itemView.findViewById(R.id.imgChecked);
        }
    }

}
