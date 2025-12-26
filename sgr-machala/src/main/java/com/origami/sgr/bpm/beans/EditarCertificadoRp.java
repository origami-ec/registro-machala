/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.ContenidoReportes;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegCertificadoPropietario;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.RegCertificadoService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class EditarCertificadoRp extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private RegistroPropiedadServices reg;
    @Inject
    private SeqGenMan sec;
    @Inject
    private RegCertificadoService rcs;
    @Inject
    private AsynchronousService as;

    protected Long numFicha;
    protected Long tarea;
    protected HashMap<String, Object> par;
    protected HistoricoTramites ht;
    protected RegpLiquidacion liquidacion;
    protected RegpTareasTramite tt;
    protected Integer render = 0;
    protected RegRegistrador registrador;
    protected RegCertificado certificado;
    protected RegFicha ficha = new RegFicha();
    protected String solvencia = "";
    protected Date fecha = new Date();
    protected AclUser user = new AclUser();
    protected Boolean razon = Boolean.FALSE;
    protected ContenidoReportes contenido;
    protected RegEnteInterviniente propietario = new RegEnteInterviniente();

    protected List<RegMovimiento> movimientos = new ArrayList<>();
    protected List<RegMovimiento> temps = new ArrayList<>();
    protected List<RegFichaPropietarios> listProp = new ArrayList<>();
    protected List<RegCertificadoMovimiento> historia = new ArrayList<>();
    protected List<RegCertificadoPropietario> propietarios = new ArrayList<>();
    protected List<ContenidoReportes> contenidos = new ArrayList<>();

    protected RegMovimiento movimiento = new RegMovimiento();
    protected RegCertificadoMovimiento rcm = new RegCertificadoMovimiento();
    protected boolean requiereCambio;
    protected RegTipoFicha tipoFicha;

    @PostConstruct
    protected void iniView() {
        try {
            requiereCambio = false;
            if (ss.getParametros() == null) {
                JsfUti.redirectFaces("/procesos/manage/certificadosRp.xhtml");
            } else if (ss.getParametros().get("tarea") == null) {
                JsfUti.redirectFaces("/procesos/manage/certificadosRp.xhtml");
            } else {
                Long tramite = (Long) ss.getParametros().get("tramite");
                map = new HashMap();
                map.put("numTramiteRp", tramite);
                liquidacion = (RegpLiquidacion) manager.findObjectByParameter(RegpLiquidacion.class, map);

                tarea = (Long) ss.getParametros().get("tarea");
                tt = manager.find(RegpTareasTramite.class, tarea);
                map = new HashMap();
                map.put("tareaTramite", tt);
                certificado = (RegCertificado) manager.findObjectByParameter(RegCertificado.class, map);
                if (certificado == null) {
                    certificado = new RegCertificado();
                    // generar codigo verficación único
                    certificado.setCodVerificacion(rcs.genCodigoVerif());
                    certificado.setNombreSolicitante(liquidacion.getSolicitante().getNombresApellidos());
                    certificado.setUsoDocumento(liquidacion.getUsoDocumento().getValor());
                }
                if (certificado.getId() != null) {
                    historia = reg.getMovsByCertificado(certificado.getId());
                    propietarios = reg.getPropsByCertificado(certificado.getId());
                }
                if (certificado.getObservacion() != null) {
                    solvencia = certificado.getObservacion();
                }
                if (certificado.getFicha() != null) {
                    ficha = certificado.getFicha();
                    numFicha = ficha.getNumFicha();
                }
                if (certificado.getPropietario() != null) {
                    propietario = certificado.getPropietario();
                }
                if (certificado.getFechaEmision() != null) {
                    fecha = certificado.getFechaEmision();
                }
                if (certificado.getDescripcionBien() != null) {
                    if (!certificado.getDescripcionBien().isEmpty()) {
                        requiereCambio = true;
                    }
                }
                user = manager.find(AclUser.class, session.getUserId());
                map = new HashMap();
                map.put("actual", Boolean.TRUE);
                registrador = (RegRegistrador) manager.findObjectByParameter(RegRegistrador.class, map);
                map = new HashMap();
                map.put("code", "SOLVENCIA_CERTIFICADO");
                contenidos = manager.findObjectByParameterList(ContenidoReportes.class, map);
                //certificado.setTipoDocumento(tt.getDetalle().getActo().getAbreviatura());
                this.renderizarPaneles(certificado.getTipoDocumento());
                tipoFicha = manager.find(RegTipoFicha.class, 2L);
                ss.instanciarParametros();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void renderizarPaneles(String code) {
        switch (code) {
            case "C01": //NO POSEER BIENES
                render = 1;
                certificado.setTipoCertificado(1L);
                break;
            /*case "C02": //CERTIFICADO SIMPLE
                render = 2;
                certificado.setTipoCertificado(2L);
                break;*/
            case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                render = 2;
                certificado.setTipoCertificado(3L);
                break;
            /*case "C04": //CERTIFICADO LINDERADO
                render = 2;
                certificado.setTipoCertificado(4L);
                break;*/
            case "C05": //CERTIFICADO MERCANTIL
                render = 3;
                certificado.setTipoCertificado(5L);
                break;
            case "C06": //COPIA DE RAZON DE INSCRIPCION
                render = 4;
                certificado.setTipoCertificado(6L);
                break;
            case "C07": //CERTIFICADO GENERAL
                render = 5;
                certificado.setTipoCertificado(7L);
                if (certificado.getDescripcionBien() == null) {
                    certificado.setDescripcionBien("CERTIFICACIÓN");
                }
                break;
            default:
                render = 0;
                break;
        }
    }

    public void nameReport(String code) {
        switch (code) {
            case "C01": //CERTIFICADO NO POSEER BIENES
                ss.setNombreReporte("CertificadoNoBienes");
                break;
            /*case "C02": //CERTIFICADO DE SOLVENCIA
                ss.setNombreReporte("CertificadoSolvencia");
                break;*/
            case "C03": //CERTIFICADO DE HISTORIA DE DOMINIO
                ss.setNombreReporte("CertificadoHistoriaDominio");
                break;
            /*case "C04": //CERTIFICADO DE FICHA PERSONAL (MERCANTIL)
                ss.setNombreReporte("CertificadoFichaPersonal");
                break;*/
            case "C05": //CERTIFICADO MERCANTIL 
                ss.setNombreReporte("CertificadoMercantil");
                break;
            case "C06": //COPIA DE RAZON DE INSCRIPCION
                ss.setNombreReporte("CopiaRazonInscripcion");
                break;
            case "C07": //CERTIFICADO GENERAL
                ss.setNombreReporte("CertificadoGeneral");
                break;
        }
        List<String> urlList = new ArrayList<>();
        String url = "/procesos/manage/certificadosRp.xhtml";
        urlList.add(SisVars.urlbase + "Documento");
        JsfUti.redirectMultipleConIP_V2(url, urlList);
    }

    public void llenarParametros() {
        try {
            ss.instanciarParametros();
            //ss.setEncuadernacion(true);
            ss.setTieneDatasource(true);
            //ss.setIdCertficado(certificado.getId());
            //ss.setFirmarCertificado(registrador.isFirmaElectronica());
            ss.setNombreSubCarpeta("certificados");
            ss.agregarParametro("ID_CERTIFICADO", certificado.getId());
            ss.agregarParametro("EMISION", certificado.getFechaEmision());
            ss.agregarParametro("SOLICITANTE", certificado.getNombreSolicitante());
            ss.agregarParametro("USO_DOCUMENTO", certificado.getUsoDocumento());
            ss.agregarParametro("ADICIONAL", requiereCambio);
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            this.nameReport(certificado.getTipoDocumento());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void agregarHistoria(RegMovimiento mov) {
        if (this.validarMovs(mov.getId())) {
            RegCertificadoMovimiento cm = new RegCertificadoMovimiento();
            cm.setMovimiento(mov);
            historia.add(cm);
            JsfUti.update("mainForm");
        } else {
            JsfUti.messageWarning(null, "Ya se encuentra seleccionado el mismo movimiento.", "");
        }
    }

    public boolean validarMovs(Long id) {
        return historia.stream().noneMatch((cm) -> (cm.getMovimiento().getId().equals(id)));
    }

    public void agregarAllMovs() {
        if (!temps.isEmpty()) {
            this.cargarPropietarios();
            //this.llenarPropietarios();
            //historia = new ArrayList<>();
            RegCertificadoMovimiento cm;
            for (RegMovimiento m : temps) {
                cm = new RegCertificadoMovimiento();
                cm.setMovimiento(m);
                historia.add(cm);
            }
            temps = new ArrayList<>();
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgMovimientos').hide();");
            JsfUti.messageInfo(null, "Se cargaron los movimientos seleccionados.", "");
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar los movimientos para el certificado.", "");
        }
    }

    public void cargarPropietarios() {
        try {
            RegCertificadoPropietario cp;
            for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                cp = new RegCertificadoPropietario();
                cp.setPropietario(fp.getPropietario());
                cp.setDocumento(fp.getPropietario().getCedRuc());
                cp.setNombres(fp.getPropietario().getNombre());
                propietarios.add(cp);

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void llenarPropietarios() {
        try {
            int flag;
            RegCertificadoPropietario cp;
            for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                flag = 0;
                for (RegCertificadoPropietario cps : propietarios) {
                    if (Objects.equals(cps.getPropietario().getId(), fp.getPropietario().getId())) {
                        cps.setDocumento(fp.getPropietario().getCedRuc());
                        cps.setNombres(fp.getPropietario().getNombre());
                        flag = 1;
                        break;
                    }
                }
                if (flag == 0) {
                    cp = new RegCertificadoPropietario();
                    cp.setPropietario(fp.getPropietario());
                    cp.setDocumento(fp.getPropietario().getCedRuc());
                    cp.setNombres(fp.getPropietario().getNombre());
                    propietarios.add(cp);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminarMovimiento(int indice) {
        RegCertificadoMovimiento cm = historia.remove(indice);
        if (cm.getId() != null) {
            manager.delete(cm);
        }
    }

    public void eliminarProp(int indice) {
        RegCertificadoPropietario cp = propietarios.remove(indice);
        if (cp.getId() != null) {
            manager.delete(cp);
        }
    }

    public void selectObjectMov(SelectEvent event) {
        try {
            RegMovimiento temp = (RegMovimiento) event.getObject();
            rcm = new RegCertificadoMovimiento();
            rcm.setMovimiento(temp);
            historia.add(rcm);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarFichaRegistral() {
        try {
            if (numFicha != null) {
                if (propietarios.isEmpty() && historia.isEmpty()) {
                    map = new HashMap();
                    map.put("numFicha", numFicha);
                    map.put("tipoFicha", new RegTipoFicha(1L));
                    ficha = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
                    if (ficha != null) {
                        certificado.setDescripcionBien(ficha.getDescripcionBien());
                        certificado.setLinderosRegistrales(ficha.getLinderos());
                        movimientos = reg.getMovimientosByFicha(ficha.getId());
                        JsfUti.update("frmMovs");
                        JsfUti.executeJS("PF('dlgMovimientos').show();");
                    } else {
                        JsfUti.messageWarning(null, "No se encontro la ficha.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "Las listas de propietarios y/o movimientos no esta vacia.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de ficha.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscarFichaMercantil() {
        try {
            if (numFicha != null) {
                if (propietarios.isEmpty() && historia.isEmpty()) {
                    map = new HashMap();
                    map.put("numFicha", numFicha);
                    map.put("tipoFicha", tipoFicha);
                    ficha = (RegFicha) manager.findObjectByParameter(RegFicha.class, map);
                    if (ficha != null) {
                        certificado.setLinderosRegistrales(ficha.getLinderos());
                        movimientos = reg.getMovimientosByFicha(ficha.getId());
                        JsfUti.update("frmMovs");
                        JsfUti.executeJS("PF('dlgMovimientos').show();");
                    } else {
                        JsfUti.messageWarning(null, "No se encontro la ficha.", "");
                    }
                } else {
                    JsfUti.messageWarning(null, "Las listas de propietarios y/o movimientos no esta vacia.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el numero de ficha.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateSolvencia() {
        if (contenido.getValor() != null) {
            solvencia = contenido.getValor();
        }
    }

    public void guardarCertificadoNoBienes() {
        try {
            /*if (!certificado.getNombreSolicitante().isEmpty() && !certificado.getUsoDocumento().isEmpty()
                    && !certificado.getLinderosRegistrales().isEmpty() && !certificado.getBeneficiario().isEmpty()) {*/
            if (!certificado.getLinderosRegistrales().isEmpty() && !certificado.getBeneficiario().isEmpty()) {
                certificado.setEditable(Boolean.FALSE);
                certificado.setUserEdicion(session.getUserId());
                certificado.setFechaEdicion(new Date());
                if (requiereCambio) {
                    if (certificado.getDescripcionBien() != null) {
                        if (certificado.getDescripcionBien().isEmpty()) {
                            JsfUti.messageWarning(null, "Debe de especificar la observacion.", "");
                            return;
                        }
                    }
                } else {
                    certificado.setDescripcionBien(null);
                }
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado.setTipoCertificado(1L);
                certificado = (RegCertificado) manager.merge(certificado);
                this.guardarObservacion();
                as.generarFirmaDigital(certificado);
                this.llenarParametros();
            } else {
                JsfUti.messageWarning(null, "Todos los campos son obligatorios.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoConficha() {
        try {
            if (this.validaciones()) {
                certificado.setEditable(Boolean.FALSE);
                certificado.setUserEdicion(session.getUserId());
                certificado.setFicha(ficha);
                certificado.setFechaEdicion(new Date());
                if (ficha.getDescripcionBien() != null) {
                    certificado.setDescripcionBien(ficha.getDescripcionBien());
                }
                if (ficha.getClaveCatastral() != null) {
                    certificado.setClaveCatastral(ficha.getClaveCatastral());
                }
                certificado.setObservacion(solvencia);
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado.setRegCertificadoPropietarioCollection(propietarios);
                certificado = reg.saveCertificadoFicha(certificado);
                if (certificado.getId() != null) {
                    historia = reg.getMovsByCertificado(certificado.getId());
                    propietarios = reg.getPropsByCertificado(certificado.getId());
                }
                this.guardarObservacion();
                as.generarFirmaDigital(certificado);
                this.llenarParametros();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoMercantil() {
        try {
            if (this.validacionesMercantil()) {
                certificado.setEditable(Boolean.FALSE);
                certificado.setUserEdicion(session.getUserId());
                certificado.setFicha(ficha);
                certificado.setFechaEdicion(new Date());
                if (ficha.getDescripcionBien() != null) {
                    certificado.setDescripcionBien(ficha.getDescripcionBien());
                }
                if (ficha.getClaveCatastral() != null) {
                    certificado.setClaveCatastral(ficha.getClaveCatastral());
                }
                certificado.setObservacion(solvencia);
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado.setRegCertificadoPropietarioCollection(propietarios);
                certificado = reg.saveCertificadoFicha(certificado);
                if (certificado.getId() != null) {
                    historia = reg.getMovsByCertificado(certificado.getId());
                    propietarios = reg.getPropsByCertificado(certificado.getId());
                }
                this.guardarObservacion();
                as.generarFirmaDigital(certificado);
                this.llenarParametros();
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCertificadoGeneral() {
        try {
            if (!certificado.getObservacion().isEmpty() && !certificado.getNombreSolicitante().isEmpty()
                    && !certificado.getUsoDocumento().isEmpty()) {
                certificado.setEditable(Boolean.FALSE);
                certificado.setUserEdicion(session.getUserId());
                certificado.setFechaEdicion(new Date());
                certificado.setCertificadoImpreso(Boolean.FALSE);
                certificado = (RegCertificado) manager.merge(certificado);
                this.guardarObservacion();
                as.generarFirmaDigital(certificado);
                this.llenarParametros();
            } else {
                JsfUti.messageWarning(null, "Todos los campos son obligatorios para generar el certificado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public boolean validaciones() {
        if (ficha.getId() == null) {
            JsfUti.messageWarning(null, "Debe buscar con el numero de ficha registral.", "");
            return false;
        }
        if (propietarios.isEmpty()) {
            JsfUti.messageWarning(null, "La ficha no tiene propietarios enlazados.", "");
            return false;
        }
        if (historia.isEmpty()) {
            JsfUti.messageWarning(null, "No ha seleccionado los movimientos para el certificado.", "");
            return false;
        }
        if (solvencia.isEmpty()) {
            JsfUti.messageWarning(null, "Debe ingresar el contenido de la solvencia para el certificado.", "");
            return false;
        }
        return true;
    }

    public boolean validacionesMercantil() {
        if (ficha.getId() == null) {
            JsfUti.messageWarning(null, "Debe buscar con el numero de ficha registral.", "");
            return false;
        }
        if (historia.isEmpty()) {
            JsfUti.messageWarning(null, "No ha seleccionado los movimientos para el certificado.", "");
            return false;
        }
        return true;
    }

    public void guardarCertificadoRazon() {
        try {
            if (!historia.isEmpty()) {
                certificado.setEditable(Boolean.FALSE);
                certificado.setUserEdicion(session.getUserId());
                certificado.setFechaEdicion(new Date());
                if (certificado.getNumCertificado() == null) {
                    certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
                }
                certificado.setRegCertificadoMovimientoCollection(historia);
                certificado = reg.saveCertificadoRazon(certificado);
                if (certificado.getId() != null) {
                    historia = (List<RegCertificadoMovimiento>) certificado.getRegCertificadoMovimientoCollection();
                }
                this.guardarObservacion();
                as.generarFirmaDigital(certificado);
                this.imprimirCopiaRazon();
            } else {
                JsfUti.messageWarning(null, "Debe seleccionar una inscripcion para generar la copia de razon.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void llenarMovimientoRazon() {
        if (movimiento.getActo() != null) {
            rcm.setActo(movimiento.getActo().getNombre());
        }
        if (movimiento.getDomicilio() != null) {
            rcm.setCanton(movimiento.getDomicilio().getNombre());
        }
        if (movimiento.getFechaOto() != null) {
            rcm.setFechaCelebracion(movimiento.getFechaOto());
        }
        if (movimiento.getFechaInscripcion() != null) {
            rcm.setFechaInscripcion(movimiento.getFechaInscripcion());
        }
        if (movimiento.getFechaResolucion() != null) {
            rcm.setFechaResolucion(movimiento.getFechaResolucion());
        }
        if (movimiento.getFolioFin() != null) {
            rcm.setFolioFin(movimiento.getFolioFin());
        }
        if (movimiento.getFolioInicio() != null) {
            rcm.setFolioInicio(movimiento.getFolioInicio());
        }
        if (movimiento.getNumInscripcion() != null) {
            rcm.setInscripcion(movimiento.getNumInscripcion());
        }
        if (movimiento.getEscritJuicProvResolucion() != null) {
            rcm.setJuicioResolucion(movimiento.getEscritJuicProvResolucion());
        }
        if (movimiento.getLibro() != null) {
            rcm.setLibro(movimiento.getLibro().getNombre());
        }
        if (movimiento.getEnteJudicial() != null) {
            rcm.setNotaria(movimiento.getEnteJudicial().getNombre());
        }
        if (movimiento.getObservacion() != null) {
            rcm.setObservacion(movimiento.getObservacion());
        }
        if (movimiento.getNumRepertorio() != null) {
            rcm.setRepertorio(movimiento.getNumRepertorio());
        }
        if (movimiento.getNumTomo() != null) {
            rcm.setTomo(movimiento.getNumTomo());
        }
        rcm.setMovimiento(movimiento);
    }

    public void imprimirCopiaRazon() {
        try {
            ss.instanciarParametros();
            ss.agregarParametro("ID_MOV", movimiento.getId());
            ss.agregarParametro("ID_CERTIFICADO", certificado.getId());
            ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "/reportes/certificados/");
            ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_documento.png"));
            ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
            if (user != null && user.getEnte() != null) {
                ss.agregarParametro("NOMBRES", user.getEnte().getNombreCompleto().toUpperCase());
            }
            ss.setNombreReporte("CopiaRazonInscripcion");
            ss.setNombreSubCarpeta("certificados");
            ss.setTieneDatasource(true);
            //ss.setEncuadernacion(Boolean.TRUE);
            List<String> urlList = new ArrayList<>();
            String url = "/procesos/manage/certificadosRp.xhtml";
            urlList.add(SisVars.urlbase + "Documento");
            JsfUti.redirectMultipleConIP_V2(url, urlList);

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void completarSubTarea() {
        try {
            if (certificado.getId() != null) {
                tt.setRealizado(Boolean.TRUE);
                tt.setRevisado(Boolean.TRUE);
                tt.setFechaFin(new Date());
                tt.setFechaRevision(new Date());
                manager.merge(tt);
                session.setTaskID(this.getTaskId());
                JsfUti.redirectFaces("/procesos/registro/certificadoNoPoseeBien.xhtml");
            } else {
                JsfUti.messageWarning(null, "Primero debe guardar el certificado.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void borrarListas() {
        try {
            for (RegCertificadoPropietario cp : propietarios) {
                if (cp.getId() != null) {
                    manager.delete(cp);
                }
            }
            for (RegCertificadoMovimiento cm : historia) {
                if (cm.getId() != null) {
                    manager.delete(cm);
                }
            }
            propietarios = new ArrayList<>();
            historia = new ArrayList<>();
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, "");
            System.out.println(e);
        }
    }

    public void guardarObservacion() {
        try {
            reg.guardarObservaciones(liquidacion.getTramite(), session.getName_user(), "Se editó el certificado nro: " + certificado.getNumCertificado(), "EDICION DE CERTIFICADO");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<RegTipoFicha> getTiposFichas() {
        return manager.findAllEntCopy(Querys.getRegTipoFicha);
    }

    public HistoricoTramites getHt() {
        return ht;
    }

    public void setHt(HistoricoTramites ht) {
        this.ht = ht;
    }

    public RegpLiquidacion getLiquidacion() {
        return liquidacion;
    }

    public void setLiquidacion(RegpLiquidacion liquidacion) {
        this.liquidacion = liquidacion;
    }

    public Long getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(Long numFicha) {
        this.numFicha = numFicha;
    }

    public Integer getRender() {
        return render;
    }

    public void setRender(Integer render) {
        this.render = render;
    }

    public String getSolvencia() {
        return solvencia;
    }

    public void setSolvencia(String solvencia) {
        this.solvencia = solvencia;
    }

    public RegFicha getFicha() {
        return ficha;
    }

    public void setFicha(RegFicha ficha) {
        this.ficha = ficha;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public RegpTareasTramite getTt() {
        return tt;
    }

    public void setTt(RegpTareasTramite tt) {
        this.tt = tt;
    }

    public RegEnteInterviniente getPropietario() {
        return propietario;
    }

    public void setPropietario(RegEnteInterviniente propietario) {
        this.propietario = propietario;
    }

    public List<RegFichaPropietarios> getListProp() {
        return listProp;
    }

    public void setListProp(List<RegFichaPropietarios> listProp) {
        this.listProp = listProp;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public RegCertificado getCertificado() {
        return certificado;
    }

    public void setCertificado(RegCertificado certificado) {
        this.certificado = certificado;
    }

    public List<RegCertificadoMovimiento> getHistoria() {
        return historia;
    }

    public void setHistoria(List<RegCertificadoMovimiento> historia) {
        this.historia = historia;
    }

    public List<RegMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public List<RegMovimiento> getTemps() {
        return temps;
    }

    public void setTemps(List<RegMovimiento> temps) {
        this.temps = temps;
    }

    public ContenidoReportes getContenido() {
        return contenido;
    }

    public void setContenido(ContenidoReportes contenido) {
        this.contenido = contenido;
    }

    public List<ContenidoReportes> getContenidos() {
        return contenidos;
    }

    public void setContenidos(List<ContenidoReportes> contenidos) {
        this.contenidos = contenidos;
    }

    public List<RegCertificadoPropietario> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegCertificadoPropietario> propietarios) {
        this.propietarios = propietarios;
    }

    public RegRegistrador getRegistrador() {
        return registrador;
    }

    public void setRegistrador(RegRegistrador registrador) {
        this.registrador = registrador;
    }

    public boolean isRequiereCambio() {
        return requiereCambio;
    }

    public void setRequiereCambio(boolean requiereCambio) {
        this.requiereCambio = requiereCambio;
    }

    public void limpiarData() {
        if (!requiereCambio) {
            certificado.setDescripcionBien(null);
        } else {
            certificado.setDescripcionBien("Pero se encuentra inscrito unos Derechos hereditarios a nombre de ");
        }
    }

    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }
}
