package org.origami.ws.models;

import java.io.Serializable;

public class Data implements Serializable {
    private String data;
    private Long id;
    private String unicode;


    public Data() {

    }

    public Data(Long id, String data) {
        this.id = id;
        this.data = data;
    }

    public Data(Long id, String data, String unicode) {
        this.data = data;
        this.id = id;
        this.unicode = unicode;
    }

    public Data(Long id) {
        this.id = id;
    }

    public Data(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnicode() {
        return unicode;
    }

    public void setUnicode(String unicode) {
        this.unicode = unicode;
    }

    @Override
    public String toString() {
        return "Data{" +
                "data='" + data + '\'' +
                ", id=" + id +
                ", unicode='" + unicode + '\'' +
                '}';
    }
}
