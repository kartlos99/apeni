package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 10/30/2017.
 */

public class Shekvetebi implements Serializable {
    private String obieqti, ludi, tarigi, chk, distrib_Name, comment, color;
    private int order_id, distrib_id, beer_id;
    private float k30in, k50in, k30wont, k50wont;

    public Shekvetebi(String obieqti, String ludi, float k30in, float k50in, float k30wont, float k50wont) {
        this.obieqti = obieqti;
        this.ludi = ludi;
        this.k30in = k30in;
        this.k50in = k50in;
        this.k30wont = k30wont;
        this.k50wont = k50wont;
    }

    @Override
    public String toString() {
        return "Shekvetebi{" +
                "obieqti='" + obieqti + '\'' +
                ", ludi='" + ludi + '\'' +
                ", tarigi='" + tarigi + '\'' +
                ", chk='" + chk + '\'' +
                ", distrib_Name='" + distrib_Name + '\'' +
                ", comment='" + comment + '\'' +
                ", color='" + color + '\'' +
                ", order_id=" + order_id +
                ", distrib_id=" + distrib_id +
                ", beer_id=" + beer_id +
                ", k30in=" + k30in +
                ", k50in=" + k50in +
                ", k30wont=" + k30wont +
                ", k50wont=" + k50wont +
                '}';
    }

    public int getBeer_id() {
        return beer_id;
    }

    public void setBeer_id(int beer_id) {
        this.beer_id = beer_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void addK30(String color) {
        this.color = color;
    }
}
