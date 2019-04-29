package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by k.diakonidze on 18.04.2018.
 */

public class ShekvetebiGR implements Serializable{
    private String name;
    private ArrayList<Shekvetebi> childs;
    private ArrayList<ShekvetebiSum> grHeadOrderSum = new ArrayList<>();;
    private float k30w = 0;
    private float k50w = 0;
    private float k30 = 0;
    private float k50 = 0;

    public ShekvetebiGR(String name, ArrayList<Shekvetebi> childs) {
        this.name = name;
        this.childs = childs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Shekvetebi> getChilds() {
        return childs;
    }

    public void setChilds(ArrayList<Shekvetebi> childs) {
        this.childs = childs;
    }

    public float getK30w() {
        return k30w;
    }

    public void setK30w(float k30w) {
        this.k30w = k30w;
    }

    public float getK50w() {
        return k50w;
    }

    public void setK50w(float k50w) {
        this.k50w = k50w;
    }

    public float getK30() {
        return k30;
    }

    public void setK30(float k30) {
        this.k30 = k30;
    }

    public float getK50() {
        return k50;
    }

    public void setK50(float k50) {
        this.k50 = k50;
    }

    public ArrayList<ShekvetebiSum> getGrHeadOrderSum() {
        return grHeadOrderSum;
    }

    public void setGrHeadOrderSum(ArrayList<ShekvetebiSum> grHeadOrderSum) {
        this.grHeadOrderSum = grHeadOrderSum;
    }
}
