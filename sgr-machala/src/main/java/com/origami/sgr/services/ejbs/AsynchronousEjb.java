/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.config.SisVars;
import com.origami.mail.entities.CorreoUsuarios;
import com.origami.mail.models.ConfiguracionDto;
import com.origami.mail.models.CorreoArchivoDto;
import com.origami.mail.models.CorreoDto;
import com.origami.mail.service.EnviarCorreoService;
import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.MsgFormatoNotificacion;
import com.origami.sgr.entities.MsgTipoFormatoNotificacion;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.entities.Valores;
import com.origami.sgr.models.ActosRequisito;
import com.origami.sgr.models.Sms;
import com.origami.sgr.restful.services.FireMessage;
import com.origami.sgr.services.interfaces.AsynchronousService;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.FirmaDigitalLocal;
import com.origami.sgr.services.interfaces.RegCertificadoService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.HiberUtil;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import com.origami.sgr.util.Constantes;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.AccessTimeout;
import javax.ejb.Asynchronous;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author Anyelo
 */
@Singleton(name = "asincrono")
public class AsynchronousEjb implements AsynchronousService {

    @Inject
    private SeqGenMan sec;
    @Inject
    private Entitymanager em;
    @Inject
    private FirmaDigitalLocal fd;
    @Inject
    private EnviarCorreoService mail;
    @Inject
    private RegCertificadoService rcs;
    @Inject
    private RegistroPropiedadServices reg;

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void testSendEmail(String destinatario, Long tipo, String usuario) {
        HashMap map;
        MsgFormatoNotificacion fn;
        CorreoDto dto = new CorreoDto();
        CorreoUsuarios user;
        try {
            map = new HashMap();
            map.put("estado", 1);
            map.put("tipo", new MsgTipoFormatoNotificacion(tipo));
            fn = (MsgFormatoNotificacion) em.findObjectByParameter(MsgFormatoNotificacion.class, map);
            if (fn != null) {
                user = sec.getMailDisponible();
                if (user != null) {
                    ConfiguracionDto configuracion = new ConfiguracionDto(user);
                    dto.setDestinatario(destinatario);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(fn.getHeader() + "CORREO DE PRUEBA" + fn.getFooter());
                    dto.setNumeroTramite(1L);
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Envia un correo al solicitante con los documentos adjuntos de la factura
     * electronica, el archivo xml y el archivo pdf
     *
     * @param liquidaciones RegpLiquidacion
     * @param cajero RenCajero
     */
    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreosRIDEbasico(List<RegpLiquidacion> liquidaciones, RenCajero cajero) {
        List<File> files;
        MsgTipoFormatoNotificacion tfn;
        MsgFormatoNotificacion fn;
        HashMap map;
        try {
            map = new HashMap();
            map.put("descripcion", Constantes.correoFactura);
            tfn = (MsgTipoFormatoNotificacion) em.findObjectByParameter(MsgTipoFormatoNotificacion.class, map);
            map = new HashMap();
            map.put("estado", 1);
            map.put("tipo", tfn);
            fn = (MsgFormatoNotificacion) em.findObjectByParameter(MsgFormatoNotificacion.class, map);
            for (RegpLiquidacion re : liquidaciones) {
                if (re.getBeneficiario().getCorreo1() != null) {
                    File xml = new File(cajero.getRutaComprobantesAutorizados() + re.getClaveAcceso() + ".xml");
                    File ride = new File(cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".pdf");
                    if (xml.exists() && ride.exists()) {
                        files = new ArrayList<>();
                        files.add(xml);
                        files.add(ride);
                        /*Email correo = new Email(re.getBeneficiario().getCorreo1(), fn.getHeader(), fn.getFooter(), files);
                        Boolean flag = correo.sendMail();
                        if (flag) {
                            File nuevo = new File(cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".xml");
                            xml.renameTo(nuevo);
                            re.setRideEnviado(Boolean.TRUE);
                            em.update(re);
                        }*/
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Envia un correo al solicitante con los documentos adjuntos de la factura
     * electronica, el archivo xml y el archivo pdf
     *
     * @param liquidaciones RegpLiquidacion
     * @param cajero RenCajero
     */
    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreosRIDE(List<RegpLiquidacion> liquidaciones, RenCajero cajero) {
        List<File> files;
        MsgFormatoNotificacion fn;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"}, new Object[]{Constantes.correoEnvioFactura});
            if (fn != null) {
                String contenido = fn.getHeader() + fn.getFooter();
                for (RegpLiquidacion re : liquidaciones) {
                    if (re.getBeneficiario().getCorreo1() != null) {
                        File xml = new File(cajero.getRutaComprobantesAutorizados() + re.getClaveAcceso() + ".xml");
                        File ride = new File(cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".pdf");
                        if (xml.exists() && ride.exists()) {
                            files = new ArrayList<>();
                            files.add(xml);
                            files.add(ride);
                            /*Email correo = new Email(re.getBeneficiario().getCorreo1(), fn.getAsunto(), contenido, files);
                            Boolean flag = correo.sendMail();
                            if (flag) {
                                File nuevo = new File(cajero.getRutaComprobantesEnviados() + re.getClaveAcceso() + ".xml");
                                xml.renameTo(nuevo);
                                re.setRideEnviado(Boolean.TRUE);
                                em.update(re);
                            }*/
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Envia un correo al solicitante indicando que el tramite ha sido
     * finalizado
     *
     * @param re RegpLiquidacion
     */
    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoTramiteFinalizadoV1(RegpLiquidacion re) {
        MsgFormatoNotificacion fn;
        //EmailUtil correo = new EmailUtil();
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"}, new Object[]{Constantes.correoTramite});
            if (fn != null) {
                String email = SisVars.correo;
                if (re.getBeneficiario().getCorreo1() != null) {
                    //email = re.getSolicitante().getCorreo1();
                    email = re.getBeneficiario().getCorreo1();
                }
                String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                // si es solicitud online, añadir link
                Long estadoOnline = 7L;
                if (re.getEstadoPago().getId().equals(estadoOnline)) {
                    String url = "";
                    //String url = rcs.generatePrintCertUrl(re.getNumTramiteRp());
                    contenido = contenido + "<br> <h3>Use el siguiente link para la impresión de su certificado:<h3> <a href=\""
                            + url + "\">Imprimir Certificado...</a>";
                    email = (String) em.getNativeQuery(Querys.getCorreoSolicitudOnLine, new Object[]{re.getTramite().getId()});
                    if (email == null) {
                        email = SisVars.correo;
                    }
                }
                //correo.sendEmail(re.getSolicitante().getCorreo1(), fn.getAsunto(), contenido, null, null);
                //correo = new Email(email, fn.getAsunto(), contenido, null);
                //correo.sendMail();
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * Envia un correo al solicitante indicando que el tramite ha sido ingresado
     * con exito y que se he iniciado el proceso del mismo dentro del registro
     *
     * @param solicitante
     * @param numTramiteRp
     */
    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void sendNotificationFirebaseUserAndorid(CatEnte solicitante, Long numTramiteRp) {

        try {
            String message = "Su trámite #" + numTramiteRp + " ha finalizado con éxito";

            String token = ";";
            URL obj = new URL(SisVars.urlWSVentanilla + "/api/user/token/0960013746");
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuffer response;
                try ( // success
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                con.getInputStream()))) {
                    String inputLine;
                    response = new StringBuffer();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                // print result
                token = response.toString();
                System.out.println(response.toString());
            } else {
                System.out.println("GET request not worked");
            }
            FireMessage f = new FireMessage(message, "MESSAGE");
            f.sendToToken(token);

        } catch (Exception ex) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoTramiteNotaDevolutiva(String email, Long tramite) {
        MsgFormatoNotificacion fn;
        String contenido;
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                    new Object[]{Constantes.correoTramiteVacio});
            if (fn != null) {
                /*String email = SisVars.correo;
                if (re.getBeneficiario().getCorreo1() != null) {
                    //email = re.getSolicitante().getCorreo1();
                    email = re.getBeneficiario().getCorreo1();
                }*/
                if (email == null || email.isEmpty()) {
                    email = SisVars.correo;
                }
                contenido = "SE GENERÓ UNA NOTA DEVOLUTIVA PARA EL TRAMITE N° " + tramite
                        + ". POR FAVOR ACERCARSE A CORREGIR SU SOLICITUD EN LA VENTANILLA RESPECTIVA.";
                //correo = new Email(email, fn.getAsunto(), fn.getHeader() + contenido + fn.getFooter(), null);
                //correo.sendMail();
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaDigital(Long tramite) {
        List<RegCertificado> certificados;
        try {
            certificados = em.findAll(Querys.getCertificados, new String[]{"tramite"}, new Object[]{tramite});
            for (RegCertificado rc : certificados) {
                //if (rc.getDocumento() == null) {}
                fd.firmarCertificado(rc);
            }
        } catch (IOException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaDigital(RegCertificado certificado) {
        try {
            fd.firmarCertificado(certificado);
        } catch (IOException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List<File> generarFirmaDigitalArchivos(Long tramite) {
        List<RegCertificado> certificados;
        List<File> files;
        File f;
        try {
            files = new ArrayList<>();
            certificados = em.findAll(Querys.getCertificados, new String[]{"tramite"}, new Object[]{tramite});
            for (RegCertificado rc : certificados) {
                //if (rc.getDocumento() == null) {}
                f = fd.firmarCertificadoPath(rc);
                if (f != null) {
                    files.add(f);
                }
            }
            return files;
        } catch (IOException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoTramiteFinalizado(RegpLiquidacion re, List<File> archivos) {
        MsgFormatoNotificacion fn;
        String contenido;
        //Email correo;
        try {
            if (re.getCertificado() && !re.getInscripcion()) {
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoFinTramiteCertificado});
            } else {
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoFinTramiteInscripcion});
            }
            if (fn != null) {
                String email = SisVars.correo;
                if (re.getBeneficiario().getCorreo1() != null) {
                    email = re.getBeneficiario().getCorreo1();
                }
                contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                if (re.getEstadoPago().getId().equals(7L)) { //TRAMITE ONLINE
                    email = (String) em.getNativeQuery(Querys.getCorreoSolicitudOnLine, new Object[]{re.getTramite().getId()});
                    if (email == null) {
                        email = SisVars.correo;
                    }
                }
                //correo = new Email(email, fn.getAsunto(), contenido, archivos);
                //correo.sendMail();
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoProformaInscripcionSmsTramite(RegpLiquidacion re, Sms sms) {
        MsgFormatoNotificacion fn;
        //EmailUtil correo = new EmailUtil();
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                    new Object[]{Constantes.correoProformaTramite});
            if (fn != null) {
                if (re.getBeneficiario().getCorreo1() != null) {
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + " CON UN VALOR A PAGAR DE "
                            + re.getTotalPagar() + fn.getFooter();
                    //correo = new Email(re.getBeneficiario().getCorreo1(), fn.getAsunto(), contenido, null);
                    //correo.sendMail();
                    /*guardarCorreo(re.getBeneficiario().getCorreo1(), "enviarCorreoProformaInscripcionSmsTramite",
                            fn.getAsunto(), contenido, re.getNumTramiteRp(), enviado, null);*/
                    reg.enviarSms(sms);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoProformaInscripcionLinkTramite(RegpLiquidacion re, Sms sms) {
        MsgFormatoNotificacion fn;
        //EmailUtil correo = new EmailUtil();
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"}, new Object[]{Constantes.correoSolictiudInscripcionPago});
            if (fn != null) {
                if (re.getBeneficiario().getCorreo1() != null) {
                    String linkPago = "<a href=\"" + sms.getLink() + "\" target=\"_new\">AQUI.</a>";
                    String contenido = fn.getHeader() + "EL TRAMITE N° " + re.getNumTramiteRp() + " CON UN VALOR A PAGAR DE " + re.getTotalPagar()
                            + " SE HA INICIARÁ EN EL REGISTRO DE LA PROPIEDAD UNA VEZ REALIZADO EL PAGO DENTRO DE <b>SIETE DIAS PLAZO.</b>  "
                            + "PUEDE CANCELAR MEDIANTE LAS SIGUIENTES FORMAS DE PAGO: <br/>"
                            + "1)VENTANILLAS DE RECAUDACION DEL REGISTRO<br/>"
                            + "2)ENTIDAD FINANCIERA QUE USTED PREFIERA MEDIANTE TRANSFERENCIA BANCARIA<br/>"
                            + "3)PUEDE PAGAR DANDO CLICK " + linkPago
                            + "<br/> RECUERDA:<br/>**LA PRESENTE REVISIÓN ES DE CUMPLIMIENTO DE LA DOCUMENTACIÓN Y DE LOS REQUISITOS DEL DOCUMENTO<br/>**DEBES ACERCARTE A LA INSTITUCION A DEJAR LOS DOCUMENTOS FISICOS EN UN PLAZO DE 48HRS"
                            + fn.getFooter();
                    //correo = new Email(re.getBeneficiario().getCorreo1(), fn.getAsunto(), contenido, null);
                    //correo.sendMail();
                    reg.enviarSms(sms);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoSolicitudIncripcionObservaciones(PubSolicitud solicitud) {
        MsgFormatoNotificacion fn;
        //EmailUtil correo = new EmailUtil();
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"}, new Object[]{Constantes.correoSolictiudInscripcionNotificacion});
            if (fn != null) {
                if (solicitud.getSolCorreo() != null) {
                    //String urlNotificacion = SisVars.urlVentanilla + "/inscripciones/notificaciones.xhtml?code1=" + solicitud.getTramite().getNumTramite() + "&code2=" + solicitud.getSolCedula() + "&code3=" + Utils.encriptaEnMD5(solicitud.getTramite().getNumTramite().toString() + "_" + solicitud.getSolCedula() + "_" + "INSCRIPCION");
                    String req = "";
                    String pronombre = solicitud.getRequisitos().size() == 1 ? "es el siguiente " : "son los siguientes ";
                    for (ActosRequisito a : solicitud.getRequisitos()) {
                        req = "*" + a.getRequisito() + "<br/>" + req;
                    }
                    String contenido = solicitud.getNotificacion()
                            + "<br/><b> Los requisitos a corregir " + pronombre + "<b/> <br/>" + req + "<br/><br/>"
                            + "<br/> RECUERDA:<br/>**LA PRESENTE REVISION NO CONSTITUYE CALIFICACIÓN JURIDICA DEL DOCUMENTO<br/>";
                    //correo = new Email(solicitud.getSolCorreo(), fn.getAsunto(), fn.getHeader() + contenido + fn.getFooter(), null);
                    //correo.sendMail();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void firmarDocumentosEnviarCorreo(RegpLiquidacion liquidacion, String usuario) {
        try {
            //FIRMA ELECTRONICA DE CERTIFICADOS
            List<File> certificados = this.generarFirmaDigitalArchivos(liquidacion.getNumTramiteRp());
            if (!certificados.isEmpty()) {
                this.enviarCorreoFinTramiteCertificado(liquidacion.getNumTramiteRp(),
                        liquidacion.getBeneficiario().getCorreo1(), certificados, null);
            }

            //FIRMA ELECTRONICA PARA INSCRIPCIONES
            List<File> razones = this.generarFirmaInscripciones(liquidacion.getNumTramiteRp(), usuario);
            if (!razones.isEmpty()) {
                this.enviarCorreoFinTramiteInscripcion(liquidacion.getNumTramiteRp(),
                        liquidacion.getBeneficiario().getCorreo1(), razones, null);
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List<File> generarFirmaInscripciones(Long tramite, String usuario) {
        List<File> archivos = new ArrayList<>();
        RegMovimiento mo;
        try {
            List<RegMovimiento> movs = em.findAll(Querys.getMovsByTramite, new String[]{"numeroTramite"}, new Object[]{tramite});
            if (!movs.isEmpty()) {
                mo = movs.remove(0);
                if (mo.getCodVerificacion() == null) {
                    mo.setCodVerificacion(rcs.genCodigoVerif());
                }
                archivos.add(fd.firmarRazonInscripcion(mo, usuario));
                for (RegMovimiento m : movs) {
                    m.setCodVerificacion(mo.getCodVerificacion());
                    m.setDocumento(mo.getDocumento());
                    em.update(m);
                }
            }
            return archivos;
        } catch (IOException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaActaInscripcion(Long tramite, String usuario) {
        try {
            List<RegMovimiento> movs = em.findAll(Querys.getMovsByTramite,
                    new String[]{"numeroTramite"}, new Object[]{tramite});
            if (!movs.isEmpty()) {
                for (RegMovimiento mo : movs) {
                    if (mo.getCodVerificacion() == null) {
                        mo.setCodVerificacion(rcs.genCodigoVerif());
                    }
                    fd.firmarActaInscripcion(mo, usuario);
                }
            }
        } catch (IOException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoCertificados(RegpLiquidacion re, List<File> archivos) {
        MsgFormatoNotificacion fn;
        String contenido;
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                    new Object[]{Constantes.correoFinTramiteCertificado});
            if (fn != null) {
                String email = SisVars.correo;
                if (re.getBeneficiario().getCorreo1() != null) {
                    email = re.getBeneficiario().getCorreo1();
                }
                contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                if (re.getEstadoPago().getId().equals(7L)) { //TRAMITE ONLINE
                    email = (String) em.getNativeQuery(Querys.getCorreoSolicitudOnLine, new Object[]{re.getTramite().getId()});
                    if (email == null) {
                        email = SisVars.correo;
                    }
                }
                //correo = new Email(email, fn.getAsunto(), contenido, archivos);
                //correo.sendMail();
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoInscripcion(RegpLiquidacion re, List<File> archivos) {
        MsgFormatoNotificacion fn;
        String contenido;
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                    new Object[]{Constantes.correoFinTramiteInscripcion});
            if (fn != null) {
                String email = SisVars.correo;
                if (re.getBeneficiario().getCorreo1() != null) {
                    email = re.getBeneficiario().getCorreo1();
                }
                contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                if (re.getEstadoPago().getId().equals(7L)) { //TRAMITE ONLINE
                    email = (String) em.getNativeQuery(Querys.getCorreoSolicitudOnLine, new Object[]{re.getTramite().getId()});
                    if (email == null) {
                        email = SisVars.correo;
                    }
                }
                //correo = new Email(email, fn.getAsunto(), contenido, archivos);
                //correo.sendMail();
                if (re.getSolicitante().getTelefono1() != null) {
                    Sms sms = new Sms();
                    sms.setDestinatario(re.getSolicitante().getTelefono1());
                    sms.setTopic(Constantes.TOPIC_SMS);
                    sms.setMensaje(Constantes.SMS_TRAMITE);
                    reg.enviarSms(sms);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public Boolean reenviarCorreoDocumentos(Long numTramite, String correo) {
        try {
            //DESCARGA CERTIFICADOS FIRMADOS
            List<File> certificados = fd.descargarCertificados(numTramite);
            if (!certificados.isEmpty()) {
                this.reenviarCorreoTramite(numTramite, correo, certificados);
            }
            //DESCARGA ACTAS FIRMADAS
            List<File> razones = fd.descargarInscripciones(numTramite);
            if (!razones.isEmpty()) {
                this.reenviarCorreoTramite(numTramite, correo, razones);
            }
            return true;
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public Boolean reenviarCorreoTramite(Long tramite, String email, List<File> archivos) {
        MsgFormatoNotificacion fn;
        String contenido;
        //Email correo;
        try {
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                    new Object[]{Constantes.correoFinTramiteCertificado});
            if (fn != null) {
                contenido = fn.getHeader() + tramite + fn.getFooter();
                //correo = new Email(email, fn.getAsunto(), contenido, archivos);
                //return correo.sendMail();
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    @Override
    public Boolean firmarCertificadoReenviarCorreo(RegCertificado ce, String usuario) {
        List<File> files = new ArrayList<>();
        String correo = SisVars.correo;
        try {
            files.add(fd.firmarCertificadoPath(ce));
            Map map = new HashMap();
            map.put("numTramiteRp", ce.getNumTramite());
            RegpLiquidacion re = (RegpLiquidacion) em.findObjectByParameter(RegpLiquidacion.class, map);
            if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                correo = re.getCorreoTramite();
            } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                correo = re.getBeneficiario().getCorreo1();
            }
            this.enviarCorreoFinTramiteCertificado(ce.getNumTramite(), correo, files, usuario);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoProformaTramite(RegpLiquidacion re, String archivo, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files;
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        String correo = SisVars.correo;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoProformaTramite});
                if (fn != null) {
                    if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                        correo = re.getCorreoTramite();
                    } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                        correo = re.getBeneficiario().getCorreo1();
                    }
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                    dto.setDestinatario(correo);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(contenido);
                    dto.setNumeroTramite(re.getNumTramiteRp());
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    if (archivo != null && !archivo.isEmpty()) {
                        file = new CorreoArchivoDto();
                        file.setArchivoBase64("");
                        file.setNombreArchivo(archivo);
                        file.setTipoArchivo("pdf");
                        files = new ArrayList<>();
                        files.add(file);
                        dto.setArchivos(files);
                    }
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoInicioTramite(RegpLiquidacion re, String usuario) {
        CorreoDto dto = new CorreoDto();
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        String correo = SisVars.correo;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoInicioTramite});
                if (fn != null) {
                    if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                        correo = re.getCorreoTramite();
                    } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                        correo = re.getBeneficiario().getCorreo1();
                    }
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                    dto.setDestinatario(correo);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(contenido);
                    dto.setNumeroTramite(re.getNumTramiteRp());
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoSolicitudIncripcion(PubSolicitud solicitud, List<ActosRequisito> requisitos) {
        HiberUtil.newTransaction();
        MsgFormatoNotificacion fn;
        CorreoDto dto = new CorreoDto();
        CorreoUsuarios user;
        try {
            Map map = new HashMap();
            map.put("code", Constantes.tiempoEsperaSolicitudLinea);
            Valores valor = (Valores) em.findObjectByParameter(Valores.class, map);

            map = new HashMap();
            map.put("solicitud", solicitud);
            String acto = "";

            if (Utils.isNotEmpty(requisitos)) {
                List<ActosRequisito> actosFiltered = requisitos.stream()
                        .filter(Utils.distinctByKey(p -> p.getIdActo()))
                        .collect(Collectors.toList());
                for (ActosRequisito r : actosFiltered) {
                    acto = " " + r.getActo() + acto + ", ";
                }
                acto = acto.substring(0, acto.length() - 1).toUpperCase();
            }
            fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion,
                    new String[]{"tipo"}, new Object[]{Constantes.correoSolictiudInscripcionOnline});
            if (fn != null) {
                if (solicitud.getSolCorreo() != null) {
                    user = sec.getMailDisponible();
                    if (user != null) {
                        ConfiguracionDto configuracion = new ConfiguracionDto(user);
                        String contenido = fn.getHeader() + "SU SOLICITUD DE INSCRIPCIÓN" + (!acto.isEmpty() ? acto : " ")
                                + "FUE INGRESADA CON EXITO, TIEMPO DE ESPERA MÁXIMO " + valor.getValorString()
                                + " MANTENGASE ATENTO A SU CORREO ELECTRÓNICO" + fn.getFooter();
                        dto.setDestinatario(solicitud.getSolCorreo());
                        dto.setAsunto(fn.getAsunto());
                        dto.setMensaje(contenido);
                        dto.setNumeroTramite(solicitud.getTramite().getNumTramite());
                        dto.setConfiguracion(configuracion);
                        dto.setUsuario("ventanilla");
                        mail.enviarCorreo(dto);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoTituloCredito(RegpLiquidacion re, String archivo, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files;
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        String correo = SisVars.correo;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoInicioTramite});
                if (fn != null) {
                    if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                        correo = re.getCorreoTramite();
                    } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                        correo = re.getBeneficiario().getCorreo1();
                    }
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                    dto.setDestinatario(correo);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(contenido);
                    dto.setNumeroTramite(re.getNumTramiteRp());
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    if (archivo != null && !archivo.isEmpty()) {
                        file = new CorreoArchivoDto();
                        file.setArchivoBase64("");
                        file.setNombreArchivo(archivo);
                        file.setTipoArchivo("pdf");
                        files = new ArrayList<>();
                        files.add(file);
                        dto.setArchivos(files);
                    }
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoNotaDevolutiva(Long idliquidacion, String archivo, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files;
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        String correo = SisVars.correo;
        try {
            RegpLiquidacion re = em.find(RegpLiquidacion.class, idliquidacion);
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoObservacionesTramite});
                if (fn != null) {
                    if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                        correo = re.getCorreoTramite();
                    } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                        correo = re.getBeneficiario().getCorreo1();
                    }
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                    dto.setDestinatario(correo);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(contenido);
                    dto.setNumeroTramite(re.getNumTramiteRp());
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    if (archivo != null && !archivo.isEmpty()) {
                        file = new CorreoArchivoDto();
                        file.setArchivoBase64("");
                        file.setNombreArchivo(archivo);
                        file.setTipoArchivo("pdf");
                        files = new ArrayList<>();
                        files.add(file);
                        dto.setArchivos(files);
                    }
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoNotaDevolutiva(Long numTramite, String correo, String archivo, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files;
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoObservacionesTramite});
                if (fn != null) {
                    if (correo != null && !correo.isEmpty()) {
                        String contenido = fn.getHeader() + numTramite + fn.getFooter();
                        dto.setDestinatario(correo);
                        dto.setAsunto(fn.getAsunto());
                        dto.setMensaje(contenido);
                        dto.setNumeroTramite(numTramite);
                        dto.setConfiguracion(configuracion);
                        dto.setUsuario(usuario);
                        if (archivo != null && !archivo.isEmpty()) {
                            file = new CorreoArchivoDto();
                            file.setArchivoBase64("");
                            file.setNombreArchivo(archivo);
                            file.setTipoArchivo("pdf");
                            files = new ArrayList<>();
                            files.add(file);
                            dto.setArchivos(files);
                        }
                        mail.enviarCorreo(dto);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoFinTramiteCertificado(RegpLiquidacion re, List<String> archivos, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files = new ArrayList<>();
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        String correo = SisVars.correo;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoFinTramiteCertificado});
                if (fn != null) {
                    if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                        correo = re.getCorreoTramite();
                    } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                        correo = re.getBeneficiario().getCorreo1();
                    }
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                    dto.setDestinatario(correo);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(contenido);
                    dto.setNumeroTramite(re.getNumTramiteRp());
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    for (String archivo : archivos) {
                        if (archivo != null && !archivo.isEmpty()) {
                            file = new CorreoArchivoDto();
                            file.setArchivoBase64("");
                            file.setNombreArchivo(archivo);
                            file.setTipoArchivo("pdf");
                            files.add(file);
                        }
                    }
                    if (!files.isEmpty()) {
                        dto.setArchivos(files);
                    }
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoFinTramiteInscripcion(RegpLiquidacion re, List<String> archivos, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files = new ArrayList<>();
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        String correo = SisVars.correo;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoFinTramiteInscripcion});
                if (fn != null) {
                    if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                        correo = re.getCorreoTramite();
                    } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                        correo = re.getBeneficiario().getCorreo1();
                    }
                    String contenido = fn.getHeader() + re.getNumTramiteRp() + fn.getFooter();
                    dto.setDestinatario(correo);
                    dto.setAsunto(fn.getAsunto());
                    dto.setMensaje(contenido);
                    dto.setNumeroTramite(re.getNumTramiteRp());
                    dto.setConfiguracion(configuracion);
                    dto.setUsuario(usuario);
                    for (String archivo : archivos) {
                        if (archivo != null && !archivo.isEmpty()) {
                            file = new CorreoArchivoDto();
                            file.setArchivoBase64("");
                            file.setNombreArchivo(archivo);
                            file.setTipoArchivo("pdf");
                            files.add(file);
                        }
                    }
                    if (!files.isEmpty()) {
                        dto.setArchivos(files);
                    }
                    mail.enviarCorreo(dto);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void reenviarCorreoDocumentos(Long numTramite, String correo, String usuario) {
        try {
            List<File> temp;
            //DESCARGA ACTA FIRMADA
            List<File> razones = fd.descargarInscripciones(numTramite);
            if (!razones.isEmpty()) {
                this.enviarCorreoFinTramiteInscripcion(numTramite, correo, razones, usuario);
            }

            //DESCARGA CERTIFICADOS FIRMADOS
            List<File> certificados = fd.descargarCertificados(numTramite);
            if (!certificados.isEmpty()) {
                for (File file : certificados) {
                    temp = new ArrayList<>();
                    temp.add(file);
                    this.enviarCorreoFinTramiteCertificado(numTramite, correo, temp, usuario);
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoFinTramiteCertificado(Long tramite, String correo, List<File> archivos, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files = new ArrayList<>();
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoFinTramiteCertificado});
                if (fn != null) {
                    if (correo != null && !correo.isEmpty()) {
                        String contenido = fn.getHeader() + tramite.toString() + fn.getFooter();
                        dto.setDestinatario(correo);
                        dto.setAsunto(fn.getAsunto());
                        dto.setMensaje(contenido);
                        dto.setNumeroTramite(tramite);
                        dto.setConfiguracion(configuracion);
                        dto.setUsuario(usuario);
                        for (File archivo : archivos) {
                            if (archivo != null) {
                                file = new CorreoArchivoDto();
                                file.setArchivoBase64("");
                                file.setNombreArchivo(archivo.getAbsolutePath());
                                file.setTipoArchivo("pdf");
                                files.add(file);
                            }
                        }
                        if (!files.isEmpty()) {
                            dto.setArchivos(files);
                        }
                        mail.enviarCorreo(dto);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoFinTramiteInscripcion(Long tramite, String correo, List<File> archivos, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files = new ArrayList<>();
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        try {
            user = sec.getMailDisponible();
            if (user != null) {
                ConfiguracionDto configuracion = new ConfiguracionDto(user);
                fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                        new Object[]{Constantes.correoFinTramiteInscripcion});
                if (fn != null) {
                    if (correo != null && !correo.isEmpty()) {
                        String contenido = fn.getHeader() + tramite.toString() + fn.getFooter();
                        dto.setDestinatario(correo);
                        dto.setAsunto(fn.getAsunto());
                        dto.setMensaje(contenido);
                        dto.setNumeroTramite(tramite);
                        dto.setConfiguracion(configuracion);
                        dto.setUsuario(usuario);
                        for (File archivo : archivos) {
                            if (archivo != null) {
                                file = new CorreoArchivoDto();
                                file.setArchivoBase64("");
                                file.setNombreArchivo(archivo.getAbsolutePath());
                                file.setTipoArchivo("pdf");
                                files.add(file);
                            }
                        }
                        if (!files.isEmpty()) {
                            dto.setArchivos(files);
                        }
                        mail.enviarCorreo(dto);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoFinTramiteInscripcion(Long tramite, String correo, String usuario) {
        CorreoDto dto = new CorreoDto();
        List<CorreoArchivoDto> files = new ArrayList<>();
        CorreoArchivoDto file;
        MsgFormatoNotificacion fn;
        CorreoUsuarios user;
        try {
            List<File> archivos = this.generarFirmaInscripciones(tramite, usuario);
            if (!archivos.isEmpty()) {
                user = sec.getMailDisponible();
                if (user != null) {
                    ConfiguracionDto configuracion = new ConfiguracionDto(user);
                    fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                            new Object[]{Constantes.correoFinTramiteInscripcion});
                    if (fn != null) {
                        if (correo != null && !correo.isEmpty()) {
                            String contenido = fn.getHeader() + tramite.toString() + fn.getFooter();
                            dto.setDestinatario(correo);
                            dto.setAsunto(fn.getAsunto());
                            dto.setMensaje(contenido);
                            dto.setNumeroTramite(tramite);
                            dto.setConfiguracion(configuracion);
                            dto.setUsuario(usuario);
                            for (File archivo : archivos) {
                                if (archivo != null) {
                                    file = new CorreoArchivoDto();
                                    file.setArchivoBase64("");
                                    file.setNombreArchivo(archivo.getAbsolutePath());
                                    file.setTipoArchivo("pdf");
                                    files.add(file);
                                }
                            }
                            if (!files.isEmpty()) {
                                dto.setArchivos(files);
                            }
                            mail.enviarCorreo(dto);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaEnviarCorreoCertificadoIndividual(RegpLiquidacion re, String usuario) {
        List<RegCertificado> certificados;
        List<File> files;
        File file;
        String correo = SisVars.correo;
        try {
            if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                correo = re.getCorreoTramite();
            } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                correo = re.getBeneficiario().getCorreo1();
            }
            certificados = em.findAll(Querys.getCertificados, new String[]{"tramite"}, new Object[]{re.getNumTramiteRp()});
            for (RegCertificado rc : certificados) {
                file = fd.firmarCertificadoPath(rc);
                Thread.sleep(2000);
                if (file != null) {
                    files = new ArrayList<>();
                    files.add(file);
                    this.enviarCorreoFinTramiteCertificado(re.getNumTramiteRp(), correo, files, usuario);
                }
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void firmarCertificadosEnviarCorreo(RegpLiquidacion re, String usuario) {
        List<File> files;
        String correo = SisVars.correo;
        try {
            if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                correo = re.getCorreoTramite();
            } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                correo = re.getBeneficiario().getCorreo1();
            }
            List<File> certificados = this.generarFirmaDigitalArchivos(re.getNumTramiteRp());
            if (!certificados.isEmpty()) {
                for (File file : certificados) {
                    Thread.sleep(2000);
                    files = new ArrayList<>();
                    files.add(file);
                    this.enviarCorreoFinTramiteCertificado(re.getNumTramiteRp(), correo, files, null);
                }
            }
        } catch (InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaEnviarCorreoDevolutiva(RegpNotaDevolutiva nd, RegpLiquidacion re, String usuario) {
        File file;
        String correo = SisVars.correo;
        try {
            if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                correo = re.getCorreoTramite();
            } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                correo = re.getBeneficiario().getCorreo1();
            }

            file = fd.firmarNotaDevolutiva(nd);
            Thread.sleep(2000);
            if (file != null) {
                this.enviarCorreoNotaDevolutiva(re.getNumTramiteRp(), correo, file.getAbsolutePath(), usuario);
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaEnviarCorreoNegativa(RegpNotaDevolutiva nd, RegpLiquidacion re, String usuario) {
        File file;
        String correo = SisVars.correo;
        try {
            if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                correo = re.getCorreoTramite();
            } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                correo = re.getBeneficiario().getCorreo1();
            }

            file = fd.firmarNegativaInscripcion(nd);
            Thread.sleep(2000);
            if (file != null) {
                this.enviarCorreoFinTramiteInscripcion(re.getNumTramiteRp(), correo, usuario, file);
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaEnviarCorreoDevolutivaCertificado(RegpNotaDevolutiva nd, RegpLiquidacion re, String usuario) {
        File file;
        String correo = SisVars.correo;
        try {
            if (re.getCorreoTramite() != null && !re.getCorreoTramite().isEmpty()) {
                correo = re.getCorreoTramite();
            } else if (re.getBeneficiario().getCorreo1() != null && !re.getBeneficiario().getCorreo1().isEmpty()) {
                correo = re.getBeneficiario().getCorreo1();
            }

            file = fd.firmarDevolutivaCertificado(nd);
            Thread.sleep(2000);
            if (file != null) {
                this.enviarCorreoNotaDevolutiva(re.getNumTramiteRp(), correo, file.getAbsolutePath(), usuario);
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void generarFirmaEnviarCorreoCertificadoOnline(RegCertificado rc, Long numTramite, String correo, String usuario) {
        List<File> files;
        File file;
        try {
            file = fd.firmarCertificadoOnline(rc);
            Thread.sleep(2000);
            if (file != null) {
                files = new ArrayList<>();
                files.add(file);
                this.enviarCorreoFinTramiteCertificado(numTramite, correo, files, usuario);
            }
        } catch (IOException | InterruptedException e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    @Asynchronous
    @Lock(LockType.READ)
    @AccessTimeout(-1)
    public void enviarCorreoFinTramiteInscripcion(Long tramite, String correo, String usuario, File archivo) {
        List<CorreoArchivoDto> files = new ArrayList<>();
        CorreoDto dto = new CorreoDto();
        MsgFormatoNotificacion fn;
        CorreoArchivoDto file;
        CorreoUsuarios user;
        try {
            if (archivo != null) {
                user = sec.getMailDisponible();
                if (user != null) {
                    ConfiguracionDto configuracion = new ConfiguracionDto(user);
                    fn = (MsgFormatoNotificacion) em.find(Querys.getMsgNotificacion, new String[]{"tipo"},
                            new Object[]{Constantes.correoFinTramiteInscripcion});
                    if (fn != null) {
                        if (correo != null && !correo.isEmpty()) {
                            String contenido = fn.getHeader() + tramite.toString() + fn.getFooter();
                            dto.setDestinatario(correo);
                            dto.setAsunto(fn.getAsunto());
                            dto.setMensaje(contenido);
                            dto.setNumeroTramite(tramite);
                            dto.setConfiguracion(configuracion);
                            dto.setUsuario(usuario);
                            file = new CorreoArchivoDto();
                            file.setArchivoBase64("");
                            file.setNombreArchivo(archivo.getAbsolutePath());
                            file.setTipoArchivo("pdf");
                            files.add(file);
                            dto.setArchivos(files);
                            mail.enviarCorreo(dto);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(AsynchronousEjb.class.getName()).log(Level.SEVERE, null, e);
        }
    }

}
