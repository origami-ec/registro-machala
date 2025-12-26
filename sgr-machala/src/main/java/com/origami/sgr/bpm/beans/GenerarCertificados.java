/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.bpm.beans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.GeneracionDocs;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class GenerarCertificados implements Serializable {

    private static final Logger LOG = Logger.getLogger(GenerarCertificados.class.getName());
    private static final long serialVersionUID = 1L;

    @EJB(beanName = "registroPropiedad")
    protected RegistroPropiedadServices reg;

    @Inject
    protected FirmaDigitalLocal fd;

    @Inject
    private Entitymanager em;

    @Inject
    private UserSession us;

    @Inject
    private ServletSession ss;

    @Inject
    private OmegaUploader ou;

    protected Map map;
    protected AclUser user;
    protected BigInteger numeroCertificado;
    protected Long numeroTramite;
    protected Long tipo = 1L;
    protected RegRegistrador registrador;
    protected GeneracionDocs gen;
    protected List<RegCertificado> certificados = new ArrayList<>();
    protected List<RegMovimiento> movimietos = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            user = em.find(AclUser.class, us.getUserId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarCertificado() {
        try {
            if (numeroCertificado != null) {
                map = new HashMap();
                map.put("numCertificado", numeroCertificado);
                RegCertificado ce = (RegCertificado) em.findObjectByParameter(RegCertificado.class, map);
                if (ce != null) {
                    ss.instanciarParametros();
                    ss.setTieneDatasource(true);
                    ss.setNombreSubCarpeta("registro");
                    ss.agregarParametro("ID_CERTIFICADO", ce.getId());
                    ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                    ss.agregarParametro("USER_NAME", us.getName_user());
                    ss.agregarParametro("EMISION", ce.getFechaEmision());
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/logo.png"));
                    ss.agregarParametro("REGISTRADOR", registrador.getNombreReportes());
                    if (user != null && user.getEnte() != null) {
                        ss.agregarParametro("NOMBRES", user.getEnte().getTituloNombreCompleto().trim().toUpperCase());
                    }
                    switch (ce.getTipoCertificado().intValue()) {
                        case 1:
                            ss.setNombreReporte("FDCertificadoHistoria");
                            break;
                        case 2:
                            ss.setNombreReporte("FDCertificadoBienesRaices");
                            break;
                        case 3:
                            ss.setNombreReporte("FDCertificadoGeneral");
                            break;
                        default:
                            JsfUti.messageInfo(null, Messages.error, "");
                            return;
                    }
                    //fd.generaPDF();
                    //String ruta = fd.firmarPdf();
                    ss.instanciarParametros();
                    //ss.setNombreDocumento(ruta);
                    JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
                } else {
                    JsfUti.messageWarning(null, "No existe el numero de certificado ingresado.", "");
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el número del certificado.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findCertificados() {
        try {
            if (Utils.isNotEmpty(movimietos)) {
                movimietos = new ArrayList<>();
            }
            if (numeroTramite != null) {
                certificados = em.findAll(Querys.getCertificadosToDownload, new String[]{"tramite"}, new Object[]{numeroTramite});
                if (certificados == null) {
                    certificados = new ArrayList<>();
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el número del trámite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void findRegMovimintos() {
        try {
            if (Utils.isNotEmpty(certificados)) {
                certificados = new ArrayList<>();
            }
            if (numeroTramite != null) {
                movimietos = em.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{numeroTramite});
                if (movimietos == null) {
                    movimietos = new ArrayList<>();
                }
            } else {
                JsfUti.messageWarning(null, "Debe ingresar el número del trámite.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void firmarCertificado(RegCertificado ce) {
        String ruta = SisVars.rutaFirmados;
        try {
            if (ce.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Código de verificación No encontrado.", "");
                return;
            }
            if (ce.getDocumento() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + ce.getNumTramite() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(ce.getDocumento(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            this.generacionDocumento(ce);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void descargarActa(RegMovimiento mov) {
        String ruta = SisVars.rutaFirmados;
        try {
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(mov.getFechaInscripcion());
            Integer periodo = fecha.get(Calendar.YEAR);
            if (mov.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Código de verificación No encontrado.", "");
                return;
            }
            if (mov.getDocumentoActa() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + "acta-" + periodo + "-" + mov.getLibro().getNombreCarpeta() + "-" + mov.getNumInscripcion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(mov.getDocumentoActa(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            this.generacionDocumento(mov);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void getDocumentMov() {
        if (Utils.isEmpty(movimietos)) {
            JsfUti.messageWarning(null, "No se encontraron inscripciones con el numero de tramite.", "");
            return;
        }
        for (RegMovimiento mov : movimietos) {
            if (mov.getCodVerificacion() == null) {
                JsfUti.messageWarning(null, "Verifique que la inscripcion numero " + mov.getNumInscripcion() + " tenga generada el codigo de verificacion.", "");
                return;
            }
        }
        RegMovimiento ce = movimietos.get(0);
        String ruta = SisVars.rutaFirmados;
        try {
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(ce.getFechaInscripcion());
            Integer periodo = fecha.get(Calendar.YEAR);
            if (ce.getDocumento() == null) {
                JsfUti.messageWarning(null, "No se encuentra el documento firmado.", "");
                return;
            } else {
                ruta = ruta + "razon-" + periodo + "-" + ce.getLibro().getNombreCarpeta() + "-" + ce.getNumInscripcion() + ".pdf";
                File file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(ce.getDocumento(), fos);
                    fos.close();
                }
            }
            ss.instanciarParametros();
            ss.setNombreDocumento(ruta);
            JsfUti.redirectNewTab(SisVars.urlbase + "DownLoadFiles");
            this.generacionDocumento(ce);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generacionDocumento(RegMovimiento ce) {
        try {
            gen = new GeneracionDocs();
            gen.setAclLogin(us.getAclLogin().getId());
            gen.setFechaGeneracion(new Date());
            gen.setUsuario(us.getUserId());
            gen.setMovimiento(ce.getId());
            gen.setObservacion("DESCARGA DE RAZON CON FIRMA DIGITAL");
            em.persist(gen);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void generacionDocumento(RegCertificado ce) {
        try {
            ce.setPrintOnline(ce.getPrintOnline() + 1);
            ce.setDatePrintOnline(new Date());
            em.update(ce);

            gen = new GeneracionDocs();
            gen.setAclLogin(us.getAclLogin().getId());
            gen.setFechaGeneracion(new Date());
            gen.setUsuario(us.getUserId());
            gen.setCertificado(ce.getId());
            gen.setObservacion("DESCARGA DE CERTIFICADO CON FIRMA DIGITAL");
            em.persist(gen);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public BigInteger getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(BigInteger numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public Long getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Long numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public List<RegCertificado> getCertificados() {
        return certificados;
    }

    public void setCertificados(List<RegCertificado> certificados) {
        this.certificados = certificados;
    }

    public List<RegMovimiento> getMovimietos() {
        return movimietos;
    }

    public void setMovimietos(List<RegMovimiento> movimietos) {
        this.movimietos = movimietos;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

}
