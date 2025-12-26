/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.RegMovimientosLazy;

import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.origami.sgr.models.ConsultaMovimientoModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import org.primefaces.PrimeFaces;

import javax.ejb.EJB;

/**
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgLazyMov implements Serializable {

    @EJB
    protected Entitymanager manager;
    @Inject
    private ServletSession ss;
    @Inject
    protected RegistroPropiedadServices reg;
    protected RegMovimientosLazy lazy;
    protected RegMovimiento movimiento;
    protected ConsultaMovimientoModel modelo = new ConsultaMovimientoModel();

    @PostConstruct
    protected void initView() {
        try {
            lazy = new RegMovimientosLazy();
            if (ss.getParametros() != null) {
                if (ss.getParametros().containsKey("idFicha")) {
                    lazy.addFilter("regMovimientoFichaCollection.ficha", ((Collection) ss.getParametros().get("idFicha")));
                }
                if (ss.getParametros().containsKey("idMovimiento")) {
                    Long idMovimiento = Long.valueOf(ss.getParametros().get("idMovimiento").toString());
                    movimiento = manager.find(RegMovimiento.class, idMovimiento);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void openDialogDetalle(RegMovimiento data) {
        movimiento = data;
        modelo = reg.getConsultaMovimiento(data.getId());
        if (modelo == null) {
            modelo = new ConsultaMovimientoModel();
        }
        JsfUti.update("formMovRegSelec");
        JsfUti.executeJS("PF('dlgMovimientoRegistral').show();");
    }

    public void selectMov(RegMovimiento mov) {
        PrimeFaces.current().dialog().closeDynamic(mov);
    }

    public RegMovimientosLazy getLazy() {
        return lazy;
    }

    public void setLazy(RegMovimientosLazy lazy) {
        this.lazy = lazy;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public ConsultaMovimientoModel getModelo() {
        return modelo;
    }

    public void setModelo(ConsultaMovimientoModel modelo) {
        this.modelo = modelo;
    }
}
