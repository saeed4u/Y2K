package glivion.y2k.android.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import glivion.y2k.R;
import glivion.y2k.android.constants.Constants;
import glivion.y2k.android.database.Y2KDatabase;
import glivion.y2k.android.model.Loan;
import glivion.y2k.android.ui.LoanDetailActivity;

/**
 * Created by blanka on 8/7/2016.
 */
public class NotificationService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new GetIncome().execute();
        Log.v("On start command", "On start command");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class GetIncome extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Y2KDatabase y2kDatabase = new Y2KDatabase(NotificationService.this);
            String time = simpleDateFormat.format(System.currentTimeMillis());
            Log.v("Time", "Time = " + time);
            ArrayList<Loan> loans = y2kDatabase.getLoans(time);
            for (Loan loan : loans) {
                String title = loan.getmLoanTitle();

                Intent intent = new Intent(NotificationService.this, LoanDetailActivity.class);
                intent.putExtra(Constants.LOAN, loan);
                PendingIntent contentIntent = PendingIntent.getActivity(NotificationService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                String description = "Loan with title\"" + title + "\" is due";
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                Log.v("Loan Shit", "Loan Title " + title);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(NotificationService.this)
                        .setContentTitle("Y2K")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
                        .setContentText(description).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setLights(Color.BLUE, 3000, 3000)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(contentIntent);
                Notification notification = builder.build();
                notificationManager.notify(loan.getmLoanId(), notification);
            }
            return null;
        }
    }
}
