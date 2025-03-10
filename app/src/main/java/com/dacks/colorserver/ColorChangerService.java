package com.dacks.colorserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ColorChangerService extends Service {
    private static final String TAG = "ColorServer.ColorChangerService";
    public final int NOTIFICATION_ID = 175774;
    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;
    private NotificationManager notificationManager;
    public ColorChangerService()
    {
        Log.d(TAG, "ColorChangerService Constructor");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new ColorChanger();
    }
    private ColorChanger binder;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "ColorChangerService OnBind");
        return binder;
    }
    public final int MessageGroupId = 1335025;
    public final String ChannelId = "CCChannel";
    //Notification.Builder? builder;
    boolean serviceIsRunning;

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("START_SERVICE")) {
            if (serviceIsRunning) return START_NOT_STICKY;
            //manager1
            int icon = android.R.drawable.stat_notify_sync;
            Context context = getApplicationContext();
            Notification notification = new Notification.Builder(context, ChannelId)
                    .setContentTitle("ColorChangerService")
                    .setSmallIcon(android.R.drawable.ic_menu_camera)
                    .setAutoCancel(true)
                    .build();
//        Toast.makeText(this, "Started!", Toast.LENGTH_LONG);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Check if there are updates here and notify if true
                    Log.d(TAG, "Service is running ...");
                }
            }, 0, UPDATE_INTERVAL);
            //Intent intentService = new Intent(context, ColorChangerService.class);
            startForeground(NOTIFICATION_ID, notification);
            serviceIsRunning = true;
            Log.d(TAG, "Service started");

        }
        // intent.Action == "STOP_SERVICE"
        else if (Objects.requireNonNull(intent.getAction()).equalsIgnoreCase("STOP_SERVICE")) {
            if (timer != null) timer.cancel();
            Log.d(TAG, "Service destroyed");
            stopForeground(STOP_FOREGROUND_REMOVE);
            stopSelf(startid);
            serviceIsRunning = false;
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }
}