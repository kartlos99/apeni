package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 12/14/2017.
 */

public class Useri implements Serializable {
    private int id, type;
    private String username, name, pass, tel, adress, comment, maker;

    public Useri(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
//        return "Useri{" +
//                "id=" + id +
//                ", type=" + type +
//                ", maker=" + maker +
//                ", username='" + username + '\'' +
//                ", name='" + name + '\'' +
//                ", tel='" + tel + '\'' +
//                ", adress='" + adress + '\'' +
//                ", comment='" + comment + '\'' +
//                '}';
        return name;
    }
}
