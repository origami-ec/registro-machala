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
public class FacturaEmitirErp implements Serializable {
    
    private String success;
    private String data;

    public FacturaEmitirErp() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaEmitirErp{");
        sb.append("success=").append(success);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
    
}
