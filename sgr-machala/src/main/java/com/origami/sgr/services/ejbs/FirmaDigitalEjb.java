/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.config.SisVars;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.FirmaElectronica;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.models.DocumentoElectronico;
import com.origami.sgr.models.FirmaElectronicaModel;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.SignPDFService;
import com.origami.sgr.servlets.OmegaUploader;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

/**
 *
 * @author Asilva
 */
@Stateless
public class FirmaDigitalEjb implements FirmaDigitalLocal {

    @Inject
    private RutasSystemContr rutas;
    @Inject
    private SignPDFService signer;
    @Inject
    private Entitymanager em;
    @Inject
    private OmegaUploader ou;
    @Inject
    private IngresoTramiteLocal itl;

    /**
     * Metodo que ingresa en el archivo la firma digital por certificado digital
     * configurado en los parametros
     *
     * @param ce
     * @return Object
     * @throws java.io.IOException
     */
    @Override
    public Boolean firmarCertificado(RegCertificado ce) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporte(ce, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //String urlPdfFirmado = signer.signPDF(filePdf, null, null, FIRMA_PATH, FIRMA_PASS, null);
                //String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(), ce.getTipoDocumento(), FIRMA_PATH, FIRMA_PASS);
                String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (Utils.isNotEmptyString(urlPdfFirmado)) {
                    File output = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(output)) {
                        Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                        if (oid != null) {
                            ce.setDocumento(oid);
                        }
                        em.update(ce);
                    }
                    File input = new File(filePdf);
                    input.delete();
                    //output.delete();
                    return true;
                } else {
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public String generarReporte(RegCertificado ce, RegRegistrador registrador) {
        Map map;
        String reporte = "";
        try {
            map = this.llenarParametrosCertificado(ce, registrador);
            switch (ce.getTipoDocumento()) {
                case "C01":
                    reporte = "CertificadoNoBienes";
                    break;
                /*case "C02":
                    reporte = "CertificadoSolvencia";
                    break;*/
                case "C03":
                    reporte = "CertificadoHistoriaDominio";
                    break;
                /*case "C04":
                    reporte = "CertificadoFichaPersonal";
                    break;*/
                case "C05":
                    reporte = "CertificadoMercantil";
                    break;
                case "C06":
                    map = this.llenarParametrosCopiaRazon(ce, registrador);
                    reporte = "CopiaRazonInscripcion";
                    break;
                case "C07":
                    reporte = "CertificadoGeneral";
                    break;
            }
            String ruta = rutas.getRootPath() + "/reportes/certificados/" + reporte + ".jasper";
            String nombre = reporte + "_" + ce.getNumCertificado() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosCertificado(RegCertificado ce, RegRegistrador registrador) {
        try {
            Map map = new HashMap();
            map.put("ID_CERTIFICADO", ce.getId());
            map.put("EMISION", ce.getFechaEmision());
            map.put("SOLICITANTE", ce.getNombreSolicitante());
            map.put("USO_DOCUMENTO", ce.getUsoDocumento());
            map.put("SHOW_SIGN", true);
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "reportes/certificados/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosCopiaRazon(RegCertificado ce, RegRegistrador registrador) {
        try {
            List<RegCertificadoMovimiento> rcm = em.findAll(Querys.getMovsByCertificado,
                    new String[]{"id"}, new Object[]{ce.getId()});
            Map map = new HashMap();
            map.put("ID_MOV", rcm.get(0).getMovimiento().getId());
            map.put("ID_CERTIFICADO", ce.getId());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "/reportes/certificados/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("SHOW_SIGN", true);
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosInforme(RegCertificado ce, RegRegistrador registrador) {
        try {
            Map map = new HashMap();
            map.put("ID_CERTIFICADO", ce.getId());
            map.put("NOMBRE", ce.getBeneficiario());
            map.put("BUSQUEDA", ce.getLinderosRegistrales());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "/reportes/certificados/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("SHOW_SIGN", true);
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("FIRMA_DIGITAL", registrador.getDigitalSign() != null ? registrador.getDigitalSign() : "");
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    protected String buildJasper(String nombrePDF, String rutaJasper, Map parametros) throws SQLException {
        Connection conn = null;
        try {
            JasperPrint jasperPrint;
            String filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaFirmados) + nombrePDF;
            Session sess = HiberUtil.getSession();
            SessionImplementor sessImpl = (SessionImplementor) sess;
            conn = sessImpl.getJdbcConnectionAccess().obtainConnection();
            jasperPrint = JasperFillManager.fillReport(rutaJasper, parametros, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
            return filePdf;
        } catch (SQLException | JRException je) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, je);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    @Override
    public String tareaFirmaCertificado(String filePdf) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String urlPdfFirmado;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                urlPdfFirmado = signer.firmaEC(filePdf, "FIRMA CERTIFICADO", "CERTIFICADO",
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File f = new File(filePdf);
                    f.delete();
                }
                return urlPdfFirmado;
            }
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public String tareaFirmaCertificado(String filePdf, Long idCertificado) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String urlPdfFirmado;
        RegCertificado certificado = em.find(RegCertificado.class, idCertificado);
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                urlPdfFirmado = signer.firmaEC(filePdf, certificado.getClaseCertificado(), certificado.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File f = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(f)) {
                        Long oid = ou.upFileDocument(fs, f.getName(), "application/pdf");
                        if (oid != null) {
                            certificado.setDocumento(oid);
                        }
                        certificado.setSecuencia(1);
                        em.update(certificado);
                    }
                }
                return urlPdfFirmado;
            }
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public File firmarCertificadoPath(RegCertificado ce) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporte(ce, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf, ce.getClaseCertificado(), ce.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File output = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(output)) {
                        Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                        if (oid != null) {
                            ce.setDocumento(oid);
                        }
                        em.update(ce);
                    }
                    File input = new File(filePdf);
                    input.delete();
                    //output.delete();
                    return output;
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public File firmarRazonInscripcion(RegMovimiento mo, String usuario) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteRazonInscripcion(mo, registrador, null, usuario);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf, mo.getActo().getNombre(), mo.getNumRepertorio().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        mo.setDocumento(oid);
                    }
                    em.update(mo);
                }
                File input = new File(filePdf);
                input.delete();
                //output.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    @Override
    public File firmarRazonInscripcion(List<RegMovimiento> movs, String usuario) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegMovimiento mo = movs.get(0);
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteRazonInscripcion(mo, registrador, null, usuario);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf, mo.getActo().getNombre(), mo.getNumRepertorio().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        for (RegMovimiento mov : movs) {
                            mov.setDocumento(oid);
                            em.update(mov);
                        }
                    }
                }
                File input = new File(filePdf);
                input.delete();
                //output.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    public String generarReporteRazonInscripcion(RegMovimiento mo, RegRegistrador registrador,
            AclUser jefeInscripcion, String usuario) {
        Map map;
        try {
            map = this.llenarParametrosRazonInscripcion(mo, registrador, jefeInscripcion, usuario);
            String ruta = rutas.getRootPath() + "/reportes/registro/RazonInscripcion_v1.jasper";
            String nombre = "RAZON_" + mo.getId() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosRazonInscripcion(RegMovimiento mo, RegRegistrador registrador,
            AclUser jefeInscripcion, String usuario) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map map = new HashMap();
            Calendar cl = Calendar.getInstance();
            if (mo.getFechaRepertorio() != null) {
                cl.setTime(mo.getFechaRepertorio());
            } else {
                cl.setTime(mo.getFechaInscripcion());
            }
            Integer year = cl.get(Calendar.YEAR);

            map.put("INSCRIPTOR", mo.getUserCreador().getUsuario().toUpperCase());
            map.put("REPERTORIO", mo.getNumRepertorio());
            map.put("ANIO", year.toString());
            map.put("FECHA_REP", mo.getFechaRepertorio());
            map.put("FECHA_REPERTORIO", sdf.format(mo.getFechaRepertorio()));
            if (mo.getTramite() != null) {
                map.put("TRAMITE", mo.getTramite().getTramite().getNumTramite());
                map.put("COMPROBANTE", mo.getTramite().getDetalle().getLiquidacion().getCodigoComprobante());
            }
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "reportes/registro/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("COD_VERIFICACION", mo.getCodVerificacion());
            map.put("PROPIEDAD", mo.getLibro().getPropiedad());
            map.put("REGISTRADOR", registrador.getNombreReportes());
            //map.put("JEFE_INSCRIPCION", jefeInscripcion.getUsuario().toUpperCase());
            map.put("FIRMA_INSCRIPTOR", mo.getUserCreador().getFirmaNombre());
            map.put("CARGO_INSCRIPTOR", mo.getUserCreador().getFirmaCargo());
            //map.put("FIRMA_JEFE", jefeInscripcion.getFirmaNombre());
            //map.put("CARGO_JEFE", jefeInscripcion.getFirmaCargo());
            map.put("USUARIO", usuario);
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List<File> descargarCertificados(Long tramite) {
        List<RegCertificado> certificados;
        List<File> files;
        String ruta;
        File file;
        try {
            files = new ArrayList<>();
            certificados = em.findAll(Querys.getCertificados, new String[]{"tramite"}, new Object[]{tramite});
            for (RegCertificado ce : certificados) {
                ruta = SisVars.rutaFirmados;
                if (ce.getDocumento() != null) {
                    ruta = ruta + ce.getCodVerificacion() + ".pdf";
                    file = new File(ruta);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        ou.streamFile(ce.getDocumento(), fos);
                        fos.close();
                    }
                    if (file.isFile()) {
                        files.add(file);
                    }
                }
            }
            return files;
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public List<File> descargarInscripciones(Long tramite) {
        RegMovimiento movimiento;
        List<File> files;
        String ruta;
        File file;
        try {
            files = new ArrayList<>();
            movimiento = (RegMovimiento) em.find(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{tramite});
            if (movimiento != null && movimiento.getDocumento() != null) {
                ruta = SisVars.rutaFirmados;
                ruta = ruta + movimiento.getCodVerificacion() + ".pdf";
                file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(movimiento.getDocumento(), fos);
                    fos.close();
                }
                if (file.isFile()) {
                    files.add(file);
                }
            }
            return files;
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public File firmarActaInscripcion(RegMovimiento mo, String usuario) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteActaInscripcion(mo, registrador, null, usuario);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf, mo.getActo().getNombre(), mo.getNumRepertorio().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        mo.setDocumentoActa(oid);
                    }
                    em.update(mo);
                }
                File input = new File(filePdf);
                input.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }

    public String generarReporteActaInscripcion(RegMovimiento mo, RegRegistrador registrador,
            AclUser jefeInscripcion, String usuario) {
        Map map;
        try {
            map = this.llenarParametrosActaInscripcion(mo, registrador, jefeInscripcion, usuario);
            String ruta = rutas.getRootPath() + "/reportes/registro/ActaInscripcion.jasper";
            String nombre = "ACTA_" + mo.getId() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public Map llenarParametrosActaInscripcion(RegMovimiento mo, RegRegistrador registrador,
            AclUser jefeInscripcion, String usuario) {
        try {
            Map map = new HashMap();
            map.put("P_MOVIMIENTO", mo.getId());
            map.put("SUBREPORT_DIR", rutas.getRootPath() + "reportes/registro/");
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("ACCION_PERSONAL", registrador.getRazonReporte());
            //map.put("JEFE_INSCRIPCION", jefeInscripcion.getUsuario().toUpperCase());
            map.put("USUARIO", usuario);
            return map;
        } catch (Exception e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public File firmarNotaDevolutiva(RegpNotaDevolutiva dev) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteDevolutiva(dev, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf,  dev.getAsunto(), dev.getNumNotaDevolutiva(), 
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        dev.setDocumento(oid);
                    }
                    em.update(dev);
                }
                File input = new File(filePdf);
                input.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }
    
    @Override
    public File firmarNegativaInscripcion(RegpNotaDevolutiva dev) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarReporteNegativa(dev, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf,  dev.getAsunto(), dev.getNumNotaDevolutiva(), 
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        dev.setDocumento(oid);
                    }
                    em.update(dev);
                }
                File input = new File(filePdf);
                input.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }
    
    @Override
    public File firmarDevolutivaCertificado(RegpNotaDevolutiva dev) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String filePdf;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            filePdf = this.generarDevolutivaCertificado(dev, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                String urlPdfFirmado = signer.firmaEC(filePdf,  dev.getAsunto(), dev.getNumNotaDevolutiva(), 
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                File output = new File(urlPdfFirmado);
                try (FileInputStream fs = new FileInputStream(output)) {
                    Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                    if (oid != null) {
                        dev.setDocumento(oid);
                    }
                    em.update(dev);
                }
                File input = new File(filePdf);
                input.delete();
                return output;
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return null;
    }
    
    public String generarReporteNegativa(RegpNotaDevolutiva dev, RegRegistrador registrador) {
        Map map = new HashMap();
        try {
            map.put("ID_NOTA", dev.getId());
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("USUARIO", dev.getElaborado());

            String ruta = rutas.getRootPath() + "/reportes/registro/RazonNegativa.jasper";
            String nombre = dev.getNumNotaDevolutiva() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public String generarReporteDevolutiva(RegpNotaDevolutiva dev, RegRegistrador registrador) {
        Map map = new HashMap();
        try {
            map.put("ID_NOTA", dev.getId());
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("USUARIO", dev.getElaborado());

            String ruta = rutas.getRootPath() + "/reportes/registro/NotaDevolutiva.jasper";
            String nombre = dev.getNumNotaDevolutiva() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }
    
    public String generarDevolutivaCertificado(RegpNotaDevolutiva dev, RegRegistrador registrador) {
        Map map = new HashMap();
        try {
            map.put("ID_NOTA", dev.getId());
            map.put("WATERMARK_URL", rutas.getRootPath() + "/resources/image/formato_documento.png");
            map.put("REGISTRADOR", registrador.getNombreReportes());
            map.put("USUARIO", dev.getElaborado());

            String ruta = rutas.getRootPath() + "/reportes/registro/NotaDevolutivaCertificado.jasper";
            String nombre = dev.getNumNotaDevolutiva() + ".pdf";
            return this.buildJasper(nombre, ruta, map);
        } catch (SQLException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public File descargarDevolutiva(RegpNotaDevolutiva dev) {
        String ruta = SisVars.rutaFirmados;
        File file = new File("");
        try {
            if (dev.getDocumento() != null) {
                ruta = ruta + dev.getNumNotaDevolutiva() + ".pdf";
                file = new File(ruta);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    ou.streamFile(dev.getDocumento(), fos);
                    fos.close();
                }
            }
            return file;
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public DocumentoElectronico verificarDocumentoElectronico(FirmaElectronicaModel firmaElectronica) throws IOException {
        return signer.verificarDocumentoElectronico(firmaElectronica);
    }

    @Override
    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronicaModel firmaElectronica) throws IOException {
        return signer.validarFirmaElectronica(firmaElectronica);
    }

    @Override
    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronica firmaElectronica, String clave) {
        FirmaElectronicaModel firmaElectronicaValidar = new FirmaElectronicaModel();
        firmaElectronicaValidar.setArchivo(SisVars.rutaFirmaEC + File.separator + firmaElectronica.getUid());
        firmaElectronicaValidar.setClave(clave);
        try {
            return signer.validarFirmaElectronica(firmaElectronicaValidar);
        } catch (IOException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public File firmarCertificadoOnline(RegCertificado ce) throws IOException {
        String FIRMA_PATH;
        String FIRMA_PASS;
        String fileOriginal, urlPdfFirmado;
        try {
            RegRegistrador registrador = (RegRegistrador) em.find(Querys.getRegRegistradorFD);
            fileOriginal = this.generarReporte(ce, registrador);
            if (registrador != null && registrador.getFileSign() != null && registrador.getPassSign() != null) {
                FIRMA_PATH = registrador.getFileSign();
                FIRMA_PASS = registrador.getPassSign();
                urlPdfFirmado = signer.firmaEC(fileOriginal, ce.getClaseCertificado(), ce.getNumCertificado().toString(),
                        registrador.getNombreCompleto(), FIRMA_PATH, FIRMA_PASS);
                if (urlPdfFirmado != null && Utils.isNotEmptyString(urlPdfFirmado)) {
                    File output = new File(urlPdfFirmado);
                    try (FileInputStream fs = new FileInputStream(output)) {
                        Long oid = ou.upFileDocument(fs, output.getName(), "application/pdf");
                        if (oid != null) {
                            ce.setDocumento(oid);
                        }
                        ce.setSecuencia(1);
                        em.update(ce);
                    }
                    return output;
                }
            }
        } catch (FileNotFoundException e) {
            Logger.getLogger(FirmaDigitalEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
