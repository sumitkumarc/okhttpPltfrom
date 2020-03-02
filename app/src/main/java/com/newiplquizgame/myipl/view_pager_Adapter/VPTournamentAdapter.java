package com.newiplquizgame.myipl.view_pager_Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.AllVideoActivity;
import com.newiplquizgame.myipl.activity.StartexamActivity;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.fragment.AllMatchFragment;
import com.newiplquizgame.myipl.fragment.HomeFragment;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.ScheduleLst;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VPTournamentAdapter extends PagerAdapter {


    LayoutInflater inflater;
    Context context;
    List<ScheduleLst> mscheduleLsts;
    public SuccessResponse successResponse;

    public VPTournamentAdapter(Context mainActivity, List<ScheduleLst> mGroupData) {
        this.context = mainActivity;
        this.mscheduleLsts = mGroupData;
    }

    public interface SuccessResponse {
        void onSuccess();
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemview;
        if (position == 4) {
            itemview = inflater.inflate(R.layout.item_row_view_more, container, false);
            Button materialButton = itemview.findViewById(R.id.MB_more);
            materialButton.setText("More Matches");
            materialButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(successResponse!=null)
                    successResponse.onSuccess();

//                    Intent intent = new Intent(context, AllVideoActivity.class);
//                    intent.putExtra("POSTION", 2);
//                    context.startActivity(intent);
                }
            });
        } else {
            itemview = inflater.inflate(R.layout.item_upcomming_match, container, false);

            TextView iv_team_name_1 = itemview.findViewById(R.id.iv_team_name_1);
            iv_team_name_1.setText(Common.isempty(mscheduleLsts.get(position).getTeamAName()));

            TextView iv_team_name_2 = itemview.findViewById(R.id.iv_team_name_2);
            iv_team_name_2.setText(Common.isempty(mscheduleLsts.get(position).getTeamBName()));

            TextView txt_date_time = itemview.findViewById(R.id.txt_date_time);
            StringBuilder sdDate_time = new StringBuilder();
            sdDate_time.append(Common.isempty(mscheduleLsts.get(position).getMatchTime().toUpperCase()));
            sdDate_time.append(",");
            sdDate_time.append(Common.getConvertedDate(mscheduleLsts.get(position).getMatchDate().toUpperCase()));
            txt_date_time.setText(sdDate_time.toString());

            TextView txt_match = itemview.findViewById(R.id.txt_match);
            txt_match.setText("Match " + String.valueOf(position + 1).toUpperCase());

            CircleImageView iv_team_1 = itemview.findViewById(R.id.iv_team_1);
            Glide.with(context).load(Common.isempty(mscheduleLsts.get(position).getTeamAIcon())).into(iv_team_1);

            CircleImageView iv_team_2 = itemview.findViewById(R.id.iv_team_2);
            Glide.with(context).load(Common.isempty(mscheduleLsts.get(position).getTeamBIcon())).into(iv_team_2);

            TextView txt_stadium = itemview.findViewById(R.id.txt_stadium_state);
            StringBuilder sdStadium_state = new StringBuilder();
            sdStadium_state.append(Common.isempty(mscheduleLsts.get(position).getStadium()));
            sdStadium_state.append(",");
            sdStadium_state.append(Common.isempty(mscheduleLsts.get(position).getCity()));
            txt_stadium.setText(sdStadium_state.toString());

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
