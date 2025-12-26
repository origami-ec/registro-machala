/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.session;

import org.bcbg.models.DatosReporte;
import org.bcbg.util.EntityBeanCopy;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author origami-idea
 */
@Named
@SessionScoped
public class ServletSession implements Serializable {

    private List<Map> reportes;
    private Map parametros = null;
    private Boolean tieneDatasource;
    private String nombreReporte;
    private String nombreDocumento;
    private String nombreSubCarpeta;
    private String contentType;
    private byte[] reportePDF;
    private List dataSource;
    private Object data;
    private Boolean agregarReporte = Boolean.FALSE;
    private Boolean fondoBlanco = Boolean.TRUE;
    private Boolean onePagePerSheet = Boolean.FALSE;
    private Boolean imprimir = Boolean.FALSE;
    private Boolean imprimeRuta = Boolean.FALSE;
    private Object tempData;

    private String urlWebService;
    private DatosReporte datos;

    public Boolean getImprimeRuta() {
        return imprimeRuta;
    }

    public void setImprimeRuta(Boolean imprimeRuta) {
        this.imprimeRuta = imprimeRuta;
    }

    public void instanciarParametros() {
        parametros = new HashMap();
    }

    public void agregarParametro(String nombre, Object value) {
        parametros.put(nombre, value);
    }

    public boolean tieneParametro(Object parametro) {
        if (parametros == null) {
            return false;
        }
        return parametros.containsKey(parametro);
    }

    public List<Map> getReportes() {
        return reportes;
    }

    public void setReportes(List<Map> reportes) {
        this.reportes = reportes;
    }

    /**
     * En el <code>Map</code> debe esta incluido un parametro con
     * <code>nombreReporte</code> donde se va tomar el nombre del reporte que se
     * va agregar al final del primero.
     * <p>
     * Si el reporte seencuentra en la misma carpeta tomara en el nombre de la
     * variable <code>nombreSubCarpeta</code> para el caso que se encuentre en
     * otra carpeta se debe incluir otro parametro <code>nombreSubCarpeta</code>
     *
     * @param map Object
     */
    public void addParametrosReportes(Map map) {
        if (reportes == null) {
            reportes = new ArrayList<>();
        }
        reportes.add(map);
    }

    public boolean estaVacio() {
        if (parametros != null) {
            return parametros.isEmpty();
        } else {
            return true;
        }
    }

    public Object retornarValor(Object parametro) {
        return parametros.get(parametro);
    }

    public void borrarDatos() {
        parametros = null;
        tieneDatasource = null;
        nombreReporte = null;
        nombreDocumento = null;
        nombreSubCarpeta = null;
        contentType = null;
        reportePDF = null;
        agregarReporte = Boolean.FALSE;
        reportes = null;
        imprimeRuta = Boolean.FALSE;
        urlWebService = null;
        datos = null;
        dataSource = null;
        data = null;
    }

    public void borrarParametros() {
        parametros = null;
        reportes = null;
        tieneDatasource = null;
    }

    public Boolean validarCantidadDeParametrosDelServlet() {
        return parametros != null && parametros.size() > 0;
    }

    public Map getParametros() {
        return parametros;
    }

    public void setParametros(Map parametros) {
        this.parametros = parametros;
    }

    public Boolean getTieneDatasource() {
        return tieneDatasource;
    }

    public void setTieneDatasource(Boolean tieneDatasource) {
        this.tieneDatasource = tieneDatasource;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }

    public String getNombreSubCarpeta() {
        return nombreSubCarpeta;
    }

    public void setNombreSubCarpeta(String nombreSubCarpeta) {
        this.nombreSubCarpeta = nombreSubCarpeta;
    }

    public byte[] getReportePDF() {
        return reportePDF;
    }

    public void setReportePDF(byte[] reportePDF) {
        this.reportePDF = reportePDF;
    }

    public List getDataSource() {
        return dataSource;
    }

    public void setDataSource(List dataSource) {
        this.dataSource = dataSource;
    }

    public Boolean getAgregarReporte() {
        return agregarReporte;
    }

    public void setAgregarReporte(Boolean agregarReporte) {
        this.agregarReporte = agregarReporte;
    }

    public Boolean getFondoBlanco() {
        return fondoBlanco;
    }

    public void setFondoBlanco(Boolean fondoBlanco) {
        this.fondoBlanco = fondoBlanco;
    }

    public Boolean getOnePagePerSheet() {
        return onePagePerSheet;
    }

    public void setOnePagePerSheet(Boolean onePagePerSheet) {
        this.onePagePerSheet = onePagePerSheet;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public Object getTempData() {
        return tempData;
    }

    public void setTempData(Object tempData) {
        try {
            Object newInstance = tempData.getClass().newInstance();
            EntityBeanCopy.cloneClass(tempData, newInstance);
            this.tempData = tempData;
        } catch (Exception ex) {
            Logger.getLogger(ServletSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getUrlWebService() {
        return urlWebService;
    }

    public void setUrlWebService(String urlWebService) {
        this.urlWebService = urlWebService;
    }

    public DatosReporte getDatos() {
        return datos;
    }

    public void setDatos(DatosReporte datos) {
        this.datos = datos;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
