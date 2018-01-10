package com.gmail.thales_silva_nascimento.alarmmed;

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;

import static android.media.AudioManager.AUDIOFOCUS_GAIN_TRANSIENT;
import static android.media.AudioManager.STREAM_ALARM;

/**
 * <p>This class controls playback of ringtones. Uses {@link Ringtone} or {@link MediaPlayer} in a
 * dedicated thread so that this class can be called from the main thread. Consequently, problems
 * controlling the ringtone do not cause ANRs in the main thread of the application.</p>
 *
 * <p>This class also serves a second purpose. It accomplishes alarm ringtone playback using two
 * different mechanisms depending on the underlying platform.</p>
 *
 * <ul>
 *     <li>Prior to the M platform release, ringtone playback is accomplished using
 *     {@link MediaPlayer}. android.permission.READ_EXTERNAL_STORAGE is required to play custom
 *     ringtones located on the SD card using this mechanism. {@link MediaPlayer} allows clients to
 *     adjust the volume of the stream and specify that the stream should be looped.</li>
 *
 *     <li>Starting with the M platform release, ringtone playback is accomplished using
 *     {@link Ringtone}. android.permission.READ_EXTERNAL_STORAGE is <strong>NOT</strong> required
 *     to play custom ringtones located on the SD card using this mechanism. {@link Ringtone} allows
 *     clients to adjust the volume of the stream and specify that the stream should be looped but
 *     those methods are marked @hide in M and thus invoked using reflection. Consequently, revoking
 *     the android.permission.READ_EXTERNAL_STORAGE permission has no effect on playback in M+.</li>
 * </ul>
 *
 * <p>If either the {@link Ringtone} or {@link MediaPlayer} fails to play the requested audio, an
 * {@link #getFallbackRingtoneUri in-app fallback} is used because playing <strong>some</strong>
 * sort of noise is always preferable to remaining silent.</p>
 */
public final class AsyncRingtonePlayer {
    //private static final LogUtils.Logger LOGGER = new LogUtils.Logger("AsyncRingtonePlayer");

    // Volume suggested by media team for in-call alarms.
    private static final float IN_CALL_VOLUME = 0.125f;

    // Message codes used with the ringtone thread.
    private static final int EVENT_PLAY = 1;
    private static final int EVENT_STOP = 2;
    private static final String RINGTONE_URI_KEY = "RINGTONE_URI_KEY";

    /**
     * Handler running on the ringtone thread.
     */
    private Handler mHandler;

    /**
     * {@link MediaPlayerPlaybackDelegate} on pre M; {@link RingtonePlaybackDelegate} on M+
     */
    private PlaybackDelegate mPlaybackDelegate;

    /**
     * The context.
     */
    private final Context mContext;

    public AsyncRingtonePlayer(Context context) {
        mContext = context;
    }

    /**
     * Plays the ringtone.
     */
    public void play(Uri ringtoneUri) {
        Log.v("Posting play", "");
        postMessage(EVENT_PLAY, ringtoneUri, 0);
    }

    /**
     * Stops playing the ringtone.
     */
    public void stop() {
        Log.v("Posting stop.", "");
        postMessage(EVENT_STOP, null, 0);
    }


    /**
     * Posts a message to the ringtone-thread handler.
     *
     * @param messageCode the message to post
     * @param ringtoneUri the ringtone in question, if any
     * @param delayMillis the amount of time to delay sending the message, if any
     */
    private void postMessage(int messageCode, Uri ringtoneUri, long delayMillis) {
        synchronized (this) {
            if (mHandler == null) {
                mHandler = getNewHandler();
            }

            final Message message = mHandler.obtainMessage(messageCode);
            if (ringtoneUri != null) {
                final Bundle bundle = new Bundle();
                bundle.putParcelable(RINGTONE_URI_KEY, ringtoneUri);
                message.setData(bundle);
            }
            Log.v("PostMessage", "Mandou mensagem");
            mHandler.sendMessageDelayed(message, delayMillis);
        }
    }

    /**
     * Creates a new ringtone Handler running in its own thread.
     */
    @SuppressLint("HandlerLeak")
    private Handler getNewHandler() {
        final HandlerThread thread = new HandlerThread("ringtone-player");
        thread.start();

        return new Handler(thread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case EVENT_PLAY:
                        final Bundle data = msg.getData();
                        final Uri ringtoneUri = data.getParcelable(RINGTONE_URI_KEY);
                        getPlaybackDelegate().play(mContext, ringtoneUri);
                        Log.v("getHandle", "Event_Play");
                        break;
                    case EVENT_STOP:
                        Log.v("getHandle", "Event_Stop");
                        getPlaybackDelegate().stop(mContext);
                        break;
                }
            }
        };
    }

    /**
     * @return <code>true</code> iff the device is currently in a telephone call
     */
    private static boolean isInTelephoneCall(Context context) {
        Log.v("isInTelephoneCall", "Entrou");
        final TelephonyManager tm = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getCallState() != TelephonyManager.CALL_STATE_IDLE;
    }

    /**
     * @return Uri of the ringtone to play when the user is in a telephone call
     */
    private static Uri getInCallRingtoneUri(Context context) {
        Log.v("getInCallRingtoneUri", "Entrou");
        //return Utils.getResourceUri(context, R.raw.alarm_expire);
        //Por Enquanto está retornando o som padrão do android
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    }

    /**
     * @return Uri of the ringtone to play when the chosen ringtone fails to play
     */
    private static Uri getFallbackRingtoneUri(Context context) {
        Log.v("getFallbackRingtoneUri", "Entrou");
        //return Utils.getResourceUri(context, R.raw.alarm_expire);
        //Por Enquanto está retornando o som padrão do android
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    }

    /**
     * Check if the executing thread is the one dedicated to controlling the ringtone playback.
     */
    private void checkAsyncRingtonePlayerThread() {
        if (Looper.myLooper() != mHandler.getLooper()) {

            Log.v("checkAsyncRingtonePlaye", "Entrou");
            //new IllegalStateException();
        }
    }


    /**
     * @return the platform-specific playback delegate to use to play the ringtone
     */
    private PlaybackDelegate getPlaybackDelegate() {
        checkAsyncRingtonePlayerThread();

        if (mPlaybackDelegate == null) {
            if (Utils.isMOrLater()) {
                // Use the newer Ringtone-based playback delegate because it does not require
                // any permissions to read from the SD card. (M+)
                mPlaybackDelegate = new RingtonePlaybackDelegate();
            } else {
                // Fall back to the older MediaPlayer-based playback delegate because it is the only
                // way to force the looping of the ringtone before M. (pre M)
                mPlaybackDelegate = new MediaPlayerPlaybackDelegate();
            }
        }

        return mPlaybackDelegate;
    }

    /**
     * This interface abstracts away the differences between playing ringtones via {@link Ringtone}
     * vs {@link MediaPlayer}.
     */
    private interface PlaybackDelegate {

        boolean play(Context context, Uri ringtoneUri);

        /**
         * Stop any ongoing ringtone playback.
         */
        void stop(Context context);

    }

    /**
     * Loops playback of a ringtone using {@link MediaPlayer}.
     */
    private class MediaPlayerPlaybackDelegate implements PlaybackDelegate {

        /**
         * The audio focus manager. Only used by the ringtone thread.
         */
        private AudioManager mAudioManager;

        /**
         * Non-{@code null} while playing a ringtone; {@code null} otherwise.
         */
        private MediaPlayer mMediaPlayer;


        /**
         * Starts the actual playback of the ringtone. Executes on ringtone-thread.
         */
        @Override
        public boolean play(final Context context, Uri ringtoneUri) {
            checkAsyncRingtonePlayerThread();

            Log.v("Play ringtone", "via android.media.MediaPlayer.");

            if (mAudioManager == null) {
                mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            }

            final boolean inTelephoneCall = isInTelephoneCall(context);
            Uri alarmNoise = null;//inTelephoneCall ? getInCallRingtoneUri(context) : ringtoneUri;
            // Fall back to the system default alarm if the database does not have an alarm stored.
            if (alarmNoise == null) {
                alarmNoise = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                Log.v("Using default alarm: ", alarmNoise.toString());
            }

            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.v("Error ", "occurred while playing audio. Stopping AlarmKlaxon.");
                    stop(context);
                    return true;
                }
            });

            try {
                // If alarmNoise is a custom ringtone on the sd card the app must be granted
                // android.permission.READ_EXTERNAL_STORAGE. Pre-M this is ensured at app
                // installation time. M+, this permission can be revoked by the user any time.
                mMediaPlayer.setDataSource(context, alarmNoise);
                Log.v("Try - Play", "MediaDelegate");
                return startPlayback(inTelephoneCall);


            } catch (Throwable t) {
                Log.v("Using ", "the fallback ringtone, could not play" + alarmNoise + t);
                // The alarmNoise may be on the sd card which could be busy right now.
                // Use the fallback ringtone.
                try {
                    // Must reset the media player to clear the error state.
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(context, getFallbackRingtoneUri(context));
                    return startPlayback(inTelephoneCall);
                } catch (Throwable t2) {
                    // At this point we just don't play anything.
                    Log.v("Failed", " to play fallback ringtone" + t2);
                }
            }

            return false;
        }

        /**
         * Prepare the MediaPlayer for playback if the alarm stream is not muted, then start the
         * playback.
         *
         * @param inTelephoneCall {@code true} if there is currently an active telephone call
         * @return {@code true} if a crescendo has started and future volume adjustments are
         * required to advance the crescendo effect
         */
        private boolean startPlayback(boolean inTelephoneCall) throws IOException {
            Log.v("StartPlayback-Media", "Entrou");
            // Do not play alarms if stream volume is 0 (typically because ringer mode is silent).
            if (mAudioManager.getStreamVolume(STREAM_ALARM) == 0) {
                Log.v("If - VolumeBaixo", "O volume do celular está no mudo");
                return false;
            }

            // Indicate the ringtone should be played via the alarm stream.
            if (Utils.isLOrLater()) {
                Log.v("If - isLOrLater", "Entrou");
                mMediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
            }

            // Check if we are in a call. If we are, use the in-call alarm resource at a low volume
            // to not disrupt the call.
            boolean scheduleVolumeAdjustment = false;
            if (inTelephoneCall) {
                Log.v("Using", " the in-call alarm");
                mMediaPlayer.setVolume(IN_CALL_VOLUME, IN_CALL_VOLUME);
            } else {
                mMediaPlayer.setVolume(1, 1);
            }


            mMediaPlayer.setAudioStreamType(STREAM_ALARM);
            mMediaPlayer.prepare();
            mAudioManager.requestAudioFocus(null, STREAM_ALARM, AUDIOFOCUS_GAIN_TRANSIENT);
            mMediaPlayer.start();

            return scheduleVolumeAdjustment;
        }

        /**
         * Stops the playback of the ringtone. Executes on the ringtone-thread.
         */
        @Override
        public void stop(Context context) {
            checkAsyncRingtonePlayerThread();

            Log.v("Stop", "ringtone via android.media.MediaPlayer.");

            // Stop audio playing
            if (mMediaPlayer != null) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

            if (mAudioManager != null) {
                mAudioManager.abandonAudioFocus(null);
            }
        }

    }

    /**
     * Loops playback of a ringtone using {@link Ringtone}.
     */
    private class RingtonePlaybackDelegate implements PlaybackDelegate {

        /**
         * The audio focus manager. Only used by the ringtone thread.
         */
        private AudioManager mAudioManager;

        /**
         * The current ringtone. Only used by the ringtone thread.
         */
        private Ringtone mRingtone;

        /**
         * The method to adjust playback volume; cannot be null.
         */
        private Method mSetVolumeMethod;

        /**
         * The method to adjust playback looping; cannot be null.
         */
        private Method mSetLoopingMethod;


        private RingtonePlaybackDelegate() {
            try {
                mSetVolumeMethod = Ringtone.class.getDeclaredMethod("setVolume", float.class);
            } catch (NoSuchMethodException nsme) {
                Log.v("Unable", "to locate method: Ringtone.setVolume(float)." + nsme);
            }

            try {
                mSetLoopingMethod = Ringtone.class.getDeclaredMethod("setLooping", boolean.class);
            } catch (NoSuchMethodException nsme) {
                Log.v("Unable", " to locate method: Ringtone.setLooping(boolean)." + nsme);
            }
        }

        /**
         * Starts the actual playback of the ringtone. Executes on ringtone-thread.
         */
        @Override
        public boolean play(Context context, Uri ringtoneUri) {
            checkAsyncRingtonePlayerThread();

            Log.v("Play", " ringtone via android.media.Ringtone.");

            if (mAudioManager == null) {
                mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            }

            final boolean inTelephoneCall = isInTelephoneCall(context);
            if (inTelephoneCall) {
                ringtoneUri = getInCallRingtoneUri(context);
            }

            // Attempt to fetch the specified ringtone.
            mRingtone = RingtoneManager.getRingtone(context, ringtoneUri);

            if (mRingtone == null) {
                // Fall back to the system default ringtone.
                ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                mRingtone = RingtoneManager.getRingtone(context, ringtoneUri);
            }

            // Attempt to enable looping the ringtone.
            try {
                mSetLoopingMethod.invoke(mRingtone, true);
            } catch (Exception e) {
                Log.v("Unable", " to turn looping off for android.media.Ringtone" + e);

                // Fall back to the default ringtone if looping could not be enabled.
                // (Default alarm ringtone most likely has looping tags set within the .ogg file)
                mRingtone = null;
            }

            // If no ringtone exists at this point there isn't much recourse.
            if (mRingtone == null) {
                Log.v("Unable", " to locate alarm ringtone, using internal fallback ringtone.");
                ringtoneUri = getFallbackRingtoneUri(context);
                mRingtone = RingtoneManager.getRingtone(context, ringtoneUri);
            }

            try {
                return startPlayback(inTelephoneCall);
            } catch (Throwable t) {
                Log.v("Using", " the fallback ringtone, could not play " + ringtoneUri + t);
                // Recover from any/all playback errors by attempting to play the fallback tone.
                mRingtone = RingtoneManager.getRingtone(context, getFallbackRingtoneUri(context));
                try {
                    return startPlayback(inTelephoneCall);
                } catch (Throwable t2) {
                    // At this point we just don't play anything.
                    Log.v("Failed", " to play fallback ringtone" + t2);
                }
            }

            return false;
        }

        /**
         * Prepare the Ringtone for playback, then start the playback.
         *
         * @param inTelephoneCall {@code true} if there is currently an active telephone call
         * @return {@code true} if a crescendo has started and future volume adjustments are
         * required to advance the crescendo effect
         */
        private boolean startPlayback(boolean inTelephoneCall) {
            Log.v("StartPlayback", "inTelephoneCall = " + String.valueOf(inTelephoneCall));
            // Indicate the ringtone should be played via the alarm stream.
            if (Utils.isLOrLater()) {
                mRingtone.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build());
            }

            // Attempt to adjust the ringtone volume if the user is in a telephone call.
            boolean scheduleVolumeAdjustment = false;
            if (inTelephoneCall) {
                Log.v("Using", " the in-call alarm");
                setRingtoneVolume(IN_CALL_VOLUME);
            } else {
                setRingtoneVolume(1);
            }

            mAudioManager.requestAudioFocus(null, STREAM_ALARM, AUDIOFOCUS_GAIN_TRANSIENT);

            mRingtone.play();

            return scheduleVolumeAdjustment;
        }

        /**
         * Sets the volume of the ringtone.
         *
         * @param volume a raw scalar in range 0.0 to 1.0, where 0.0 mutes this player, and 1.0
         *               corresponds to no attenuation being applied.
         */
        private void setRingtoneVolume(float volume) {
            try {
                mSetVolumeMethod.invoke(mRingtone, volume);
            } catch (Exception e) {
                Log.v("Unable", " to set volume for android.media.Ringtone" + e);
            }
        }

        /**
         * Stops the playback of the ringtone. Executes on the ringtone-thread.
         */
        @Override
        public void stop(Context context) {
            checkAsyncRingtonePlayerThread();

            Log.v("Stop", " ringtone via android.media.Ringtone.");

            if (mRingtone != null && mRingtone.isPlaying()) {
                Log.v("Ringtone.stop()", " invoked.");
                mRingtone.stop();
            }

            mRingtone = null;

            if (mAudioManager != null) {
                mAudioManager.abandonAudioFocus(null);
            }
        }
    }

}