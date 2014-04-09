package com.example.untitled2;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

import java.io.InputStream;
interface AfterPlay{
    void doAfterPlay();
}

public class GifPlayer {

    public boolean IS_PLAYING = false;
    private InputStream inputStream;
    private GifDecoder gifDecoder;     // need GifDecoder.java
    private ImageView imageView;
    private boolean stopAnim = false;
    private boolean isLoop = false;
    private int frameNumber;
    private AfterPlay afterPlay;
    private boolean isDoneOnce = false;
    private Runnable run;
    private Handler h;
    private int invalidate = 33;

    public GifPlayer(InputStream inputStream, final ImageView imageView){
        this.inputStream = inputStream;
        this.imageView = imageView;
        h = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                anim();
                if(!stopAnim){
                    h.postDelayed(run, invalidate);
                    if(!isDoneOnce){
                        afterPlay.doAfterPlay();
                        isDoneOnce = true;
                    }
                }else{
                    imageView.setImageBitmap(gifDecoder.getFrame(gifDecoder.frameCount-1));
                    IS_PLAYING = false;
                    afterPlay.doAfterPlay();
                }
            }
        };
        gifDecoder = new GifDecoder();
        gifDecoder.read(this.inputStream);
    }

    public InputStream getGifInputStream(){
        return this.inputStream;
    }

    public void setGifInputStream(InputStream inputStream){
        if(IS_PLAYING)
            stopPlaying();
        else
            this.inputStream = inputStream;
    }

    public ImageView getGifImageView(){
        return this.imageView;
    }

    public void setGifImageView(ImageView imageView){
        this.imageView = imageView;
    }

    public void startPlaying(boolean isLoop){
        this.isLoop = isLoop;
        stopPlaying();
        stopAnim = false;
        frameNumber = 0;
        h.postDelayed(run, invalidate);
    }

    public void startPlaying(boolean isLoop, AfterPlay afterPlay){
        this.afterPlay = afterPlay;
        this.isLoop = isLoop;
        stopPlaying();
        stopAnim = false;
        frameNumber = 0;
        h.postDelayed(run, invalidate);
    }

    public void stopPlaying(){
        this.stopAnim = true;
    }

    private void anim(){
        imageView.setImageBitmap(gifDecoder.getFrame(frameNumber));
        if(frameNumber == gifDecoder.getFrameCount()){
            frameNumber = 0;
            if(!isLoop)
                stopAnim = true;
        }else{
            frameNumber++;
        }
    }
}
