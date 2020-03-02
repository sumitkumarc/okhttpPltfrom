package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.recyclerview_adapter.RVGroupListAdapter;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class MyGropsFragment extends Fragment implements APIcall.ApiCallListner, SwipeRefreshLayout.OnRefreshListener {

    static RecyclerView recycler_view;
    SwipeRefreshLayout swipeToRefresh;
    Handler handler = new Handler();
    static Activity activity;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    static RVGroupListAdapter groupListAdapter;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gruops, container, false);
        findViewHolder(root);
        return root;
    }

    private void findViewHolder(View root) {
        activity = getActivity();
        recycler_view = root.findViewById(R.id.recycler_view);
        ll_no_data = root.findViewById(R.id.ll_no_data);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        this.swipeToRefresh = root.findViewById(R.id.swipeToRefresh);
        this.swipeToRefresh.setOnRefreshListener(this);
        recycler_view.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);
        callAPIGroupList();
    }

    private void showDialog() {
        dialog = new ProgressDialog(getContext());
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
        if (operationCode == APIcall.OPERATION_GROUP_LIST) {
            showDialog();
        }
    }

    @Override
    public void onProgress(int operationCode, int progress) {

    }

    @Override
    public void onSuccess(int operationCode, String response, Object customData) {
        try {
            if (operationCode == APIcall.OPERATION_GROUP_LIST) {
                Gson gson = new Gson();
                mgruopMaster = gson.fromJson(response, GruopMaster.class);
                if (mgruopMaster.getStatus() == 0) {
                    mGroupData = new ArrayList<>();
                    mGroupData = mgruopMaster.getData();
                    recycler_view.setVisibility(View.VISIBLE);
                    ll_no_data.setVisibility(View.GONE);
                    swipeToRefresh.setRefreshing(false);
                    groupListAdapter = new RVGroupListAdapter(activity, mGroupData);
                    recycler_view.setAdapter(groupListAdapter);
                    recycler_view.getViewTreeObserver().addOnPreDrawListener(
                            new ViewTreeObserver.OnPreDrawListener() {
                                @Override
                                public boolean onPreDraw() {
                                    recycler_view.getViewTreeObserver().removeOnPreDrawListener(this);
                                    for (int i = 0; i < recycler_view.getChildCount(); i++) {
                                        View v = recycler_view.getChildAt(i);
                                        v.setAlpha(0.0f);
                                        v.animate().alpha(1.0f)
                                                .setDuration(300)
                                                .setStartDelay(i * 50)
                                                .start();
                                    }

                                    return true;
                                }
                            });
                } else {
                    recycler_view.setVisibility(View.GONE);
                    swipeToRefresh.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
                    Common.displayToastMessageShort(activity, "" + Common.isempty(mgruopMaster.getMsg()), true);
                }
                hideDialog();
            }
            hideDialog();
        } catch (Exception e) {
            hideDialog();
            recycler_view.setVisibility(View.GONE);
            swipeToRefresh.setVisibility(View.GONE);
            ll_no_data.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onRefresh() {
        swipeToRefresh.setRefreshing(true);
        recycler_view.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);
        this.handler.postDelayed(new Runnable() {
            public void run() {
                callAPIGroupList();
            }
        }, 500);
    }

    private void callAPIGroupList() {
        String url = AppConstant.GET_GROUP_LIST;
        APIcall apIcall = new APIcall(getApplicationContext());
        apIcall.isPost(false);
        apIcall.execute(url, APIcall.OPERATION_GROUP_LIST, this);
    }

    @Override
    public void onFail(int operationCode, String response) {
        hideDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}