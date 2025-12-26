/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans.indexacion;

import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.SecuenciaInscripcion;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.ActividadesTransaccionales;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.origami.sgr.util.Querys;
import java.math.BigInteger;
import java.util.Calendar;

/**
 *
 * @author eduar
 */
@Named
@ViewScoped
public class CorregirSecuenciaInscripcion implements Serializable {

    @Inject
    private SeqGenMan sec;

    @Inject
    private Entitymanager em;

    @Inject
    private BitacoraServices bs;

    protected RegLibro libro;
    protected List<RegLibro> libros;
    protected List<RegMovimiento> inscripciones;
    protected List<SecuenciaInscripcion> secuencias;
    protected Integer periodo;

    protected RegMovimiento movimiento;
    protected RegMovimientosLazy movimientosLazy;

    @PostConstruct
    public void initView() {
        try {
            movimientosLazy = new RegMovimientosLazy();
            libros = em.findAllEntCopy(Querys.getRegLibroList);
            periodo = Calendar.getInstance().get(Calendar.YEAR);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void buscarSecuencias() {
        try {
            if (libro != null && periodo != null) {
                secuencias = em.findMax(Querys.getSecuenciasxLibro, new String[]{"idlibro", "periodo"},
                        new Object[]{BigInteger.valueOf(libro.getId()), periodo}, 10);
                inscripciones = em.findMax(Querys.getMovimientosxLibro, new String[]{"idlibro", "periodo"},
                        new Object[]{libro.getId(), periodo.toString()}, 10);
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar el Libro.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void editarInscripcion(RegMovimiento mov) {
        try {
            mov.setNumInscripcion(null);
            mov.setFechaInscripcion(null);
            em.update(mov);
            this.buscarSecuencias();
            JsfUti.messageInfo(null, "Transaccion realizada con exito.", "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, "Error de sistema.", "");
        }
    }

    public void crearSecuencia() {
        try {
            if (libro != null && periodo != null) {
                sec.getSecuenciaInscripcion(libro.getId());
                this.buscarSecuencias();
                JsfUti.messageInfo(null, "Transaccion realizada con exito.", "");
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar el Libro.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void borrarSecuencia(SecuenciaInscripcion sec) {
        try {
            em.delete(sec);
            this.buscarSecuencias();
            JsfUti.messageInfo(null, "Transaccion realizada con exito.", "");
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageError(null, "Error de sistema.", "");
        }
    }

    public void showDlgEditarInscripcion(RegMovimiento mov) {
        try {
            movimiento = mov;
            JsfUti.update("formCorregir");
            JsfUti.executeJS("PF('dlgCorregir').show();");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void actualizarMovimiento() {
        try {
            if (movimiento.getNumInscripcion() != null && movimiento.getNumRepertorio() != null
                    && movimiento.getFechaInscripcion() != null && movimiento.getFechaRepertorio() != null) {
                em.update(movimiento);
                /*bs.registrarMovimiento(null, movimiento, ActividadesTransaccionales.MODIFICACION_INSCRIPCION, 
                        new BigInteger(periodo.toString()));*/
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgCorregir').hide();");
            } else {
                JsfUti.messageError(null, "Todos los campos son obligatorios.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public List<RegLibro> getLibros() {
        return libros;
    }

    public void setLibros(List<RegLibro> libros) {
        this.libros = libros;
    }

    public List<RegMovimiento> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<RegMovimiento> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public List<SecuenciaInscripcion> getSecuencias() {
        return secuencias;
    }

    public void setSecuencias(List<SecuenciaInscripcion> secuencias) {
        this.secuencias = secuencias;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public RegMovimientosLazy getMovimientosLazy() {
        return movimientosLazy;
    }

    public void setMovimientosLazy(RegMovimientosLazy movimientosLazy) {
        this.movimientosLazy = movimientosLazy;
    }

}
