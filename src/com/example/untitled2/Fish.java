package com.example.untitled2;

import java.util.ArrayList;

public class Fish {

    private String fishName;
    private int fishCount;
    private int minWeight;
    private int maxWeight;
    private int depthToBottom;
    private int depthToSurface;
    private Bait baits;
    private ArrayList<Integer> fishBaits;

    public Fish(String fishName, int fishCount){
        this.fishName = fishName;
        this.fishCount = fishCount;
        baits = new Bait();
        fishBaits = new ArrayList<Integer>();
    }

    public String getFishName(){
        return this.fishName;
    }

    public boolean isFishEatThis(String bait){
        for(int i = 0; i < fishBaits.size(); i++)
            if(bait == baits.getBaitName(fishBaits.get(i)))
                return true;
        return false;
    }

    public void setFishBaits(ArrayList<Integer> fishBaits){
        this.fishBaits = fishBaits;
    }

    public void setMinWeight(int minWeight){
        this.minWeight = minWeight;
    }

    public void setMaxWeight(int maxWeight){
        this.maxWeight = maxWeight;
    }

    public int getMinWeight(){
        return this.minWeight;
    }

    public int getMaxWeight(){
        return this.maxWeight;
    }

    public void setDepthToBottom(int depthToBottom){
        this.depthToBottom = depthToBottom;
    }

    public void setDepthToSurface(int depthToSurface){
        this.depthToSurface = depthToSurface;
    }

    public int getDepthToBottom(){
        return this.depthToBottom;
    }

    public int getDepthToSurface(){
        return this.depthToSurface;
    }
}
