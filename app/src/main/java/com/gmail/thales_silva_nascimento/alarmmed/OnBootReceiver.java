package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.activity.MainActivity;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("Onboot", "Teste do receiver");
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }
}
