package com.newiplquizgame.myipl.recyclerview_adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.AllUserActivity;
import com.newiplquizgame.myipl.activity.NewAllUserActivity;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.UserGroupDatum;

import java.util.ArrayList;
import java.util.List;

public class RVUserListAdapter extends RecyclerView.Adapter {
    NewAllUserActivity mallUserActivity;
    private List<GroupDatum> kgroupData;
    public static List<GroupDatum> mFilteredkgroupData;

    public RVUserListAdapter(NewAllUserActivity allUserActivity, List<GroupDatum> mGroupData) {
        this.mallUserActivity = allUserActivity;
        this.kgroupData = mGroupData;
        this.mFilteredkgroupData = mGroupData;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Button bt_action;
        TextView txt_title;

        public MyViewHolder(View view) {
            super(view);
            this.bt_action = view.findViewById(R.id.bt_action);
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.txt_title.setText("" + Common.isempty(mFilteredkgroupData.get(position).getFirstName()));
        myViewHolder.bt_action.setText("Invite");
        myViewHolder.bt_action.setBackground(mallUserActivity.getDrawable(R.drawable.bt_bg_invite));
        myViewHolder.bt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int USER_ID = mFilteredkgroupData.get(position).getUserId();
                mallUserActivity.showSendInvitation(USER_ID);
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
