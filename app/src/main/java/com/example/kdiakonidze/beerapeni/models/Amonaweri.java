package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 11/14/2017.
 */

public class Amonaweri implements Serializable {
    private String tarigi, comment;
    private float k_in, k_out, k_balance, price, pay, balance;
    private int id;

    @Override
    public String toString() {
        return "Amonaweri{" +
                "tarigi='" + tarigi + '\'' +
                ", price=" + price +
                ", pay=" + pay +
                ", balance=" + balance +
                ", k_in=" + k_in +
                ", k_out=" + k_out +
                ", k_balance=" + k_balance +
                ", id=" + id +
                '}';
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

    public String getTarigi() {
        return tarigi;
    }

    public void setTarigi(String tarigi) {
        this.tarigi = tarigi;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public float getK_in() {
        return k_in;
    }

    public void setK_in(float k_in) {
        this.k_in = k_in;
    }

    public float getK_out() {
        return k_out;
    }

    public void setK_out(float k_out) {
        this.k_out = k_out;
    }

    public float getK_balance() {
        return k_balance;
    }

    public void setK_balance(float k_balance) {
        this.k_balance = k_balance;
    }
}
