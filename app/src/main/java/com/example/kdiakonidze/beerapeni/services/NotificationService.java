package com.example.kdiakonidze.beerapeni.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.kdiakonidze.beerapeni.MainActivity;
import com.example.kdiakonidze.beerapeni.R;
import com.example.kdiakonidze.beerapeni.utils.Constantebi;
import com.example.kdiakonidze.beerapeni.utils.GlobalServise;
import com.example.kdiakonidze.beerapeni.utils.MyUtil;
import com.example.kdiakonidze.beerapeni.utils.PrivateKey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationService extends Service {

    private static final String TAG = "NotificationService";
    public static final String CHANEL_ID = "mychanel";
    private static final String CHANEL_NAME = "chanelName";
    private static final String CHANEL_DESC = "desc";

    private MyUtil util;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        util = new MyUtil(getApplicationContext());

        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference(getString(R.string.location_en));
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String lastValue = util.loadLastValue();

                if (dataSnapshot.getValue() != null) {
                    String text = dataSnapshot.getValue().toString();
                    if (!lastValue.equals(text) && !lastValue.isEmpty()) {
                        if (MainActivity.ACTIVE) {
                            if (iInstanse == null) {
                                Log.d(TAG, "interf is null");
                                iInstanse = Constantebi.nSinterface;
                            } else {
                                Log.d(TAG, "interf NOT null");
                            }
                            iInstanse.doNow();
                            MediaPlayer player = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_NOTIFICATION_URI);
                            player.start();
                        } else {
                            displayNotification(text);
                        }
                        MainActivity.NEED_COMENTS_UPDATE = true;
                    }
                    lastValue = text;
                    util.saveLastValue(lastValue);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;// super.onStartCommand(intent, flags, startId);
    }

    void displayNotification(String text) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 22, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANEL_ID)
                .setSmallIcon(R.drawable.ic_comment_icon)
                .setContentTitle(getString(R.string.notif_title))
                .setContentText(getString(R.string.notif_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANEL_DESC);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);

            mBuilder.setChannelId(CHANEL_ID);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        } else {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        }

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        managerCompat.notify(1, mBuilder.build());
//        startForeground(1, mBuilder.build());
    }

    public interface NSinterface {
        void doNow();
    }

    private NSinterface iInstanse = Constantebi.nSinterface;

}
