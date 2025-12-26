/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.PubSolicitud;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.entities.RenCajero;
import com.origami.sgr.models.ActosRequisito;
import com.origami.sgr.models.Sms;
import java.io.File;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Anyelo
 */
@Local
public interface AsynchronousService {

    public void testSendEmail(String destinatario, Long tipo, String usuario);

    public void enviarCorreosRIDEbasico(List<RegpLiquidacion> liquidaciones, RenCajero cajero);

    public void enviarCorreosRIDE(List<RegpLiquidacion> liquidaciones, RenCajero cajero);

    public void enviarCorreoTramiteFinalizadoV1(RegpLiquidacion re);

    public void enviarCorreoProformaTramite(RegpLiquidacion re, String archivo, String usuario);

    public void sendNotificationFirebaseUserAndorid(CatEnte solicitante, Long numTramiteRp);

    public void enviarCorreoTramiteNotaDevolutiva(String email, Long tramite);

    public void generarFirmaDigital(Long numTramite);

    public void generarFirmaDigital(RegCertificado certificado);

    public List<File> generarFirmaDigitalArchivos(Long tramite);

    public void enviarCorreoTramiteFinalizado(RegpLiquidacion re, List<File> archivos);

    public void enviarCorreoProformaInscripcionSmsTramite(RegpLiquidacion re, Sms sms);

    public void enviarCorreoProformaInscripcionLinkTramite(RegpLiquidacion re, Sms sms);

    public void enviarCorreoSolicitudIncripcion(PubSolicitud solicitud, List<ActosRequisito> requisitos);

    public void enviarCorreoSolicitudIncripcionObservaciones(PubSolicitud solicitud);

    public void firmarDocumentosEnviarCorreo(RegpLiquidacion liquidacion, String usuario);

    public List<File> generarFirmaInscripciones(Long tramite, String usuario);

    public void generarFirmaActaInscripcion(Long tramite, String usuario);

    public void enviarCorreoCertificados(RegpLiquidacion re, List<File> archivos);

    public void enviarCorreoInscripcion(RegpLiquidacion re, List<File> archivos);

    public Boolean reenviarCorreoDocumentos(Long numTramite, String correo);

    public Boolean firmarCertificadoReenviarCorreo(RegCertificado ce, String usuario);

    public void enviarCorreoInicioTramite(RegpLiquidacion re, String usuario);

    public void enviarCorreoTituloCredito(RegpLiquidacion re, String archivo, String usuario);

    public void enviarCorreoNotaDevolutiva(Long idliquidacion, String archivo, String usuario);

    public void enviarCorreoNotaDevolutiva(Long numTramite, String correo, String archivo, String usuario);

    public void enviarCorreoFinTramiteCertificado(RegpLiquidacion re, List<String> archivos, String usuario);

    public void enviarCorreoFinTramiteInscripcion(RegpLiquidacion re, List<String> archivos, String usuario);

    public void reenviarCorreoDocumentos(Long numTramite, String correo, String usuario);

    public void enviarCorreoFinTramiteCertificado(Long tramite, String correo, List<File> archivos, String usuario);

    public void enviarCorreoFinTramiteInscripcion(Long tramite, String correo, List<File> archivos, String usuario);

    public void enviarCorreoFinTramiteInscripcion(Long tramite, String correo, String usuario);

    public void generarFirmaEnviarCorreoCertificadoIndividual(RegpLiquidacion re, String usuario);

    public void firmarCertificadosEnviarCorreo(RegpLiquidacion liquidacion, String usuario);

    public void generarFirmaEnviarCorreoDevolutiva(RegpNotaDevolutiva nd, RegpLiquidacion re, String usuario);
    
    public void generarFirmaEnviarCorreoNegativa(RegpNotaDevolutiva nd, RegpLiquidacion re, String usuario);
    
    public void generarFirmaEnviarCorreoDevolutivaCertificado(RegpNotaDevolutiva nd, RegpLiquidacion re, String usuario);

    public void generarFirmaEnviarCorreoCertificadoOnline(RegCertificado rc, Long numTramite, String correo, String usuario);
    
    public void enviarCorreoFinTramiteInscripcion(Long tramite, String correo, String usuario, File archivo);
    
}
