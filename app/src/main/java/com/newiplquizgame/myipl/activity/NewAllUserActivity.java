package com.newiplquizgame.myipl.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.recyclerview_adapter.RVGroupUserListAdapter;
import com.newiplquizgame.myipl.recyclerview_adapter.RVUserListAdapter;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

public class NewAllUserActivity extends AppCompatActivity implements APIcall.ApiCallListner {
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    List<GroupDatum> mUserGroup;
    RVGroupUserListAdapter groupUserListAdapter;

    RVUserListAdapter rvUserListAdapter;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    static RecyclerView recycler_view;
    RecyclerView recycler_user_view;
    TextView ed_gruop_name;
    Button ll_leave_group;
    Toolbar toolbar;
    CircleImageView iv_ed_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_all_user);
        Bindview();
    }

    private void Bindview() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recycler_view = findViewById(R.id.recycler_view);
        ll_leave_group = findViewById(R.id.ll_leave_group);
        ll_leave_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAPILeaveGroup();
            }
        });
        ll_no_data = findViewById(R.id.ll_no_data);
        ed_gruop_name = findViewById(R.id.ed_gruop_name);
        ed_gruop_name.setText(Common.GROUP_NAME);
        recycler_user_view = findViewById(R.id.recycler_user_view);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_user_view.setLayoutManager(gridLayoutManager);
        recycler_user_view.setItemAnimator(new DefaultItemAnimator());
        recycler_user_view.setVisibility(View.GONE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setVisibility(View.GONE);

        ll_no_data.setVisibility(View.GONE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.onActionViewExpanded(); //new Added line
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Here");

        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvUserListAdapter.getFilter().filter(newText);
                recycler_user_view.setVisibility(View.VISIBLE);
                recycler_view.setVisibility(View.GONE);
                if (rvUserListAdapter.mFilteredkgroupData.size() == 0 || newText.length() == 0) {
                    recycler_user_view.setVisibility(View.GONE);
                    recycler_view.setVisibility(View.VISIBLE);
                } else {
                    recycler_user_view.setVisibility(View.VISIBLE);
                    recycler_view.setVisibility(View.GONE);
                }
                return true;
            }
        }));
        iv_ed_profile = findViewById(R.id.iv_ed_profile);
        try {
            Glide.with(NewAllUserActivity.this).load(Common.GROUP_URL).centerCrop().placeholder(R.drawable.login_back).into(iv_ed_profile);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                iv_ed_profile.setImageDrawable(getDrawable(R.drawable.login_back));
            }
        }
        if (Common.GROUP_ADMIN == 1) {
            iv_ed_profile.setVisibility(View.VISIBLE);
            searchView.setVisibility(View.VISIBLE);
            ll_leave_group.setVisibility(View.GONE);
        } else {
            iv_ed_profile.setVisibility(View.GONE);
            searchView.setVisibility(View.GONE);
            ll_leave_group.setVisibility(View.VISIBLE);
        }
        callAPIAllUserList();
        callAPIUserList();

    }

    private void callAPIAllUserList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.GROUP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url = AppConstant.GET_USER_LIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url, APIcall.OPERATION_USER_LIST, this);
    }

    private void callAPIUserList() {
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

    private void callAPILeaveGroup() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Id", Common.GROUP_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_LEAVEGROUP;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_LEAVEGROUP, this);

    }

    private void showDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void hideDialog() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_GROUP_USER_LIST) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_SEND_INVITATION) {
            showDialog();
        }


    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_GROUP_USER_LIST) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getStatus() != 0) {
                recycler_view.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.VISIBLE);
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                mGroupData = new ArrayList<>();
                mGroupData = mgruopMaster.getData();


//                for(int theInt : mGroupData) {
//                    System.out.println(theInt);
//                }


//                for(int i = 0; i < mgruopMaster.getData().size(); i++){
//                    if(mGroupData.get(i).getIsApproved() == 0){
//                        mAGroupData.add(mGroupData.get(i));
//                    }
//                }

//
//                for(int i = 0; i < mgruopMaster.getData().size(); i++){
//                    if(mGroupData.get(i).getIsApproved() == 1){
//                        mAGroupData.add(mGroupData.get(i));
//                    }
//                }
//                for(int i = 0; i < mgruopMaster.getData().size(); i++){
//                    if(mGroupData.get(i).getIsApproved() == 2){
//                        mAGroupData.add(mGroupData.get(i));
//                    }
//                }


//                Collections.sort(mGroupData, new Comparator<GroupDatum>() {
//                    @Override
//                    public int compare(GroupDatum lhs, GroupDatum rhs) {
//                        return lhs.getIsApproved().compareTo(rhs.getIsApproved());
//                    }
//                });

                groupUserListAdapter = new RVGroupUserListAdapter(this, mGroupData);
                recycler_view.setVisibility(View.VISIBLE);
                recycler_user_view.setVisibility(View.GONE);
                ll_no_data.setVisibility(View.GONE);
                recycler_view.setAdapter(groupUserListAdapter);
            }
            hideDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_LIST) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getData().size() == 0) {
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                mUserGroup = mgruopMaster.getData();
                rvUserListAdapter = new RVUserListAdapter(this, mUserGroup);
                recycler_user_view.setAdapter(rvUserListAdapter);
            }
            hideDialog();
        }
        if (operationCode == APIcall.OPERATION_SEND_INVITATION) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getStatus().equals("0")) {
                OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
                String userID = status.getSubscriptionStatus().getUserId();
                String IMAGEURL = "data:image/jpeg;base64";
//                String userID = "2b9c5829-8099-497f-9546-2ca9f89c90e6";
                boolean isSubscribed = status.getSubscriptionStatus().getSubscribed();
                if (!isSubscribed)
                    return;

                try {
                    OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + Common.GROUP_DES + "'}, " +
                                    "'key1':['"+ Common.GROUP_ID +"']"+
                                    "'include_player_ids': ['" + userID + "'], " +
                                    "'headings': {'en': ' " + Common.GROUP_NAME + "joint GROUP" + "'}, " +
                                    "'data': {'openURL': ' " + Common.GROUP_URL + "'}," +
                                    "'buttons':[{'id': 'id1', 'text': ''}, {'id':'id2', 'text': 'Joint Group'}]}"),


                            new OneSignal.PostNotificationResponseHandler() {
                                @Override
                                public void onSuccess(JSONObject response) {
                                    Log.i("OneSignalExample", "postNotification Success: " + response);
                                }

                                @Override
                                public void onFailure(JSONObject response) {
                                    Log.e("OneSignalExample", "postNotification Failure: " + response);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            }
            hideDialog();
            callAPIAllUserList();
            callAPIUserList();
        }
        if (operationCode == APIcall.OPERATION_LEAVEGROUP) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getStatus().equals("0")) {
                onBackPressed();
                finish();
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            }
            hideDialog();
        }
        hideDialog();
    }

    public void showSendInvitation(int USER_ID) {
        recycler_user_view.setVisibility(View.GONE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("GroupId", Common.GROUP_ID);
            jsonObject.put("UserId", USER_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(APIcall.JSON, jsonObject + "");
        String url2 = AppConstant.GET_SEND_INVITATION;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(true);
        apIcall.setBody(body);
        apIcall.execute(url2, APIcall.OPERATION_SEND_INVITATION, this);
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
