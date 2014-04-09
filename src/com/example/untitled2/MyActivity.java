package com.example.untitled2;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.Random;

import android.os.Handler;

public class MyActivity extends Activity implements View.OnClickListener{

    private ImageView img; // GIF WATER
    private ImageView spin; // GIF FISHING ROD
    private ImageView fon; // IMG SKY
    private ImageView pop; // IMG FLOAT
    private ImageView bar; // IMG BAR GRAPH
    private ImageView runner; // IMG BAR GRAPH RUNNER
    private ImageView selectedBait; // IMG SELECTED BAIT
    private ImageView btnPutX; // IMG BUTTON PUT AND X
    private ImageView btnCatchGet; // IMG BUTTON CATCH AND GET
    private ImageView btnGet;
    private TextView tv; // IMG WATER DEPTH IN THROW POSITION
    private TextView tv2; // TEXT WITH CHANGE DEPTH
    private TextView tv3; // TEXT WITH CURRENT DEPTH
    private InputStream stream; // STREAM WITH GIF
    private GifPlayer gifPlayer; // GIF PLAYER
    private Location loc2; // LOCATION PARAMETERS
    private Bait baits; // BAITS
    private Fish f; // FISH
    private boolean isDialogShow = false; // DIALOG SHOWN
    private boolean isPutBtn = true; // BUTTON STATE
    private boolean isXPushed = false;
    private boolean isRunnerBack = false;
    private boolean isGetBtnPressed = false;
    private boolean isFishCaught = false;
    private int throwDepth = 50; // THROWING DEPTH
    private String throwBait = null; // THROWING BAIT

    /**
     *  ON_WINDOW_FOCUS_CHANGED();
     *  Called when the activity change focus.
     *  PLAY GIFs
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!isDialogShow){
            try{
                GifPlayer gifPlayer1 = new GifPlayer(getAssets().open("loc2_0.gif"), img);
                final InputStream inputStream = getAssets().open("loc2.dat");
                gifPlayer1.startPlaying(true, new AfterPlay() {
                    @Override
                    public void doAfterPlay() {
                        loc2 = new Location("loc2", img, 8, 10, inputStream);
                            }
                    });
                stream = getAssets().open("spin.gif");
                gifPlayer = new GifPlayer(stream, spin);
                selectedBait.setOnClickListener(this);
                btnPutX.setOnClickListener(this);
                btnCatchGet.setOnClickListener(this);
                tv2.setOnClickListener(this);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        throwIt(img);
    }

    /**
     *  ON_CREATE();
     *  Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /* Initialization Views & Other Elements */
        intitGraphics();
        btnGet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.e("BTNGET", "Action down");
                        isGetBtnPressed = true;
                        isRunnerBack = false;
                        new RunnerTask().execute(runner.getX(), pop.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.e("BTNGET", "Action up");
                        isGetBtnPressed = false;
                        isRunnerBack = true;
                        new RunnerTask().execute(runner.getX(), pop.getY());
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView4:
                selectBait();
                break;
            case R.id.imageView7:
                if(isPutBtn){
                        gifPlayer.startPlaying(false, new AfterPlay() {
                            @Override
                            public void doAfterPlay() {
                                pop.setBackgroundResource(R.drawable.pop);
                                btnPutX.setBackgroundResource(R.drawable.btn_x);
                                isPutBtn = false;
                            }
                        });
                        isPutBtn = false;
                        Random rand = new Random();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!isXPushed){
                                    f = loc2.getFish(throwBait, throwDepth);
                                    if (f != null) {
                                        isFishCaught = false;
                                        btnCatchGet.setEnabled(true);
                                        btnCatchGet.setBackgroundResource(R.drawable.btn_catch);
                                        btnCatchGet.setVisibility(View.VISIBLE);
                                        btnCatchGet.bringToFront();
                                    }
                                }else{
                                    isXPushed = false;
                                }
                            }
                        }, rand.nextInt(10000));
                }else{
                    bar.setVisibility(View.INVISIBLE);
                    runner.setVisibility(View.INVISIBLE);
                    btnCatchGet.setVisibility(View.INVISIBLE);
                    btnCatchGet.setEnabled(false);
                    btnGet.setVisibility(View.INVISIBLE);
                    btnGet.setEnabled(false);
                    pop.setBackgroundResource(R.drawable.cross);
                    btnPutX.setBackgroundResource(R.drawable.btn_put);
                    isXPushed = true;
                    isPutBtn = true;
                }
                break;
            case R.id.imageView8:
                    tv2.setVisibility(View.INVISIBLE);
                    bar.setVisibility(View.VISIBLE);
                    runner.setVisibility(View.VISIBLE);
                    runner.setX(bar.getX());
                    btnCatchGet.setVisibility(View.INVISIBLE);
                    btnCatchGet.setEnabled(false);
                    btnGet.setVisibility(View.VISIBLE);
                    btnGet.setEnabled(true);
                    btnGet.setBackgroundResource(R.drawable.btn_get);
                    btnGet.bringToFront();
                break;
            case R.id.textView2:
                selectDepth(tv3);
                break;
        }
    }

    private class RunnerTask extends AsyncTask<Float, Void, Float[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Float[] doInBackground(Float... params) {

            if(isRunnerBack){
                params[0] -= 4.0f;
            }else{
                params[1] += 1.0f;
                params[0] += 3.0f;
            }
            return params;
        }

        @Override
        protected void onPostExecute(Float[] result) {
            super.onPostExecute(result);
            runner.setX(result[0]);
            if(isRunnerBack){
                if((runner.getX()) > (bar.getX()))
                    new RunnerTask().execute(result[0], result[1]);
            }else{
                pop.setY(result[1]);
                if((isFishCaught == false) && ((result[1] + pop.getHeight()) >= (img.getY() + img.getHeight()))){
                    Toast.makeText(getApplicationContext(), f.getFishName(), Toast.LENGTH_LONG).show();
                    isFishCaught = true;
                    pop.setBackgroundResource(R.drawable.cross);
                    btnPutX.setBackgroundResource(R.drawable.btn_put);
                    isPutBtn = true;
                    isGetBtnPressed = false;
                    runner.setVisibility(View.INVISIBLE);
                    bar.setVisibility(View.INVISIBLE);
                    btnGet.setVisibility(View.INVISIBLE);
                    btnGet.setEnabled(false);
                    tv2.setVisibility(View.VISIBLE);
                    tv2.bringToFront();
                }
                if((runner.getX() + runner.getWidth()) < (bar.getX() + bar.getWidth())){
                    if(isGetBtnPressed){
                        new RunnerTask().execute(result[0], result[1]);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Леска оборвалась!", Toast.LENGTH_LONG).show();
                    pop.setBackgroundResource(R.drawable.cross);
                    btnPutX.setBackgroundResource(R.drawable.btn_put);
                    isPutBtn = true;
                    runner.setVisibility(View.INVISIBLE);
                    bar.setVisibility(View.INVISIBLE);
                    btnGet.setVisibility(View.INVISIBLE);
                    btnGet.setEnabled(false);
                    tv2.setVisibility(View.VISIBLE);
                    tv2.bringToFront();
                }
            }
        }
    }

    private void intitGraphics(){
        fon = (ImageView)findViewById(R.id.imageView);
        img = (ImageView)findViewById(R.id.imageView1);
        spin = (ImageView)findViewById(R.id.imageView2);
        pop = (ImageView)findViewById(R.id.imageView3);
        selectedBait = (ImageView)findViewById(R.id.imageView4);
        bar = (ImageView)findViewById(R.id.imageView5);
        runner = (ImageView)findViewById(R.id.imageView6);
        btnPutX = (ImageView)findViewById(R.id.imageView7);
        btnCatchGet = (ImageView)findViewById(R.id.imageView8);
        btnGet = (ImageView)findViewById(R.id.imageView9);
        tv = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        tv3 = (TextView)findViewById(R.id.textView3);
        tv3.setText(String.valueOf(throwDepth));
        tv2.bringToFront();
        btnCatchGet.setEnabled(false);
        btnGet.setEnabled(false);
        baits = new Bait();
    }

    private void selectDepth(final TextView depthShow){
                isDialogShow = true;
                final Dialog d = new Dialog(MyActivity.this);
                d.setTitle("Change Depth");
                d.setContentView(R.layout.depth_picker);
                Button b1 = (Button) d.findViewById(R.id.button);
                final SeekBar sb = (SeekBar) d.findViewById(R.id.seekBar);
                final TextView tvDepth = (TextView) d.findViewById(R.id.textView);
                final TextView tvMin = (TextView) d.findViewById(R.id.textView3);
                final TextView tvMax = (TextView) d.findViewById(R.id.textView2);
                int max = 500;
                final int min = 10;
                tvDepth.setText(String.valueOf(throwDepth));
                tvMin.setText(String.valueOf(min));
                tvMax.setText(String.valueOf(max));
                sb.setMax(max - min);
                sb.setProgress(throwDepth);
                sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        tvDepth.setText(String.valueOf(progress + min));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        throwDepth = sb.getProgress() + min;
                        depthShow.setText(String.valueOf(throwDepth));
                        d.dismiss();
                    }
                });
                d.show();
    }

    private void selectBait(){
                isDialogShow = true;
                final Dialog d = new Dialog(MyActivity.this);
                d.setTitle("Change Bait");
                d.setContentView(R.layout.bait_picker);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom_spinner, baits.getBaitsNames());
                adapter.setDropDownViewResource(R.layout.custom_spinner);
                final Spinner spinner = (Spinner)d.findViewById(R.id.spinner);
                spinner.setAdapter(adapter);
                spinner.setPrompt("Baits");
                Button btn = (Button)d.findViewById(R.id.button);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        throwBait = (String) spinner.getSelectedItem();
                        selectedBait.setImageBitmap(baits.getBaitImg(getApplicationContext(), spinner.getSelectedItemPosition()));
                        d.dismiss();
                    }
                });
                d.show();
    }

    private void throwIt(View water){
        pop.setBackgroundResource(R.drawable.cross);
        water.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(isPutBtn){
                    pop.setX(event.getX());
                    pop.setY(fon.getHeight() + event.getY());
                    int throwPosition = loc2.getWaterDepth((int) event.getX(), (int) event.getY());
                    tv.setText(throwPosition + "");
                    tv2.bringToFront();
                    if (throwPosition < 0) {
                        Toast.makeText(getApplicationContext(), "Sorry, but you can't throw there!", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }
}
