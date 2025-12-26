/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.FirmaElectronica;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegpNotaDevolutiva;
import com.origami.sgr.models.DocumentoElectronico;
import com.origami.sgr.models.FirmaElectronicaModel;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author asilva
 */
@Local
public interface FirmaDigitalLocal {

    public Boolean firmarCertificado(RegCertificado ce) throws IOException;

    public String tareaFirmaCertificado(String filePdf) throws IOException;

    public String tareaFirmaCertificado(String filePdf, Long idCertificado) throws IOException;

    public File firmarCertificadoPath(RegCertificado ce) throws IOException;

    public File firmarRazonInscripcion(RegMovimiento mo, String usuario) throws IOException;
    
    public File firmarRazonInscripcion(List<RegMovimiento> mo, String usuario) throws IOException;

    public List<File> descargarCertificados(Long tramite);

    public List<File> descargarInscripciones(Long tramite);
    
    public File firmarActaInscripcion(RegMovimiento mo, String usuario) throws IOException;
    
    public File firmarNotaDevolutiva(RegpNotaDevolutiva dev) throws IOException;
    
    public File firmarNegativaInscripcion(RegpNotaDevolutiva dev) throws IOException;
    
    public File descargarDevolutiva(RegpNotaDevolutiva dev);
    
    public DocumentoElectronico verificarDocumentoElectronico(FirmaElectronicaModel firmaElectronica) throws IOException;

    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronicaModel firmaElectronica) throws IOException;

    public FirmaElectronicaModel validarFirmaElectronica(FirmaElectronica firmaElectronica, String clave);

    public File firmarCertificadoOnline(RegCertificado ce) throws IOException;
    
    public File firmarDevolutivaCertificado(RegpNotaDevolutiva dev) throws IOException;
    
}
