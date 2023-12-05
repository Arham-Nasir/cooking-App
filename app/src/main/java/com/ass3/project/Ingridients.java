package com.ass3.project;

import java.io.Serializable;

public class Ingridients  implements Serializable {
    public String name;
    public  String amount;


    public Ingridients() {
    }

    public Ingridients(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
