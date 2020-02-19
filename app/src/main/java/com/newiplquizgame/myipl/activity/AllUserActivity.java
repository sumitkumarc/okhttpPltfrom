package com.newiplquizgame.myipl.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.recyclerview_adapter.RVUserListAdapter;
import com.newiplquizgame.myipl.view_pager_Adapter.VPDashboardAdapter;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.fragment.ApproveFragment;
import com.newiplquizgame.myipl.fragment.PendingFragment;
import com.newiplquizgame.myipl.fragment.RejectFragment;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

public class AllUserActivity extends AppCompatActivity implements APIcall.ApiCallListner {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_cricket,
            R.drawable.ic_cricket,
            R.drawable.ic_cricket
    };

    Toolbar toolbar;
    public static RecyclerView recycler_view;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;

    RVUserListAdapter muserListAdapter;
    public static CardView cd_search_list;
    private ProgressDialog dialog;
    private SharedPreferenceManagerFile sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_user);

        sharedPref = new SharedPreferenceManagerFile(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        recycler_view = findViewById(R.id.user_search_list);
        cd_search_list = findViewById(R.id.cd_search_list);
        cd_search_list.setVisibility(View.GONE);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());

        callAPIUserList();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        VPDashboardAdapter adapter = new VPDashboardAdapter(getSupportFragmentManager());
        adapter.addFrag(new ApproveFragment(), "Approve");
        adapter.addFrag(new PendingFragment(), "Pending");
        adapter.addFrag(new RejectFragment(), "Reject");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.serch_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.ic_searchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                muserListAdapter.getFilter().filter(newText);
                if (muserListAdapter.mFilteredkgroupData.size() == 0 || newText.length() == 0) {
                    cd_search_list.setVisibility(View.GONE);
                } else {
                    cd_search_list.setVisibility(View.VISIBLE);
                }

                return true;
            }
        }));
        return true;
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

    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_USER_LIST) {
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
        if (operationCode == APIcall.OPERATION_USER_LIST) {
            Gson gson = new Gson();
            mgruopMaster = gson.fromJson(response, GruopMaster.class);
            if (mgruopMaster.getData().size() == 0) {
                Common.displayToastMessageShort(this, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                mGroupData = new ArrayList<>();
                mGroupData = mgruopMaster.getData();
//                muserListAdapter = new RVUserListAdapter(AllUserActivity.this, mGroupData);
//                recycler_view.setAdapter(muserListAdapter);
            }
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
        }
    }

    @Override
    public void onFail(int operationCode, String response) {

    }

    private void callAPIUserList() {
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
}
