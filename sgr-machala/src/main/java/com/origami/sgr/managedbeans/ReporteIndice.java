/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.entities.IndiceCertificacion;
import com.origami.sgr.entities.IndiceVentanilla;
import com.origami.sgr.entities.IndiceInscripcion;
import com.origami.sgr.entities.IndiceRevision;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sql.ConsultasSqlLocal;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Usuario
 */
@Named
@ViewScoped
public class ReporteIndice implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReporteIndice.class.getName());

    @Inject
    private ServletSession ss;

    @Inject
    private UserSession us;

    @Inject
    private ConsultasSqlLocal sql;

    @Inject
    protected Entitymanager em;

    protected SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

    protected Map map;
    private Date mesAnio;
    protected RegRegistrador registrador;
    protected Integer tipoindice = 0, subTipoindice = 0, subTipoIndV = 0;
    protected long totalValorIns = 0, totalValorCertif = 0, totalCeroIns = 0, totalCeroCertif = 0;
    String mesAnioTexto = "";
    protected List<IndiceInscripcion> inscripciones;
    protected long totalAsignados = 0, totalTiempo = 0, totalRetraso = 0, totalReasigMas = 0, totalReasigMenos = 0,
            totalPendientes = 0, totalPendienteAnterior = 0;
    
    protected long totalReasignadosRev = 0, totalTransferidos = 0, totalDevolutivas = 0, totalNegativas = 0, totalHabilitados = 0, totalPendientesRev = 0;

    protected double totalPorcent = 0;
    protected long totalReasignadosIns = 0;

    protected List<IndiceVentanilla> ventanilla;
    protected List<IndiceCertificacion> certificacion;
    protected List<IndiceRevision> revision;

    private boolean mostrarResultado = false;

    @PostConstruct
    protected void iniView() {
        // Instanciar la fecha
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        mesAnio = cal.getTime();

        map = new HashMap();
        map.put("actual", Boolean.TRUE);
        registrador = (RegRegistrador) em.findObjectByParameter(RegRegistrador.class, map);

        iniciarValores();
    }

    public void generar() {
        try {
            iniciarValores();
            mostrarResultado = true;
            SimpleDateFormat formatoMesAnio = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("es", "ES"));
            mesAnioTexto = formatoMesAnio.format(mesAnio);

            switch (tipoindice) {
                case 0:
                    iniciarValores();
                    JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                    break;
                case 1: // INDICE DE VENTANILLA
                    generarSubtipoVentanilla();
                    break;
                case 2: // INDICE DE CERTIFICACIONES
                    generarSubIndCertif();
                    break;
                case 3: // INDICE DE REVISIÓN
                    revision = sql.getIndiceRevision(sdf.format(mesAnio));
                    getTotalIndicesRevision();
                    break;
                case 4: // INDICE DE INSCRIPCIÓN
                    inscripciones = sql.getIndiceInscripcion(sdf.format(mesAnio));
                    getTotalIndicesInscripcion();
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void generarSubtipoVentanilla() {
        try {
            switch (subTipoIndV) {
                case 0:
                    iniciarValores();
                    JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                    break;
                case 1: // INDICE DE VENTANILLA
                    ventanilla = sql.getIndicesVentanilla(sdf.format(mesAnio));
                    getTotalIndicesVentanilla();
                    break;
                case 2: // INDICE DE VENTANILLA INGRESADAS
                    ventanilla = sql.getIndicesVentanillaIngreso(sdf.format(mesAnio));
                    getTotalIndicesVentanilla();
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void imprimirReportes() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(Boolean.FALSE);
            ss.agregarParametro("USUARIO", us.getName_user());
            switch (tipoindice) {
                case 1: // VENTANILLA
                    generarReportSubIndVentanilla();
                    break;
                case 2: // CERTIFICACIONES
                    generarReportSubIndCertif();
                    break;
                case 3: // REVISIÓN
                    ss.setDataSource(revision);
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                    ss.agregarParametro("MES_INDICE", mesAnioTexto);
                    ss.setNombreSubCarpeta("archivos");
                    ss.setNombreReporte("IndiceRevision");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                case 4: // INSCRIPCIÓN
                    ss.setDataSource(inscripciones);
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                    ss.agregarParametro("MES_INDICE", mesAnioTexto);
                    ss.setNombreSubCarpeta("archivos");
                    ss.setNombreReporte("IndiceInscripcion");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                default:
                    JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
            }

        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void generarReportSubIndVentanilla() {
        try {
            switch (subTipoIndV) {
                case 0:
                    iniciarValores();
                    JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                    break;
                case 1: // TRAMITES CREADOS
                    ss.setDataSource(ventanilla);
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                    ss.agregarParametro("MES_INDICE", mesAnioTexto);
                    ss.setNombreSubCarpeta("archivos");
                    ss.setNombreReporte("IndicesVentanilla");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                case 2: // TRAMITES INGRESADOS
                    ss.setDataSource(ventanilla);
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                    ss.agregarParametro("MES_INDICE", mesAnioTexto);
                    ss.setNombreSubCarpeta("archivos");
                    ss.setNombreReporte("IndicesVentanillaIngreso");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void generarReportSubIndCertif() {
        try {
            switch (subTipoindice) {
                case 0:
                    iniciarValores();
                    JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                    break;
                case 1: // CERTIFICADOS EMITIDOS
                    ss.setDataSource(certificacion);
                    ss.agregarParametro("IMG_URL", JsfUti.getRealPath("/resources/image/"));
                    ss.agregarParametro("MES_INDICE", mesAnioTexto);
                    ss.setNombreSubCarpeta("archivos");
                    ss.setNombreReporte("IndicesCertificados");
                    JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
                    break;
                case 2:

                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void generarSubIndCertif() {
        try {
            switch (subTipoindice) {
                case 0:
                    iniciarValores();
                    JsfUti.messageWarning(null, "Seleccione el tipo de reporte.", "");
                    break;
                case 1: // CERTIFICADOS EMITIDOS
                    certificacion = sql.getIndiceCertificacion(sdf.format(mesAnio));
                    getTotalIndicesCertificacion();
                    break;
                case 2: // RECTIFICACIONES
                    break;
                case 3: // CONTROL DE CALIDAD
                    break;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            JsfUti.messageError(null, "ERROR DE APLICACION.", "");
        }
    }

    public void getTotalIndicesVentanilla() {
        totalValorIns = 0;
        totalValorCertif = 0;
        totalCeroIns = 0;
        totalCeroCertif = 0;
        if (ventanilla != null) {
            for (IndiceVentanilla i : ventanilla) {
                if (i.getInscripcionSinValor() != null) {
                    totalCeroIns += i.getInscripcionSinValor().longValue();
                }
                if (i.getInscripcionConValor() != null) {
                    totalValorIns += i.getInscripcionConValor().longValue();
                }
                if (i.getCertificadoSinValor() != null) {
                    totalCeroCertif += i.getCertificadoSinValor().longValue();
                }
                if (i.getCertificadoConValor() != null) {
                    totalValorCertif += i.getCertificadoConValor().longValue();
                }
            }
        }
    }

    public void getTotalIndicesInscripcion() {
        totalAsignados = 0;
        totalTiempo = 0;
        totalRetraso = 0;
        totalReasignadosIns = 0;
        totalTransferidos = 0;
        totalPendientes = 0;
        if (inscripciones != null) {
            for (IndiceInscripcion i : inscripciones) {
                if (i.getAsignados() != null) {
                    totalAsignados += i.getAsignados().longValue();
                }
                if (i.getEntregadosATiempo() != null) {
                    totalTiempo += i.getEntregadosATiempo().longValue();
                }
                if (i.getEntregadosConRetraso() != null) {
                    totalRetraso += i.getEntregadosConRetraso().longValue();
                }
                if 
                   (i.getReasignados() != null) 
                    totalReasignadosIns += i.getReasignados().longValue();
                if 
                   (i.getTransferidos() != null) 
                    totalTransferidos += i.getTransferidos().longValue();
                if 
                   (i.getPendientes() != null) 
                    totalPendientes += i.getPendientes().longValue();
            }
        }
    }

    public void getTotalIndicesCertificacion() {
        totalAsignados = 0;
        totalTiempo = 0;
        totalRetraso = 0;
        totalReasigMas = 0;
        totalReasigMenos = 0;
        totalPendientes = 0;
        totalPorcent = 0;

        if (certificacion != null) {

            for (IndiceCertificacion c : certificacion) {
                if (c.getAsignados() != null) {
                    totalAsignados += c.getAsignados().longValue();
                }
                if (c.getReasignadosQuitar() != null) {
                    totalReasigMenos += c.getReasignadosQuitar().longValue();
                }
                if (c.getReasignadosMas() != null) {
                    totalReasigMas += c.getReasignadosMas().longValue();
                }
                if (c.getEntregadosTiempo() != null) {
                    totalTiempo += c.getEntregadosTiempo().longValue();
                }
                if (c.getEntregadosAtras() != null) {
                    totalRetraso += c.getEntregadosAtras().longValue();
                }
                if (c.getPendientes() != null) {
                    totalPendientes += c.getPendientes().longValue();
                }
            }

            totalPorcent
                    = ((double) (totalTiempo + totalRetraso)
                    / (totalAsignados - totalReasigMenos + totalReasigMas)) * 100.0;

            totalPorcent = BigDecimal
                    .valueOf(totalPorcent)
                    .setScale(2, RoundingMode.DOWN)
                    .doubleValue();

        }

    }
    
    public void getTotalIndicesRevision() {
        // Reiniciamos totales
        totalAsignados = 0;
        totalTiempo = 0;
        totalRetraso = 0;
        totalReasignadosRev = 0;
        totalTransferidos = 0;
        totalDevolutivas = 0;
        totalNegativas = 0;
        totalHabilitados = 0;
        totalPendientesRev = 0;
        totalPorcent = 0;

        if (revision != null) {
            for (IndiceRevision r : revision) {
                if (r.getAsignados() != null) totalAsignados += r.getAsignados().longValue();
                if (r.getRevisadosATiempo() != null) totalTiempo += r.getRevisadosATiempo().longValue();
                if (r.getRevisadosFueraTiempo() != null) totalRetraso += r.getRevisadosFueraTiempo().longValue();
                if (r.getReasignados() != null) totalReasignadosRev += r.getReasignados().longValue();
                if (r.getTransferidos() != null) totalTransferidos += r.getTransferidos().longValue();
                if (r.getDevolutivas() != null) totalDevolutivas += r.getDevolutivas().longValue();
                if (r.getNegativas() != null) totalNegativas += r.getNegativas().longValue();
                if (r.getHabilitados() != null) totalHabilitados += r.getHabilitados().longValue();
                if (r.getPendientes() != null) totalPendientesRev += r.getPendientes().longValue();
            }

           
            /*if (totalAsignados > 0) {
                 totalPorcent = ((double) (totalTiempo + totalRetraso) / totalAsignados) * 100.0;
                 totalPorcent = BigDecimal.valueOf(totalPorcent)
                                          .setScale(2, RoundingMode.DOWN)
                                          .doubleValue();
            }*/
        }
    }

    private void iniciarValores() {
        totalValorIns = 0;
        totalValorCertif = 0;
        totalCeroIns = 0;
        totalCeroCertif = 0;
        ventanilla = new ArrayList<>();

        totalAsignados = 0;
        totalTiempo = 0;
        totalRetraso = 0;
        
        inscripciones = new ArrayList<>();
        totalReasignadosIns = 0;
        
        revision = new ArrayList<>();
        totalReasignadosRev = 0; 
        totalTransferidos = 0; 
        totalDevolutivas = 0; 
        totalNegativas = 0; 
        totalHabilitados = 0;
        totalPendientesRev = 0;
        
        totalPendientes = 0;
    }

    public void onChangeOpcion() {
        mostrarResultado = false;
        iniciarValores();
    }

    // GETTERS Y SETTERS
    public Integer getTipoindice() {
        return tipoindice;
    }

    public void setTipoindice(Integer tipoindice) {
        this.tipoindice = tipoindice;
    }

    public Integer getSubTipoindice() {
        return subTipoindice;
    }

    public void setSubTipoindice(Integer subTipoindice) {
        this.subTipoindice = subTipoindice;
    }

    public Date getMesAnio() {
        return mesAnio;
    }

    public void setMesAnio(Date mesAnio) {
        this.mesAnio = mesAnio;
    }

    public boolean isMostrarResultado() {
        return mostrarResultado;
    }

    public void setMostrarResultado(boolean mostrarResultado) {
        this.mostrarResultado = mostrarResultado;
    }

    public List<IndiceVentanilla> getVentanilla() {
        return ventanilla;
    }

    public void setVentanilla(List<IndiceVentanilla> ventanilla) {
        this.ventanilla = ventanilla;
    }

    public long getTotalValorIns() {
        return totalValorIns;
    }

    public void setTotalValorIns(long totalValorIns) {
        this.totalValorIns = totalValorIns;
    }

    public long getTotalValorCertif() {
        return totalValorCertif;
    }

    public void setTotalValorCertif(long totalValorCertif) {
        this.totalValorCertif = totalValorCertif;
    }

    public long getTotalCeroIns() {
        return totalCeroIns;
    }

    public void setTotalCeroIns(long totalCeroIns) {
        this.totalCeroIns = totalCeroIns;
    }

    public long getTotalCeroCertif() {
        return totalCeroCertif;
    }

    public void setTotalCeroCertif(long totalCeroCertif) {
        this.totalCeroCertif = totalCeroCertif;
    }

    public List<IndiceInscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<IndiceInscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }

    public long getTotalAsignados() {
        return totalAsignados;
    }

    public void setTotalAsignados(long totalAsignados) {
        this.totalAsignados = totalAsignados;
    }

    public long getTotalTiempo() {
        return totalTiempo;
    }

    public void setTotalTiempo(long totalTiempo) {
        this.totalTiempo = totalTiempo;
    }

    public long getTotalRetraso() {
        return totalRetraso;
    }

    public void setTotalRetraso(long totalRetraso) {
        this.totalRetraso = totalRetraso;
    }

    public Integer getSubTipoIndV() {
        return subTipoIndV;
    }

    public void setSubTipoIndV(Integer subTipoIndV) {
        this.subTipoIndV = subTipoIndV;
    }

    public long getTotalReasigMas() {
        return totalReasigMas;
    }

    public void setTotalReasigMas(long totalReasigMas) {
        this.totalReasigMas = totalReasigMas;
    }

    public long getTotalReasigMenos() {
        return totalReasigMenos;
    }

    public void setTotalReasigMenos(long totalReasigMenos) {
        this.totalReasigMenos = totalReasigMenos;
    }

    public long getTotalPendientes() {
        return totalPendientes;
    }

    public void setTotalPendientes(long pendientes) {
        this.totalPendientes = pendientes;
    }

    public long getTotalPendienteAnterior() {
        return totalPendienteAnterior;
    }

    public void setTotalPendienteAnterior(long pendienteAnterior) {
        this.totalPendienteAnterior = pendienteAnterior;
    }

    public List<IndiceCertificacion> getCertificacion() {
        return certificacion;
    }

    public void setCertificacion(List<IndiceCertificacion> certificar) {
        this.certificacion = certificar;
    }

    public double getTotalPorcent() {
        return totalPorcent;
    }

    public void setTotalPorcent(double totalPorcent) {
        this.totalPorcent = totalPorcent;
    }

    public List<IndiceRevision> getRevision() {
        return revision;
    }

    public void setRevision(List<IndiceRevision> revision) {
        this.revision = revision;
    }

    public long getTotalReasignadosRev() {
        return totalReasignadosRev;
    }

    public void setTotalReasignadosRev(long totalReasignadosRev) {
        this.totalReasignadosRev = totalReasignadosRev;
    }

    public long getTotalTransferidos() {
        return totalTransferidos;
    }

    public void setTotalTransferidos(long totalTransferidos) {
        this.totalTransferidos = totalTransferidos;
    }

    public long getTotalDevolutivas() {
        return totalDevolutivas;
    }

    public void setTotalDevolutivas(long totalDevolutivas) {
        this.totalDevolutivas = totalDevolutivas;
    }

    public long getTotalNegativas() {
        return totalNegativas;
    }

    public void setTotalNegativas(long totalNegativas) {
        this.totalNegativas = totalNegativas;
    }

    public long getTotalHabilitados() {
        return totalHabilitados;
    }

    public void setTotalHabilitados(long totalHabilitados) {
        this.totalHabilitados = totalHabilitados;
    }
    public long getTotalPendientesRev() { 
        return totalPendientesRev; 
    }
    
    public void setTotalPendientesRev(long totalPendientesRev) {
        this.totalPendientesRev = totalPendientesRev; 
    }
    
    public long getTotalReasignadosIns() {
        return totalReasignadosIns;
    }

    public void setTotalReasignadosIns(long totalReasignadosIns) {
        this.totalReasignadosIns = totalReasignadosIns;
    }
    
}
