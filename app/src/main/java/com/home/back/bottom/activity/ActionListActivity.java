package com.home.back.bottom.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.home.back.bottom.R;
import com.home.back.bottom.adapter.ActionListAdapter;
import com.home.back.bottom.dialog.SingleChoiceDialogFragment;
import com.home.back.bottom.util.Action;
import com.home.back.bottom.util.Inter_OnItemClickListener;
import com.home.back.bottom.util.Util_Share;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionListActivity extends AppCompatActivity {
    ImageView iv_back,iv_save;
    RecyclerView rv_action_list;
    ActionListAdapter listAdapter;
    private List<Action> actions;
    int choice=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_list);
        findViews();
    }

    private void findViews() {
        iv_back=findViewById(R.id.iv_back);
        iv_save=findViewById(R.id.iv_save);
        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Util_Share.choice=choice;
                Util_Share.click_action=getIntent().getStringExtra("clickaction");
                Log.e("POSITION", "onClick: 11"+ getIntent().getStringExtra("clickaction"));
                Log.e("POSITION", "onClick: 22"+ getIntent().getStringExtra("position"));
                Util_Share.position=getIntent().getStringExtra("position");
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rv_action_list=findViewById(R.id.rv_action_list);
        actions = new ArrayList(Arrays.asList(Action.values()));
        if (Build.VERSION.SDK_INT < 21) {
            actions.remove(Action.SCREENSHOT);
            actions.remove(Action.POWER_DIALOG);
            actions.remove(Action.TASK_MANAGER_2X);
        }
        if (Build.VERSION.SDK_INT < 17) {
            actions.remove(Action.QUICK_SETTINGS);
        }
        if (Build.VERSION.SDK_INT < 24) {
            actions.remove(Action.SPLIT_SCREEN);
        }
        listAdapter = new ActionListAdapter(this, actions);
        rv_action_list.setLayoutManager(new LinearLayoutManager(ActionListActivity.this));
        rv_action_list.setAdapter(listAdapter);

        listAdapter.setOnItemClickListener(new Inter_OnItemClickListener() {
            @Override
            public void onItemClickLister(View v, int position, RadioButton rd_button) {
                choice=position;
                rd_button.setChecked(true);
            }
        });
    }
}