/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegpRespuestaJudicial;
import com.origami.sgr.entities.RegpTareasDinardap;
import com.origami.sgr.entities.RegpTareasTramite;
import javax.ejb.Local;import org.primefaces.model.file.UploadedFile;
;

/**
 *
 * @author Anyelo
 */
@Local
public interface DocumentsManagedLocal {
    
    public Boolean saveDocumentoHabilitante(UploadedFile up, HistoricoTramites ht, String user);
    
    public Boolean saveDocumentoTarea(UploadedFile up, RegpTareasTramite tt, Long user);
    
    public Boolean saveDocRespuestaJudicial(UploadedFile up, RegpRespuestaJudicial rdt);
    
    public Boolean saveDocsTareaDinardap(UploadedFile up, RegpTareasDinardap td, Long user);
    
    public Boolean saveDocumentoMovimiento(UploadedFile up, RegMovimiento mov, String user);
    
}
