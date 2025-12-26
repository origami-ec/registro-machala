/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.inscripciones;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.NamedItem;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author origami
 */
@Named
@ViewScoped
public class FoliacionInscripciones extends BpmManageBeanBaseRoot implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(FoliacionInscripciones.class.getName());

    @Inject
    private RegistroPropiedadServices rps;

    protected HistoricoTramites ht;
    protected RegMovimientosLazy foliosLazy;
    protected RegMovimiento movimiento;
    protected List<RegLibro> regLibroList;
    protected RegLibro libro;
    protected Date inscripcionDesde;
    protected Date inscripcionHasta;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private Integer desde;
    private Integer hasta;

    @PostConstruct
    public void initView() {
        try {
            if (session.getTaskID() != null) {
                this.setTaskId(session.getTaskID());
                Long tramite = (Long) this.getVariable(session.getTaskID(), "tramite");
                if (tramite != null) {
                    map = new HashMap();
                    map.put("numTramite", tramite);
                    ht = (HistoricoTramites) manager.findObjectByParameter(HistoricoTramites.class, map);
                    inscripcionDesde = new Date();
                    inscripcionHasta = new Date();
                    regLibroList = manager.findAllEntCopy(Querys.getRegLibroList);
                } else {
                    this.continuar();
                }
            } else {
                this.continuar();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void consultarFolios() {
        try {
            foliosLazy = null;
            if (libro != null && inscripcionDesde != null && inscripcionHasta != null) {
                if (!inscripcionDesde.after(inscripcionHasta)) {
                    foliosLazy = new RegMovimientosLazy(libro, inscripcionDesde, this.getFechaHasta(inscripcionHasta));
                } else {
                    JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Libro y Fechas campos obligatorios para la busqueda.", "");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public Date getFechaHasta(Date actual) {
        Calendar fecha = Calendar.getInstance();
        fecha.setTime(actual);
        fecha.add(Calendar.DAY_OF_MONTH, 1);
        fecha.add(Calendar.SECOND, -1);
        return fecha.getTime();
    }

    public void showDlgFolioSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            JsfUti.update("formMovRegSelec");
            JsfUti.executeJS("PF('dlgMovRegSelec').show();");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void generarPdfFolios() {
        try {
            if (desde == null && hasta == null) {
                JsfUti.messageError(null, "Debe ingresa el desde y hasta.", "");
                return;
            }
            if (desde > hasta) {
                JsfUti.messageError(null, "Desde no debe ser mayor al hasta.", "");
                return;
            }
            ss.instanciarParametros();
            ss.setNombreReporte("foliacion");
            ss.setTieneDatasource(false);
            ss.setNombreSubCarpeta("ingreso");
            ss.agregarParametro("ciRuc", ("Folio:" + desde + "-" + hasta));
            List<NamedItem> list = new ArrayList<>();
            for (int i = desde; i <= hasta; i++) {
                list.add(new NamedItem(new BigInteger(String.valueOf(i))));
            }
            ss.setDataSource(list);
            ss.agregarParametro("desde", desde);
            ss.agregarParametro("hasta", hasta);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
//            JsfUti.redirectNewTab(SisVars.urlbase + "PdfFolio");
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public void updateMovimiento() {
        try {
            if (movimiento.getFolioInicio() != null && movimiento.getFolioFin() != null
                    && !movimiento.getNumTomo().isEmpty()) {
                manager.update(movimiento);
                JsfUti.update("formFolios");
                JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
                JsfUti.messageInfo(null, "Se actualizó el movimiento con éxito.", "");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar los 3 campos obligatorios(*).", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOGGER.log(Level.SEVERE, null, e);
        }
    }

    public void documentalDSM(RegMovimiento mov) {
        try {
            if (mov.getFolioInicio() != null && mov.getFolioFin() != null && !mov.getNumTomo().isEmpty()) {
                Long result = rps.insertDSM(mov);
                if (result != null) {
                    mov.setFolioAnterior(result.intValue());
                    manager.update(mov);
                    JsfUti.messageInfo(null, "Se registró el indice DSM.", "Id de transaccion: " + result);
                } else {
                    JsfUti.messageWarning(null, "No se puedo registrar el indice DSM.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Primero debe estar foliada la inscripcion antes de indexar.", "");
            }
        } catch (Exception e) {
            System.out.println(e);
            JsfUti.messageWarning(null, "ERROR DE TRANSACCION.", "");
        }
    }

    public void redirectFoliacion() {
        try {
            session.setTaskID(this.getTaskId());
            JsfUti.redirectFaces("/procesos/inscripciones/procesoFoliacion.xhtml");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegMovimientosLazy getFoliosLazy() {
        return foliosLazy;
    }

    public void setFoliosLazy(RegMovimientosLazy foliosLazy) {
        this.foliosLazy = foliosLazy;
    }

    public List<RegLibro> getRegLibroList() {
        return regLibroList;
    }

    public void setRegLibroList(List<RegLibro> regLibroList) {
        this.regLibroList = regLibroList;
    }

    public RegLibro getLibro() {
        return libro;
    }

    public void setLibro(RegLibro libro) {
        this.libro = libro;
    }

    public Date getInscripcionDesde() {
        return inscripcionDesde;
    }

    public void setInscripcionDesde(Date inscripcionDesde) {
        this.inscripcionDesde = inscripcionDesde;
    }

    public Date getInscripcionHasta() {
        return inscripcionHasta;
    }

    public void setInscripcionHasta(Date inscripcionHasta) {
        this.inscripcionHasta = inscripcionHasta;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public Integer getDesde() {
        return desde;
    }

    public void setDesde(Integer desde) {
        this.desde = desde;
    }

    public Integer getHasta() {
        return hasta;
    }

    public void setHasta(Integer hasta) {
        this.hasta = hasta;
    }

}
