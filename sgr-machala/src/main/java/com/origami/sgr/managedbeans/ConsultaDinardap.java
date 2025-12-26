/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.dinarp.models.RespuestaDinarp;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.models.PubPersona;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class ConsultaDinardap extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private RegistroPropiedadServices reg;

    protected PubPersona persona;
    protected String fechaNacimiento;
    protected SimpleDateFormat sdf;
    protected Integer consulta = 1;
    protected RespuestaDinarp respuesta;
    protected String documento;

    @PostConstruct
    protected void initView() {
        try {
            persona = new PubPersona();
            sdf = new SimpleDateFormat("dd/MM/yyyy");
            respuesta = new RespuestaDinarp();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarDinardap() {
        try {
            if (!persona.getCedRuc().isEmpty()) {
                persona = reg.buscarDinardap(persona.getCedRuc());
                if (persona != null) {
                    JsfUti.messageInfo(null, "Los datos encontrados son los siguientes.", "");
                    if (persona.getFechaNacimientoLong() != null) {
                        fechaNacimiento = sdf.format(new Date(persona.getFechaNacimientoLong()));
                    }
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar campo CI/RUC.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultarDatosDinarp() {
        try {
            if (documento != null && !documento.isEmpty()) {
                respuesta = reg.buscarDatosDinarp(documento);
                if (respuesta == null || respuesta.getNombre() == null || respuesta.getNombre().isEmpty()) {
                    JsfUti.messageError(null, "NO se encontraron datos.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar campo CI/RUC.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Integer getConsulta() {
        return consulta;
    }

    public void setConsulta(Integer consulta) {
        this.consulta = consulta;
    }

    public PubPersona getPersona() {
        return persona;
    }

    public void setPersona(PubPersona persona) {
        this.persona = persona;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public RespuestaDinarp getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(RespuestaDinarp respuesta) {
        this.respuesta = respuesta;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

}
