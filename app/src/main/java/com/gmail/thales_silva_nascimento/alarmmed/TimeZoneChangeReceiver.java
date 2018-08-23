package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class TimeZoneChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("TIME ZONE RECEIVER", "Trocou o fuso horario");
    }
}
