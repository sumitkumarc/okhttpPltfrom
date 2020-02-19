package com.newiplquizgame.myipl.view_pager_Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.AllVideoActivity;
import com.newiplquizgame.myipl.activity.StartexamActivity;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.fragment.AllMatchFragment;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.ScheduleLst;

import java.util.List;

public class VPTournamentAdapter extends PagerAdapter {
    LayoutInflater inflater;
    Context context;
    List<ScheduleLst> mscheduleLsts;

    public VPTournamentAdapter(Context mainActivity, List<ScheduleLst> mGroupData) {
        this.context = mainActivity;
        this.mscheduleLsts = mGroupData;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview;
        if (position == 5) {
            itemview = inflater.inflate(R.layout.item_row_view_more, container, false);
            Button materialButton = itemview.findViewById(R.id.MB_more);
            materialButton.setText("More Match's");
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AllVideoActivity.class);
                    intent.putExtra("POSTION", 2);
                    context.startActivity(intent);
                }
            });
        } else {
            itemview = inflater.inflate(R.layout.item_upcomming_match, container, false);
            TextView txt_t_name = itemview.findViewById(R.id.txt_t_name);
            txt_t_name.setSelected(true);
            TextView txt_date_time = itemview.findViewById(R.id.txt_date_time);
            txt_date_time.setText("Date : " + Common.getConvertedDate(mscheduleLsts.get(position).getMatchDate()));
            txt_t_name.setText(Common.isempty(mscheduleLsts.get(position).getAvsB()));

            TextView txt_time = itemview.findViewById(R.id.txt_time);
            txt_time.setText("Time : " + Common.isempty(mscheduleLsts.get(position).getMatchTime()));

            TextView txt_loc = itemview.findViewById(R.id.txt_loc);
            txt_loc.setText("City : " + Common.isempty(mscheduleLsts.get(position).getCity()));

            TextView txt_state = itemview.findViewById(R.id.txt_state);
            txt_state.setText("State : " + Common.isempty(mscheduleLsts.get(position).getState()));

            TextView txt_stadium = itemview.findViewById(R.id.txt_stadium);
            txt_stadium.setText("Stadium : " + Common.isempty(mscheduleLsts.get(position).getStadium()));

            Button MB_pridict_now = itemview.findViewById(R.id.MB_pridict_now);
            MB_pridict_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, StartexamActivity.class);
                    context.startActivity(intent);
                }
            });
        }


        ((ViewPager) container).addView(itemview);
        return itemview;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == object);

    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

}
