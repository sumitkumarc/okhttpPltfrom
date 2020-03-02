package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.InviteuserlistActivity;
import com.newiplquizgame.myipl.activity.MyGroupInfoActivity;
import com.newiplquizgame.myipl.activity.NewAllUserActivity;
import com.newiplquizgame.myipl.recyclerview_adapter.RVGroupUserListAdapter;
import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.newiplquizgame.myipl.managers.communications.APIcall;
import com.newiplquizgame.myipl.pkg.GroupDatum;
import com.newiplquizgame.myipl.pkg.GruopMaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AllUserfragment extends Fragment implements APIcall.ApiCallListner {
    static Activity activity;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    LinearLayout ll_invite;
    static RecyclerView recycler_view;
    private SharedPreferenceManagerFile sharedPref;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    RVGroupUserListAdapter groupUserListAdapter;

    public AllUserfragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pending, container, false);
        findViewHolder(root);
        return root;
    }

    private void findViewHolder(View root) {
        activity = getActivity();
        sharedPref = new SharedPreferenceManagerFile(activity);
        recycler_view = root.findViewById(R.id.recycler_view);
        ll_no_data = root.findViewById(R.id.ll_no_data);
        ll_invite = root.findViewById(R.id.ll_invite);
        if (Common.GROUP_ID == 0 | Common.GROUP_ADMIN == 0) {
            ll_invite.setVisibility(View.GONE);
        } else {
            ll_invite.setVisibility(View.VISIBLE);
        }

        ll_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InviteuserlistActivity.class));
            }
        });


        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);

        callAPIUserList();
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
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onStartLoading(int operationCode) {
        if (operationCode == APIcall.OPERATION_GROUP_USER_LIST) {
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
                Common.displayToastMessageShort(getApplicationContext(), "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                mGroupData = new ArrayList<>();
                mGroupData = mgruopMaster.getData();
                if (Common.GROUP_ADMIN == 1) {
                    Comparator c = Collections.reverseOrder(new Comparator<GroupDatum>() {
                        @Override
                        public int compare(GroupDatum lhs, GroupDatum rhs) {
                            return lhs.getIsApproved().compareTo(rhs.getIsApproved());
                        }
                    });
                    Collections.sort(mGroupData, c);
                    groupUserListAdapter = new RVGroupUserListAdapter(getActivity(), mGroupData);
                } else {
                    ArrayList<GroupDatum> mfGroupData = new ArrayList<>();
                    for (int i = 0; i < mgruopMaster.getData().size(); i++) {
                        if (mGroupData.get(i).getIsApproved() == 1) {
                            mfGroupData.add(mGroupData.get(i));
                        }
                    }
                    groupUserListAdapter = new RVGroupUserListAdapter(getActivity(), mfGroupData);
                }
                recycler_view.setVisibility(View.VISIBLE);
                ll_no_data.setVisibility(View.GONE);
                recycler_view.setAdapter(groupUserListAdapter);
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
            }
            hideDialog();
        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {
        hideDialog();
    }
}
