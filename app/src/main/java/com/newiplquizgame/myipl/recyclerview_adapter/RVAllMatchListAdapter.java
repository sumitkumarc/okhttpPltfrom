package com.newiplquizgame.myipl.recyclerview_adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.StartexamActivity;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.pkg.ScheduleLst;

import java.util.List;

public class RVAllMatchListAdapter extends RecyclerView.Adapter {
    Activity mactivity;
    List<ScheduleLst> mScheduleLsts;

    public RVAllMatchListAdapter(Activity activity, List<ScheduleLst> scheduleLst) {
        this.mactivity = activity;
        this.mScheduleLsts = scheduleLst;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button materialButton;
        TextView txt_tital;
        TextView txt_date_time;
        TextView txt_time;
        TextView txt_loc;
        TextView txt_state;
        TextView txt_stadium;

        public MyViewHolder(View view) {
            super(view);
            this.materialButton = view.findViewById(R.id.MB_pridict_now);
            this.txt_tital = view.findViewById(R.id.txt_t_name);
            this.txt_date_time = view.findViewById(R.id.txt_date_time);
            this.txt_time = view.findViewById(R.id.txt_time);
            this.txt_loc = view.findViewById(R.id.txt_loc);
            this.txt_state = view.findViewById(R.id.txt_state);
            this.txt_stadium = view.findViewById(R.id.txt_stadium);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_match_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.txt_tital.setText(Common.isempty(mScheduleLsts.get(position).getAvsB()));
        myViewHolder.txt_tital.setSelected(true);
        myViewHolder.txt_date_time.setText("Date :"+Common.getConvertedDate(mScheduleLsts.get(position).getMatchDate()));
        myViewHolder.txt_time.setText("Time : " + Common.isempty(mScheduleLsts.get(position).getMatchTime()));
        myViewHolder.txt_loc.setText("City : " + Common.isempty(mScheduleLsts.get(position).getCity()));
        myViewHolder.txt_state.setText("State : " + Common.isempty(mScheduleLsts.get(position).getState()));
        myViewHolder.txt_stadium.setText("Stadium : " + Common.isempty(mScheduleLsts.get(position).getStadium()));


        myViewHolder.materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mactivity, StartexamActivity.class);
                mactivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScheduleLsts.size();
    }
}
