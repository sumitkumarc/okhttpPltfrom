package com.newiplquizgame.myipl.managers.communications;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.newiplquizgame.myipl.extra.AppConstant;
import com.newiplquizgame.myipl.extra.Common;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class APIcall {

    public static int OPERATION_LOGIN = 100000;
    public static int OPERATION_REGEGISTRATION= 100002;
    public static int OPERATION_GROUP_CREATE= 100003;
    public static int OPERATION_GROUP_LIST= 100004;
    public static int OPERATION_USER_LIST= 100005;
    public static int OPERATION_GROUP_USER_LIST= 100006;
    public static int OPERATION_SEND_INVITATION= 100007;
    public static int OPERATION_ALL_QUESTION= 100008;
    public static int OPERATION_ALL_VIDEO= 100009;
    public static int OPERATION_ALL_TOURNAMENT= 100010;
    public static int OPERATION_LEAVEGROUP= 100011;
    public static int OPERATION_GROUP_ACCEPT_REJECT= 100012;

 //   public static int OPERATION_ID_GET_AVAILABLE_FLIGHT = 100002;














    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private ApiCallListner apiCallListner;
    Context moContext;
    private FormBody.Builder postBuilder;

    public APIcall(Context context) {
        moContext = context;
    }

    private boolean isPost;

    private RequestBody body;

    public void setBody(RequestBody body) {
        this.body = body;
    }

    public void isPost(boolean isPost) {
        this.isPost = isPost;
    }

    public void setPostBuilder(FormBody.Builder postBuilder) {
        this.postBuilder = postBuilder;
    }

    private String tempSessionGUID;

    public void setTempSessionId(String tempSessionGUID) {
        this.tempSessionGUID = tempSessionGUID;
    }

    public interface ApiCallListner {
        public void onStartLoading(int operationCode);

        public void onProgress(int operationCode, int progress);

        public void onSuccess(int operationCode, String response, Object customData);

        public void onFail(int operationCode, String response);
    }

    private Object customData;

    public void setCustomData(Object customData) {
        this.customData = customData;
    }

    private int operationCode;
    private String url;
    private CallApiExecute callApiExecute;

    public void execute(String url, int operationCode, ApiCallListner apiCallListner) {
        this.operationCode = operationCode;
        this.url = url;
        this.url = this.url.replaceAll(" ", "%20");
        this.apiCallListner = apiCallListner;
        this.apiCallListner.onStartLoading(operationCode);
        callApiExecute = new CallApiExecute();
        callApiExecute.execute();
    }

    private class CallApiExecute extends AsyncTask<String, String, String> {

        String errorMsg = "Offers not found";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            if (!url.contains("http")) {
                url = AppConstant.BASE_URL + url;
            }
            SharedPreferenceManagerFile sharedPreferenceManagerFile = new SharedPreferenceManagerFile(moContext);
            String userAgent = System.getProperty("http.agent");
            Log.i("userAgent", "userAgent:::" + userAgent);

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
            okHttpBuilder.connectTimeout(60000, TimeUnit.MILLISECONDS);
            okHttpBuilder.readTimeout(60000, TimeUnit.MILLISECONDS);
            OkHttpClient client = okHttpBuilder.build();//new OkHttpClient(okHttpBuilder);
        //    String token = sharedPreferenceManagerFile.getFromSharedPreference(SharedPreferenceManagerFile.GCM_TOKEN);
//            if(Common.isEmpty(token)){
//                token = "simulator";
//            }

            FormBody formBody = null;
            Request request;
            if (isPost && postBuilder != null) {
                //FormBody.Builder builder = new FormBody.Builder();
           //     postBuilder.add(AppConstant.REQUEST_DEVICE_TYPE, "A");
               // postBuilder.add(AppConstant.REQUEST_DEVICE_TOKEN, token);
             //   formBody = postBuilder.build();
                Log.i("CallApiExecute", "API Url:" + url);
            }
            //client.networkInterceptors().add(new UserAgentInterceptor("fPrLeimKr0uF3"));
            Request.Builder reqBuilder = new Request.Builder();
            String sessionGUID = sharedPreferenceManagerFile.getFromSharedPreference(SharedPreferenceManagerFile.SESSION_GUID);
            Log.e("sessionGUID", "" + sessionGUID);
            if (!Common.isEmpty(sessionGUID)) {
                reqBuilder.addHeader(AppConstant.REQUEST_HEADER_TOKEN, sessionGUID);
            } else if (!Common.isEmpty(tempSessionGUID)) {
                reqBuilder.addHeader(AppConstant.REQUEST_HEADER_TOKEN, tempSessionGUID);
            }
            if (formBody != null) {
                if (false && !TextUtils.isEmpty(sessionGUID)) {
                    /*request = new Request.Builder().addHeader(AppConstant.REQUEST_HEADER_TOKEN, sessionGUID)
                            .url(url).post(formBody)
                            .build();*/
                } else {
                    request = reqBuilder.url(url).post(formBody)
                            .build();
                }
            } else if (body != null) {
             //   RequestBody body = RequestBody.create(JSON, json);
                request = reqBuilder.url(url).post(body).build();
            } else {
//                String otherParams = AppConstant.REQUEST_DEVICE_TYPE + "=A&" + AppConstant.REQUEST_DEVICE_TOKEN + "="+token;
//                if (!url.contains(AppConstant.REQUEST_DEVICE_TYPE)) {
//                    if (url.contains("?")) {
//                        url = url + "&" + otherParams;
//                    } else {
//                        url = url + "?" + otherParams;
//                    }
//                }
                request = reqBuilder.url(url).build();
            }
            Response response = null;
            String responseStr = null;
            try {
                response = client.newCall(request).execute();
                responseStr = response.body().string();
                Log.i("CallApiExecute", "CallApiExecute Response got:" + responseStr);
                return responseStr;
            } catch (IOException e) {
                Log.i("CallApiExecute", "Exception Message:" + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!TextUtils.isEmpty(result)) {
                apiCallListner.onSuccess(operationCode, result, customData);
            } else {
                apiCallListner.onFail(operationCode, null);
            }
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            if (apiCallListner != null) {
                apiCallListner.onProgress(operationCode, Integer.parseInt(progress[0]));
            }
            super.onProgressUpdate(progress);
        }
    }
}
