package com.newiplquizgame.myipl.extra;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Common {
    public static Toast toast;
    public static Integer GROUP_ID = 0;
    public static String GROUP_NAME = "";
    public static Integer USER_ID = 0;
    public static Integer GROUP_ADMIN = 0;
    public static String GROUP_DES = "";
    public static String GROUP_URL = "";

    public static String EMAIL_PATTEN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());


    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
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
            SimpleDateFormat formatter = new SimpleDateFormat("EEE dd MMM");
            String newFormat = formatter.format(testDate);
            System.out.println(".....Date..." + newFormat);
            return newFormat;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String getConvertesdDate(String date) {
        //String date = "2011/11/12 16:05:06";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            Date testDate = null;
            try {
                testDate = sdf.parse(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
        int readPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (readPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 2);
            return false;
        }
        return true;
    }

    public static File getCompressed(Context context, String path) throws IOException {
        if (context == null)
            throw new NullPointerException();
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getCacheDir();
        String rootDir = cacheDir.getAbsolutePath() + "/ImageCompressor";
        File root = new File(rootDir);
        if (!root.exists())
            root.mkdirs();
        Bitmap bitmap = decodeImageFromFiles(path, 300, 300);
        File compressed = new File(root, SDF.format(new Date()) + ".jpg");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
        fileOutputStream.write(byteArrayOutputStream.toByteArray());
        fileOutputStream.flush();
        fileOutputStream.close();
        return compressed;
    }

    public static Bitmap decodeImageFromFiles(String path, int width, int height) {
        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scaleOptions);
        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width
                && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }
        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, outOptions);
    }



}
