package com.newiplquizgame.myipl.extra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Common {
    public static Toast toast;
    public static Integer GROUP_ID = 0;
    public static String GROUP_NAME = "";
    public static Integer USER_ID = 0;
    public static Integer GROUP_ADMIN = 0;
    public static String GROUP_DES = "";
    public static String GROUP_URL = "";

    public static String EMAIL_PATTEN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static String getString(InputStream paramInputStream)
            throws IOException {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
        StringBuffer localStringBuffer = new StringBuffer();
        char[] arrayOfChar = new char[2048];
        for (int i = localBufferedReader.read(arrayOfChar); ; i = localBufferedReader.read(arrayOfChar)) {
            if (i <= 0)
                return localStringBuffer.toString();
            localStringBuffer.append(arrayOfChar, 0, i);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean isEmpty(String value) {
        boolean isEmpty = TextUtils.isEmpty(value);
        if (!isEmpty) {
            isEmpty = value.toLowerCase().equals("null");
        }
        return isEmpty;
    }

    public static boolean isInternetAvailable(Context foContext) {
        NetworkInfo loNetInfo = ((ConnectivityManager) foContext
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();

        if (loNetInfo != null)
            if (loNetInfo.isAvailable())
                if (loNetInfo.isConnected())
                    return true;

        return false;
    }

    public static void displayToastMessageShort(Context context, String msg, boolean isLong) {
        if (toast == null)
            toast = Toast.makeText(context, msg, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static String getDateFormat(String paramLong) {
        Date localDate = new Date(paramLong);
        try {
            String str = new SimpleDateFormat("yyyy-MM-dd").format(localDate);
            return str;
        } catch (Exception localException) {
        }
        return localDate.toString();
    }

    @Nullable
    public static String isempty(String s) {
        if (s == null) {
            return " ";
        } else {
            return s.trim();
        }
    }

    public static String ConvertBitmapToString(Bitmap bitmap) {
        String encodedImage = "";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        try {
            encodedImage = URLEncoder.encode(Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedImage;
    }

    public static String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String getConvertedDate(String date) {
        //String date = "2011/11/12 16:05:06";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            String newFormat = formatter.format(testDate);
            System.out.println(".....Date..." + newFormat);
            return newFormat;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean checkAndRequestPermissions(Context context) {

        int locationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (result1 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            return false;
        }
        return true;
    }

}
