package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by kartl on 07.08.2018.
 */

public class ShekvetebiSum implements Serializable {
    private String ludi, distrib_Name;
    private int k30in, k50in, k30wont, k50wont, distrib_id;

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

    public int getK30in() {
        return k30in;
    }

    public void setK30in(int k30in) {
        this.k30in = k30in;
    }

    public int getK50in() {
        return k50in;
    }

    public void setK50in(int k50in) {
        this.k50in = k50in;
    }

    public int getK30wont() {
        return k30wont;
    }

    public void setK30wont(int k30wont) {
        this.k30wont = k30wont;
    }

    public int getK50wont() {
        return k50wont;
    }

    public void setK50wont(int k50wont) {
        this.k50wont = k50wont;
    }

    public int getDistrib_id() {
        return distrib_id;
    }

    public void setDistrib_id(int distrib_id) {
        this.distrib_id = distrib_id;
    }
}
