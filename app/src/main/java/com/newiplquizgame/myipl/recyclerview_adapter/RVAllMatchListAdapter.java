package com.newiplquizgame.myipl.recyclerview_adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.StartexamActivity;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.pkg.ScheduleLst;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVAllMatchListAdapter extends RecyclerView.Adapter {
    Activity mactivity;
    List<ScheduleLst> mScheduleLsts;

    public RVAllMatchListAdapter(Activity activity, List<ScheduleLst> scheduleLst) {
        this.mactivity = activity;
        this.mScheduleLsts = scheduleLst;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_stadium_state;
        TextView txt_match;
        TextView txt_date_time;
        TextView iv_team_name_1;
        TextView iv_team_name_2;
        CircleImageView iv_team_2;
        CircleImageView iv_team_1;

        public MyViewHolder(View view) {
            super(view);
            this.txt_match = view.findViewById(R.id.txt_match);
            this.txt_date_time = view.findViewById(R.id.txt_date_time);
            this.iv_team_name_1 = view.findViewById(R.id.iv_team_name_1);
            this.iv_team_name_2 = view.findViewById(R.id.iv_team_name_2);
            this.iv_team_2 = view.findViewById(R.id.iv_team_2);
            this.iv_team_1 = view.findViewById(R.id.iv_team_1);
            this.txt_stadium_state = view.findViewById(R.id.txt_stadium_state);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_new, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.txt_match.setText("Match " + String.valueOf(position + 1).toUpperCase());
        myViewHolder.txt_date_time.setText(Common.isempty(mScheduleLsts.get(position).getMatchTime().toUpperCase()) + " , " + Common.getConvertedDate(mScheduleLsts.get(position).getMatchDate()).toUpperCase());
        myViewHolder.iv_team_name_1.setText(Common.isempty(mScheduleLsts.get(position).getTeamAName()));
        myViewHolder.iv_team_name_2.setText(Common.isempty(mScheduleLsts.get(position).getTeamBName()));
        Glide.with(mactivity).load(Common.isempty(mScheduleLsts.get(position).getTeamAIcon())).into(myViewHolder.iv_team_1);
        Glide.with(mactivity).load(Common.isempty(mScheduleLsts.get(position).getTeamBIcon())).into(myViewHolder.iv_team_2);
        StringBuilder sdStadium_state = new StringBuilder();
        sdStadium_state.append(Common.isempty(mScheduleLsts.get(position).getStadium()));
        sdStadium_state.append(",");
        sdStadium_state.append(Common.isempty(mScheduleLsts.get(position).getCity()));
        myViewHolder.txt_stadium_state.setText(sdStadium_state.toString());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
