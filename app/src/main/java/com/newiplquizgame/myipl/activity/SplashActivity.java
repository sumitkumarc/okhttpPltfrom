package com.newiplquizgame.myipl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.onesignal.OneSignal;

import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.ISLOGIN;

public class SplashActivity extends AppCompatActivity {

    Boolean islogin = false;
    SharedPreferenceManagerFile sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        sharedPref = new SharedPreferenceManagerFile(this);
        islogin = sharedPref.getBooleanSharedPreference(ISLOGIN);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (islogin == true) {
                    Intent intent = new Intent(SplashActivity.this, DashboardActivity.class);
                    intent.putExtra("OPEN_FRAGMENT", 0);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                    SplashActivity.this.finish();
                }


            }
        }, 2000);
    }
}
