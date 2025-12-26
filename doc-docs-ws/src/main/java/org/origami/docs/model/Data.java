package org.origami.docs.model;

import java.io.Serializable;

public class Data implements Serializable {

    private Long id;
    private String data;
    private String status;

    public Data() {
    }

    public Data(Long id, String data) {
        this.id = id;
        this.data = data;
    }

    public Data(Long id) {
        this.id = id;
    }

    public Data(String data) {
        this.data = data;
    }

    public Data(Long id, String data, String status) {
        this.id = id;
        this.data = data;
        this.status = status;
    }

    public Data(String data, String status) {
        this.data = data;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
