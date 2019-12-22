package ru.ssspokd.apacheignite.model;

import java.io.Serializable;

public class Payment implements Serializable {
    private Long id;
    private  String namePayment;

    public String getNamePayment() {
        return namePayment;
    }

    public void setNamePayment(String namePayment) {
        this.namePayment = namePayment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
