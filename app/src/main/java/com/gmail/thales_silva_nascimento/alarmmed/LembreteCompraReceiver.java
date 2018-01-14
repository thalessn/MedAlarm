package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by thales on 14/01/18.
 */

public class LembreteCompraReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("LembreteReceive", "Entrou no Lembrete Receiver");
        //Recebe a id do medicamento que gerará o lembrete
        long idMedicamento = intent.getExtras().getLong("idMedicamento");
        if(idMedicamento < 0)
            idMedicamento = -1;

        //Intent que chamará o serviço do Lebrete compra.
        Intent i = new Intent(context, LembreteCompraService.class);
        i.putExtra("idMedicamento", idMedicamento);
        context.startService(i);
    }
}
