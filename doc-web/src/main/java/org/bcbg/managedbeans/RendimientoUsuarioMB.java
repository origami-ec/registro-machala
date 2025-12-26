/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Attachment;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.Observaciones;
import org.bcbg.entities.RolUsuario;
import org.bcbg.entities.ServiciosDepartamento;
import org.bcbg.entities.TipoTramite;
import org.bcbg.entities.Tramites;
import org.bcbg.lazymodels.LazyModelWS;
import org.bcbg.models.TareasHistoricas;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author Ricardo
 */
@ViewScoped
@Named
public class RendimientoUsuarioMB extends BpmManageBeanBaseRoot implements Serializable {

    private BarChartModel barModel;
    private BarChartModel barModelUsuarioTramite;
    private List<TipoTramite> tipoTramites;
    private TipoTramite tipoTramite;
    private List<Tramites> analisisTramite;
    private List<Tramites> analisisTramiteUsuario;
    private Date fechaDesde, fechaHasta, fechaHastaUT, fechaDesdeUT;
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private Boolean categoriaTramite, eleccionUsuarios;
    private Departamento departamento;
    private ServiciosDepartamento item;
    private List<ServiciosDepartamento> itemList;
    private List<RolUsuario> usuariosLista;
    private String posicionGlobalUser, posicionColumnas;
    private List<RolUsuario> usuariosAsignadosList;
    private List<Departamento> departamentos;
    private LazyModelWS<TareasHistoricas> lazyTareasHistoricas;
    private TareasHistoricas tareasHistoricas;
    protected List<HistoricTaskInstance> tareas = new ArrayList<>();
    protected List<Attachment> listAttach = new ArrayList<>();
    private StreamedContent imageProcessInstance;
    private List<Observaciones> observaciones;
    protected HistoricoTramites ht;
    private List<TareasHistoricas> groupByTareas;
    private Boolean groupTareas;

    @PostConstruct
    public void init() {
        loadData();
    }

    public void loadData() {
        lazyTareasHistoricas = new LazyModelWS<>(SisVars.ws + "tareas/historicas/find", TareasHistoricas[].class, session.getToken());
        analisisTramiteUsuario = new ArrayList<>();
        barModelUsuarioTramite = new BarChartModel();
        tipoTramites = appServices.getTipoTramites();
        item = new ServiciosDepartamento();
        eleccionUsuarios = Boolean.TRUE;
        initDepartamentos();
        loadModel();
    }

    public void loadModel() {
        fechaDesde = Utils.getFechaInit();
        fechaHasta = new Date();
        fechaHastaUT = new Date();
        fechaDesdeUT = Utils.getFechaInit();
        loadBarModel();
    }

    public void loadModelUsuarioTramite() {
        fechaHastaUT = new Date();
        fechaDesdeUT = Utils.getFechaInit();
        usuariosAsignadosList = new ArrayList<>();
        item = new ServiciosDepartamento();
        eleccionUsuarios = Boolean.TRUE;
        usuariosLista = new ArrayList<>();
        barModelUsuarioTramite = new BarChartModel();
        tipoTramite = new TipoTramite();
        departamento = new Departamento();
    }

    public void loadBarModel() {
        analisisTramite = service.methodListGET(SisVars.ws + "analisis/tramites/"
                + format.format(fechaDesde) + "/" + format.format(fechaHasta), Tramites[].class);
        createBarModel();
    }

    private void initDepartamentos() {
        departamentos = appServices.getListDepartamentos();
    }

    public void createBarModel() {
        barModel = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSetAtendidos = new BarChartDataSet();
        barDataSetAtendidos.setLabel("Atendidos");
        barDataSetAtendidos.setBackgroundColor("rgba(113, 203, 87, 0.4)");
        barDataSetAtendidos.setBorderColor("rgba(113, 203, 87, 0.4)");
        barDataSetAtendidos.setBorderWidth(1);

        BarChartDataSet barDataSetPendientes = new BarChartDataSet();
        barDataSetPendientes.setLabel("Pendientes");
        barDataSetPendientes.setBackgroundColor("rgb(205, 37, 0, 0.4)");
        barDataSetPendientes.setBorderColor("rgb(205, 37, 0, 0.4)");
        barDataSetPendientes.setBorderWidth(1);

        List<Number> atendidos = new ArrayList<>();
        List<Number> pendientes = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        analisisTramite.forEach(a -> {
            labels.add(a.getIdTipoTramite().getAbreviatura());
            atendidos.add(a.getTerminados());
            pendientes.add(a.getPendientes());
        });
        barDataSetAtendidos.setData(atendidos);
        barDataSetPendientes.setData(pendientes);

        data.addChartDataSet(barDataSetAtendidos);
        data.addChartDataSet(barDataSetPendientes);

        data.setLabels(labels);
        barModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Tareas Trámites");
        options.setTitle(title);

        barModel.setOptions(options);
    }

    public void generarPDF() {
        if (analisisTramite != null && !analisisTramite.isEmpty()) {
            for (Tramites t : analisisTramite) {
                t.setStartTime(fechaDesde);
                t.setEndTime(fechaHasta);
            }
            ss.borrarDatos();
            ss.borrarParametros();
            ss.setDataSource(analisisTramite);
            ss.setUrlWebService(SisVars.ws + "reportes/tramites/AnalisisUsuario");
            JsfUti.redirectFacesNewTab("/DocumentoWs");
        }
    }

    public void generarPDFUsuario() {
        ss.borrarDatos();
        ss.borrarParametros();
//        analisisTramiteUsuario
        if (tipoTramite != null && tipoTramite.getId() != null && analisisTramiteUsuario != null && !analisisTramiteUsuario.isEmpty()
                && departamento != null && departamento.getId() != null) {
            for (Tramites tt : analisisTramiteUsuario) {
                tt.setIdTipoTramite(tipoTramite);
                tt.setDepartamento(departamento.getNombre());
                tt.setServicio(item != null && item.getId() != null ? item.getNombre() : "");
                tt.setStartTime(fechaDesdeUT);
                tt.setEndTime(fechaHastaUT);
            }
            ss.setDataSource(analisisTramiteUsuario);
            ss.setUrlWebService(SisVars.ws + "reportes/tramites/AnalisisUsuarioDepartamento");
            JsfUti.redirectFacesNewTab("/DocumentoWs");
        }
    }

    public void itemSelect(ItemSelectEvent event) {
        JsfUti.messageInfo(null, "", "Item Index: " + event.getItemIndex() + ", DataSet Index:" + event.getDataSetIndex());
    }

    public void initDepartamentoDesdeTipoTramite() {
        categoriaTramite = Boolean.FALSE;
        item = new ServiciosDepartamento();
        usuariosLista = new ArrayList<>();
        usuariosAsignadosList = new ArrayList<>();
        if (tipoTramite != null && tipoTramite.getId() != null && tipoTramite.getDepartamento() != null) {
            departamento = tipoTramite.getDepartamento();
            initServiciosDepartamentos();
        } else {
            departamento = null;
        }
    }

    public void initServiciosDepartamentos() {
        categoriaTramite = Boolean.FALSE;
        if (departamento != null && departamento.getId() != null) {
            itemList = new ArrayList<>();
            ServiciosDepartamento i = new ServiciosDepartamento(new Departamento(departamento.getId()),
                    new TipoTramite(tipoTramite.getId()));
            itemList = appServices.getListItems(i);
            initListUsuarios();
        }
    }

    public void initListUsuarios() {
        eleccionUsuarios = Boolean.TRUE;
        usuariosAsignadosList = new ArrayList<>();
//        usuariosLista = appServices.getRolUsuarioXDepts(new RolUsuario(new User(Boolean.TRUE), new Rol(new Departamento(departamento.getId())), Boolean.FALSE));
//        if (Utils.isNotEmpty(usuariosLista)) {
//            for (RolUsuario ru : usuariosLista) {
//                if (ru.getResponsable()) {
//                    usuariosAsignadosList.add(ru);
//                }
//            }
//        }
    }

    public void loadBarModelUsuarioTramite() {
        if (validar()) {
            analisisTramiteUsuario = new ArrayList<>();
            for (RolUsuario ru : usuariosAsignadosList) {
//                Tramites t = (Tramites) service.methodGET(SisVars.ws
//                        + "analisis/tramites/" + format.format(fechaDesdeUT) + "/" + format.format(fechaHastaUT)
//                        + "/" + ru.getUsuario().getNombreUsuario() + "/" + (item != null && item.getId() != null ? item.getId() : tipoTramite.getId()) + (item != null && item.getId() != null ? "/SERVICIO" : "/TRAMITE"), Tramites.class);
//                analisisTramiteUsuario.add(t);
            }
            createBarModelUsuarioTramite();
        }
    }

    private Boolean validar() {
        if (tipoTramite == null || tipoTramite.getId() == null) {
            JsfUti.messageError(null, "", "Debe seleccionar un tramite");
            return false;
        }
        if (departamento == null || departamento.getId() == null) {
            JsfUti.messageError(null, "", "Debe seleccionar un departamento");
            return false;
        }
        if (usuariosAsignadosList.isEmpty()) {
            JsfUti.messageError(null, "", "Debe seleccionar al menos un usuario");
            return false;
        }
        return true;
    }

    public void createBarModelUsuarioTramite() {
        barModelUsuarioTramite = new BarChartModel();
        ChartData data = new ChartData();

        BarChartDataSet barDataSetAtendidos = new BarChartDataSet();
        barDataSetAtendidos.setLabel("Atendidos");
        barDataSetAtendidos.setBackgroundColor("rgba(113, 203, 87, 0.4)");
        barDataSetAtendidos.setBorderColor("rgba(113, 203, 87, 0.4)");
        barDataSetAtendidos.setBorderWidth(1);

        BarChartDataSet barDataSetPendientes = new BarChartDataSet();
        barDataSetPendientes.setLabel("Pendientes");
        barDataSetPendientes.setBackgroundColor("rgb(205, 37, 0, 0.4)");
        barDataSetPendientes.setBorderColor("rgb(205, 37, 0, 0.4)");
        barDataSetPendientes.setBorderWidth(1);

        List<Number> atendidos = new ArrayList<>();
        List<Number> pendientes = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        analisisTramiteUsuario.forEach(a -> {
            labels.add(a.getParticipants());
            atendidos.add(a.getTerminados());
            pendientes.add(a.getPendientes());
        });
        barDataSetAtendidos.setData(atendidos);
        barDataSetPendientes.setData(pendientes);

        data.addChartDataSet(barDataSetAtendidos);
        data.addChartDataSet(barDataSetPendientes);

        data.setLabels(labels);
        barModelUsuarioTramite.setData(data);
        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);
        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
        Title title = new Title();
        title.setDisplay(true);
        title.setText("Tareas Trámites");
        options.setTitle(title);
        barModelUsuarioTramite.setOptions(options);
    }

    public void showDetalis(TareasHistoricas historicas) {
        try {
            tareasHistoricas = historicas;
            tareas = this.getTaskByProcessInstanceIdMain(historicas.getProcInstId());
            listAttach = this.getProcessInstanceAllAttachmentsFiles(tareasHistoricas.getProcInstId());
            ht = appServices.buscarHistoricoTramite(new HistoricoTramites(tareasHistoricas.getTramite().getId()));
            observaciones = appServices.getObservaciones(new Observaciones(new HistoricoTramites(ht.getId())));
            InputStream diagram = engine.getProcessInstanceDiagram(tareasHistoricas.getProcInstId());
            if (diagram != null) {
                imageProcessInstance = DefaultStreamedContent.builder().stream(() -> diagram).build();
            }
            this.initDocEsc(this.ht.getNumTramite().toString());
            JsfUti.update("formInformLiq");
            JsfUti.executeJS("PF('dlgVerInfoRp').show();");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void closeDlogoViewTramite() {
        JsfUti.executeJS("PF('dlgVerInfoRp').hide()");
        JsfUti.update("formInformLiq");
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<TareasHistoricas> getGroupByTareas() {
        return groupByTareas;
    }

    public void setGroupByTareas(List<TareasHistoricas> groupByTareas) {
        this.groupByTareas = groupByTareas;
    }

    public StreamedContent getImageProcessInstance() {
        return imageProcessInstance;
    }

    public void setImageProcessInstance(StreamedContent imageProcessInstance) {
        this.imageProcessInstance = imageProcessInstance;
    }

    public List<Observaciones> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(List<Observaciones> observaciones) {
        this.observaciones = observaciones;
    }

    public List<HistoricTaskInstance> getTareas() {
        return tareas;
    }

    public void setTareas(List<HistoricTaskInstance> tareas) {
        this.tareas = tareas;
    }

    public TareasHistoricas getTareasHistoricas() {
        return tareasHistoricas;
    }

    public void setTareasHistoricas(TareasHistoricas tareasHistoricas) {
        this.tareasHistoricas = tareasHistoricas;
    }

    public LazyModelWS<TareasHistoricas> getLazyTareasHistoricas() {
        return lazyTareasHistoricas;
    }

    public void setLazyTareasHistoricas(LazyModelWS<TareasHistoricas> lazyTareasHistoricas) {
        this.lazyTareasHistoricas = lazyTareasHistoricas;
    }

    public List<RolUsuario> getUsuariosLista() {
        return usuariosLista;
    }

    public void setUsuariosLista(List<RolUsuario> usuariosLista) {
        this.usuariosLista = usuariosLista;
    }

    public List<RolUsuario> getUsuariosAsignadosList() {
        return usuariosAsignadosList;
    }

    public void setUsuariosAsignadosList(List<RolUsuario> usuariosAsignadosList) {
        this.usuariosAsignadosList = usuariosAsignadosList;
    }

    public Boolean getEleccionUsuarios() {
        return eleccionUsuarios;
    }

    public void setEleccionUsuarios(Boolean eleccionUsuarios) {
        this.eleccionUsuarios = eleccionUsuarios;
    }

    public Date getFechaHastaUT() {
        return fechaHastaUT;
    }

    public void setFechaHastaUT(Date fechaHastaUT) {
        this.fechaHastaUT = fechaHastaUT;
    }

    public Date getFechaDesdeUT() {
        return fechaDesdeUT;
    }

    public void setFechaDesdeUT(Date fechaDesdeUT) {
        this.fechaDesdeUT = fechaDesdeUT;
    }

    public BarChartModel getBarModelUsuarioTramite() {
        return barModelUsuarioTramite;
    }

    public void setBarModelUsuarioTramite(BarChartModel barModelUsuarioTramite) {
        this.barModelUsuarioTramite = barModelUsuarioTramite;
    }

    public ServiciosDepartamento getItem() {
        return item;
    }

    public List<ServiciosDepartamento> getItemList() {
        return itemList;
    }

    public void setItemList(List<ServiciosDepartamento> itemList) {
        this.itemList = itemList;
    }

    public void setItem(ServiciosDepartamento item) {
        this.item = item;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<TipoTramite> getTipoTramites() {
        return tipoTramites;
    }

    public void setTipoTramites(List<TipoTramite> tipoTramites) {
        this.tipoTramites = tipoTramites;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }
//</editor-fold>
}
