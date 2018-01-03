package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by k.diakonidze on 11/1/2017.
 */

public class Obieqti implements Serializable {
    private String dasaxeleba, adress, tel, comment, sk, sakpiri;
    private Integer id;
    private ArrayList<Double> fasebi = new ArrayList<>();

    public Obieqti(String dasaxeleba) {
        this.dasaxeleba = dasaxeleba;

    }

    public Integer getId() {
        return id;
    }

    public ArrayList<Double> getFasebi() {
        return fasebi;
    }

    public void setFasebi(ArrayList<Double> fasebi) {
        this.fasebi = fasebi;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDasaxeleba() {
        return dasaxeleba;
    }

    public void setDasaxeleba(String dasaxeleba) {
        this.dasaxeleba = dasaxeleba;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }

    public String getSakpiri() {
        return sakpiri;
    }

    public void setSakpiri(String sakpiri) {
        this.sakpiri = sakpiri;
    }

    @Override
    public String toString() {
        return dasaxeleba;
    }
}
