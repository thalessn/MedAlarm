package com.gmail.thales_silva_nascimento.alarmmed;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

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

        //Por Enquanto está usando a Uri do RingTone padrão para alarme.
        Uri mRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Pega um instãncia do AsyncRingTonePlayer para executar o som
        getAsyncRingtonePlayer(context).play(mRingtone);

        //Pegar esta informação depois da tela de configuração
        final Vibrator vibrator = getVibrator(context);
        if (Utils.isLOrLater()) {
            vibrateLOrLater(vibrator);
        } else {
            vibrator.vibrate(VIBRATE_PATTERN, 0);
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