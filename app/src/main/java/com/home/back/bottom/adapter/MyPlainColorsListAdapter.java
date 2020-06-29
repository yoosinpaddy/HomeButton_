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

public class MyPlainColorsListAdapter extends RecyclerView.Adapter<MyPlainColorsListAdapter.MyViewHolder> {

    Inter_OnItemClickListener onItemClickListener;
    private List<IconsModel> list;
    private Context context;
    private static final String TAG = "MyPlainColorsListAdapter";
    int[] colorDrawables;


    public MyPlainColorsListAdapter(Context context, List<IconsModel> listx) {
        this.context = context;
        this.list = listx;
        colorDrawables = new int[]{
                R.drawable.disk_red,
                R.drawable.disk_blue,
                R.drawable.disk_green,
                R.drawable.disk_purple,
                R.drawable.disk_white,
                R.drawable.disk_black,
                R.drawable.disk_amber,
                R.drawable.disk_orange,
                R.drawable.disk_pink,
                R.drawable.disk_lime,
                R.drawable.disk_teal,
                R.drawable.disk_indigo,
                R.drawable.icon_12,
                R.drawable.icon_13,
                R.drawable.icon_14,
                R.drawable.icon_15,
                R.drawable.icon_16,
                R.drawable.icon_17,
                R.drawable.icon_18,
                R.drawable.icon_19,
                R.drawable.icon_20,
                R.drawable.icon_21,
                R.drawable.icon_22,
                R.drawable.icon_23,
                R.drawable.icon_24,
                R.drawable.icon_25,
                R.drawable.icon_26,
                R.drawable.icon_27,
                R.drawable.icon_28,
                R.drawable.icon_29,
                R.drawable.icon_11
        };
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_button_color_plain, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        IconsModel plainColor = list.get(position);
        holder.imgIcon.setImageDrawable(context.getResources().getDrawable(colorDrawables[position]));

        if (plainColor.isChecked()) {
            holder.imgChecked.setVisibility(View.VISIBLE);
            plainColor.setChecked(false);
        } else holder.imgChecked.setVisibility(View.GONE);

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
            imgIcon = itemView.findViewById(R.id.imgColorZ);
            imgChecked = itemView.findViewById(R.id.imgColorZChecked);
        }
    }

}
