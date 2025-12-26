package org.origami.ws.async;

import org.origami.ws.service.SincronizacionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

//@Component
public class TareaAutomatica {

    @Autowired
    private SincronizacionServices sincronizacionServices;


   // @Scheduled(cron = "0 0/1 * * * ?") //Cada 1 minuto
    public void actualizarDatosDepartamento() throws SQLException {
        sincronizacionServices.actualizarDatosDepartamento();
    }
    // @Scheduled(cron = "0 0/1 * * * ?") //Cada 1 minuto
    public void tramitesXcaducar() throws SQLException {
        sincronizacionServices.tramitesXcaducar();
    }


}
