/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.RegLibro;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegMovimientoReferencia;
import com.origami.sgr.lazymodels.LazyModel;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sql.ConsultasSqlLocal;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author origami
 */
@Named
@ViewScoped
public class CrearMarginaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(CrearMarginaciones.class.getName());

    @Inject
    private Entitymanager em;
    @Inject
    private UserSession sess;
    @Inject
    private RegistroPropiedadServices services;
    @Inject
    private ConsultasSqlLocal sql;
    @Inject
    private ServletSession ss;
    @Inject
    private Entitymanager manager;

    //private TbLibrerias bases;
    private RegMovimientosLazy movimientos;
    private LazyModel<RegMovimientoReferencia> lazyReferencia;
    private RegMovimiento movimiento;
    private RegMovimiento movimientoRefencia;
    //private TbData data;
    private List<RegLibro> regLibroList;
    private List<AclUser> inscriptores = new ArrayList<>();
    private AclUser inscriptor;
    private RegLibro libro;
    private Date inscripcionDesde;
    private Date inscripcionHasta;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Calendar cal;
    private Map<String, Object> filterss;

    // Marginaciones Agregadas
    private List<RegMovimientoMarginacion> marginaciones;
    private RegMovimientoMarginacion nuevaMarg;

    //private TbBlob blobSelecccionado;

    @PostConstruct
    public void initView() {
        try {
            if (!JsfUti.isAjaxRequest()) {
                cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -1);
                inscripcionDesde = cal.getTime();
                inscripcionHasta = cal.getTime();
                regLibroList = em.findAllEntCopy(Querys.getRegLibroList);
                movimientos = new RegMovimientosLazy(libro, inscripcionDesde, inscripcionHasta);
                inscriptores = sql.getUsuariosByRolName("inscriptor");
                /*if (bases == null) {
                    try {
                        //List<TbLibrerias> librerias = de.findAll(TbLibrerias.class, null);
                        List<TbLibrerias> librerias = new ArrayList<>();
                        if (Utils.isNotEmpty(librerias)) {
                            bases = librerias.get(0);
                        }
                    } catch (Exception e) {
                        LOG.log(Level.SEVERE, "Connexion a mysql", e);
                    }
                }*/
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultar() {
        try {
//            System.out.println("Desde " + inscripcionDesde);
            if (inscripcionDesde.compareTo(inscripcionHasta) > 0) {
                JsfUti.messageWarning(null, "Fecha Hasta debe ser mayor o igual a Fecha Desde.", "");
                return;
            }
            if (inscripcionDesde.equals(inscripcionHasta)) {
                cal = Calendar.getInstance();
                cal.setTime(inscripcionHasta);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                inscripcionHasta = cal.getTime();
            }
            filterss = new HashMap<>();
            filterss.put("fechaInscripcionDesde", inscripcionDesde);
            filterss.put("fechaInscripcionHasta", inscripcionHasta);
            List<Long> movimientosId = null;
            if (inscriptor != null) {
                filterss.put("user", inscriptor);
                movimientosId = manager.findNamedQuery(Querys.getMovimientoIds, filterss);
            } else {
                movimientosId = manager.findNamedQuery(Querys.getMovimientoIds1, filterss);
            }
            movimientos = null;
            filterss = new HashMap<>();
            movimientos = new RegMovimientosLazy();
            filterss.put("regMovimientoReferenciaCollection.movimiento", movimientosId);
            filterss.put("regMovimientoReferenciaCollection.movimiento", movimientosId);
            movimientos.setFilterss(filterss);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarReporte() {
        try {
            if (inscripcionDesde == null) {
                JsfUti.messageError(null, "Debe seleccionar la fecha desde.", "");
                return;
            }
            ss.instanciarParametros();
            if (libro != null) {
                ss.agregarParametro("LIBRO", libro.getId());
            }
            if (inscriptor != null) {
                ss.agregarParametro("USUARIO", inscriptor.getId());
            }
            if (inscripcionDesde != null) {
                ss.agregarParametro("FECHA", inscripcionDesde);
            }

            ss.setNombreReporte("ReporteInscripcionesMarginacion");
            ss.setNombreSubCarpeta("workflow");
            ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
            ss.setTieneDatasource(true);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegMovimientoReferencia> getReferencias(RegMovimiento mov) {
        if (mov != null) {
            List<RegMovimientoReferencia> regMovRefByIdRegMov = services.getRegMovRefByIdRegMov(mov.getId());
            return regMovRefByIdRegMov;
        }
        return null;
    }

    public List<RegMovimientoReferencia> getReferencias(Long mov) {
        if (mov != null) {
            List<RegMovimientoReferencia> regMovRefByIdRegMov = services.getRegMovRefByIdRegMov(mov);
            return regMovRefByIdRegMov;
        }
        return null;
    }

    public void dlfMarginacion(RegMovimiento movimientoRefencia, RegMovimiento movimiento) {
        this.movimiento = movimiento;
        this.movimientoRefencia = movimientoRefencia;
        if (Utils.isNotEmpty(movimientoRefencia.getRegMovimientoReferenciaCollection())) {
            RegMovimientoReferencia get = Utils.get(movimientoRefencia.getRegMovimientoReferenciaCollection(), 0);
            if (get != null) {
                this.movimiento = manager.find(RegMovimiento.class, get.getMovimiento());
            }
        }
        //blobsLazy = null;
        /*List<TbData> dataFiles = de.getDataFiles(bases, movimientoRefencia.getFechaInscripcion(),
                movimientoRefencia.getNumInscripcion(), movimientoRefencia.getNumRepertorio(),
                movimientoRefencia.getActo().getLibro().getNombre());
        if (Utils.isNotEmpty(dataFiles)) {
            if (dataFiles.size() > 0) {
                data = dataFiles.get(0);
            } else {
                System.out.println("Hay mas de un registro");
            }
        }*/
        /*if (data != null) {
            Map<String, Object> pms = new HashMap<>();
            pms.put("idTransaccion", data.getIdTransaccion());
            //blobsLazy = new LazyModelDocs<>(bases.getDesLibreriaBlob(), TbBlob.class, pms);
            if (data.getIdPadre() != null && data.getIdPadre().longValue() > 0l) {
                pms.clear();
                pms.put("1", data.getIdBlobReg());
                String sql = "SELECT b.ord_salida FROM doc_blob_reg.tb_blob_reg br INNER JOIN doc_blob_reg.tb_blob b ON b.id_blob = br.id_blob WHERE br.id_blob_reg = ?";
                //Integer page = de.find(sql, pms);
                pms.clear();
                pms.put("idTransaccion", data.getIdPadre().longValue());
                final DataTable d = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmMarg:tbBlobs");
                //d.setFirst(page - 1);
            }
        }*/
        if (marginaciones == null) {
            marginaciones = new ArrayList<>();
        }
        nuevaMarg = new RegMovimientoMarginacion();
        nuevaMarg.setMovimiento(movimientoRefencia);
        nuevaMarg.setObservacion(movimientoRefencia.getDescripcionMarginacion(this.movimiento));
        nuevaMarg.setUserIngreso(this.sess.getUserId());
        JsfUti.executeJS("PF('dlgDescripcionMarg').show();");
        JsfUti.update("frmMarg");
    }

    public RegMovimiento movimientoRef(Long idMovRef) {
        return manager.find(RegMovimiento.class, idMovRef);
    }

    public void agregarMarginacion() {
        marginaciones.add(nuevaMarg);
        nuevaMarg = new RegMovimientoMarginacion();
        nuevaMarg.setMovimiento(movimientoRefencia);
        nuevaMarg.setUserIngreso(this.sess.getUserId());
    }

    public void guardarMarginaciones() {
        if (Utils.isEmpty(marginaciones)) {
            JsfUti.messageError(null, "", "Debe Ingresar por lo menos una marginacion.");
            return;
        }
        /*if (blobSelecccionado == null) {
            JsfUti.messageError(null, "", "Debe Selecciona la paguina para la marginacion.");
            return;
        }

        for (RegMovimientoMarginacion marginacion : marginaciones) {
            marginacion.setFechaIngreso(new Date());
            TbMarginacion marg = new TbMarginacion(data.getIdTransaccion(), blobSelecccionado);
            marg.setDescripcion(marginacion.getObservacion());
            marg.setUsrUlttran(sess.getName_user());
            marg.setFlgEstado(Short.valueOf("0"));
            //marg = (TbMarginacion) de.persist(marg);
            if (marg != null) {
                TbMarginacionBit marginacionBit = new TbMarginacionBit(marg.getIdMarginacion(), new Date());
                marginacionBit.setTipTransaccion(Short.valueOf("1"));
                marginacionBit.setUsrTransaccion(sess.getName_user());
                //de.persist(marginacionBit);
                marginacion.setIdBlob(marg.getIdTransaccion());
                this.em.persist(marginacion);
            }
        }*/
        marginaciones = new ArrayList<>();
        JsfUti.executeJS("PF('dlgDescripcionMarg').hide()");
    }

    public String showMarginacion() {
        StringBuilder buffer = new StringBuilder();
        if (marginaciones != null) {
            marginaciones.forEach((tm) -> {
                buffer.append(tm.getObservacion()).append("<br />");
            });
        }
        return buffer.toString();
    }

    public void eliminarMarginacion(int index) {
        marginaciones.remove(index);
    }

    public RegMovimientosLazy getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(RegMovimientosLazy movimientos) {
        this.movimientos = movimientos;
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

    public RegMovimiento getMovimientoRefencia() {
        return movimientoRefencia;
    }

    public void setMovimientoRefencia(RegMovimiento movimientoRefencia) {
        this.movimientoRefencia = movimientoRefencia;
    }

    /*public TbData getData() {
        return data;
    }

    public void setData(TbData data) {
        this.data = data;
    }

    public TbLibrerias getBases() {
        return bases;
    }

    public void setBases(TbLibrerias bases) {
        this.bases = bases;
    }*/

    public List<RegMovimientoMarginacion> getMarginaciones() {
        return marginaciones;
    }

    public void setMarginaciones(List<RegMovimientoMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }

    public RegMovimientoMarginacion getNuevaMarg() {
        return nuevaMarg;
    }

    public void setNuevaMarg(RegMovimientoMarginacion nuevaMarg) {
        this.nuevaMarg = nuevaMarg;
    }

    /*public LazyModelDocs<TbBlob> getBlobsLazy() {
        return blobsLazy;
    }

    public void setBlobsLazy(LazyModelDocs<TbBlob> blobsLazy) {
        this.blobsLazy = blobsLazy;
    }*/

    /*public TbBlob getBlobSelecccionado() {
        return blobSelecccionado;
    }

    public void setBlobSelecccionado(TbBlob blobSelecccionado) {
        this.blobSelecccionado = blobSelecccionado;
    }*/

    public List<AclUser> getInscriptores() {
        return inscriptores;
    }

    public void setInscriptores(List<AclUser> inscriptores) {
        this.inscriptores = inscriptores;
    }

    public AclUser getInscriptor() {
        return inscriptor;
    }

    public void setInscriptor(AclUser inscriptor) {
        this.inscriptor = inscriptor;
    }

}
