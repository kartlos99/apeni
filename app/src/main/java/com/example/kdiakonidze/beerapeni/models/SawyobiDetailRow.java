package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 16.03.2018.
 */

public class SawyobiDetailRow implements Serializable {
    private String tarigi, distributor, ludi, comment, chek, id;
    private int ludisID;
    private float k30, k50;

    public String getId() {
        return id;
    }

    public SawyobiDetailRow(String id, String tarigi, String distributor, String ludi, int ludisID, String comment, float k30, float k50, String chek) {
        this.tarigi = tarigi;
        this.distributor = distributor;
        this.ludi = ludi;
        this.comment = comment;
        this.k30 = k30;
        this.k50 = k50;
        this.chek = chek;
        this.id = id;
        this.ludisID = ludisID;
    }

    public int getLudisID() {
        return ludisID;
    }

    public String getTarigi() {
        return tarigi;
    }

    public String getDistributor() {
        return distributor;
    }

    public String getLudi() {
        return ludi;
    }

    public String getComment() {
        return comment;
    }

    public float getK30() {
        return k30;
    }

    public float getK50() {
        return k50;
    }

    public String getChek() {
        return chek;
    }

    @Override
    public String toString() {
        return "SawyobiDetailRow{" +
                "tarigi='" + tarigi + '\'' +
                ", distributor='" + distributor + '\'' +
                ", ludi='" + ludi + '\'' +
                ", comment='" + comment + '\'' +
                ", chek='" + chek + '\'' +
                ", id='" + id + '\'' +
                ", k30=" + k30 +
                ", k50=" + k50 +
                '}';
    }
}
