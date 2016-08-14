package glivion.y2k.android.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by blanka on 8/13/2016.
 */
public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationService = new Intent(context, NotificationService.class);
        context.startService(notificationService);
    }
}
