/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.RegCertificado;
import java.sql.SQLException;
import javax.ejb.Local;

/**
 *
 * @author eduar
 */
@Local
public interface RegCertificadoService {
    
    public RegCertificado find(Long id);
    
    public String genCodigoVerif();
    
    public String genCertificadoPdf(Long id) throws SQLException;
    
    public void countPrint(RegCertificado ce);
    
    public String genCertificadoValidatePdf(Long id) throws SQLException;
    
    public RegCertificado findByValidationCode(String validationCode);
    
    public String generatePrintCertUrl(Long numTramite);
    
    public RegCertificado getUniqueByNumTramite(Long num);
    
    public Long findByValidationCode(String validationCode, Integer tipo);
    
    public String findByValidationCodeDetalle(String validationCode, Integer tipo);
    
    public String findByValidationCode2(String validationCode);
    
}
