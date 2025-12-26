/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author eduar
 */
public class FacturaConsultaErp implements Serializable {

    private String success;
    private String sumary;
    private String message;
    private String timestamp;
    private List<FacturaData> data;

    public FacturaConsultaErp() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSumary() {
        return sumary;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<FacturaData> getData() {
        return data;
    }

    public void setData(List<FacturaData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FacturaConsultaErp{");
        sb.append("success=").append(success);
        sb.append(", sumary=").append(sumary);
        sb.append(", message=").append(message);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }

}
