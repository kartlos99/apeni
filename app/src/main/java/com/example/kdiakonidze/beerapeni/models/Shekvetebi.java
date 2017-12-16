package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class Shekvetebi implements Serializable {
    String obieqti, ludi, tarigi, chk, distrib_Name;
    int k30in, k50in, k30wont, k50wont, order_id, distrib_id;

    public Shekvetebi(String obieqti, String ludi, int k30in, int k50in, int k30wont, int k50wont) {
        this.obieqti = obieqti;
        this.ludi = ludi;
        this.k30in = k30in;
        this.k50in = k50in;
        this.k30wont = k30wont;
        this.k50wont = k50wont;
    }

    public String getObieqti() {
        return obieqti;
    }

    public void setObieqti(String obieqti) {
        this.obieqti = obieqti;
    }

    public String getLudi() {
        return ludi;
    }

    public void setLudi(String ludi) {
        this.ludi = ludi;
    }

    public String getTarigi() {
        return tarigi;
    }

    public void setTarigi(String tarigi) {
        this.tarigi = tarigi;
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

    public String getChk() {
        return chk;
    }

    public void setChk(String chk) {
        this.chk = chk;
    }

    public String getDistrib_Name() {
        return distrib_Name;
    }

    public void setDistrib_Name(String distrib_Name) {
        this.distrib_Name = distrib_Name;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getDistrib_id() {
        return distrib_id;
    }

    public void setDistrib_id(int distrib_id) {
        this.distrib_id = distrib_id;
    }
}
