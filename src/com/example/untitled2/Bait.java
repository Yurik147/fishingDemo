package com.example.untitled2;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

public class Bait {

    private int baitsCount = 20;
    private String[] baitsNames;
    private String[] baitsImgNames;
    private int[] baitsPrices;

    public Bait(){
        baitsNames = new String[baitsCount];
        baitsImgNames = new String[baitsCount];
        baitsPrices = new int[baitsCount];
        for(int i = 0; i < baitsCount; i++){
            baitsImgNames[i] = "baits/bait"+(i+1)+".png";
        }
        baitsNames[0] = "Черьв";
        baitsNames[1] = "Хлеб";
        baitsNames[2] = "Тесто";
        baitsNames[3] = "Мотыль";
        baitsNames[4] = "Опарыш";
        baitsNames[5] = "Личинка короеда";
        baitsNames[6] = "Манка";
        baitsNames[7] = "Поденка";
        baitsNames[8] = "Картофель";
        baitsNames[9] = "Мясо";
        baitsNames[10] = "Живец";
        baitsNames[11] = "Кузнечик";
        baitsNames[12] = "Икра";
        baitsNames[13] = "Зелень";
        baitsNames[14] = "Горошек";
        baitsNames[15] = "Лягушка";
        baitsNames[16] = "Кукуруза";
        baitsNames[17] = "Сырный кубик";
        baitsNames[18] = "Жук";
        baitsNames[19] = "Боил";
    }

    public int getBaitsCount(){
        return this.baitsCount;
    }

    public String[] getBaitsNames(){
        return this.baitsNames;
    }

    public Bitmap getBaitImg(Context context, int index){
        AssetManager assetManager = context.getAssets();
        InputStream is;
        Bitmap bitmap;
        try {
            is = assetManager.open(baitsImgNames[index]);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public String getBaitName(int index){
        return this.baitsNames[index];
    }
}
