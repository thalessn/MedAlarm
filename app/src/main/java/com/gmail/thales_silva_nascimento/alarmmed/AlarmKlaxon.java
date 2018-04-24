package com.gmail.thales_silva_nascimento.alarmmed;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

public final class AlarmKlaxon {
    private static final long[] VIBRATE_PATTERN = {500, 700, 500, 700, 500, 700, 500, 700, 500, 700};

    private static boolean sStarted = false;
    private static AsyncRingtonePlayer sAsyncRingtonePlayer;


    private AlarmKlaxon() {}

    public static void stop(Context context) {
        if (sStarted) {
            Log.v("AlarmKlaxon - ","AlarmKlaxon.stop()");
            sStarted = false;

            getAsyncRingtonePlayer(context).stop();
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).cancel();
        }
    }

    public static void start(Context context) {
        // Make sure we are stopped before starting
        stop(context);
        Log.v("AlarmKlaxon","AlarmKlaxon.start()");

        //Uri padrão do alarme. Utilizada caso o toque salvo não exista.
        Uri mRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Pega a uri do toque do alarme, salvo na tela de configurações
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE);
        String u = sharedPreferences.getString(context.getString(R.string.uri_som),mRingtone.getPath());
        Log.v("UriSom", u);
        Uri uri = Uri.parse(u);
        getAsyncRingtonePlayer(context).play(uri);
        //Verifica se o arquivo existe
//        File file = new File(u);
//        Log.v("File", file.getAbsolutePath());
//        if(file.exists()){
//            //Pega um instãncia do AsyncRingTonePlayer para executar o som
//            Uri uri = Uri.fromFile(file);
//            getAsyncRingtonePlayer(context).play(uri);
//        }else {
//            getAsyncRingtonePlayer(context).play(mRingtone);
//        }


        //Verifica na configurações se deve vibrar
        SharedPreferences ss = PreferenceManager.getDefaultSharedPreferences(context);
        boolean vibrar = ss.getBoolean(context.getString(R.string.vibrar), true);
        if(vibrar){
            final Vibrator vibrator = getVibrator(context);
            if (Utils.isLOrLater()) {
                vibrateLOrLater(vibrator);
            } else {
                vibrator.vibrate(VIBRATE_PATTERN, 0);
            }
        }

        sStarted = true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void vibrateLOrLater(Vibrator vibrator) {
        vibrator.vibrate(VIBRATE_PATTERN, 0, new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build());
    }

    private static Vibrator getVibrator(Context context) {
        return ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
    }

    private static synchronized AsyncRingtonePlayer getAsyncRingtonePlayer(Context context) {
        if (sAsyncRingtonePlayer == null) {
            sAsyncRingtonePlayer = new AsyncRingtonePlayer(context.getApplicationContext());
        }

        return sAsyncRingtonePlayer;
    }
}