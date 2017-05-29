package com.sustech.se.scoree;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import android.util.Log;

import com.sustech.se.scoree.audioCapturer.AudioCapturer;
import com.sustech.se.scoree.audioCapturer.AudioCapturerConfig;
import com.sustech.se.scoree.audioCapturer.AudioCapturerInterface;

/**
 * Created by liaoweiduo on 08/04/2017.
 */

public class Data extends Application {
    private AudioCapturerConfig audioCapturerConfig;
    private AudioCapturerInterface audioCapturer=null;
    private int pageTurnSetting;

    public AudioCapturerConfig getAudioCapturerConfig() {
        return audioCapturerConfig;
    }
    public void setAudioCapturerConfig(AudioCapturerConfig acc){
        audioCapturerConfig=acc;
    }

    public AudioCapturerInterface getAudioCapturer(){
        if (audioCapturer==null){
            synchronized (Data.class){
                if (audioCapturer==null){
                    audioCapturer=new AudioCapturer();
                    audioCapturer.audioCaptuerInit(audioCapturerConfig);
                }
            }
        }
        return audioCapturer;
    }

    public int getPageTurnSetting() {
        return pageTurnSetting;
    }

    public void setPageTurnSetting(int pageTurnSetting) {
        this.pageTurnSetting = pageTurnSetting;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        int SOURCE = sp.getInt(getString(R.string.source), MediaRecorder.AudioSource.MIC);
        int SAMPLE_RATE = sp.getInt(getString(R.string.sampleRate), 8000);
        int CHANNEL_CONFIG = sp.getInt(getString(R.string.channelConfig), AudioFormat.CHANNEL_IN_STEREO);
        int AUDIO_FORMAT = sp.getInt(getString(R.string.audioFormat), AudioFormat.ENCODING_PCM_16BIT);
        int BUFFER_SIZE = sp.getInt(getString(R.string.bufferSize), 2048);
        int pageTurnSetting = sp.getInt("pageTurnSetting", R.string.normal);
        this.audioCapturerConfig = new AudioCapturerConfig(SOURCE, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        this.pageTurnSetting = pageTurnSetting;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SharedPreferences sp = getSharedPreferences("data", MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt("pageTurnSetting", pageTurnSetting);
        editor.apply();
    }
}
