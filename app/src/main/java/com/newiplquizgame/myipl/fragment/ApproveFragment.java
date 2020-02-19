package com.newiplquizgame.myipl.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.newiplquizgame.myipl.R;
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
import java.util.List;

import okhttp3.RequestBody;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ApproveFragment extends Fragment implements APIcall.ApiCallListner{
    static Activity activity;
    private ProgressDialog dialog;
    LinearLayout ll_no_data;
    static RecyclerView recycler_view;
    SwipeRefreshLayout swipeToRefresh;
    Handler handler = new Handler();
    private SharedPreferenceManagerFile sharedPref;
    GruopMaster mgruopMaster;
    List<GroupDatum> mGroupData;
    RVGroupUserListAdapter groupUserListAdapter;

    public ApproveFragment() {

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
                Common.displayToastMessageShort(activity, "" + Common.isempty(mgruopMaster.getMsg()), true);
            } else {
                List<GroupDatum>  mAGroupData = new ArrayList<>();
                mGroupData = new ArrayList<>();
                mGroupData = mgruopMaster.getData();
                for(int i = 0; i < mgruopMaster.getData().size(); i++){
                    if(mGroupData.get(i).getIsApproved() == 1){
                        mAGroupData.add(mGroupData.get(i));
                    }
                }
                if(mAGroupData.size() == 0){
                    recycler_view.setVisibility(View.GONE);
                    ll_no_data.setVisibility(View.VISIBLE);
                }else {
//                    groupUserListAdapter = new RVGroupUserListAdapter(activity, mAGroupData);
//                    recycler_view.setVisibility(View.VISIBLE);
                    ll_no_data.setVisibility(View.GONE);
                    recycler_view.setAdapter(groupUserListAdapter);
                    Common.displayToastMessageShort(activity, "" + Common.isempty(mgruopMaster.getMsg()), true);

                }
            }
        }
        hideDialog();
    }

    @Override
    public void onFail(int operationCode, String response) {
        hideDialog();
    }
}
