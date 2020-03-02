package com.newiplquizgame.myipl.recyclerview_adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.MyGroupInfoActivity;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.pkg.GroupDatum;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RVGroupUserListAdapter extends RecyclerView.Adapter {
    Context mallUserActivity;
    private List<GroupDatum> kgroupData;
    public static List<GroupDatum> mFilteredkgroupData;

    public RVGroupUserListAdapter(FragmentActivity allUserActivity, List<GroupDatum> mGroupData) {
        this.mallUserActivity = allUserActivity;
        this.kgroupData = mGroupData;
        this.mFilteredkgroupData = mGroupData;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button bt_action;
        TextView txt_title;
        CircleImageView iv_profile;

        public MyViewHolder(View view) {
            super(view);
            this.bt_action = view.findViewById(R.id.bt_action);
            this.txt_title = view.findViewById(R.id.txt_title);
            this.iv_profile = view.findViewById(R.id.iv_profile);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_user, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.txt_title.setText("" + Common.isempty(mFilteredkgroupData.get(position).getFirstName()));
        try {
            Glide.with(mallUserActivity).load(Common.isempty(mFilteredkgroupData.get(position).getPPhoto())).error(R.drawable.logo).into(myViewHolder.iv_profile);
        } catch (Exception e) {
        }
        if (mFilteredkgroupData.get(position).getIsApproved() == 0) {
            myViewHolder.bt_action.setText("Padding");
            myViewHolder.bt_action.setBackground(mallUserActivity.getDrawable(R.drawable.bt_bg_invite));
        } else if (mFilteredkgroupData.get(position).getIsApproved() == 1) {
            myViewHolder.bt_action.setText("Added");
            myViewHolder.bt_action.setBackground(mallUserActivity.getDrawable(R.drawable.bt_bg_add));
        } else if (mFilteredkgroupData.get(position).getIsApproved() == 2) {
            myViewHolder.bt_action.setText("Rejected");
            myViewHolder.bt_action.setBackground(mallUserActivity.getDrawable(R.drawable.bt_bg_rejected));
        }
        if (mFilteredkgroupData.get(position).getIsAdmin() == 1) {
            myViewHolder.bt_action.setText("Admin");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                myViewHolder.bt_action.setTextColor(mallUserActivity.getColor(R.color.main_color));
            }
            myViewHolder.bt_action.setBackground(mallUserActivity.getDrawable(R.drawable.bt_bg_admin));
        }
        myViewHolder.bt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFilteredkgroupData.get(position).getIsApproved() == 2) {
                    int USER_ID = mFilteredkgroupData.get(position).getUserId();
                    // showSendInvitation(USER_ID,mFilteredkgroupData.get(position).getOneSignalToken());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mFilteredkgroupData.size();
    }

    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredkgroupData = kgroupData;
                } else {

                    List<GroupDatum> filteredList = new ArrayList<>();
                    for (GroupDatum androidVersion : kgroupData) {

                        if (androidVersion.getFirstName().toLowerCase().contains(charString) && androidVersion.getEmail().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }
                    mFilteredkgroupData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredkgroupData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredkgroupData = (ArrayList<GroupDatum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}