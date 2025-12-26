/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.origami.archivos.async;

import org.origami.archivos.config.AppProps;
import org.origami.archivos.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author eduar
 */
@Component
@EnableScheduling
public class TareasAutomaticas {

    @Autowired
    private AppProps appProps;

    // DE LUNES AL VIERNES A LAS 11 PM
    @Scheduled(cron = "0 0 23 * * MON-FRI")
    public void taskDeleteFiles() {
        try {
            System.out.println("// task automatic ...");
            Utils.borrarDirectorio(appProps.getImagenes());
            Thread.sleep(5000);
            Utils.crearDirectorio(appProps.getImagenes());
            System.out.println("// end task ...");
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

}
