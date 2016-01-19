package com.pipit.agc.agc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * For math and stuff
 * Created by Eric on 12/14/2015.
 */
public class Util {

    public static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public static boolean putListToSharedPref(final SharedPreferences.Editor edit, final String key, final List<String> list){

        try{
            Log.d("Util", "putListToSharedPref with list " + list.toString());
            JSONArray mJSONArray = new JSONArray(list);

            edit.putString(key, mJSONArray.toString());
            edit.commit();
            return true;
        } catch(Exception e){
            Log.d("Util", "putListToSharedPref failed with " + e.toString());
            return false;
        }
    }

    public static ArrayList<String> getListFromSharedPref(final SharedPreferences prefs, final String key){
        ArrayList<String> list = new ArrayList<String>();
        try{
            String k = prefs.getString(key, "");
            Log.d("Util", "getArrayFromSharedPref retrieved String " + k);
            JSONArray jsonArray = new JSONArray(k);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    list.add(jsonArray.get(i).toString());
                }
            }
            return list;
        } catch (Exception e){
            Log.d("Util", "getArrayFromSharedPref Failed to convert into jsonArray " + e.toString());
            return list;
        }
    }

    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_NOW);
        return sdf.format(date);
    }

    public static Date stringToDate(String stringdate){
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_NOW);
        Date date = new Date();
        try {
            date = sdf.parse(stringdate);
        } catch (Exception e){
            Log.d("Util", "Failed to parse " + stringdate + " into date format");
        }
        return date;
    }

    public static void sendNotification(Context context, String title, String body){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle(title)
                        .setContentText(body);
        Intent resultIntent = new Intent(context, AllinOneActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(AllinOneActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    public static float getScreenHeightMinusStatusBar(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float screen_h = dm.heightPixels;

        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            screen_h -= context.getResources().getDimensionPixelSize(resId);
        }

        TypedValue typedValue = new TypedValue();
        if(context.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)){
            screen_h -= context.getResources().getDimensionPixelSize(typedValue.resourceId);
        }

        return screen_h;
    }


}
