/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.managedbeans;

import com.origami.config.SisVars;
import com.origami.sgr.entities.RegEnteInterviniente;
import com.origami.sgr.entities.RegFicha;
import com.origami.sgr.entities.RegRegistrador;
import com.origami.sgr.lazymodels.RegFichaLazy;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.util.JsfUti;
import com.origami.sgr.util.Messages;
import com.origami.sgr.util.Querys;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import static com.origami.sgr.bpm.managedbeans.BpmManageBeanBaseRoot.LOG;
import com.origami.sgr.entities.Barrios;
import com.origami.sgr.entities.CatParroquia;
import com.origami.sgr.entities.CodigosFicha;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegTipoFicha;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Anyelo
 */
@Named
@ViewScoped
public class FichasIngresadas implements Serializable {

    private static final Logger LOG = Logger.getLogger(FichasIngresadas.class.getName());

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;
    @Inject
    private Entitymanager em;
    @Inject
    protected RegistroPropiedadServices reg;

    protected Map map;
    protected RegRegistrador registrador;
    protected RegFichaLazy listadoFichas;
    protected RegFicha fichaSel = new RegFicha();
    protected List<RegMovimientoFicha> movimientos;
    protected Boolean permitido = false, admin = false;
    protected List<RegEnteInterviniente> propietarios = new ArrayList<>();
    protected List<CtlgItem> estadosInformacion = new ArrayList<>();
    protected RegTipoFicha tipoFicha;
    protected RegTipoFicha newTipoFicha;
    protected CatParroquia parroquia;
    protected List<CatParroquia> parroquias;

    protected Barrios ciudadela;
    protected CodigosFicha nuevoCodigo;
    protected List<CodigosFicha> codigosFicha;
    protected List<CtlgItem> tipoCodigosPredio;

    protected RegMovimiento movimiento;
    protected List<RegFicha> fichas = new ArrayList<>();
    protected List<RegMovimientoCliente> clientes = new ArrayList<>();
    protected List<RegMovimientoMarginacion> marginaciones = new ArrayList<>();

    @PostConstruct
    protected void iniView() {
        try {
            tipoFicha = em.find(RegTipoFicha.class, 1L);
            newTipoFicha = em.find(RegTipoFicha.class, 1L);
            listadoFichas = new RegFichaLazy(tipoFicha);
            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);
            map = new HashMap<>();
            map.put("catalogo", "ficha.estado_informacion");
            estadosInformacion = em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
            map = new HashMap<>();
            map.put("catalogo", "ficha.tipo_codigo_predio");
            tipoCodigosPredio = em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);
            this.validaRoles();
            parroquias = em.findAllEntCopy(Querys.getCatParroquiaAll);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        admin = us.getRoles().contains(1L);
        permitido = us.getRoles().contains(34L) || us.getRoles().contains(1L);
    }

    public void imprimirFichaRegistral(RegFicha ficha) {
        try {
            if (ficha.getEstado().getValor().equalsIgnoreCase("INACTIVO")) {
                JsfUti.messageError(null, "No se imprime Ficha Registral, estado de Ficha: INACTIVA.", "");
            } else {
                ss.instanciarParametros();
                ss.setTieneDatasource(true);
                ss.setNombreSubCarpeta("registro");
                if (ficha.getTipoFicha().getCodigo() < 1) {
                    ss.setNombreReporte("FichaRegistral");
                } else {
                    ss.setNombreReporte("FichaMercantil");
                }
                ss.agregarParametro("ID_FICHA", ficha.getId());
                ss.agregarParametro("SUBREPORT_DIR", JsfUti.getRealPath("/") + "reportes/registro/");
                ss.agregarParametro("USER_NAME", us.getName_user());
                ss.agregarParametro("WATERMARK_URL", JsfUti.getRealPath("/resources/image/formato_verificacion.png"));
                //ss.setEncuadernacion(Boolean.TRUE);
                JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgFichaSelect(RegFicha rf) {
        try {
            fichaSel = rf;
            movimientos = reg.getRegMovByIdFicha(rf.getId());
            propietarios = reg.getPropietariosByFicha(rf.getId());
            JsfUti.update("formFichaSelect");
            JsfUti.executeJS("PF('dlgFichaSelect').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void imprimirBitacora() {
        try {
            ss.instanciarParametros();
            ss.setTieneDatasource(true);
            ss.setNombreSubCarpeta("registro");
            ss.setNombreReporte("Bitacora");
            ss.agregarParametro("codMovimiento", null);
            //ss.agregarParametro("numFicha", fichaSel.getNumFicha());
            ss.agregarParametro("numFicha", fichaSel.getId());
            ss.agregarParametro("titulo", Messages.bitacoraFicha);
            JsfUti.redirectNewTab(SisVars.urlbase + "Documento");
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNewFicha() {
        JsfUti.update("formTipoFicha");
        JsfUti.executeJS("PF('dlgTipoFicha').show();");
    }

    public void redirectFichaNueva() {
        if (newTipoFicha != null) {
            ss.instanciarParametros();
            ss.agregarParametro("tipoFicha", newTipoFicha.getId());
            JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
        } else {
            JsfUti.messageWarning("Nueva Ficha", "Debe seleccionar el tipo ficha", "");
        }
    }

    public void redirectEditarFicha(RegFicha ficha) {
        ss.instanciarParametros();
        ss.agregarParametro("idFicha", ficha.getId());
        ss.agregarParametro("tipoFicha", ficha.getTipoFicha().getId());
        JsfUti.redirectFaces("/procesos/manage/fichaIngresoNuevo.xhtml");
    }

    public void redirectIndexarFicha(RegFicha ficha) {
        ss.instanciarParametros();
        ss.agregarParametro("idFicha", ficha.getId());
        ss.agregarParametro("tipoFicha", ficha.getTipoFicha().getId());
        JsfUti.redirectFaces("/documental/indexacion/indexacionFicha.xhtml");
    }

    public void habilitarEdicion() {
        try {
            if (permitido) {
                em.update(fichaSel);
                JsfUti.update("mainForm");
                JsfUti.executeJS("PF('dlgFichaSelect').hide();");
                JsfUti.messageInfo(null, "Ficha Registral habilitada para edicion.", "");
            } else {
                JsfUti.messageWarning(null, "Usuario no permitido.", "");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<RegTipoFicha> getTiposFichas() {
        return em.findAllEntCopy(Querys.getRegTipoFicha);
    }

    public void updateLazy() {
        listadoFichas = new RegFichaLazy(tipoFicha);
    }

    public void filterLazy() {
        try {
            listadoFichas = new RegFichaLazy();
            listadoFichas.addFilter("tipoFicha", tipoFicha);
            if (parroquia != null) {
                listadoFichas.addFilter("parroquia", parroquia);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgProcesoFichas(RegFicha ficha) {
        try {
            fichaSel = ficha;
            nuevoCodigo = new CodigosFicha();
            codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
            JsfUti.update("formProcesoFicha");
            JsfUti.executeJS("PF('dlgProcesoFichas').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCodigoFicha() {
        try {
            if (this.validarCampos()) {
                nuevoCodigo.setFicha(fichaSel);
                nuevoCodigo.setUsuarioCreacion(us.getName_user());
                nuevoCodigo.setFechaCreacion(new Date());
                em.persist(nuevoCodigo);
                codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
                nuevoCodigo = new CodigosFicha();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public Boolean validarCampos() {
        if (nuevoCodigo.getTipo() == null) {
            JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe ingresar el tipo de código disponible.");
            return false;
        }
        if (nuevoCodigo.getTipo().getCodename().equalsIgnoreCase("URBANISTICO")) {
            if (nuevoCodigo.getUrb() == null) {
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe seleccionar la ciudadela.");
                return false;
            }
            if (nuevoCodigo.getMz() == null || nuevoCodigo.getMz().isEmpty()) {
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe ingresar el campo manzana.");
                return false;
            }
            if (nuevoCodigo.getVilla() == null || nuevoCodigo.getVilla().isEmpty()) {
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe ingresar el campo villa.");
                return false;
            }
        } else {
            if (nuevoCodigo.getCodigo() == null || nuevoCodigo.getCodigo().isEmpty()) {
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe ingresar el campo código.");
                return false;
            }
        }
        return true;
    }
public void visualizaScann(RegMovimiento mov) {
        try {
            Calendar fecha = Calendar.getInstance();
            fecha.setTime(mov.getFechaInscripcion());
            Integer periodo = fecha.get(Calendar.YEAR);

            JsfUti.redirectNewTab(
                    SisVars.urlbase + "documental/visorPdf.xhtml?periodo=" + periodo
                    + "&libro=" + mov.getLibro().getNombreCarpeta()
                    + "&inscripcion=" + mov.getNumInscripcion()
                    + "&movimiento=" + mov.getId()
            );

        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }
    public void inactivarCodigo(CodigosFicha cf) {
        try {
            cf.setEstado(Boolean.FALSE);
            em.update(cf);
            codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }
    
    public void corregirCodigo(CodigosFicha cf) {
        try {
            nuevoCodigo = cf;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarUbicacion() {
        try {
            fichaSel.setInformacionFicha(new CtlgItem(18L));
            em.update(fichaSel);
            JsfUti.messageInfo(null, "Datos actualizados", "Campo ubicación actualizado con éxito.");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarLazy() {
        try {
            listadoFichas = new RegFichaLazy(tipoFicha);
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgProcesoFichas').hide();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgNuevaCiudadela() {
        try {
            ciudadela = new Barrios();
            JsfUti.update("formCiudadela");
            JsfUti.executeJS("PF('dlgCiudadela').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarCiudadela() {
        try {
            if (ciudadela.getParroquia() == null) {
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe seleccionar la parroquia.");
                return;
            }
            if (ciudadela.getNombre() == null || ciudadela.getNombre().isEmpty()) {
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe ingresar el nombre.");
                return;
            }
            ciudadela.setEstado(Boolean.TRUE);
            ciudadela.setUserCreacion(us.getName_user());
            ciudadela.setFechaCreacion(new Date());
            em.persist(ciudadela);
            JsfUti.executeJS("PF('dlgCiudadela').hide();");
            JsfUti.update("mainForm");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgMovSelect(RegMovimiento mov) {
        try {
            movimiento = mov;
            movimiento.setRegMovimientoReferenciaCollection(em.findAll(Querys.getRegMovimientoReferenciaByIdMov,
                    new String[]{"idmov"}, new Object[]{movimiento.getId()}));
            fichas = reg.getRegFichaByIdRegMov(mov.getId());
            clientes = reg.getRegMovClienteByIdMov(mov.getId());
            marginaciones = reg.getRegMovMargByIdMov(mov.getId());
            JsfUti.update("formMovRegSelec");
            JsfUti.executeJS("PF('dlgMovRegSelec').show();");
        } catch (Exception e) {
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void integracionYura() {
        try {
            if (movimiento.getDigitalizacion() == null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/consultaDocs.xhtml?repertorio=" + movimiento.getNumRepertorio()
                        + "&fecha=" + movimiento.getFechaInscripcion().getTime());
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + movimiento.getDigitalizacion()
                        + "&name=" + movimiento.getDigitalizacion() + ".pdf&tipo=1&content=application/pdf");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            Logger.getLogger(ConsultasRp.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public List<Barrios> getBarrios() {
        return em.findAllEntCopy(Querys.getBarriosList);
    }

    public RegFichaLazy getListadoFichas() {
        return listadoFichas;
    }

    public void setListadoFichas(RegFichaLazy listadoFichas) {
        this.listadoFichas = listadoFichas;
    }

    public RegFicha getFichaSel() {
        return fichaSel;
    }

    public void setFichaSel(RegFicha fichaSel) {
        this.fichaSel = fichaSel;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
    }

    public List<RegEnteInterviniente> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(List<RegEnteInterviniente> propietarios) {
        this.propietarios = propietarios;
    }

    public List<RegMovimientoFicha> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<RegMovimientoFicha> movimientos) {
        this.movimientos = movimientos;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public List<CtlgItem> getEstadosInformacion() {
        return estadosInformacion;
    }

    public void setEstadosInformacion(List<CtlgItem> estadosInformacion) {
        this.estadosInformacion = estadosInformacion;
    }

    public RegTipoFicha getTipoFicha() {
        return tipoFicha;
    }

    public void setTipoFicha(RegTipoFicha tipoFicha) {
        this.tipoFicha = tipoFicha;
    }

    public RegTipoFicha getNewTipoFicha() {
        return newTipoFicha;
    }

    public void setNewTipoFicha(RegTipoFicha newTipoFicha) {
        this.newTipoFicha = newTipoFicha;
    }

    public CatParroquia getParroquia() {
        return parroquia;
    }

    public void setParroquia(CatParroquia parroquia) {
        this.parroquia = parroquia;
    }

    public List<CatParroquia> getParroquias() {
        return parroquias;
    }

    public void setParroquias(List<CatParroquia> parroquias) {
        this.parroquias = parroquias;
    }

    public CodigosFicha getNuevoCodigo() {
        return nuevoCodigo;
    }

    public void setNuevoCodigo(CodigosFicha nuevoCodigo) {
        this.nuevoCodigo = nuevoCodigo;
    }

    public List<CodigosFicha> getCodigosFicha() {
        return codigosFicha;
    }

    public void setCodigosFicha(List<CodigosFicha> codigosFicha) {
        this.codigosFicha = codigosFicha;
    }

    public List<CtlgItem> getTipoCodigosPredio() {
        return tipoCodigosPredio;
    }

    public void setTipoCodigosPredio(List<CtlgItem> tipoCodigosPredio) {
        this.tipoCodigosPredio = tipoCodigosPredio;
    }

    public Barrios getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(Barrios ciudadela) {
        this.ciudadela = ciudadela;
    }

    public RegMovimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(RegMovimiento movimiento) {
        this.movimiento = movimiento;
    }

    public List<RegFicha> getFichas() {
        return fichas;
    }

    public void setFichas(List<RegFicha> fichas) {
        this.fichas = fichas;
    }

    public List<RegMovimientoCliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<RegMovimientoCliente> clientes) {
        this.clientes = clientes;
    }

    public List<RegMovimientoMarginacion> getMarginaciones() {
        return marginaciones;
    }

    public void setMarginaciones(List<RegMovimientoMarginacion> marginaciones) {
        this.marginaciones = marginaciones;
    }

}
