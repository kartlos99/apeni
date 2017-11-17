package com.example.kdiakonidze.beerapeni.models;

import java.io.Serializable;

/**
 * Created by k.diakonidze on 11/14/2017.
 */

public class Amonaweri implements Serializable {
    String tarigi;
    double price, pay, balance;
    int k_in, k_out, k_balance;

    public String getTarigi() {
        return tarigi;
    }

    public void setTarigi(String tarigi) {
        this.tarigi = tarigi;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getK_in() {
        return k_in;
    }

    public void setK_in(int k_in) {
        this.k_in = k_in;
    }

    public int getK_out() {
        return k_out;
    }

    public void setK_out(int k_out) {
        this.k_out = k_out;
    }

    public int getK_balance() {
        return k_balance;
    }

    public void setK_balance(int k_balance) {
        this.k_balance = k_balance;
    }
}
