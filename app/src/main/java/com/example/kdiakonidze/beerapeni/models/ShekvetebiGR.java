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
    private int k30w = 0;
    private int k50w = 0;
    private int k30 = 0;
    private int k50 = 0;

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

    public int getK30w() {
        return k30w;
    }

    public void setK30w(int k30w) {
        this.k30w = k30w;
    }

    public int getK50w() {
        return k50w;
    }

    public void setK50w(int k50w) {
        this.k50w = k50w;
    }

    public int getK30() {
        return k30;
    }

    public void setK30(int k30) {
        this.k30 = k30;
    }

    public int getK50() {
        return k50;
    }

    public void setK50(int k50) {
        this.k50 = k50;
    }

    public ArrayList<ShekvetebiSum> getGrHeadOrderSum() {
        return grHeadOrderSum;
    }

    public void setGrHeadOrderSum(ArrayList<ShekvetebiSum> grHeadOrderSum) {
        this.grHeadOrderSum = grHeadOrderSum;
    }
}
