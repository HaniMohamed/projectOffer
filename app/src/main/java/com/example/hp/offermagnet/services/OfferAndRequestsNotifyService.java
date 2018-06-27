package com.example.hp.offermagnet.services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hp.offermagnet.Database;
import com.example.hp.offermagnet.MainActivity;
import com.example.hp.offermagnet.NavDrawer;
import com.example.hp.offermagnet.R;
import com.example.hp.offermagnet.Splash1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class OfferAndRequestsNotifyService extends Service {


    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    Database db;
    SharedPreferences appPrefrences;
    SharedPreferences.Editor prefEditor;

    int seenOffer =0, seenRequest=0;

    @Override
    public void onCreate() {
        db = new Database(context);

        prefEditor= context.getSharedPreferences("AppPrefrences", Context.MODE_PRIVATE).edit();
        appPrefrences= context.getSharedPreferences("AppPrefrences", Context.MODE_PRIVATE);


        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                getOffers();
               getRequests();

                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);

        Toast.makeText(this, "Notifications Stopped...", Toast.LENGTH_LONG).show();
    }


    public void getOffers(){
        seenOffer=appPrefrences.getInt("seenOffer",0);
        //Toast.makeText(context, "Service still running", Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://offer-system.000webhostapp.com/GetOffersnotification.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                                int id = Integer.parseInt(jsonObject1.getString("id"));
                                String title=jsonObject1.getString("title");
                                String desc=jsonObject1.getString("description");
                                //Toast.makeText(context, jsonObject1.getString("id"), Toast.LENGTH_LONG).show();

                                if(id>seenOffer){
                                    NotificationCompat.Builder builder =
                                            new NotificationCompat.Builder(context)
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle(title)
                                                    .setContentText(desc);

                                    Intent notificationIntent = new Intent(context, NavDrawer.class);
                                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(contentIntent);
                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    builder.setSound(alarmSound);
                                    // Add as notification
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(id, builder.build());

                                    prefEditor.putInt("seenOffer",id);
                                    prefEditor.apply();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("id", db.getId());
                hashMap.put("city", db.getCity());
                return hashMap;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);


    }
    public void getRequests(){
        seenRequest=appPrefrences.getInt("seenRequest",0);
        //Toast.makeText(context, "Service still running", Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://offer-system.000webhostapp.com/GetRequestsnotification.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");

                            for (int x = 0; x < jsonArray.length(); x++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                                int id = Integer.parseInt(jsonObject1.getString("id"));
                                String title=jsonObject1.getString("title");
                                String desc=jsonObject1.getString("description");
                                //Toast.makeText(context, jsonObject1.getString("id"), Toast.LENGTH_LONG).show();

                                if(id>seenRequest){
                                    NotificationCompat.Builder builder =
                                            new NotificationCompat.Builder(context)
                                                    .setSmallIcon(R.mipmap.ic_launcher)
                                                    .setContentTitle(title)
                                                    .setContentText(desc);

                                    Intent notificationIntent = new Intent(context, NavDrawer.class);
                                    PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);
                                    builder.setContentIntent(contentIntent);
                                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    builder.setSound(alarmSound);
                                    // Add as notification
                                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    manager.notify(id, builder.build());

                                    prefEditor.putInt("seenRequest",id);
                                    prefEditor.apply();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap hashMap = new HashMap();
                hashMap.put("id", db.getId());
                hashMap.put("city", db.getCity());
                return hashMap;
            }
        };
        Volley.newRequestQueue(context).add(stringRequest);


    }

}