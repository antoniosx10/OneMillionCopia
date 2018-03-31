package com.example.antonio.todash_copia;

import java.util.Date;

/**
 * Created by Antonio on 22/03/2018.
 */

public class Task {

    private String contenuto;
    private Date data;

    public Task(String contenuto, Date data) {
        this.contenuto = contenuto;
        this.data = data;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
