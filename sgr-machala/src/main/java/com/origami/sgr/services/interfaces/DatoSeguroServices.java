/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.interfaces;

import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.models.DatoSeguro;
import java.net.URLConnection;
import javax.ejb.Local;

/**
 *
 * @author CarlosLoorVargas
 */
@Local
public interface DatoSeguroServices {
    
    public DatoSeguro getDatos(String cedula, boolean empresa, Integer intentos);

    public URLConnection configureConnection(URLConnection con);
    
    public CatEnte getEnteFromDatoSeguro(DatoSeguro data);

    public CatEnte llenarEnte(DatoSeguro data, CatEnte ente, Boolean cabiarCiRuc);
    
}
