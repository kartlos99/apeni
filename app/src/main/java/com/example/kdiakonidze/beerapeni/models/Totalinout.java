package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 12.03.2018.
 */

public class Totalinout implements Serializable {
    private String ludi;
    private int k30s, k50s, k30r, k50r;

    public Totalinout(String ludi, int k30s, int k50s, int k30r, int k50r) {
        this.ludi = ludi;
        this.k30s = k30s;
        this.k50s = k50s;
        this.k30r = k30r;
        this.k50r = k50r;
    }

    public String getLudi() {
        return ludi;
    }

    public void setLudi(String ludi) {
        this.ludi = ludi;
    }

    public int getK30s() {
        return k30s;
    }

    public void setK30s(int k30s) {
        this.k30s = k30s;
    }

    public int getK50s() {
        return k50s;
    }

    public void setK50s(int k50s) {
        this.k50s = k50s;
    }

    public int getK30r() {
        return k30r;
    }

    public void setK30r(int k30r) {
        this.k30r = k30r;
    }

    public int getK50r() {
        return k50r;
    }

    public void setK50r(int k50r) {
        this.k50r = k50r;
    }
}
