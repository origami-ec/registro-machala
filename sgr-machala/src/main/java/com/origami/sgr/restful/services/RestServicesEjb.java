/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful.services;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.HistoricoTramites;
import com.origami.sgr.entities.RegCertificado;
import com.origami.sgr.entities.RegCertificadoMovimiento;
import com.origami.sgr.entities.RegCertificadoMovimientoIntervinientes;
import com.origami.sgr.entities.RegCertificadoPropietario;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegFichaPropietarios;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.entities.RegpDocsTarea;
import com.origami.sgr.entities.RegpLiquidacion;
import com.origami.sgr.entities.RegpLiquidacionDetalles;
import com.origami.sgr.entities.RegpTareasTramite;
import com.origami.sgr.restful.models.CatalogoErrorExterno;
import com.origami.sgr.restful.models.ConsultaFicha;
import com.origami.sgr.restful.models.DatosCertificado;
import com.origami.sgr.restful.models.DatosFicha;
import com.origami.sgr.restful.models.DatosIntervinientes;
import com.origami.sgr.restful.models.DatosMovimientosFicha;
import com.origami.sgr.restful.models.DatosProforma;
import com.origami.sgr.restful.models.DatosPropietariosFicha;
import com.origami.sgr.restful.models.DatosSolicitud;
import com.origami.sgr.restful.models.DetalleContratos;
import com.origami.sgr.restful.models.RespuestaWs;
import com.origami.sgr.services.ejbs.HibernateEjbInterceptor;
import com.origami.sgr.services.interfaces.BpmBaseEngine;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegCertificadoService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.SeqGenMan;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Constantes;
import com.origami.sgr.util.Utils;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 *
 * @author asilva
 */
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Stateless(name = "restservices")
@Interceptors(value = {HibernateEjbInterceptor.class})
public class RestServicesEjb implements RestServices {

    @EJB
    private Entitymanager services;
    @EJB
    protected Entitymanager em;
    @EJB
    protected BpmBaseEngine engine;
    @EJB
    protected RegCertificadoService rcs;
    @EJB
    protected SeqGenMan sec;
    @EJB
    protected RegistroPropiedadServices reg;

    @Override
    public AclUser getAclUserByID(Long id) {
        return (AclUser) services.find(Querys.getAclUserByID, new String[]{"id"}, new Object[]{id});
    }

    @Override
    public RegpLiquidacion getRegpLiquidacionByNumTramite(Long numTramite) {
        return (RegpLiquidacion) services.find(Querys.getRegpLiquidacionByNumTramite, new String[]{"numTramite"}, new Object[]{numTramite});
    }

    @Override
    public List<RegpLiquidacionDetalles> getRegpLiquidacionDetalles(Long id) {
        return (List<RegpLiquidacionDetalles>) services.findAll(Querys.getDetallesByLiquidacion, new String[]{"parametro"}, new Object[]{id});
    }

    @Override
    public RegCertificado getRegCertificadoByNumCertif(Long numCertif) {
        return (RegCertificado) services.find(Querys.getCertificadoByNumCertif, new String[]{"certificado"}, new Object[]{numCertif});
    }

    @Override
    public RegpDocsTarea getRegpDocsTareaByTareaTramite(Long tareaTramite) {
        return (RegpDocsTarea) services.find(Querys.getDocsTareasByTareaTramite, new String[]{"idTarea"}, new Object[]{tareaTramite});
    }

    @Override
    public RegFicha getRegFichaByNumFicha(Long numFicha) {
        return (RegFicha) services.find(Querys.getRegFichaNumFicha, new String[]{"numFicha"}, new Object[]{numFicha});
    }

    @Override
    public RegFicha getRegFichaByCodPredial(String codigo) {
        return (RegFicha) services.find(Querys.getRegFichaCodPredial, new String[]{"codigo"}, new Object[]{codigo});
    }

    @Override
    public List<RegMovimiento> getMovimientosByFicha(Long idFicha) {
        List<RegMovimiento> list;
        try {
            list = services.findAll(Querys.getMovimientosByIdFicha, new String[]{"idFicha"}, new Object[]{idFicha});
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return list;
    }

    @Override
    public List<RegEnteInterviniente> getPropietariosByFicha(Long id) {
        List<RegEnteInterviniente> props;
        try {
            props = services.findAll(Querys.getPropietariosByFichaId, new String[]{"ficha"}, new Object[]{id});
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return props;
    }

    @Override
    public DatosCertificado getDatosByCertificado(Long numero) {
        DatosCertificado datos = new DatosCertificado();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosPropietariosFicha dpf;
        DatosMovimientosFicha dmf;
        DatosIntervinientes dci;
        List<DatosPropietariosFicha> props = new ArrayList<>();
        List<DatosMovimientosFicha> movs = new ArrayList<>();
        List<DatosIntervinientes> intrvs;
        try {
            RegCertificado certificado = this.getRegCertificadoByNumCertif(numero);
            if (certificado != null) {
                datos.setNumCertificado(certificado.getNumCertificado());
                datos.setTipoCertificado(certificado.getClaseCertificado());
                if (certificado.getFicha() != null) {
                    datos.setNumFichaRegistral(certificado.getFicha().getNumFicha());
                }
                //  datos.setFechaEmision(sdf.format(certificado.getFechaEmision()));
                /*RegpDocsTarea docTar = this.getRegpDocsTareaByTareaTramite(certificado.getTareaTramite().getId());
                if (docTar != null) {
                    datos.setUrl(SisVars.urlPublica + "/OmegaDownDocs?code=" + docTar.getDoc() + "&name=" + docTar.getNombreArchivo()
                            + "&tipo=2&content=" + docTar.getContentType());
                }*/
                if (certificado.getCodVerificacion() != null) {
                    // datos.setUrl(Constantes.urlDownDocsVentanilla + certificado.getCodVerificacion());
                }
                if (certificado.getNumTramite() != null) {
                    datos.setNumTramite(certificado.getNumTramite());
                }
                if (certificado.getObservacion() != null) {
                    datos.setSolvencia(certificado.getObservacion());
                }
                if (certificado.getLinderosRegistrales() != null) {
                    datos.setLinderosRegistrales(certificado.getLinderosRegistrales());
                }
                if (certificado.getParroquia() != null) {
                    datos.setParroquia(certificado.getParroquia());
                }
                if (certificado.getClaveCatastral() != null) {
                    datos.setClaveCatastral(certificado.getClaveCatastral());
                }
                for (RegCertificadoPropietario cp : certificado.getRegCertificadoPropietarioCollection()) {
                    dpf = new DatosPropietariosFicha();
                    dpf.setCi(cp.getDocumento());
                    dpf.setNombre(cp.getNombres());
                    props.add(dpf);
                }
                datos.setPropietarios(props);
                for (RegCertificadoMovimiento cm : certificado.getRegCertificadoMovimientoCollection()) {
                    dmf = new DatosMovimientosFicha();
                    intrvs = new ArrayList<>();
                    dmf.setActo(cm.getActo());
                    dmf.setFechaInscripcion(sdf.format(cm.getFechaInscripcion()));
                    dmf.setInscripcion(cm.getInscripcion());
                    dmf.setLibro(cm.getLibro());
                    dmf.setObservacion(cm.getObservacion());
                    dmf.setRepertorio(cm.getRepertorio());
                    for (RegCertificadoMovimientoIntervinientes cmi : cm.getRegCertificadoMovimientoIntervinientesCollection()) {
                        dci = new DatosIntervinientes();
                        dci.setCi(cmi.getDocumento());
                        dci.setDomicilio(cmi.getDomicilio());
                        dci.setEstadocivil(cmi.getEstadoCivil());
                        dci.setNombre(cmi.getNombres());
                        dci.setPapel(cmi.getPapel());
                        intrvs.add(dci);
                    }
                    dmf.setIntervinientes(intrvs);
                    movs.add(dmf);
                }
                datos.setMovimientos(movs);
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return datos;
    }

    @Override
    public DatosFicha getDatosByNumFicha(Long numero) {
        DatosFicha datos = new DatosFicha();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosPropietariosFicha dpf;
        DatosMovimientosFicha dmf;
        DatosIntervinientes dfi;
        List<DatosPropietariosFicha> props = new ArrayList<>();
        List<DatosMovimientosFicha> movs = new ArrayList<>();
        List<DatosIntervinientes> intrvs;
        try {
            RegFicha ficha = this.getRegFichaByNumFicha(numero);
            if (ficha != null) {
                if (ficha.getClaveCatastral() != null) {
                    datos.setCodigoPredial(ficha.getClaveCatastral());
                }
                datos.setFechaApertura(sdf.format(ficha.getFechaApe()));
                datos.setLinderos(ficha.getLinderos());
                datos.setNumFicha(ficha.getNumFicha());
                if (ficha.getParroquia() != null) {
                    datos.setParroquia(ficha.getParroquia().getDescripcion());
                }
                datos.setTipoPredio(ficha.getTipoPredio());
                for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                    dpf = new DatosPropietariosFicha();
                    dpf.setCi(fp.getPropietario().getCedRuc());
                    dpf.setNombre(fp.getPropietario().getNombre());
                    props.add(dpf);
                }
                datos.setPropietarios(props);
                for (RegMovimientoFicha mf : ficha.getRegMovimientoFichaCollection()) {
                    dmf = new DatosMovimientosFicha();
                    intrvs = new ArrayList<>();
                    dmf.setActo(mf.getMovimiento().getActo().getNombre());
                    dmf.setFechaInscripcion(sdf.format(mf.getMovimiento().getFechaInscripcion()));
                    dmf.setInscripcion(mf.getMovimiento().getNumInscripcion());
                    dmf.setLibro(mf.getMovimiento().getLibro().getNombre());
                    if (mf.getMovimiento().getObservacion() != null) {
                        dmf.setObservacion(mf.getMovimiento().getObservacion());
                    }
                    dmf.setRepertorio(mf.getMovimiento().getNumRepertorio());
                    for (RegMovimientoCliente mc : mf.getMovimiento().getRegMovimientoClienteCollection()) {
                        dfi = new DatosIntervinientes();
                        dfi.setCi(mc.getEnteInterv().getCedRuc());
                        if (mc.getDomicilio() != null) {
                            dfi.setDomicilio(mc.getDomicilio().getNombre());
                        }
                        if (mc.getEstado() != null) {
                            dfi.setEstadocivil(mc.getEstado());
                        }
                        dfi.setNombre(mc.getEnteInterv().getNombre());
                        if (mc.getPapel() != null) {
                            dfi.setPapel(mc.getPapel().getNombre());
                        }
                        intrvs.add(dfi);
                    }
                    dmf.setIntervinientes(intrvs);
                    movs.add(dmf);
                }
                datos.setMovimientos(movs);
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return datos;
    }

    @Override
    public DatosFicha getDatosFichaByCodPredial(String codigo) {
        DatosFicha datos = new DatosFicha();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DatosPropietariosFicha dpf;
        DatosMovimientosFicha dmf;
        DatosIntervinientes dfi;
        List<DatosPropietariosFicha> props = new ArrayList<>();
        List<DatosMovimientosFicha> movs = new ArrayList<>();
        List<DatosIntervinientes> intrvs;
        try {
            RegFicha ficha = this.getRegFichaByCodPredial(codigo);
            if (ficha != null) {
                if (ficha.getClaveCatastral() != null) {
                    datos.setCodigoPredial(ficha.getClaveCatastral());
                }
                datos.setFechaApertura(sdf.format(ficha.getFechaApe()));
                datos.setLinderos(ficha.getLinderos());
                datos.setNumFicha(ficha.getNumFicha());
                if (ficha.getParroquia() != null) {
                    datos.setParroquia(ficha.getParroquia().getDescripcion());
                }
                datos.setTipoPredio(ficha.getTipoPredio());
                for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                    dpf = new DatosPropietariosFicha();
                    dpf.setCi(fp.getPropietario().getCedRuc());
                    dpf.setNombre(fp.getPropietario().getNombre());
                    props.add(dpf);
                }
                datos.setPropietarios(props);
                for (RegMovimientoFicha mf : ficha.getRegMovimientoFichaCollection()) {
                    dmf = new DatosMovimientosFicha();
                    intrvs = new ArrayList<>();
                    dmf.setActo(mf.getMovimiento().getActo().getNombre());
                    dmf.setFechaInscripcion(sdf.format(mf.getMovimiento().getFechaInscripcion()));
                    dmf.setInscripcion(mf.getMovimiento().getNumInscripcion());
                    dmf.setLibro(mf.getMovimiento().getLibro().getNombre());
                    if (mf.getMovimiento().getObservacion() != null) {
                        dmf.setObservacion(mf.getMovimiento().getObservacion());
                    }
                    dmf.setRepertorio(mf.getMovimiento().getNumRepertorio());
                    for (RegMovimientoCliente mc : mf.getMovimiento().getRegMovimientoClienteCollection()) {
                        dfi = new DatosIntervinientes();
                        dfi.setCi(mc.getEnteInterv().getCedRuc());
                        if (mc.getDomicilio() != null) {
                            dfi.setDomicilio(mc.getDomicilio().getNombre());
                        }
                        if (mc.getEstado() != null) {
                            dfi.setEstadocivil(mc.getEstado());
                        }
                        dfi.setNombre(mc.getEnteInterv().getNombre());
                        if (mc.getPapel() != null) {
                            dfi.setPapel(mc.getPapel().getNombre());
                        }
                        intrvs.add(dfi);
                    }
                    dmf.setIntervinientes(intrvs);
                    movs.add(dmf);
                }
                datos.setMovimientos(movs);
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
        return datos;
    }

    @Override
    public RespuestaWs findFichaByNumFicha(ConsultaFicha consulta) {
        DatosFicha datos = new DatosFicha();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (consulta.getFicha() != null) {
                RegFicha ficha = this.getRegFichaByNumFicha(consulta.getFicha());
                if (ficha != null) {
                    datos.setNumficha(BigInteger.valueOf(ficha.getNumFicha()));
                    datos.setTipopredio(ficha.getTipoPredioTemp());
                    datos.setFechaapertura(sdf.format(ficha.getFechaApe()));
                    datos.setLinderos(ficha.getLinderosUnificados());
                    datos.setDescripcion(ficha.getDescripcionBien());
                    datos.setAutomatico(Boolean.FALSE);
                    if (ficha.getFichaMatriz() != null) {
                        datos.setCodigopredio(ficha.getFichaMatriz());
                    }
                    if (ficha.getClaveCatastral() != null) {
                        datos.setCodigopredial(ficha.getClaveCatastral());
                    }
                    if (ficha.getParroquia() != null) {
                        datos.setParroquia(ficha.getParroquia().getDescripcion());
                    }
                    if (ficha.getBarrio() != null) {
                        datos.setCiudadela(ficha.getBarrio().getNombre());
                        //datos.setAutomatico(ficha.getBarrio().getAutomatico());
                        datos.setAutomatico(Boolean.FALSE);
                    }
                    return new RespuestaWs(CatalogoErrorExterno.E000.codigo(), CatalogoErrorExterno.E000.descripcion(), datos);
                } else {
                    return new RespuestaWs(CatalogoErrorExterno.E003.codigo(), CatalogoErrorExterno.E003.descripcion());
                }
            } else {
                return new RespuestaWs(CatalogoErrorExterno.E003.codigo(), CatalogoErrorExterno.E003.descripcion());
            }
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return new RespuestaWs(CatalogoErrorExterno.E001.codigo(), CatalogoErrorExterno.E001.descripcion());
        }
    }

    @Override
    public RegCertificado generarCertificadoOnline(DatosSolicitud solicitud) {
        List<RegCertificadoMovimiento> historia;
        List<RegCertificadoPropietario> propietarios;
        try {
            Map map = new HashMap();
            map.put("actual", Boolean.TRUE);
            RegRegistrador registrador = (RegRegistrador) em.findObjectByParameter(RegRegistrador.class, map);

            RegCertificado certificado = new RegCertificado();
            certificado.setNumTramite(solicitud.getNumTramite());
            certificado.setCodVerificacion(rcs.genCodigoVerif());
            certificado.setNombreSolicitante(solicitud.getNombreCompletoSolicitante());
            //certificado.setUsoDocumento(Constantes.USO_DOCUMENTO_POR_DEFECTO);
            certificado.setFechaCreacion(new Date());
            certificado.setFechaEmision(certificado.getFechaCreacion());
            certificado.setFechaVencimiento(Utils.sumarRestarDiasFecha(certificado.getFechaCreacion(),
                    Constantes.diasValidezCertificado));
            certificado.setNumCertificado(sec.getSecuenciaGeneral(Constantes.secuenciaCertificados));
            certificado.setTareaTramite(new RegpTareasTramite(solicitud.getIdTarea()));
            certificado.setUserCreador(new AclUser(-1L));
            certificado.setCertificadoImpreso(Boolean.FALSE);
            certificado.setRegistrador(registrador.getId());

            if (solicitud.getTipoCertificado() == 1) {
                certificado.setTipoCertificado(8L);
                certificado.setTipoDocumento("C08");
                certificado.setLinderosRegistrales(solicitud.getNombreCompletoPropietario());
                certificado.setBeneficiario(solicitud.getBeneficiarioCedula());
                certificado = (RegCertificado) em.merge(certificado);
            } else {
                map = new HashMap<>();
                map.put("numFicha", solicitud.getNumeroFicha());
                RegFicha ficha = (RegFicha) em.findObjectByParameter(RegFicha.class, map);

                certificado.setFicha(ficha);
                certificado.setTipoCertificado(9L);
                certificado.setTipoDocumento("C09");
                certificado.setDescripcionBien(ficha.getDescripcionBien());
                certificado.setLinderosRegistrales(reg.getLinderosStringFicha(ficha.getId()));
                certificado.setClaveCatastral(ficha.getClaveCatastral());

                propietarios = this.cargarPropietarios(ficha);
                if (propietarios != null && !propietarios.isEmpty()) {
                    certificado.setRegCertificadoPropietarioCollection(propietarios);
                }
                historia = this.cargarMovimientos(ficha);
                if (historia != null && !historia.isEmpty()) {
                    certificado.setRegCertificadoMovimientoCollection(historia);
                }
                certificado = reg.saveCertificadoFicha(certificado);
            }

            return certificado;
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<RegCertificadoPropietario> cargarPropietarios(RegFicha ficha) {
        try {
            RegCertificadoPropietario cp;
            List<RegCertificadoPropietario> propietarios = new ArrayList<>();
            for (RegFichaPropietarios fp : ficha.getRegFichaPropietariosCollection()) {
                cp = new RegCertificadoPropietario();
                cp.setPropietario(fp.getPropietario());
                cp.setDocumento(fp.getPropietario().getCedRuc());
                cp.setNombres(fp.getPropietario().getNombre());
                propietarios.add(cp);
            }
            return propietarios;
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    public List<RegCertificadoMovimiento> cargarMovimientos(RegFicha ficha) {
        try {
            RegCertificadoMovimiento cm;
            List<RegCertificadoMovimiento> historia = new ArrayList<>();
            List<RegMovimiento> movimientos = reg.getMovimientosByFicha(ficha.getId());
            for (RegMovimiento m : movimientos) {
                cm = new RegCertificadoMovimiento();
                cm.setMovimiento(m);
                historia.add(cm);
            }
            return historia;
        } catch (Exception e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }
    }

    @Override
    public DatosProforma findTramiteValidationCode(String validationCode) {
        Map map = new HashMap<String, Object>();
        map.put("codVerificacion", validationCode);
        try {
            DatosProforma modelo = new DatosProforma();
            List<DetalleContratos> lista;
            RegCertificado certificado = (RegCertificado) em.findObjectByParameter(RegCertificado.class, map);
            if (certificado == null) {
                RegMovimiento movimiento = (RegMovimiento) em.findObjectByParameter(RegMovimiento.class, map);
                if (movimiento == null) {
                    return null;
                } else {
                    HistoricoTramites ht = movimiento.getTramite().getTramite();
                    RegpLiquidacion li = movimiento.getTramite().getDetalle().getLiquidacion();
                    modelo.setNumerotramite(ht.getNumTramite());
                    modelo.setRepertorio(movimiento.getNumRepertorio());
                    modelo.setDoc_solicitante(ht.getSolicitante().getCiRuc());
                    modelo.setNombre_solicitante(ht.getSolicitante().getNombreCompleto());
                    modelo.setCorreo_solicitante(ht.getSolicitante().getCorreo1());
                    modelo.setDoc_beneficiario(li.getBeneficiario().getCiRuc());
                    modelo.setNombre_beneficiario(li.getBeneficiario().getNombreCompleto());
                    modelo.setCorreo_beneficiario(li.getBeneficiario().getCorreo1());
                    modelo.setNumerofactura(li.getCodigoComprobante());
                    modelo.setTotalPagar(li.getTotalPagar());
                    modelo.setDetalleSolicitud(li.getInfAdicionalProf());
                    modelo.setFechaingreso(ht.getFechaIngreso().getTime());
                    modelo.setFechaentrega(ht.getFechaEntrega().getTime());
                    modelo.setClaveacceso(li.getClaveAcceso());
                    String json = (String)em.getNativeQuery(Querys.getContratosJson, new Object[]{validationCode});
                    if (json == null || json.trim().isEmpty() || json.equals("null")) {
                        lista = new ArrayList<>();
                    } else {
                        Type listType = new TypeToken<List<DetalleContratos>>() {
                        }.getType();
                        lista = new GsonBuilder().create().fromJson(json, listType);
                    }
                    modelo.setContratos(lista);
                    modelo.setTipo_tramite("INSCRIPCION");
                    if (movimiento.getTramite().getTramite().getEntregado()) {
                        modelo.setEstadotramite("FINALIZADO");
                    } else {
                        modelo.setEstadotramite("EN PROCESO");
                    }
                    return modelo;
                }
            } else {
                RegpLiquidacion li = certificado.getTareaTramite().getDetalle().getLiquidacion();
                modelo.setNumerotramite(certificado.getNumTramite());
                modelo.setFechaingreso(certificado.getTareaTramite().getTramite().getFechaIngreso().getTime());
                modelo.setFechaentrega(certificado.getTareaTramite().getTramite().getFechaEntrega().getTime());
                modelo.setDoc_solicitante(li.getSolicitante().getCiRuc());
                modelo.setNombre_solicitante(li.getSolicitante().getNombreCompleto());
                modelo.setCorreo_solicitante(li.getSolicitante().getCorreo1());
                modelo.setDoc_beneficiario(li.getBeneficiario().getCiRuc());
                modelo.setNombre_beneficiario(li.getBeneficiario().getNombreCompleto());
                modelo.setCorreo_beneficiario(li.getBeneficiario().getCorreo1());
                modelo.setNumerofactura(li.getCodigoComprobante());
                modelo.setTotalPagar(li.getTotalPagar());
                modelo.setDetalleSolicitud(li.getInfAdicionalProf());
                modelo.setClaveacceso(li.getClaveAcceso());
                if (certificado.getTareaTramite().getTramite().getEntregado()) {
                    modelo.setEstadotramite("FINALIZADO");
                } else {
                    modelo.setEstadotramite("EN PROCESO");
                }
                modelo.setTipo_tramite("CERTIFICACION");
                modelo.setTipo_certificado(certificado.getClaseCertificado());
                if (certificado.getFicha() != null) {
                    modelo.setNumero_ficha(certificado.getFicha().getNumFicha());
                    modelo.setCodigo_catastral(certificado.getFicha().getClaveCatastral());
                    modelo.setTipo_predio(certificado.getFicha().getTipoPredio());
                    if (certificado.getFicha().getEstado().getId() == 122L) {
                        modelo.setCodigo_gravamen(2);
                        modelo.setEstado_gravamen("Este bien inmueble, de acuerdo a los movimientos registrales, SE ENCUENTRA INEXISTENTE.");
                    } else if (certificado.getCantidadGravamenes() == null || certificado.getCantidadGravamenes() < 1) {
                        modelo.setCodigo_gravamen(0);
                        modelo.setEstado_gravamen("Este bien inmueble, de acuerdo a los movimientos registrales, NO SOPORTA GRAVAMENES.");
                    } else if (certificado.getCantidadGravamenes() > 0) {
                        modelo.setCodigo_gravamen(1);
                        modelo.setEstado_gravamen("Este bien inmueble, de acuerdo a los movimientos registrales, SI SOPORTA GRAVAMENES.");
                    }
                    if (certificado.getRegCertificadoPropietarioCollection() != null) {
                        List<String> props = new ArrayList<>();
                        for (RegCertificadoPropietario rcp : certificado.getRegCertificadoPropietarioCollection()) {
                            props.add(rcp.getNombres());
                        }
                        modelo.setPropietarios(props);
                    }
                }
                if (certificado.getTipoDocumento().equalsIgnoreCase("C01")) { //CERTIFICADO DE NO POSEER BIENES
                    modelo.setEstado_gravamen("A NOMBRE DE: " + certificado.getLinderosRegistrales());
                }
                return modelo;
            }

        } catch (JsonSyntaxException e) {
            Logger.getLogger(RestServicesEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

}
