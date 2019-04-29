package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by kartl on 07.08.2018.
 */

public class ShekvetebiSum implements Serializable {
    private String ludi, distrib_Name;
    private int distrib_id;
    private float k30in, k50in, k30wont, k50wont;

    public String getLudi() {
        return ludi;
    }

    public void setLudi(String ludi) {
        this.ludi = ludi;
    }

    public String getDistrib_Name() {
        return distrib_Name;
    }

    public void setDistrib_Name(String distrib_Name) {
        this.distrib_Name = distrib_Name;
    }

    public float getK30in() {
        return k30in;
    }

    public void setK30in(float k30in) {
        this.k30in = k30in;
    }

    public float getK50in() {
        return k50in;
    }

    public void setK50in(float k50in) {
        this.k50in = k50in;
    }

    public float getK30wont() {
        return k30wont;
    }

    public void setK30wont(float k30wont) {
        this.k30wont = k30wont;
    }

    public float getK50wont() {
        return k50wont;
    }

    public void setK50wont(float k50wont) {
        this.k50wont = k50wont;
    }

    public int getDistrib_id() {
        return distrib_id;
    }

    public void setDistrib_id(int distrib_id) {
        this.distrib_id = distrib_id;
    }
}
