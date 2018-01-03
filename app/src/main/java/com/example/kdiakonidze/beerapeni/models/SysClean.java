package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by kartl on 03.01.2018.
 */

public class SysClean implements Serializable{
    int id, distr_id, dge;
    String tarigi, dasaxeleba, comment;

    @Override
    public String toString() {
        return dasaxeleba;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistr_id() {
        return distr_id;
    }

    public void setDistr_id(int distr_id) {
        this.distr_id = distr_id;
    }

    public int getDge() {
        return dge;
    }

    public void setDge(int dge) {
        this.dge = dge;
    }

    public String getTarigi() {
        return tarigi;
    }

    public void setTarigi(String tarigi) {
        this.tarigi = tarigi;
    }

    public String getDasaxeleba() {
        return dasaxeleba;
    }

    public void setDasaxeleba(String dasaxeleba) {
        this.dasaxeleba = dasaxeleba;
    }
}
