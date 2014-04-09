package com.example.untitled2;

import android.graphics.Point;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class Location {

    private Zone[][] depthZones;
    private String locationName;
    private ImageView img;
    private InputStream locationData;
    private int rowCount;
    private int colCount;
    private ArrayList<Fish> locationFishes;
    private int throwingDepth;

    public Location(String name, ImageView img, int rowCount, int colCount, InputStream inputStream){
        this.locationName = name;
        this.img = img;
        this.locationData = inputStream;
        this.rowCount = rowCount;
        this.colCount = colCount;
        createZones(rowCount, colCount);
        initFishes();
    }

    private void initFishes() {
        ArrayList<Integer> baits1 = new ArrayList<Integer>();
        baits1.add(0);
        baits1.add(1);
        baits1.add(2);
        baits1.add(3);
        baits1.add(4);
        baits1.add(5);
        baits1.add(6);
        baits1.add(7);
        Fish fish1 = new Fish("Плотва", 10000);
        fish1.setMaxWeight(3000);
        fish1.setMinWeight(150);
        fish1.setDepthToSurface(30);
        fish1.setDepthToBottom(10);
        fish1.setFishBaits(baits1);
        ArrayList<Integer> baits2 = new ArrayList<Integer>();
        baits2.add(0);
        baits2.add(1);
        baits1.add(2);
        baits1.add(3);
        baits1.add(4);
        baits1.add(6);
        baits2.add(8);
        baits1.add(16);
        baits1.add(19);
        Fish fish2 = new Fish("Карась", 5000);
        fish2.setMaxWeight(4000);
        fish2.setMinWeight(300);
        fish2.setDepthToSurface(100);
        fish2.setDepthToBottom(0);
        fish2.setFishBaits(baits2);
        locationFishes = new ArrayList<Fish>();
        locationFishes.add(fish1);
        locationFishes.add(fish2);
    }

    public Fish getFish(String baitName, int depth){
        if(baitName != null){
            ArrayList<Fish> canEat = new ArrayList<Fish>();
            ArrayList<Fish> canBe = new ArrayList<Fish>();
            for(int i = 0; i < locationFishes.size(); i++){
                Fish f = locationFishes.get(i);
                if(f.isFishEatThis(baitName))
                    canEat.add(f);
            }
            for(int i = 0; i < canEat.size(); i++){
                Fish f = canEat.get(i);
                if((depth <= (throwingDepth - f.getDepthToBottom())) && depth >= f.getDepthToSurface())
                    canBe.add(f);
            }
            Random rand  = new Random();
            if(canBe.size() != 0)
                return canBe.get(rand.nextInt(canBe.size()));
            else
                return null;
        }else{
            return null;
        }
    }

    public String getLocationName(){
        return this.locationName;
    }

    public void setLocationName(String locationName){
        this.locationName = locationName;
    }

    public Zone[][] getDepthZones(){
        return this.depthZones;
    }

    public void setDepthZones(Zone[][] depthZones){
        this.depthZones = depthZones;
    }

    private void createZones(int rowCount, int colCount){
        depthZones = new Zone[rowCount][colCount];
        for(int i = 0; i < rowCount; i++)
            for(int j = 0; j < colCount; j++)
                depthZones[i][j] = new Zone();
        Log.e("SYKA WO ZA NAXYI BLYA", "" + img.getHeight() + "; " + img.getWidth());
        int height = img.getHeight()/rowCount;
        int width = img.getWidth()/colCount;
        for(int i = 0; i < rowCount - 1; i++)
            for(int j = 0; j < colCount - 1; j++){
                depthZones[i][j].setP1(new Point(j * width, i * height));
                depthZones[i][j].setP2(new Point((j + 1) * width, (i + 1) * height));
            }
        for(int i = 0; i < rowCount - 1; i++){
            depthZones[i][colCount - 1].setP1(new Point(depthZones[i][8].getP2().x, depthZones[i][8].getP1().y));
            depthZones[i][colCount - 1].setP2(new Point(img.getWidth(), depthZones[i][8].getP2().y));
        }
        for(int i = 0; i < colCount - 1; i++){
            depthZones[rowCount - 1][i].setP1(new Point(i * width, depthZones[6][i].getP2().y));
            depthZones[rowCount - 1][i].setP2(new Point((i + 1) * width, img.getHeight()));
        }
        depthZones[rowCount - 1][colCount - 1].setP1(new Point(depthZones[0][8].getP2().x, depthZones[6][0].getP2().y));
        depthZones[rowCount - 1][colCount - 1].setP2(new Point(img.getWidth(), img.getHeight()));
        setDepths();
    }

    private void setDepths(){
        try {
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(locationData));
            String line = "";
            line = bufferedReader.readLine();
            bufferedReader.close();
            int k = 0;
            for(int i = 0; i < rowCount; i++)
                for(int j = 0; j < colCount; j++){
                    depthZones[i][j].setDepth(Integer.parseInt(line.split(";")[k]));
                    Log.e("POINTS", "P1-"+depthZones[i][j].getP1().x+", "+depthZones[i][j].getP1().y);
                    Log.e("POINTS", "P2-"+depthZones[i][j].getP2().x+", "+depthZones[i][j].getP2().y);
                    Log.e("AFDS", depthZones[i][j].getDepth()+"");
                    k++;
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWaterDepth(int x, int y){
        int depth = 0;
        int px = 0;
        int py = 0;

        for(int i = 0; i < colCount; i++)
            if(x <= depthZones[0][i].getP2().x){
                px = i;
                break;
            }
        for(int i = 0; i < rowCount; i++)
            if(y <= depthZones[i][px].getP2().y){
                py = i;
                break;
            }

        depth = depthZones[py][px].getDepth();
        this.throwingDepth = depth;
        return depth;
    }
}
