/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.documental.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.documental.models.TipoDato;
import org.bcbg.documental.models.TipoDatoCompuesto;
import org.bcbg.util.JsfUti;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class TipoDatos implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TipoDatos.class.getName());

    @Inject
    private BcbgService irs;

    protected TipoDato tipo;
    protected TipoDatoCompuesto tdc;
    protected List<TipoDato> datosSimple;
    protected List<TipoDato> datosLista;
    protected List<TipoDatoCompuesto> lista;

    @PostConstruct
    public void initView() {
        lista = new ArrayList<>();
        datosSimple = irs.methodListGET(SisVars.ws + "tipoDato/datoSimple", TipoDato[].class);
        datosLista = irs.methodListGET(SisVars.ws + "tipoDato/datoLista", TipoDato[].class);
    }

    public void newTipoDato(int tipodato) {
        tipo = new TipoDato();
        tipo.setTipo(tipodato);
        JsfUti.update("formEdit");
        JsfUti.executeJS("PF('dlgEdicion').show();");
    }

    public void showDlgEdit(TipoDato td) {
        tipo = td;
        JsfUti.update("formEdit");
        JsfUti.executeJS("PF('dlgEdicion').show();");
    }

    public void saveTipoDato() {
        try {
            irs.methodPOST(tipo, SisVars.ws + "tipoDato/guardar", TipoDato.class);
            this.initView();
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgEdicion').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar tipo dato.", e);
        }
    }

    public void agregarCompuesto() {
        if (tipo.getId() == null) {
            JsfUti.messageWarning(null, "Advertencia", "Debe guardar el nuevo tipo de dato para agregar detalles.");
            return;
        }
        tdc = new TipoDatoCompuesto();
        tdc.setTipoDato(tipo);
        JsfUti.executeJS("PF('dlgAddDetalle').hide();");
        JsfUti.update("formCompuesto");
        JsfUti.executeJS("PF('dlgCompuesto').show();");
    }

    public void editarCompuesto(TipoDatoCompuesto td) {
        tdc = td;
        JsfUti.executeJS("PF('dlgAddDetalle').hide();");
        JsfUti.update("formCompuesto");
        JsfUti.executeJS("PF('dlgCompuesto').show();");
    }

    public void saveCompuesto() {
        try {
            if (tdc.getId() == null) {
                irs.methodPOST(tdc, SisVars.ws + "tipoDatoCompuesto/guardar", TipoDatoCompuesto.class);
            } else {
                irs.methodPUT(tdc, SisVars.ws + "tipoDatoCompuesto/update", TipoDatoCompuesto.class);
            }
            JsfUti.executeJS("PF('dlgCompuesto').hide();");
            this.showDlgDetalles(tdc.getTipoDato());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Guardar dato compuesto.", e);
        }
    }

    public void showDlgDetalles(TipoDato td) {
        tipo = td;
        lista = irs.methodListGET(SisVars.ws + "tipoDatoCompuesto/tipoDato/" + td.getId(), TipoDatoCompuesto[].class);
        JsfUti.update("formDetalle");
        JsfUti.executeJS("PF('dlgAddDetalle').show();");
    }

    public TipoDato getTipo() {
        return tipo;
    }

    public void setTipo(TipoDato tipo) {
        this.tipo = tipo;
    }

    public TipoDatoCompuesto getTdc() {
        return tdc;
    }

    public void setTdc(TipoDatoCompuesto tdc) {
        this.tdc = tdc;
    }

    public List<TipoDatoCompuesto> getLista() {
        return lista;
    }

    public void setLista(List<TipoDatoCompuesto> lista) {
        this.lista = lista;
    }

    public List<TipoDato> getDatosSimple() {
        return datosSimple;
    }

    public void setDatosSimple(List<TipoDato> datosSimple) {
        this.datosSimple = datosSimple;
    }

    public List<TipoDato> getDatosLista() {
        return datosLista;
    }

    public void setDatosLista(List<TipoDato> datosLista) {
        this.datosLista = datosLista;
    }

}
