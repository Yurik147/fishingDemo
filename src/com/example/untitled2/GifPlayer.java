package com.example.untitled2;

import android.os.AsyncTask;
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
    private PlayingTask playingTask;
    private boolean stopAnim = false;
    private boolean isLoop = false;
    private int frameNumber;
    private AfterPlay afterPlay;
    private boolean isDoneOnce = false;

    public GifPlayer(InputStream inputStream, ImageView imageView){
        this.inputStream = inputStream;
        this.imageView = imageView;
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
        new PlayingTask().execute();
    }

    public void startPlaying(boolean isLoop, AfterPlay afterPlay){
        this.afterPlay = afterPlay;
        this.isLoop = isLoop;
        stopPlaying();
        stopAnim = false;
        frameNumber = 0;
        new PlayingTask().execute();
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

    private class PlayingTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            anim();
            if(!stopAnim){
                if(!isDoneOnce){
                    afterPlay.doAfterPlay();
                    isDoneOnce = true;
                }
                new PlayingTask().execute();
            }else{
                imageView.setImageBitmap(gifDecoder.getFrame(gifDecoder.frameCount-1));
                IS_PLAYING = false;
                afterPlay.doAfterPlay();
            }
        }
    }

}
