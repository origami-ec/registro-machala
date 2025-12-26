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
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.lazymodels.RegMovimientosLazy;
import com.origami.sgr.models.ConsultaMovimientoModel;
import com.origami.sgr.services.interfaces.BitacoraServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class Inscripciones implements Serializable {

    private static final Logger LOG = Logger.getLogger(Inscripciones.class.getName());

    @Inject
    private Entitymanager em;
    @Inject
    private BitacoraServices bs;
    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;
    @Inject
    private FirmaDigitalLocal fd;
    @Inject
    private IngresoTramiteLocal itl;
    @Inject
    private RegistroPropiedadServices reg;

    protected RegMovimiento movimiento;
    protected RegMovimientosLazy movimientosLazy;
    protected Date fechaIngreso = new Date();
    protected Date fechaActa = new Date();
    protected Date fechaDesde = new Date();
    protected Date fechaHasta = new Date();
    protected Calendar cal = Calendar.getInstance();
    protected String urlDownload = "";
    protected Integer anio, paginas = 0;
    protected BigInteger periodo;
    protected Integer tomo = 0, folio = 0, pagina = 1;
    protected Map map;
    protected RegRegistrador registrador;
    protected AclUser user;
    protected AclUser jefe_inscripcion;
    protected List<RegMovimientoMarginacion> marginaciones;
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected RegMovimientoMarginacion marg = new RegMovimientoMarginacion();
    protected ConsultaMovimientoModel modelo = new ConsultaMovimientoModel();
    protected Boolean administrador = false, habilitarEdicion = false, habilitarFima = false;
    private RegFicha fichaSeleccionada;
    private List<RegMovimientoFicha> movimientosFichas;

    @PostConstruct
    protected void iniView() {
        try {
            map = new HashMap();
            map.put("actual", Boolean.TRUE);
            registrador = (RegRegistrador) em.findObjectByParameter(RegRegistrador.class, map);
            periodo = new BigInteger(Integer.toString(Calendar.getInstance().get(Calendar.YEAR)));
            movimientosLazy = new RegMovimientosLazy();
            this.validaRoles();
            jefe_inscripcion = itl.getUserByRolName("jefe_inscripcion");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void validaRoles() {
        administrador = us.getRoles().contains(1L);         //rol administrador
        habilitarFima = us.getRoles().contains(58L);        //rol registrador
        //habilitarEdicion = us.getRoles().contains(60L);     //rol jefe inscripciones
        habilitarEdicion = us.getRoles().contains(58L);     //rol registrador
    }

    public void showDlgInscripciones() {
        JsfUti.update("frmConsultarInscripciones");
        JsfUti.executeJS("PF('dlgInscripciones').show();");
    }

    public void showDlgDocumentos(RegMovimiento mov) {
        movimiento = mov;
        JsfUti.update("frmDocuments");
        JsfUti.executeJS("PF('dlgDocumentos').show();");
    }

    public void redirectFacelet(String cadena) {
        JsfUti.redirectFaces(cadena);
    }

    public void imprimirInscripciones(Boolean encuadernacion, Integer margen) {
        try {
            if (fechaIngreso != null) {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setEncuadernacion(encuadernacion);
                ss.setMargen(margen);
                ss.setNombreReporte("reporteInscripcionesIngresadas");
                ss.setNombreSubCarpeta("registro");
                ss.agregarParametro("FECHA_INSCRIPCION", fechaIngreso);
                ss.agregarParametro("USUARIO", us.getUserId());
                ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.jpg"));
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
                ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void seleccionarFicha(RegFicha ficha) {
        try {
            this.fichaSeleccionada = ficha;
            this.movimientosFichas = reg.getRegMovByIdFicha(ficha.getId());
            JsfUti.update("formFichaSelect");
            JsfUti.executeJS("PF('dlgFichaDetalle').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, Messages.error, "Error al cargar detalles de la ficha.");
        }
    }
    public void visualizaScann(RegMovimiento mov) {
        try {
            if (mov == null) {
                JsfUti.messageWarning(null, "No se ha seleccionado un movimiento.", "");
                return;
            }
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(mov.getFechaInscripcion());
            Integer anioInscripcion = fecha.get(Calendar.YEAR);
            JsfUti.redirectNewTab(SisVars.urlbase + "documental/visorPdf.xhtml?periodo=" + anioInscripcion
                    + "&libro=" + mov.getLibro().getNombreCarpeta()
                    + "&inscripcion=" + mov.getNumInscripcion()
                    + "&movimiento=" + mov.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirActaInicio(Boolean encuadernacion, Integer margen) {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(encuadernacion);
            ss.setMargen(margen);
            ss.setNombreReporte("Repertorio_apertura");
            ss.setNombreSubCarpeta("registro");
            sdf = new SimpleDateFormat("EEEEE dd 'de' MMMMM 'de' yyyy");
            ss.agregarParametro("FECHA_STRING", sdf.format(fechaActa));
            ss.agregarParametro("FECHA", fechaActa);
            ss.agregarParametro("TOMO", tomo);
            ss.agregarParametro("FOLIO", folio);
            ss.agregarParametro("PAGINA", pagina);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            cal.setTime(fechaIngreso);
            ss.agregarParametro("ANIO", cal.get(Calendar.YEAR));
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void editarInscripcion(RegMovimiento mov) {
        try {
            if (!mov.getEditable()) {
                JsfUti.messageError(null, "NO puede editar este Movimiento.", "Debe solicitar autorización del REGISTRADOR.");
                return;
            }
            if (mov.getTramite() == null) {
                ss.instanciarParametros();
                ss.agregarParametro("idMov", mov.getId());
                JsfUti.redirectFaces("/procesos/manage/inscripcionEdicion.xhtml");
            } else if (mov.getTramite().getRealizado()) {
                ss.instanciarParametros();
                ss.agregarParametro("idMov", mov.getId());
                JsfUti.redirectFaces("/procesos/manage/inscripcionEdicion.xhtml");
            } else {
                JsfUti.messageWarning(null, "Debe de terminar la tarea para poder editar este movimiento.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirInscripcion(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("P_MOVIMIENTO", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
            ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.getUsuario().toUpperCase());
            ss.agregarParametro("USUARIO", us.getName_user());
            ss.setNombreReporte("ActaInscripcion");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setEncuadernacion(Boolean.FALSE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirRazon(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("ACCION_PERSONAL", registrador.getRazonReporte());
            ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.getUsuario().toUpperCase());
            ss.agregarParametro("USUARIO", us.getName_user());
            ss.setNombreReporte("RazonInscripcion");
            ss.setNombreSubCarpeta("registro");
            ss.setTieneDatasource(true);
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirCopiaRazon(RegMovimiento mov) {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", mov.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generarRazonGrupal(RegMovimiento mov) {
        Calendar cl = Calendar.getInstance();
        if (mov.getFechaRepertorio() != null) {
            cl.setTime(mov.getFechaRepertorio());
        } else {
            cl.setTime(mov.getFechaInscripcion());
        }
        Integer year = cl.get(Calendar.YEAR);

        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            ss.instanciarParametros();
            if (mov.getUserCreador() != null) {
                ss.agregarParametro("INSCRIPTOR", mov.getUserCreador().getUsuario().toUpperCase());
            }
            ss.agregarParametro("REPERTORIO", mov.getNumRepertorio());
            ss.agregarParametro("ANIO", year.toString());
            ss.agregarParametro("FECHA_REP", mov.getFechaRepertorio());
            ss.agregarParametro("FECHA_REPERTORIO", sdf.format(mov.getFechaRepertorio()));
            if (mov.getTramite() != null) {
                ss.agregarParametro("TRAMITE", mov.getTramite().getTramite().getNumTramite());
                ss.agregarParametro("COMPROBANTE", mov.getTramite().getDetalle().getLiquidacion().getCodigoComprobante());
            }
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/registro/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("COD_VERIFICACION", mov.getCodVerificacion());
            ss.agregarParametro("PROPIEDAD", mov.getLibro().getPropiedad());
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("JEFE_INSCRIPCION", jefe_inscripcion.getUsuario().toUpperCase());
            ss.agregarParametro("FIRMA_INSCRIPTOR", mov.getUserCreador().getFirmaNombre());
            ss.agregarParametro("CARGO_INSCRIPTOR", mov.getUserCreador().getFirmaCargo());
            ss.agregarParametro("FIRMA_JEFE", jefe_inscripcion.getFirmaNombre());
            ss.agregarParametro("CARGO_JEFE", jefe_inscripcion.getFirmaCargo());
            ss.agregarParametro("USUARIO", us.getName_user());
            ss.setNombreReporte("RazonInscripcion_v1");
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            //ss.setEncuadernacion(Boolean.TRUE);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            modelo = reg.getConsultaMovimiento(mov.getId());
            if (modelo != null) {
                cal.setTime(mov.getFechaInscripcion());
                anio = cal.get(Calendar.YEAR);
                JsfUti.update("formMovRegSelec");
                JsfUti.executeJS("PF('dlgMovRegSelec').show();");
            } else {
                JsfUti.messageError(null, "No se pudo hacer la consulta.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacora() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", movimiento.getId());
            ss.agregarParametro("numFicha", null);
            ss.agregarParametro("titulo", Messages.bitacoraMovimiento);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void visualizaScann() {
        try {
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(movimiento.getFechaInscripcion());
            Integer anioInscripcion = fecha.get(Calendar.YEAR);
            JsfUti.redirectNewTab(SisVars.urlbase + "documental/visorPdf.xhtml?periodo=" + anioInscripcion
                    + "&libro=" + movimiento.getLibro().getNombreCarpeta()
                    + "&inscripcion=" + movimiento.getNumInscripcion()
                    + "&movimiento=" + movimiento.getId());
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void permitirEdicion() {
        try {
            if (habilitarEdicion) {
                movimiento.setEditable(true);
                em.update(movimiento);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgMovRegSelec').hide();");
                JsfUti.messageInfo(null, "Movimiento habilitado para edicion.", "");
            } else {
                JsfUti.messageWarning(null, "Usuario no permitido.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarRazonInscripcion(RegMovimiento mo) {
        try {
            File temp = fd.firmarRazonInscripcion(mo, us.getName_user());
            if (temp != null) {
                reg.guardarObservaciones(mo.getTramite().getTramite(), us.getName_user(),
                        "Se vuelve a firmar la razon de inscripcion: " + mo.getNumInscripcion(), "Firma de documento");
                JsfUti.messageInfo(null, "Razon de inscripcion firmada con exito.", "");
            } else {
                JsfUti.messageError(null, "No se pudo firmar el documento.", "");
            }
        } catch (java.io.IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarActaInscripcion(RegMovimiento mo) {
        try {
            File temp = fd.firmarActaInscripcion(mo, us.getName_user());
            if (temp != null) {
                reg.guardarObservaciones(mo.getTramite().getTramite(), us.getName_user(),
                        "Se vuelve a firmar el acta de inscripcion: " + mo.getNumInscripcion(), "Firma de documento");
                JsfUti.messageInfo(null, "Acta de inscripcion firmada con exito.", "");
            } else {
                JsfUti.messageError(null, "No se pudo firmar el documento.", "");
            }
        } catch (java.io.IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMarginaciones(RegMovimiento mov) {
        try {
            movimiento = mov;
            marg = new RegMovimientoMarginacion();
            marginaciones = reg.getRegMovMargByIdMov(mov.getId());
            JsfUti.update("formMarginacion");
            JsfUti.executeJS("PF('dlgMarginacion').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNewMarginacion() {
        marg = new RegMovimientoMarginacion();
        JsfUti.update("formMarginacion");
        JsfUti.executeJS("PF('dlgMarginacion').show();");
    }

    public void agregarMarginacion() {
        try {
            if (marg.getObservacion() != null && !marg.getObservacion().isEmpty() && movimiento.getId() != null) {
                marg.setFechaIngreso(new Date());
                marg.setUserIngreso(us.getUserId());
                marg.setUsuario(us.getName_user());
                marg.setMovimiento(movimiento);
                em.persist(marg);
                bs.registrarMovMarginacion(movimiento, marg.getObservacion(), periodo);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgMarginacion').hide();");
                JsfUti.messageInfo(null, "Marginacion registrada con exito.", "");
                //modelo.setMarginaciones(reg.getRegMovMargByIdMov(movimiento.getId()));
                //JsfUti.update("formMovRegSelec:tvMovimiento:dtMarginacion");
            } else {
                JsfUti.messageWarning(null, "Debe ingresar contenido de texto a la marginacion.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void inactivarMarginacion(RegMovimientoMarginacion rmm) {
        try {
            if (rmm.getUsuario().equals(us.getName_user())) {
                rmm.setEstado(false);
                rmm.setFechaEdicion(new Date());
                rmm.setUserEdicion(us.getUserId());
                em.persist(rmm);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgMarginacion').hide();");
                JsfUti.messageInfo(null, "Marginacion desactivada con exito.", "");
            } else {
                JsfUti.messageWarning(null, "Solo el mismo usuario puede desactivar.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public RegMovimientosLazy getMovimientosLazy() {
        return movimientosLazy;
    }

    public void setMovimientosLazy(RegMovimientosLazy movimientosLazy) {
        this.movimientosLazy = movimientosLazy;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public RegMovimientoMarginacion getMarg() {
        return marg;
    }

    public void setMarg(RegMovimientoMarginacion marg) {
        this.marg = marg;
    }

    public Integer getPaginas() {
        return paginas;
    }

    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }

    public Integer getTomo() {
        return tomo;
    }

    public void setTomo(Integer tomo) {
        this.tomo = tomo;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public Date getFechaActa() {
        return fechaActa;
    }

    public void setFechaActa(Date fechaActa) {
        this.fechaActa = fechaActa;
    }

    public Integer getPagina() {
        return pagina;
    }

    public void setPagina(Integer pagina) {
        this.pagina = pagina;
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Boolean getHabilitarEdicion() {
        return habilitarEdicion;
    }

    public void setHabilitarEdicion(Boolean habilitarEdicion) {
        this.habilitarEdicion = habilitarEdicion;
    }

    public Boolean getHabilitarFima() {
        return habilitarFima;
    }

    public void setHabilitarFima(Boolean habilitarFima) {
        this.habilitarFima = habilitarFima;
    }

    public List<RegMovimientoMarginacion> getMarginaciones() {
        return marginaciones;
    }

    public void setMarginaciones(List<RegMovimientoMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }

    public RegFicha getFichaSeleccionada() {
        return fichaSeleccionada;
    }

    public void setFichaSeleccionada(RegFicha fichaSeleccionada) {
        this.fichaSeleccionada = fichaSeleccionada;
    }

    public List<RegMovimientoFicha> getMovimientosFichas() {
        return movimientosFichas;
    }

    public void setMovimientosFichas(List<RegMovimientoFicha> movimientosFichas) {
        this.movimientosFichas = movimientosFichas;
    }
}
