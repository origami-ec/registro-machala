/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.auto;

import com.origami.config.SisVars;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.IngresoTramiteLocal;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.Querys;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Administrator
 */
@Named
@Stateless
@javax.enterprise.context.Dependent
public class TareasAutomaticas {

    @Inject
    private SeqGenMan sec;
    @Inject
    private Entitymanager em;
    @Inject
    private IngresoTramiteLocal itl;

    /**
     * A LAS 23:30 SE BORRAN LOS ARCHIVOS TEMPORALES QUE GENERA EL SISTEMA
     */
    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "23", dayOfMonth = "*", year = "*", minute = "30", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTaskDeleteTempFiles() {
        try {
            FileUtils.deleteDirectory(new File(SisVars.rutaTemporales));
            File temp = new File(SisVars.rutaTemporales);
            if (temp.mkdir()) {
                System.out.println("// TAREA AUTOMATICA: BORRAR ARCHIVOS DE LA CARPETA TEMPORAL.");
            }
            FileUtils.deleteDirectory(new File(SisVars.rutaFirmados));
            temp = new File(SisVars.rutaFirmados);
            if (temp.mkdir()) {
                System.out.println("// TAREA AUTOMATICA: BORRAR ARCHIVOS TEMPORALES CON FIRMA ELECTRONICA.");
            }
            FileUtils.deleteDirectory(new File(SisVars.rutaAnexos));
            temp = new File(SisVars.rutaAnexos);
            if (temp.mkdir()) {
                System.out.println("// TAREA AUTOMATICA: BORRAR ARCHIVOS TEMPORALES DE LOS ANEXOS GENERADOS.");
            }
        } catch (IOException e) {
            System.out.println("// ERROR AL BORRAR LOS ARCHIVOS TEMPORALES...");
            System.out.println(e);
        }
    }

    /**
     * A LAS 01:00:00 SE APERTURA LA ASIGNACION DIARIA
     */
    @Schedule(dayOfWeek = "*", month = "*", hour = "1", dayOfMonth = "*", year = "*", minute = "0", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void timerTaskAsignacionDiaria() {
        try {
            sec.encerarTareasUsuarios(new Date());
            System.out.println("// TAREA AUTOMATICA: APERTURA DE ASIGNACION DIARIA.");
        } catch (Exception e) {
            System.out.println("// ERROR AL EJECUTAR EL METODO sec.encerarTareasUsuarios(fecha)");
            System.out.println(e);
        }
    }

    /**
     * A LAS 18:00:00 SE GENERAN FACTURAS DEL DIA
     */
    @Schedule(dayOfWeek = "*", month = "*", hour = "18", dayOfMonth = "*", year = "*", minute = "0", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void generarFacturas() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<RegpLiquidacion> tramites;
        Boolean flag;
        try {
            tramites = em.findAll(Querys.getLiquidacionesNoFacturadas, new String[]{"fecha"}, new Object[]{sdf.format(new Date())});
            if (!tramites.isEmpty()) {
                for (RegpLiquidacion liq : tramites) {
                    flag = itl.registrarLiquidacionERP(liq);
                    if (!flag) {
                        liq.setEstadoWs("ERROR EMISION");
                        em.update(liq);
                    }
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR AL GENERAR FACTURAS");
            System.out.println(e);
        }
    }

    /**
     * A LAS 20:00:00 AUTORIZAR FACTURAS DEL DIA
     */
    @Schedule(dayOfWeek = "*", month = "*", hour = "20", dayOfMonth = "*", year = "*", minute = "0", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void autorizarFacturas() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        List<RegpLiquidacion> tramites;
        Boolean flag;
        try {
            tramites = em.findAll(Querys.getLiquidacionesNoAutorizadas, new String[]{"fecha"}, new Object[]{sdf.format(new Date())});
            if (!tramites.isEmpty()) {
                for (RegpLiquidacion liq : tramites) {
                    flag = itl.emitirFacturaErp(liq);
                    if (!flag) {
                        liq.setEstadoWs("ERROR AUTORIZACION");
                        em.update(liq);
                    }
                    Thread.sleep(2000);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("ERROR AL AUTORIZAR FACTURAS");
            System.out.println(e);
        }
    }

}
