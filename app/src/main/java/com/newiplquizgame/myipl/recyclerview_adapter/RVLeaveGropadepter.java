package com.newiplquizgame.myipl.recyclerview_adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.pkg.UserGroupDatum;

import java.util.ArrayList;
import java.util.List;

public class RVLeaveGropadepter extends RecyclerView.Adapter {
    Activity mallUserActivity;
    private List<UserGroupDatum> kgroupData;
    public static List<UserGroupDatum> mFilteredkgroupData;

    public RVLeaveGropadepter(Activity allUserActivity, List<UserGroupDatum> mGroupData) {
        this.mallUserActivity = allUserActivity;
        this.kgroupData = mGroupData;
        this.mFilteredkgroupData = mGroupData;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_action;
        TextView txt_title;

        public MyViewHolder(View view) {
            super(view);
            this.txt_title = view.findViewById(R.id.txt_title);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_user, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);

        myViewHolder.txt_title.setText("" + Common.isempty(mFilteredkgroupData.get(position).getFirstName()));
        myViewHolder.txt_action.setVisibility(View.GONE);
        myViewHolder.txt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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

                    List<UserGroupDatum> filteredList = new ArrayList<>();
                    for (UserGroupDatum androidVersion : kgroupData) {

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
                mFilteredkgroupData = (ArrayList<UserGroupDatum>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}