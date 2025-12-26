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
public class PersonaTurno implements Serializable {

    private String pers_codigo;
    private String pers_documento;
    private String pers_razon_social;
    private String pers_email;
    private String pers_directorio_token;
    private String pers_direccion;
    private String pers_telefono_fijo;
    private String pers_telefono_movil;
    private String pers_nombres;
    private String pers_apellidos;
    private Integer tdet_codigo_tipo_documento;

    public PersonaTurno() {
    }

    public String getPers_codigo() {
        return pers_codigo;
    }

    public void setPers_codigo(String pers_codigo) {
        this.pers_codigo = pers_codigo;
    }

    public String getPers_documento() {
        return pers_documento;
    }

    public void setPers_documento(String pers_documento) {
        this.pers_documento = pers_documento;
    }

    public String getPers_razon_social() {
        return pers_razon_social;
    }

    public void setPers_razon_social(String pers_razon_social) {
        this.pers_razon_social = pers_razon_social;
    }

    public String getPers_email() {
        return pers_email;
    }

    public void setPers_email(String pers_email) {
        this.pers_email = pers_email;
    }

    public String getPers_directorio_token() {
        return pers_directorio_token;
    }

    public void setPers_directorio_token(String pers_directorio_token) {
        this.pers_directorio_token = pers_directorio_token;
    }

    public String getPers_direccion() {
        return pers_direccion;
    }

    public void setPers_direccion(String pers_direccion) {
        this.pers_direccion = pers_direccion;
    }

    public String getPers_telefono_fijo() {
        return pers_telefono_fijo;
    }

    public void setPers_telefono_fijo(String pers_telefono_fijo) {
        this.pers_telefono_fijo = pers_telefono_fijo;
    }

    public String getPers_telefono_movil() {
        return pers_telefono_movil;
    }

    public void setPers_telefono_movil(String pers_telefono_movil) {
        this.pers_telefono_movil = pers_telefono_movil;
    }

    public String getPers_nombres() {
        return pers_nombres;
    }

    public void setPers_nombres(String pers_nombres) {
        this.pers_nombres = pers_nombres;
    }

    public String getPers_apellidos() {
        return pers_apellidos;
    }

    public void setPers_apellidos(String pers_apellidos) {
        this.pers_apellidos = pers_apellidos;
    }

    public Integer getTdet_codigo_tipo_documento() {
        return tdet_codigo_tipo_documento;
    }

    public void setTdet_codigo_tipo_documento(Integer tdet_codigo_tipo_documento) {
        this.tdet_codigo_tipo_documento = tdet_codigo_tipo_documento;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PersonaTurno{");
        sb.append("pers_codigo=").append(pers_codigo);
        sb.append(", pers_documento=").append(pers_documento);
        sb.append(", pers_razon_social=").append(pers_razon_social);
        sb.append(", pers_email=").append(pers_email);
        sb.append(", pers_directorio_token=").append(pers_directorio_token);
        sb.append(", pers_direccion=").append(pers_direccion);
        sb.append(", pers_telefono_fijo=").append(pers_telefono_fijo);
        sb.append(", pers_telefono_movil=").append(pers_telefono_movil);
        sb.append(", pers_nombres=").append(pers_nombres);
        sb.append(", pers_apellidos=").append(pers_apellidos);
        sb.append(", tdet_codigo_tipo_documento=").append(tdet_codigo_tipo_documento);
        sb.append('}');
        return sb.toString();
    }

}
