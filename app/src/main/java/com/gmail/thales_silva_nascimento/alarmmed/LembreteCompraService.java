package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.activity.LembreteCompraRemedio;

/**
 * Created by thales on 14/01/18.
 */

public class LembreteCompraService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            stopSelf();
        }else{

            Log.v("Lembrete Service", "Entrou no Lembrete Service");
            //Acorda a CPU do aparelho se estiver dormindo
            AlarmeAlertWakeLock.acquireCpuWakeLock(this);

            long idMedicamento = intent.getExtras().getLong("idMedicamento");

//            Intent i = new Intent(this, LembreteCompraRemedio.class);
//            i.putExtra("idMedicamento", idMedicamento);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);

            //Monta a notificação
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Lembrete de Compra")
                    .setAutoCancel(true)
                    .setContentText("Testando a notificação");


            Intent resultIntent = new Intent(this, LembreteCompraRemedio.class);
            resultIntent.putExtra("idMedicamento", idMedicamento);

            // The stack builder object will contain an artificial back stack for the
            // started Activity.
            // This ensures that navigating backward from the Activity leads out of
            // your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

            // Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(LembreteCompraRemedio.class);

            // Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // mId allows you to update the notification later on.
            mNotificationManager.notify(Long.valueOf(idMedicamento).hashCode(), mBuilder.build());

            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //Libera a CPU
        AlarmeAlertWakeLock.releaseCpuLock();
        super.onDestroy();
    }
}
