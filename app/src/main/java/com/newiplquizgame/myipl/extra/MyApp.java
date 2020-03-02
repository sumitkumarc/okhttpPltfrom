package com.newiplquizgame.myipl.extra;

import android.app.Application;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.facebook.appevents.AppEventsLogger;
import com.newiplquizgame.myipl.R;
import com.newiplquizgame.myipl.activity.DashboardActivity;
import com.newiplquizgame.myipl.activity.StartActivity;
import com.newiplquizgame.myipl.activity.StartexamActivity;
import com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import static com.newiplquizgame.myipl.managers.SharedPreferenceManagerFile.ISLOGIN;

public class MyApp extends Application {
    Boolean islogin ;
    SharedPreferenceManagerFile sharedPref;
    Object activityToLaunch;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPref = new SharedPreferenceManagerFile(this);
        islogin = sharedPref.getBooleanSharedPreference(ISLOGIN);
        OneSignal.setSubscription(true);
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(new ExampleNotificationReceivedHandler())
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        sharedPref.setStringSharedPreference(SharedPreferenceManagerFile.ONESIGNAL_TOKeN, status.getSubscriptionStatus().getUserId());
        AppEventsLogger.activateApp(this);
    }

    private class ExampleNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
        @Override
        public void notificationReceived(OSNotification notification) {
            JSONObject data = notification.payload.additionalData;
            String notificationID = notification.payload.notificationID;
//            String title = notification.payload.title;
//            String body = notification.payload.body;
//            String smallIcon = notification.payload.smallIcon;
//            String largeIcon = notification.payload.largeIcon;
//            String bigPicture = notification.payload.bigPicture;
//            String smallIconAccentColor = notification.payload.smallIconAccentColor;
//            String sound = notification.payload.sound;
//            String ledColor = notification.payload.ledColor;
//            int lockScreenVisibility = notification.payload.lockScreenVisibility;
//            String groupKey = notification.payload.groupKey;
//            String groupMessage = notification.payload.groupMessage;
//            String fromProjectNumber = notification.payload.fromProjectNumber;
//            String rawPayload = notification.payload.rawPayload;
            String customKey;
            Log.i("OneSignalExample", "NotificationID received: " + notificationID);
            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: " + customKey);
            }
        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenResult result) {
            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject data = result.notification.payload.additionalData;
            if (islogin == true) {
                activityToLaunch = DashboardActivity.class;
            } else {
                activityToLaunch = StartActivity.class;
            }
            if (data != null) {
                Common.GROUP_NAME = data.optString("customkey", null);
                Common.GROUP_NAME = data.optString("customkey", null);
                if (data.optString("GROUP_NAME", null) != null)
                    Common.GROUP_NAME = data.optString("GROUP_NAME", null);
                if (data.optString("GROUP_DES", null) != null)
                    Common.GROUP_DES = data.optString("GROUP_DES", null);
                if (data.optString("GROUP_ID", null) != null)
                    Common.GROUP_ID = Integer.valueOf(data.optString("GROUP_ID", null));
                if (data.optString("GROUP_IMAGE_URL", null) != null)
                    Common.GROUP_URL = data.optString("GROUP_IMAGE_URL", null);
            }

            Intent intent = new Intent(getApplicationContext(), (Class<?>) activityToLaunch);
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("OPEN_FRAGMENT", 1);
            startActivity(intent);
        }
    }
}
