/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.apache.commons.text.WordUtils;
import org.bcbg.bpm.models.TareaEntregaDocumento;
import org.bcbg.bpm.models.TareaWF;
import org.bcbg.config.SisVars;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.User;
import org.bcbg.models.DatosReporte;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.ws.AppServices;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author Origami
 */
@Named
@ViewScoped
public class ReportesDiarios implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReportesDiarios.class.getName());

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;
    @Inject
    private BcbgService irs;
    @Inject
    private AppServices appService;

    protected Boolean reporteGeneral, excel = false, renderDepartamentos = false;
    protected Integer tipo = 0, tipofecha = 0, tipotarea = 0;
    protected Integer reporte = 0, reporte1 = 0, reporte2 = 0, reporte3 = 0;
    protected Date desde1, desde2, desde3, desde4, desde5, desde6, desde7, desde8;
    protected Date hasta1, hasta2, hasta3, hasta4, hasta5, hasta6, hasta7, hasta8;
    protected String nombre = "", busqueda = "";
    protected SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    protected SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    protected User usuario, usuario1;
//    protected User usuario;
//    protected RolUsuario rolUsuario;
    protected Departamento departamento;
    protected Calendar ca;
    protected int mes, anio;
    protected Map map;

    protected List<TareaEntregaDocumento> entregaDocumento = new ArrayList<>(); 
    protected List<User> funcionarios = new ArrayList<>(); 
    protected List<Departamento> departamentos = new ArrayList<>();
    protected List<User> revisores = new ArrayList<>();
    protected List<TareaWF> taskWorkflow;
 
    @PostConstruct
    protected void iniView() {
        try {
            reporteGeneral = Boolean.FALSE;
            ca = Calendar.getInstance();
            mes = ca.get(Calendar.MONTH);
            anio = ca.get(Calendar.YEAR);
            desde1 = ca.getTime();
            hasta1 = ca.getTime();

            departamentos = new ArrayList<>();
            if (us.getIsUserAdmin()) {
                departamentos = appService.findAllDepartamentoByServiciosDepartamento();
            } else {
                departamento = appService.buscarDepartamento(new Departamento(us.getDepts().get(0)));
                departamentos.add(departamento); 
                loadFuncionariosByDepartamento();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void loadFuncionariosByDepartamento() {
        if (departamento != null) {
     funcionarios = appService.getUserXDepts(departamento.getId());
        } else {
           JsfUti.messageWarning(null, "Debe seleccionar el departamento.", "");
        }
    }
 
     
    public void generarReporte() {
        try {
            DatosReporte data;
            if (reporte1 < 1) {
                JsfUti.messageWarning(null, "Advertencia", "Debe seleccionar el tipo de reporte");
                return;
            }
              map = new HashMap();
              map.put("DESDE", Utils.dateFormatPattern("yyyy-MM-dd", desde1));
                    map.put("HASTA", Utils.dateFormatPattern("yyyy-MM-dd", hasta1));
            switch (reporte1) {
                case 1:
                    ss.borrarDatos();
                    data = new DatosReporte();
                    data.setFechadesde(desde1.getTime());
                    data.setFechahasta(hasta1.getTime());
                  
                    if (usuario1 != null) {
                        map.put("NOMBRES", usuario1.getNombreUsuario());
                        data.setUsuario(usuario1.getUsuarioNombre());
                        data.setNombreArchivoPDF("REPORTE DE TAREAS REALIZADAS POR " +usuario1.getUsuarioNombre());
                    } else {
                        map.put("DEPARTAMENTO", departamento.getNombre());
                        data.setDepartamento(departamento.getNombre());
                        data.setNombreArchivoPDF("REPORTE DE TAREAS REALIZADAS DE " +departamento.getNombre());
                    }
                    map.put("NOMBRE_REPORTE",data.getNombreArchivoPDF());
                    ss.setUrlWebService(SisVars.ws + "reportes/tareasrealizadas/usuario/");
                    data.setParametros(map);
                    ss.setDatos(data);
                    JsfUti.redirectFacesNewTab("/DocumentoWs");
                    break;
                case 2:
                    ss.borrarDatos(); 
                    data = new DatosReporte();
                    data.setFechadesde(desde1.getTime());
                    data.setFechahasta(hasta1.getTime());
                    if (usuario1 != null) {
                        map.put("NOMBRES", usuario1.getNombreUsuario());
                        data.setUsuario(usuario1.getUsuarioNombre());
                        data.setNombreArchivoPDF("REPORTE DE TAREAS PENDIENTES POR " +usuario1.getUsuarioNombre());
                    } else {
                        map.put("DEPARTAMENTO", departamento.getNombre());
                        data.setDepartamento(departamento.getNombre());
                        data.setNombreArchivoPDF("REPORTE DE TAREAS PENDIENTES DE " + departamento.getNombre());
                    }
                     map.put("NOMBRE_REPORTE",data.getNombreArchivoPDF());
                    ss.setUrlWebService(SisVars.ws + "reportes/tareaspendientes/usuario/");
                    data.setParametros(map);
                    ss.setDatos(data);
                    JsfUti.redirectFacesNewTab("/DocumentoWs");
                    break;
                case 3:
                    JsfUti.messageWarning(null, "Advertencia", "No se pudo generar el reporte.");
                    break;
                default:
                    JsfUti.messageWarning(null, "Advertencia", "No se pudo generar el reporte.");
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

    public Date getDesde1() {
        return desde1;
    }

    public void setDesde1(Date desde1) {
        this.desde1 = desde1;
    }

    public Date getHasta1() {
        return hasta1;
    }

    public void setHasta1(Date hasta1) {
        this.hasta1 = hasta1;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public Integer getReporte() {
        return reporte;
    }

    public void setReporte(Integer reporte) {
        this.reporte = reporte;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public List<TareaEntregaDocumento> getEntregaDocumento() {
        return entregaDocumento;
    }

    public void setEntregaDocumento(List<TareaEntregaDocumento> entregaDocumento) {
        this.entregaDocumento = entregaDocumento;
    }

    public Integer getTipofecha() {
        return tipofecha;
    }

    public void setTipofecha(Integer tipofecha) {
        this.tipofecha = tipofecha;
    }

    public Date getDesde2() {
        return desde2;
    }

    public void setDesde2(Date desde2) {
        this.desde2 = desde2;
    }

    public Date getHasta2() {
        return hasta2;
    }

    public void setHasta2(Date hasta2) {
        this.hasta2 = hasta2;
    }

    public Date getDesde3() {
        return desde3;
    }

    public void setDesde3(Date desde3) {
        this.desde3 = desde3;
    }

    public Date getHasta3() {
        return hasta3;
    }

    public void setHasta3(Date hasta3) {
        this.hasta3 = hasta3;
    }

    public Date getDesde4() {
        return desde4;
    }

    public void setDesde4(Date desde4) {
        this.desde4 = desde4;
    }

    public Date getHasta4() {
        return hasta4;
    }

    public void setHasta4(Date hasta4) {
        this.hasta4 = hasta4;
    }

    public Integer getTipotarea() {
        return tipotarea;
    }

    public void setTipotarea(Integer tipotarea) {
        this.tipotarea = tipotarea;
    }

    public Date getDesde5() {
        return desde5;
    }

    public void setDesde5(Date desde5) {
        this.desde5 = desde5;
    }

    public Date getHasta5() {
        return hasta5;
    }

    public void setHasta5(Date hasta5) {
        this.hasta5 = hasta5;
    }

    public Integer getReporte1() {
        return reporte1;
    }

    public void setReporte1(Integer reporte1) {
        this.reporte1 = reporte1;
    }

    public Integer getReporte2() {
        return reporte2;
    }

    public void setReporte2(Integer reporte2) {
        this.reporte2 = reporte2;
    }

    public Integer getReporte3() {
        return reporte3;
    }

    public void setReporte3(Integer reporte3) {
        this.reporte3 = reporte3;
    }

    public Date getDesde6() {
        return desde6;
    }

    public void setDesde6(Date desde6) {
        this.desde6 = desde6;
    }

    public Date getHasta6() {
        return hasta6;
    }

    public void setHasta6(Date hasta6) {
        this.hasta6 = hasta6;
    }

    public Date getDesde7() {
        return desde7;
    }

    public void setDesde7(Date desde7) {
        this.desde7 = desde7;
    }

    public Date getHasta7() {
        return hasta7;
    }

    public void setHasta7(Date hasta7) {
        this.hasta7 = hasta7;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public Boolean getExcel() {
        return excel;
    }

    public void setExcel(Boolean excel) {
        this.excel = excel;
    }

    public Date getDesde8() {
        return desde8;
    }

    public void setDesde8(Date desde8) {
        this.desde8 = desde8;
    }

    public Date getHasta8() {
        return hasta8;
    }

    public void setHasta8(Date hasta8) {
        this.hasta8 = hasta8;
    }

    public Boolean getReporteGeneral() {
        return reporteGeneral;
    }

    public void setReporteGeneral(Boolean reporteGeneral) {
        this.reporteGeneral = reporteGeneral;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public User getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(User usuario1) {
        this.usuario1 = usuario1;
    }
//      public RolUsuario getRolUsuario() {
//        return rolUsuario;
//    }
//
//    public void setRolUsuario(RolUsuario rolUsuario) {
//        this.rolUsuario = rolUsuario;
//    }

  
    public List<User> getFuncionarios() {
        return funcionarios;
    }

    public void setFuncionarios(List<User> funcionarios) {
        this.funcionarios = funcionarios;
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

    public List<User> getRevisores() {
        return revisores;
    }

    public void setRevisores(List<User> revisores) {
        this.revisores = revisores;
    }

    public List<TareaWF> getTaskWorkflow() {
        return taskWorkflow;
    }

    public void setTaskWorkflow(List<TareaWF> taskWorkflow) {
        this.taskWorkflow = taskWorkflow;
    }

    public Boolean getRenderDepartamentos() {
        return renderDepartamentos;
    }

    public void setRenderDepartamentos(Boolean renderDepartamentos) {
        this.renderDepartamentos = renderDepartamentos;
    }

//    public List<RolUsuario> getListRolUsuario() {
//        return listRolUsuario;
//    }
//
//    public void setListRolUsuario(List<RolUsuario> listRolUsuario) {
//        this.listRolUsuario = listRolUsuario;
//    }
}
