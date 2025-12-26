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
import com.origami.sgr.entities.Barrios;
import com.origami.sgr.entities.CatParroquia;
import com.origami.sgr.entities.CodigosFicha;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.FichaProceso;
import com.origami.sgr.entities.FichaProcesoLinderos;
import com.origami.sgr.entities.FichaProcesoObservaciones;
import com.origami.sgr.entities.RegFichaLinderos;
import com.origami.sgr.entities.RegMovimiento;
import com.origami.sgr.entities.RegMovimientoCliente;
import com.origami.sgr.entities.RegMovimientoFicha;
import com.origami.sgr.entities.RegMovimientoMarginacion;
import com.origami.sgr.entities.RegTipoFicha;
import com.origami.sgr.entities.RegTipoLindero;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ProcesoActualizacionFichas implements Serializable {

    private static final Logger LOG = Logger.getLogger(ProcesoActualizacionFichas.class.getName());

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
    protected Boolean rol_codificador = false, rol_control_calidad = false, rol_supervisor = false;
    protected List<RegEnteInterviniente> propietarios = new ArrayList<>();
    protected List<CtlgItem> estadosInformacion = new ArrayList<>();
    protected RegTipoFicha tipoFicha;
    protected RegTipoFicha newTipoFicha;
    protected CatParroquia parroquia;
    protected List<CatParroquia> parroquias;

    protected Barrios ciudadela;
    protected CodigosFicha nuevoCodigo;
    protected FichaProceso fichaProceso;
    protected FichaProcesoLinderos lindero;
    protected List<CodigosFicha> codigosFicha;
    protected List<CtlgItem> tipoCodigosPredio;
    protected List<FichaProcesoLinderos> linderos;
    protected FichaProcesoObservaciones comentario;
    protected List<FichaProcesoObservaciones> observaciones;

    protected RegMovimiento movimiento;
    protected List<RegFicha> fichas = new ArrayList<>();
    protected List<RegMovimientoCliente> clientes = new ArrayList<>();
    protected List<RegMovimientoMarginacion> marginaciones = new ArrayList<>();
    protected Long desde, hasta;
    protected String comentarioIngresado;

    @PostConstruct
    protected void iniView() {
        try {
            lindero = new FichaProcesoLinderos();
            comentario = new FichaProcesoObservaciones();
            tipoFicha = em.find(RegTipoFicha.class, 1L);
            newTipoFicha = em.find(RegTipoFicha.class, 1L);
            parroquias = em.findAllEntCopy(Querys.getCatParroquiaAll);
            registrador = (RegRegistrador) em.find(Querys.getRegRegistrador);

            map = new HashMap<>();
            map.put("catalogo", "ficha.estado_informacion");
            estadosInformacion = em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);

            map = new HashMap<>();
            map.put("catalogo", "ficha.tipo_codigo_predio");
            tipoCodigosPredio = em.findNamedQuery(Querys.getCtlgItemListByNombreDeCatalogo, map);

            this.validaRoles();
            this.iniciarLazy();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void validaRoles() {
        rol_codificador = us.getRoles().contains(89L);
        rol_control_calidad = us.getRoles().contains(90L);
        rol_supervisor = us.getRoles().contains(91L);
    }

    public void iniciarLazy() {
        listadoFichas = new RegFichaLazy(tipoFicha);
        if (this.rol_codificador) {
            listadoFichas.addFilter("descripcionBien", "aurora");
            listadoFichas.addFilter("descripcionBien:nc", "camposanto");
            listadoFichas.addFilter("informacionFicha", new CtlgItem(17L));     //SIN CODIFICAR
        }
        if (this.rol_control_calidad) {
            listadoFichas.addFilter("informacionFicha", new CtlgItem(18L));     //PROCESADO
            listadoFichas.addFilter("fichaProceso.estado", new CtlgItem(153L)); //ESTADO CODIFICADO
        }
        if (this.rol_supervisor) {
            listadoFichas.addFilter("informacionFicha", new CtlgItem(18L));     //PROCESADO
            listadoFichas.addFilter("fichaProceso.estado", new CtlgItem(155L)); //ESTADO APROBADO
        }
    }

    public void filtarLazy() {
        try {
            if (desde != null && hasta != null) {
                if (desde.compareTo(hasta) < 0) {
                    List<Long> list = new ArrayList<>();
                    list.add(desde);
                    list.add(hasta);
                    this.iniciarLazy();
                    //listadoFichas.addFilter("numFicha:between", new Long[]{desde, hasta});
                    listadoFichas.addFilter("numFicha:between", list);
                } else {
                    JsfUti.messageError(null, "Ficha desde debe ser menor a ficha hasta", "");
                }
            } else {
                JsfUti.messageError(null, "Debe ingresar ficha desde y ficha hasta para la búsqueda.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
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
            movimientos = reg.getRegMovActivosByIdFicha(rf.getId());
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
            map = new HashMap<>();
            map.put("regFicha.id", fichaSel.getId());
            fichaProceso = (FichaProceso) em.findObjectByParameter(FichaProceso.class, map);
            if (fichaProceso == null) {
                fichaProceso = new FichaProceso();
                fichaProceso.setRegFicha(fichaSel);
                fichaProceso.setEstado(new CtlgItem(152L));
                fichaProceso.setUsuarioCreacion(us.getName_user());
                fichaProceso.setFechaCreacion(new Date());
                fichaProceso = (FichaProceso) em.persist(fichaProceso);
            } else if (!fichaProceso.getUsuarioCreacion().equalsIgnoreCase(us.getName_user())) {
                JsfUti.messageError(null, "NO se puede codificar", "El usuario: " + fichaProceso.getUsuarioCreacion()
                        + ", se encuentra codificando la Ficha: " + fichaSel.getNumFicha());
                return;
            }
            codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
            if (codigosFicha.isEmpty()) {
                if (fichaSel.getFichaMatriz() != null) {
                    nuevoCodigo = new CodigosFicha();
                    nuevoCodigo.setCodigo(fichaSel.getFichaMatriz().toString());
                    nuevoCodigo.setTipo(new CtlgItem(146L));
                    nuevoCodigo.setFicha(fichaSel);
                    nuevoCodigo.setUsuarioCreacion(us.getName_user());
                    nuevoCodigo.setFechaCreacion(new Date());
                    em.persist(nuevoCodigo);
                    codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
                }
            }
            nuevoCodigo = new CodigosFicha();
            map = new HashMap<>();
            map.put("fichaProceso.id", fichaProceso.getId());
            linderos = em.findObjectByParameterOrderList(FichaProcesoLinderos.class, map, new String[]{"tipo"}, true);
            if (linderos.isEmpty()) {
                this.crearLinderosRevision();
            }
            lindero = new FichaProcesoLinderos();
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
                JsfUti.messageError(null, "Faltan Ingresar Campos", "Debe ingresar el campo lote.");
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
            this.iniciarLazy();
            JsfUti.update("mainForm");
            JsfUti.executeJS("PF('dlgProcesoFichas').hide();");
            JsfUti.executeJS("PF('dlgControlCalidad').hide();");
            JsfUti.executeJS("PF('dlgSupervisor').hide();");
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
            LOG.log(Level.SEVERE, null, e);
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
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void consultaYura(RegMovimiento temp) {
        try {
            if (temp.getDigitalizacion() == null) {
                JsfUti.redirectNewTab(SisVars.urlbase + "documental/consultaDocs.xhtml?repertorio=" + temp.getNumRepertorio()
                        + "&fecha=" + temp.getFechaInscripcion().getTime());
            } else {
                JsfUti.redirectNewTab(SisVars.urlbase + "OmegaDownDocs?code=" + temp.getDigitalizacion()
                        + "&name=" + temp.getDigitalizacion() + ".pdf&tipo=1&content=application/pdf");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pasarTareaControl() {
        try {
            if (codigosFicha.isEmpty()) {
                JsfUti.messageError(null, "Debe ingresar al menos un código para enviar a Control de Calidad.", "");
                return;
            }
            if (linderos.isEmpty()) {
                JsfUti.messageError(null, "Error", "No se cargaron los linderos.");
                return;
            }
            if (fichaProceso.getArea() == null || fichaProceso.getUnidadMedida() == null) {
                JsfUti.messageError(null, "Faltan Datos", "Debe ingresar el área y la unidad de medida.");
                return;
            }

            this.pasarProcesoCodificado();
            if (comentarioIngresado != null && !comentarioIngresado.isEmpty()) {
                this.guardarObservacion(comentarioIngresado);
            } else {
                this.guardarObservacion("ENVÍO A CONTROL DE CALIDAD");
            }
        } catch (Exception e) {
            JsfUti.messageError(null, Messages.error, e.getMessage());
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void crearLinderosRevision() {
        try {
            FichaProcesoLinderos temp;
            for (RegFichaLinderos rfl : fichaSel.getRegFichaLinderosCollection()) {
                temp = new FichaProcesoLinderos();
                temp.setEstado(true);
                temp.setFichaProceso(fichaProceso);
                temp.setLinderante(rfl.getLinderante());
                temp.setLongitud(rfl.getLongitud());
                temp.setRegFicha(fichaSel.getId());
                temp.setRegFichaLindero(rfl.getId());
                temp.setTipo(rfl.getTipo());
                temp = (FichaProcesoLinderos) em.persist(temp);
                linderos.add(temp);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgControlCalidad(RegFicha ficha) {
        try {
            nuevoCodigo = new CodigosFicha();
            lindero = new FichaProcesoLinderos();
            fichaSel = ficha;
            codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
            map = new HashMap<>();
            map.put("regFicha.id", ficha.getId());
            fichaProceso = (FichaProceso) em.findObjectByParameter(FichaProceso.class, map);
            if (fichaProceso != null) {
                if (fichaProceso.getUsuarioControl() == null) {
                    fichaProceso.setUsuarioControl(us.getName_user());
                    em.update(fichaProceso);
                } else if (!fichaProceso.getUsuarioControl().equalsIgnoreCase(us.getName_user())) {
                    JsfUti.messageError(null, "NO se puede realizar Control", "El usuario: " + fichaProceso.getUsuarioControl()
                            + ", se encuentra en Control de Calidad de la Ficha: " + fichaSel.getNumFicha());
                    return;
                }
                map = new HashMap<>();
                map.put("fichaProceso.id", fichaProceso.getId());
                linderos = em.findObjectByParameterOrderList(FichaProcesoLinderos.class, map, new String[]{"tipo"}, true);
                JsfUti.update("formControlCalidad");
                JsfUti.executeJS("PF('dlgControlCalidad').show();");
            } else {
                JsfUti.messageError(null, "NO se puede procesar", "No se registra el proceso de Codificación.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgSupervision(RegFicha ficha) {
        try {
            comentarioIngresado = null;
            fichaSel = ficha;
            codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
            map = new HashMap<>();
            map.put("regFicha.id", ficha.getId());
            fichaProceso = (FichaProceso) em.findObjectByParameter(FichaProceso.class, map);
            if (fichaProceso != null) {
                map = new HashMap<>();
                map.put("fichaProceso.id", fichaProceso.getId());
                linderos = em.findObjectByParameterOrderList(FichaProcesoLinderos.class, map, new String[]{"tipo"}, true);
                JsfUti.update("formSupervisor");
                JsfUti.executeJS("PF('dlgSupervisor').show();");
            } else {
                JsfUti.messageError(null, "NO se puede Revisar", "No se registra el proceso de Codificación.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void vincularProceso() {
        try {
            JsfUti.messageWarning(null, "NO se puede vincular", "No está habilitado el servicio web de Catastro");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void selectLindero(FichaProcesoLinderos fpl) {
        try {
            lindero = fpl;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateLindero() {
        try {
            if (lindero.getTipo() == null) {
                JsfUti.messageError(null, "Faltan Datos", "Debe ingresar el tipo de lindero.");
                return;
            }
            if (lindero.getLinderante() == null || lindero.getLinderante().isEmpty()) {
                JsfUti.messageError(null, "Faltan Datos", "Debe ingresar el campo lindero.");
                return;
            }

            lindero.setEstado(true);
            lindero.setFichaProceso(fichaProceso);
            lindero.setRegFicha(fichaProceso.getRegFicha().getId());
            em.persist(lindero);

            linderos = reg.getLinderosProcesoFichaById(fichaProceso.getId());
            lindero = new FichaProcesoLinderos();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void updateDatosFicha() {
        try {
            if (fichaProceso.getId() != null) {
                if (fichaProceso.getArea() == null || fichaProceso.getUnidadMedida() == null) {
                    JsfUti.messageError(null, "Faltan Datos", "Debe ingresar el área y la unidad de medida.");
                    return;
                }
                fichaProceso.setUsuarioModificacion(us.getName_user());
                fichaProceso.setFechaModificacion(new Date());
                em.update(fichaProceso);
                JsfUti.messageInfo(null, "Datos actualizados con exito.", "");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showDlgVisualizar(RegFicha ficha) {
        try {
            fichaSel = ficha;
            map = new HashMap<>();
            map.put("regFicha.id", ficha.getId());
            fichaProceso = (FichaProceso) em.findObjectByParameter(FichaProceso.class, map);
            if (fichaProceso != null) {
                codigosFicha = reg.getCodigosFichaById(fichaSel.getId());
                map = new HashMap<>();
                map.put("fichaProceso.id", fichaProceso.getId());
                linderos = em.findObjectByParameterOrderList(FichaProcesoLinderos.class, map, new String[]{"tipo"}, true);
                JsfUti.update("formVisualizarProceso");
                JsfUti.executeJS("PF('dlgVisualizarProceso').show();");
            } else {
                JsfUti.messageError(null, "NO se puede Revisar", "No se registra el proceso de Codificación.");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void regresarControlCalidad() {
        try {
            if (comentarioIngresado != null && !comentarioIngresado.isEmpty()) {
                this.guardarObservacion(comentarioIngresado);
            } else {
                this.guardarObservacion("REGRESAR A CONTROL DE CALIDAD");
            }
            this.pasarProcesoCodificado();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pasarProcesoCodificado() {
        try {
            fichaSel.setInformacionFicha(new CtlgItem(18L));    //ESTADO PROCESADO
            em.update(fichaSel);

            fichaProceso.setEstado(new CtlgItem(153L));         //ESTADO CODIFICADO
            em.update(fichaProceso);

            this.actualizarLazy();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void regresarProcesoPendiente() {
        try {
            fichaSel.setInformacionFicha(new CtlgItem(17L));    //ESTADO SIN PROCESAR
            em.update(fichaSel);

            fichaProceso.setEstado(new CtlgItem(152L));         //ESTADO PENDIENTE
            em.update(fichaProceso);

            this.actualizarLazy();
            if (comentarioIngresado != null && !comentarioIngresado.isEmpty()) {
                this.guardarObservacion(comentarioIngresado);
            } else {
                this.guardarObservacion("REGRESAR A CODIFICACIÓN");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void pasarProcesoAprobado() {
        try {
            fichaSel.setInformacionFicha(new CtlgItem(18L));    //ESTADO PROCESADO
            em.update(fichaSel);

            fichaProceso.setEstado(new CtlgItem(155L));         //ESTADO APROBADO
            fichaProceso.setUsuarioControl(us.getName_user());
            fichaProceso.setFechaControl(new Date());
            em.update(fichaProceso);

            this.actualizarLazy();
            if (comentarioIngresado != null && !comentarioIngresado.isEmpty()) {
                this.guardarObservacion(comentarioIngresado);
            } else {
                this.guardarObservacion("MARCADO COMO APROBADO");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void cambiarEstadoRevisado() {
        try {
            fichaProceso.setRevisado(true);
            fichaProceso.setUsuarioSupervisor(us.getName_user());
            fichaProceso.setFechaSupervision(new Date());
            em.update(fichaProceso);

            this.actualizarLazy();
            if (comentarioIngresado != null && !comentarioIngresado.isEmpty()) {
                this.guardarObservacion(comentarioIngresado);
            } else {
                this.guardarObservacion("MARCADO COMO REVISADO");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showObservacionesProceso(RegFicha ficha) {
        try {
            fichaSel = ficha;
            map = new HashMap<>();
            map.put("idFicha", fichaSel.getId());
            observaciones = em.findNamedQuery(Querys.getObservacionesProcesoFichaByFicha, map);
            JsfUti.update("formObservaciones");
            JsfUti.executeJS("PF('dlgObservaciones').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void guardarObservacion(String observacion) {
        try {
            comentario = new FichaProcesoObservaciones();
            comentario.setFecha(new Date());
            comentario.setFichaProceso(fichaProceso);
            comentario.setObservacion(observacion);
            comentario.setUsuario(us.getName_user());
            em.persist(comentario);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public List<CtlgItem> getUnidades() {
        return em.findAllEntCopy(Querys.getCtlgItemUnidades);
    }

    public List<RegTipoLindero> getTiposLinderos() {
        return em.findAllEntCopy(Querys.getRegTipoLinderos);
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

    public Boolean getRol_codificador() {
        return rol_codificador;
    }

    public void setRol_codificador(Boolean rol_codificador) {
        this.rol_codificador = rol_codificador;
    }

    public Boolean getRol_control_calidad() {
        return rol_control_calidad;
    }

    public void setRol_control_calidad(Boolean rol_control_calidad) {
        this.rol_control_calidad = rol_control_calidad;
    }

    public Boolean getRol_supervisor() {
        return rol_supervisor;
    }

    public void setRol_supervisor(Boolean rol_supervisor) {
        this.rol_supervisor = rol_supervisor;
    }

    public List<FichaProcesoLinderos> getLinderos() {
        return linderos;
    }

    public void setLinderos(List<FichaProcesoLinderos> linderos) {
        this.linderos = linderos;
    }

    public FichaProceso getFichaProceso() {
        return fichaProceso;
    }

    public void setFichaProceso(FichaProceso fichaProceso) {
        this.fichaProceso = fichaProceso;
    }

    public FichaProcesoLinderos getLindero() {
        return lindero;
    }

    public void setLindero(FichaProcesoLinderos lindero) {
        this.lindero = lindero;
    }

    public Long getDesde() {
        return desde;
    }

    public void setDesde(Long desde) {
        this.desde = desde;
    }

    public Long getHasta() {
        return hasta;
    }

    public void setHasta(Long hasta) {
        this.hasta = hasta;
    }

    public FichaProcesoObservaciones getComentario() {
        return comentario;
    }

    public void setComentario(FichaProcesoObservaciones comentario) {
        this.comentario = comentario;
    }

    public List<FichaProcesoObservaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<FichaProcesoObservaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public String getComentarioIngresado() {
        return comentarioIngresado;
    }

    public void setComentarioIngresado(String comentarioIngresado) {
        this.comentarioIngresado = comentarioIngresado;
    }

}
