package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class AlarmeService extends Service {

    private boolean ativo = false;
    LocalBroadcastManager lbc;

    @Override
    public void onCreate() {
        Log.v("onCreate", "Passou");
        lbc = LocalBroadcastManager.getInstance(this);
        super.onCreate();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent == null){
            /**
             * IMPLEMENTAR O CÓDIGO PARA TERMINAR ESSE SERVIÇO
             * POIS NÃO TERÁ UM HORÁRIO NA INTENT NÃO TENDO HORARIO
             */
            return super.onStartCommand(intent, flags, startId);
        }else{
            if(!ativo){

                //Acorda a CPU do aparelho se estiver dormindo
                AlarmeAlertWakeLock.acquireCpuWakeLock(this);

                //Pega a idHorario a ser pesquisada
                long idhorario = intent.getExtras().getLong("horario");
                Log.v("idHorario", String.valueOf(idhorario));
                ativo = true;
                Log.v("onStartCommand - Id = ", String.valueOf(startId));

                Log.v("ALARME SERVICE", "INICIOU SERVICE");
                Intent i = new Intent(this, TelaAlarme.class);
                i.putExtra("horario", idhorario);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                AlarmKlaxon.start(AlarmeService.this);
                //Contador para parar a música
                CountDownTimer timer = new CountDownTimer(15000,1000) {
                    @Override
                    public void onTick(long l) {

                    }
                    @Override
                    public void onFinish() {
                        AlarmKlaxon.stop(AlarmeService.this);
                    }
                };
                timer.start();



                Log.v("ALARME SERVICE", "Iniciou Activity");
                  return super.onStartCommand(intent, flags, startId);

            }else {
                Log.v("Else", "Do Alarme Service");
                //Pega a idHorario do AlarmeReceiver
                long idhorario = intent.getExtras().getLong("horario");

                //Envia a intent para o LocalBroadcastReceiver
                Intent a = new Intent(TelaAlarme.TELA_ALARME_ATUALIZAR);
                a.putExtra("horario",idhorario);
                LocalBroadcastManager.getInstance(this).sendBroadcast(a);

                return super.onStartCommand(intent, flags, startId);
            }
        }
    }

    @Override
    public void onDestroy() {
        Log.v("ALARME SERVICE", "OnDestroy");
        //Libera a CPU
        AlarmeAlertWakeLock.releaseCpuLock();
        super.onDestroy();
    }


}
