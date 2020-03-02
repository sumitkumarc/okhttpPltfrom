package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;
import com.newiplquizgame.myipl.recyclerview_adapter.RVUserListAdapter;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

public class InviteuserlistActivity extends AppCompatActivity implements APIcall.ApiCallListner , SwipeRefreshLayout.OnRefreshListener{
    Toolbar mToolbar;
    CircleImageView profileImage;
    RecyclerView recycler_user_view;
    LinearLayout ll_no_data;
    RVUserListAdapter rvUserListAdapter;
    private ProgressDialog dialog;
    GruopMaster mgruopMaster;
    List<GroupDatum> mUserGroup;
    SwipeRefreshLayout swipeToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviteuser);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(Common.GROUP_NAME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bindViewData();

    }

    private void bindViewData() {
        recycler_user_view = findViewById(R.id.recycler_user_view);
        this.swipeToRefresh = findViewById(R.id.swipeToRefresh);
        this.swipeToRefresh.setOnRefreshListener(this);
        ll_no_data = findViewById(R.id.ll_no_data);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_user_view.setLayoutManager(gridLayoutManager);
        recycler_user_view.setItemAnimator(new DefaultItemAnimator());
        SearchView searchView = (SearchView) findViewById(R.id.search);
        searchView.requestFocus();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search Here");
        Common.hideKeyboard(InviteuserlistActivity.this);

        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                rvUserListAdapter.getFilter().filter(newText);
                if (rvUserListAdapter.mFilteredkgroupData.size() == 0 || newText.length() == 0) {
                    recycler_user_view.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
                } else {
                    recycler_user_view.setVisibility(View.VISIBLE);
                    ll_no_data.setVisibility(View.GONE);
                }
                return true;
            }
        }));
        callAPIAllUserList();
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
        if (operationCode == APIcall.OPERATION_SEND_INVITATION) {
            showDialog();
        }
        if (operationCode == APIcall.OPERATION_USER_LIST) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        if (operationCode == APIcall.OPERATION_USER_LIST) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getStatus() == 4) {
                ll_no_data.setVisibility(View.VISIBLE);
                recycler_user_view.setVisibility(View.GONE);
                swipeToRefresh.setVisibility(View.GONE);
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else if (mgruopMaster.getStatus() == 0) {
                ll_no_data.setVisibility(View.GONE);
                recycler_user_view.setVisibility(View.VISIBLE);

                swipeToRefresh.setRefreshing(false);
                mUserGroup = mgruopMaster.getData();
                rvUserListAdapter = new RVUserListAdapter(this, mUserGroup);
                recycler_user_view.setAdapter(rvUserListAdapter);
                recycler_user_view.getViewTreeObserver().addOnPreDrawListener(
                        new ViewTreeObserver.OnPreDrawListener() {
                            @Override
                            public boolean onPreDraw() {
                                recycler_user_view.getViewTreeObserver().removeOnPreDrawListener(this);
                                for (int i = 0; i < recycler_user_view.getChildCount(); i++) {
                                    View v = recycler_user_view.getChildAt(i);
                                    v.setAlpha(0.0f);
                                    v.animate().alpha(1.0f)
                                            .setDuration(300)
                                            .setStartDelay(i * 50)
                                            .start();
                                }

                                return true;
                            }
                        });

            } else if (mgruopMaster.getStatus() == 1) {
                ll_no_data.setVisibility(View.VISIBLE);
                recycler_user_view.setVisibility(View.GONE);
                swipeToRefresh.setVisibility(View.GONE);
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            }
            hideDialog();
        }
        if (operationCode == APIcall.OPERATION_SEND_INVITATION) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getStatus().equals("0")) {
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            }
            hideDialog();
            callAPIAllUserList();
        }
        hideDialog();
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.my_group_info, menu);
//        MenuItem menuItem = menu.findItem(R.id.menu_two);
//        MenuItem pop_edit_group = menu.findItem(R.id.pop_edit_group);
//        MenuItem pop_invite_group = menu.findItem(R.id.pop_invite_group);
//        MenuItem pop_leave_group = menu.findItem(R.id.pop_leave_group);
//        if (Common.GROUP_ADMIN == 1) {
//            pop_edit_group.setVisible(true);
//            pop_invite_group.setVisible(true);
//            pop_leave_group.setVisible(false);
//        } else {
//            pop_leave_group.setVisible(false);
//            pop_edit_group.setVisible(false);
//            pop_invite_group.setVisible(false);
//        }
//        View view = MenuItemCompat.getActionView(menuItem);
//        profileImage = view.findViewById(R.id.toolbar_profile_image);
//        Glide.with(this).load(Common.GROUP_URL).into(profileImage);
//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(InviteuserlistActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }

    public void showSendInvitation(int USER_ID, String oneSignalToken) {
        try {
            OneSignal.postNotification(new JSONObject("{'contents': {'en':'" + Common.GROUP_DES + "'}, " +
                            "'include_player_ids': ['" + oneSignalToken + "'], " +
                            "'headings': {'en': '" + Common.GROUP_NAME + "joint GROUP" + "'}, " +
                            "'data': {'GROUP_IMAGE_URL': '" + Common.GROUP_URL + "','GROUP_ID': '" + Common.GROUP_ID + "','GROUP_NAME': '" + Common.GROUP_NAME + "','GROUP_DES': '" + Common.GROUP_DES + "'}," +
                            "'buttons':[{'id': 'id1', 'text': ''}, {'id':'id2', 'text': 'Joint Group'}]}"),

                    new OneSignal.PostNotificationResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Success: " + response);
                        }

                        @Override
                        public void onFailure(JSONObject response) {
                            Log.e("OneSignalExample", "postNotification Failure: " + response);
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("OneSignalExample", "postNotification Error: " + e);
        }
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
    public void onRefresh() {
        swipeToRefresh.setRefreshing(true);
        recycler_user_view.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callAPIAllUserList();
            }
        }, 500);
    }
}
