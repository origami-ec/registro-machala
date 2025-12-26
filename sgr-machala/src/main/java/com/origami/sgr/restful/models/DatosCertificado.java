package com.origami.sgr.restful.models;

import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.util.Constantes;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Anyelo
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DatosCertificado implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigInteger numCertificado;
    private Long numFichaRegistral;
    private Long fechaEmision;
    private Long numTramite;
    private Integer claseCertificado;
    private String urlValidacionOnline;
    private String tipoCertificado;
    private String solvencia;
    private String linderosRegistrales;
    private String parroquia;
    private String claveCatastral;
    private String codigoVerificacion;

    protected List<DatosPropietariosFicha> propietarios = new ArrayList<>();
    protected List<DatosMovimientosFicha> movimientos = new ArrayList<>();

    public DatosCertificado() {
    }

    public DatosCertificado(RegCertificado certificado) {
        try {
            numCertificado = certificado.getNumCertificado();
            if (certificado.getFicha() != null) {
                numFichaRegistral = certificado.getFicha().getNumFicha();
            }
            fechaEmision = certificado.getFechaEmision().getTime();
            numTramite = certificado.getNumTramite();
            //urlValidacionOnline = Constantes.urlValidacionCertificado + certificado.getCodVerificacion() + Constantes.complementoUrl;
            tipoCertificado = certificado.getClaseCertificado();
            codigoVerificacion = certificado.getCodVerificacion();
            //claseCertificado = certificado.getTipoDocumentoGenerado();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public BigInteger getNumCertificado() {
        return numCertificado;
    }

    public void setNumCertificado(BigInteger numCertificado) {
        this.numCertificado = numCertificado;
    }

    public Integer getClaseCertificado() {
        return claseCertificado;
    }

    public void setClaseCertificado(Integer claseCertificado) {
        this.claseCertificado = claseCertificado;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public void setTipoCertificado(String tipoCertificado) {
        this.tipoCertificado = tipoCertificado;
    }

    public Long getNumFichaRegistral() {
        return numFichaRegistral;
    }

    public void setNumFichaRegistral(Long numFichaRegistral) {
        this.numFichaRegistral = numFichaRegistral;
    }

    public Long getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Long fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getUrlValidacionOnline() {
        return urlValidacionOnline;
    }

    public void setUrlValidacionOnline(String urlValidacionOnline) {
        this.urlValidacionOnline = urlValidacionOnline;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getSolvencia() {
        return solvencia;
    }

    public void setSolvencia(String solvencia) {
        this.solvencia = solvencia;
    }

    public String getLinderosRegistrales() {
        return linderosRegistrales;
    }

    public void setLinderosRegistrales(String linderosRegistrales) {
        this.linderosRegistrales = linderosRegistrales;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(String codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public List<DatosPropietariosFicha> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<DatosPropietariosFicha> propietarios) {
        this.propietarios = propietarios;
    }

    public List<DatosMovimientosFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<DatosMovimientosFicha> movimientos) {
        this.movimientos = movimientos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DatosCertificado{");
        sb.append("numCertificado=").append(numCertificado);
        sb.append(", numFichaRegistral=").append(numFichaRegistral);
        sb.append(", fechaEmision=").append(fechaEmision);
        sb.append(", numTramite=").append(numTramite);
        sb.append(", urlValidacionOnline=").append(urlValidacionOnline);
        sb.append(", tipoCertificado=").append(tipoCertificado);
        sb.append(", claseCertificado=").append(claseCertificado);
        sb.append(", solvencia=").append(solvencia);
        sb.append(", linderosRegistrales=").append(linderosRegistrales);
        sb.append(", parroquia=").append(parroquia);
        sb.append(", claveCatastral=").append(claveCatastral);
        sb.append(", codigoVerificacion=").append(codigoVerificacion);
        sb.append(", propietarios=").append(propietarios);
        sb.append(", movimientos=").append(movimientos);
        sb.append('}');
        return sb.toString();
    }

}
