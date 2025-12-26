/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author asanc
 */
@Named
@ViewScoped
public class DlgFicha implements Serializable {

    @EJB
    protected Entitymanager manager;
    @Inject
    private ServletSession ss;
    @Inject
    protected RegistroPropiedadServices reg;

    private RegFicha ficha;
    protected List<RegMovimientoFicha> movimientos;
    protected List<RegEnteInterviniente> propietarios = new ArrayList<>();

    @PostConstruct
    protected void initView() {
        try {

            if (ss.getParametros() != null) {
                if (ss.getParametros().containsKey("idFicha")) {
                    Long idFicha = Long.valueOf(ss.getParametros().get("idFicha").toString());
                    ficha = manager.find(RegFicha.class, idFicha);
                    movimientos = reg.getRegMovByIdFicha(ficha.getId());
                    propietarios = reg.getPropietariosByFicha(ficha.getId());

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public List<RegMovimientoFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimientoFicha> movimientos) {
        this.movimientos = movimientos;
    }

    public List<RegEnteInterviniente> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegEnteInterviniente> propietarios) {
        this.propietarios = propietarios;
    }

}
