/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.component;

import com.origami.session.ServletSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.InscripcionesLazy;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class DlgLazyInscripciones implements Serializable {

    @Inject
    private ServletSession ss;

    protected InscripcionesLazy lazy;
    protected List<RegMovimiento> movimientosSeleccionados;

    @PostConstruct
    protected void initView() {
        lazy = new InscripcionesLazy();
        if (ss.getParametros() != null) {
            if (ss.getParametros().containsKey("idFicha")) {
                lazy.getFilterss().put("regMovimientoFichaCollection.ficha", ((Collection) ss.getParametros().get("idFicha")));
            }
        }
        ss.borrarDatos();
    }

    public void selectMov(RegMovimiento mov) {
        PrimeFaces.current().dialog().closeDynamic(mov);
    }

    public void devolverMovimientos() {
        try {
            if (!movimientosSeleccionados.isEmpty()) {
                PrimeFaces.current().dialog().closeDynamic(movimientosSeleccionados);
            } else {
                JsfUti.messageWarning(null, "No ha seleccionado ningún movimiento.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            System.out.println(e);
        }
    }

    public List<RegMovimiento> getMovimientosSeleccionados() {
        return movimientosSeleccionados;
    }

    public void setMovimientosSeleccionados(List<RegMovimiento> movimientosSeleccionados) {
        this.movimientosSeleccionados = movimientosSeleccionados;
    }

    public InscripcionesLazy getLazy() {
        return lazy;
    }

    public void setLazy(InscripcionesLazy lazy) {
        this.lazy = lazy;
    }

}
