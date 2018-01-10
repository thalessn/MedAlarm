package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class AlarmeReceiver extends BroadcastReceiver {

    //PendingIntent - Classe que guarda a intent que será exEcutada quando o Alarm tocar.
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"TOCOU O ALARME", Toast.LENGTH_LONG).show();
        Log.v("ALARME RECEIVER", "Inicio O ALARME");

        //Pega a id de horário contido na intent do onReceive e passa na intent abaixo
        long  idHorario = intent.getExtras().getLong("horario");
        if(idHorario < 0)
            idHorario = -1;

        Intent i = new Intent(context, AlarmeService.class);
        i.putExtra("horario", idHorario);
        context.startService(i);
    }
}
