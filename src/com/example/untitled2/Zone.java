package com.example.untitled2;

import android.graphics.Point;

public class Zone {
    private Point p1;
    private Point p2;
    private int depth;

    public Zone(){
    }

    public Zone(Point p1, Point p2, int depth){
        this.p1 = p1;
        this.p2 = p2;
        this.depth = depth;
    }

    public void setP1(Point p1){
        this.p1 = p1;
    }

    public void setP2(Point p2){
        this.p2 = p2;
    }

    public Point getP1(){
        return this.p1;
    }

    public Point getP2(){
        return this.p2;
    }

    public void setDepth(int depth){
        this.depth = depth;
    }

    public int getDepth(){
        return this.depth;
    }
}
