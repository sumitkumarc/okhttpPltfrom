package com.newiplquizgame.myipl.recyclerview_adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.DashboardActivity;
import com.newiplquizgame.myipl.activity.MyGroupInfoActivity;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.fragment.MyGropsFragment;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.UserGroupDatum;
import com.newiplquizgame.myipl.pkg.UserGroupMaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RVGroupListAdapter extends RecyclerView.Adapter implements APIcall.ApiCallListner {
    Activity mactivity;
    List<GroupDatum> mGroupData;
    Dialog mdialog;

    UserGroupMaster userGroupMaster;
    List<UserGroupDatum> userGroupDatumList;

    RVLeaveGropadepter leaveGropadepter;
    RecyclerView d_recycler_view;

    public RVGroupListAdapter(Activity activity, List<GroupDatum> groupData) {
        this.mactivity = activity;
        this.mGroupData = groupData;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageButton bt_pop_menu;
        TextView txt_title;
        TextView txt_des;
        TextView txt_invite;
        CircleImageView iv_profile;

        public MyViewHolder(View view) {
            super(view);
//            this.bt_pop_menu = view.findViewById(R.id.bt_pop_menu);
            this.txt_title = view.findViewById(R.id.txt_title);
            this.txt_des = view.findViewById(R.id.txt_des);
//            this.txt_invite = view.findViewById(R.id.txt_invite);
            this.iv_profile = view.findViewById(R.id.iv_profile);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_my_gruops, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        return myViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = ((MyViewHolder) holder);
        myViewHolder.txt_title.setText("" + Common.isempty(mGroupData.get(position).getGroupName()));
        myViewHolder.txt_des.setText("" + Common.isempty(mGroupData.get(position).getDescription()));
        try {
            Glide.with(mactivity).load(Common.isempty(mGroupData.get(position).getIcon())).placeholder(R.drawable.logo).into(myViewHolder.iv_profile);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                myViewHolder.iv_profile.setImageDrawable(mactivity.getDrawable(R.drawable.logo));
            }
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.GROUP_ID = mGroupData.get(position).getGroupId();
                Common.GROUP_ADMIN = mGroupData.get(position).getIsAdmin();
                Common.USER_ID = mGroupData.get(position).getGroupUserId();
                Common.GROUP_DES = mGroupData.get(position).getDescription();
                Common.GROUP_URL = mGroupData.get(position).getIcon();
                Common.GROUP_NAME = mGroupData.get(position).getGroupName();
                Intent N_intent = new Intent(mactivity, MyGroupInfoActivity.class);
                mactivity.startActivity(N_intent);
            }
        });
//        if (mGroupData.get(position).getIsActive() == true) {
//            myViewHolder.bt_pop_menu.setVisibility(View.VISIBLE);
//            myViewHolder.txt_invite.setVisibility(View.GONE);
//            myViewHolder.bt_pop_menu.setVisibility(View.GONE);
//            myViewHolder.bt_pop_menu.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MenuBuilder menuBuilder = new MenuBuilder(mactivity);
//                    MenuInflater inflater = new MenuInflater(mactivity);
//                    inflater.inflate(R.menu.group_poupup_menu, menuBuilder);
//                    MenuPopupHelper optionsMenu = new MenuPopupHelper(mactivity, menuBuilder, v);
//                    optionsMenu.setForceShowIcon(true);
//                    menuBuilder.setCallback(new MenuBuilder.Callback() {
//                        int G_id = mGroupData.get(position).getGroupId();
//
//                        @Override
//                        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
//                            switch (item.getItemId()) {
//                                case R.id.pop_leave_group:
//                                    Common.GROUP_ID = G_id;
//                                    Common.GROUP_DES = mGroupData.get(position).getDescription();
//                                    Common.GROUP_NAME = mGroupData.get(position).getGroupName();
//                                    //    ShowLeaveGroupDialog();
//                                    return true;
//                                case R.id.pop_edit_group:
//                                    Common.GROUP_ID = G_id;
//                                    Common.GROUP_DES = mGroupData.get(position).getDescription();
//                                    Common.GROUP_NAME = mGroupData.get(position).getGroupName();
//                                    showDialogCreatGroup();
//
//                                    return true;
//                                case R.id.pop_all_member:
//                                    Common.GROUP_ID = G_id;
//                                    Intent N_intent = new Intent(mactivity, NewAllUserActivity.class);
//                                    mactivity.startActivity(N_intent);
//                                    return true;
//                                case R.id.pop_point_table:
//                                    Common.GROUP_ID = G_id;
//                                    Common.GROUP_NAME = mGroupData.get(position).getGroupName();
//                                    Intent P_T_intent = new Intent(mactivity, SingleGroupActivity.class);
//                                    mactivity.startActivity(P_T_intent);
//                                    return true;
//
//                                default:
//                                    return false;
//                            }
//                        }
//
//                        @Override
//                        public void onMenuModeChange(MenuBuilder menu) {
//                        }
//                    });
//                    optionsMenu.show();
//                }
//            });
//        } else {
//            myViewHolder.bt_pop_menu.setVisibility(View.GONE);
//            myViewHolder.txt_invite.setVisibility(View.VISIBLE);
//            myViewHolder.txt_invite.setText("Accept");
//            myViewHolder.txt_invite.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Common.GROUP_DES = mGroupData.get(position).getDescription();
//                    Common.GROUP_NAME = mGroupData.get(position).getGroupName();
//                    showAcceptRequestDialog();
//                }
//            });
//        }


    }

    private void showAcceptRequestDialog() {
        final Dialog mdialog = new Dialog(mactivity);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.invitation_to_join);
        mdialog.setCancelable(true);
        ((TextView) mdialog.findViewById(R.id.ed_gruop_name)).setText(Common.GROUP_NAME);
        ((TextView) mdialog.findViewById(R.id.txt_des)).setText(Common.GROUP_DES);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ((ImageButton) mdialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });
        mdialog.show();
        mdialog.getWindow().setAttributes(lp);
    }

//    private void ShowLeaveGroupDialog() {
//        final Dialog mdialog = new Dialog(mactivity);
//        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mdialog.setContentView(R.layout.leave_and_congratulation);
//        mdialog.setCancelable(true);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(mdialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//        ((TextView) mdialog.findViewById(R.id.txt_title)).setText(Common.GROUP_NAME);
//        ((TextView) mdialog.findViewById(R.id.txt_des)).setText(Common.GROUP_DES);
//
//
//        d_recycler_view = mdialog.findViewById(R.id.recycler_view);
//
//        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mactivity);
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        d_recycler_view.setLayoutManager(gridLayoutManager);
//        d_recycler_view.setItemAnimator(new DefaultItemAnimator());
//        callAPIGroupUserList();
//        ((ImageButton) mdialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mdialog.dismiss();
//            }
//        });
//        mdialog.show();
//        mdialog.getWindow().setAttributes(lp);
//    }

    private void callAPIGroupUserList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.GROUP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_GROUP_USER_LIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_GROUP_USER_LIST, this);
    }

    private void showDialogCreatGroup() {
        mdialog = new Dialog(mactivity);
        mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mdialog.setContentView(R.layout.pop_create_group);
        mdialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mdialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final EditText ed_gruop_name = mdialog.findViewById(R.id.ed_gruop_name);
        final EditText ed_gruop_des = mdialog.findViewById(R.id.ed_gruop_des);
        ed_gruop_name.setText(Common.isempty(Common.GROUP_NAME));
        ed_gruop_des.setText(Common.isempty(Common.GROUP_DES));

        ((Button) mdialog.findViewById(R.id.bt_send_invitation)).setText("Update Group");

        ((Button) mdialog.findViewById(R.id.bt_send_invitation)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_gruop_name.getText().toString().equals("") || ed_gruop_name.getText().toString() == null) {
                    Common.displayToastMessageShort(mactivity, "Enter your name.", true);
                    return;
                }
                if (ed_gruop_des.getText().toString().equals("") || ed_gruop_des.getText().toString() == null) {
                    Common.displayToastMessageShort(mactivity, "Enter your description.", true);
                    return;
                }
                callAPItoCreatGroup(ed_gruop_name.getText().toString(), ed_gruop_des.getText().toString(), Common.GROUP_ID);
                mdialog.dismiss();
            }
        });
        ((ImageButton) mdialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdialog.dismiss();
            }
        });


        mdialog.show();
        mdialog.getWindow().setAttributes(lp);
    }

    private void callAPItoCreatGroup(String g_name, String g_des, Integer groupId) {
        Common.hideKeyboard(mactivity);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("GroupId", groupId);
            jsonObject.put("GroupName", g_name);
            jsonObject.put("Icon", "##### #### ##### #### #### #### #### #");
            jsonObject.put("Description", g_des);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_GROUP_CREATE;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_GROUP_CREATE, this);
    }

    @Override
    public int getItemCount() {
        return mGroupData.size();
    }

    @Override
    public void onStartLoading(int operationCode) {

    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_GROUP_USER_LIST) {
            Gson gson = new Gson();
            userGroupMaster = gson.fromJson(response, UserGroupMaster.class);
            if (userGroupMaster.getStatus() == 0) {
                userGroupDatumList = new ArrayList<>();
                userGroupDatumList = userGroupMaster.getData();
                d_recycler_view.setVisibility(View.VISIBLE);
                leaveGropadepter = new RVLeaveGropadepter(mactivity, userGroupDatumList);
                d_recycler_view.setAdapter(leaveGropadepter);
            } else {
                Toast.makeText(mactivity, "" + userGroupMaster.getMsg(), Toast.LENGTH_SHORT).show();
            }
            // hideDialog();

        }
        if (operationCode == APIcall.OPERATION_GROUP_CREATE) {
            Gson gson = new Gson();
            userGroupMaster = gson.fromJson(response, UserGroupMaster.class);
            if (userGroupMaster.getStatus() == 0) {
                Common.displayToastMessageShort(mactivity, "" + Common.isempty(userGroupMaster.getMsg()), true);
                MyGropsFragment homeFragment = new MyGropsFragment();
                FragmentTransaction hom_Fragment = ((DashboardActivity) mactivity).getSupportFragmentManager().beginTransaction();
                hom_Fragment.replace(R.id.nav_host_fragment, homeFragment);
                hom_Fragment.commit();
            } else {

            }
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

}

