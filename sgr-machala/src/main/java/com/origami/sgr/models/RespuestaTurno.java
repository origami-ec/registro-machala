/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import java.io.Serializable;

/**
 *
 * @author eduar
 */
public class RespuestaTurno implements Serializable {

    private String status;
    private PersonaTurno data;

    public RespuestaTurno() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public PersonaTurno getData() {
        return data;
    }

    public void setData(PersonaTurno data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RespuestaTurno{");
        sb.append("status=").append(status);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

}
