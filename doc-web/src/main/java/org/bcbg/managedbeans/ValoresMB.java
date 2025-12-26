/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.CorreoSettings;
import org.bcbg.entities.Valores;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.DocumentalService;

/**
 *
 * @author ORIGAMI
 */
@Named(value = "valoresMB")
@ViewScoped
public class ValoresMB implements Serializable {

    @Inject
    private AppServices services;
    @Inject
    protected UserSession us;
    @Inject
    protected DocumentalService documentalService;

    private Valores valor;
    private List<Valores> valores;

    @PostConstruct
    public void init() {
        try {
            valor = new Valores();
            valores = services.getValoresPredeterminados();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarValor(Valores v) {
        this.valor = v;
    }

    public void guardarValor() {
        if (Utils.isEmptyString(valor.getCode())) {
            JsfUti.messageError(null, Messages.ingresarCodigo, "");
            return;
        }
        if (Utils.isEmptyString(valor.getValorString())) {
            valor.setValorString("");
        }
        if (valor.getValorNumeric() == null) {
            valor.setValorNumeric(BigDecimal.ZERO);
        }
        Valores rest = services.guardarValores(valor);
        if (rest != null) {
            setVariables(rest);
            valor = new Valores();
            JsfUti.messageInfo(null, Messages.correcto, "");
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public void setVariables(Valores rest) {
        if (rest.getCode().contains((Variables.correo))) {
            List<Valores> correos = services.getValores(Variables.correo);
            CorreoSettings correoSettings = new CorreoSettings();
            for (Valores v : correos) {
                switch (v.getCode()) {
                    case "CORREO":
                        correoSettings.setCorreo(v.getValorString());
                        break;
                    case "CORREO_PASS":
                        correoSettings.setCorreoClave(v.getValorString());
                        break;
                    case "CORREO_HOST":
                        correoSettings.setCorreoHost(v.getValorString());
                        break;
                    case "CORREO_PORT":
                        correoSettings.setCorreoPort(v.getValorString());
                        break;
                }
            }
            services.guardarConfiguracionesCorreo(correoSettings);
        }

    }

    public void abrirJenkins() {
        JsfUti.redirectNewTab(SisVars.JENKINS);
    }

    public Valores getValor() {
        return valor;
    }

    public void setValor(Valores valor) {
        this.valor = valor;
    }

    public List<Valores> getValores() {
        return valores;
    }

    public void setValores(List<Valores> valores) {
        this.valores = valores;
    }

}
