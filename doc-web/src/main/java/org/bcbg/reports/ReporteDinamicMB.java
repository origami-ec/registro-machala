/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.reports;

import com.google.gson.Gson;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
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
import jdk.jfr.Description;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.User;
import org.bcbg.entities.Valores;
import org.bcbg.models.Data;
import org.bcbg.models.DatosReporte;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import org.bcbg.util.Variables;
import org.bcbg.ws.AppEjb;
import org.bcbg.ws.AppServices;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class ReporteDinamicMB extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private UserSession us;
    @Inject
    private AppServices services;
    private List<Valores> valores, plantillas;
    private List<ReportField> tablas;
    private List<ReportFieldDet> seleccionados;
    private Date desde, hasta;
    private List<Departamento> departamentos;
    private Departamento departamento;
    private List<User> usuarios = new ArrayList<>();
    private User usuario;
    private String nombreReporte;
    private Boolean incluirFechas;
    private Class clazzPrincipal;
    private String nombrePlantilla;
    private Long idPlantilla;

    @PostConstruct
    public void init() {
        try {
            Calendar ca = Calendar.getInstance();
            desde = ca.getTime();
            hasta = ca.getTime();
            incluirFechas = Boolean.FALSE;
            valores = services.getValores(Variables.tablasDinamicas);
            plantillas = services.getValores(Variables.tablaPlantilla);
            tablas = new ArrayList<>();
            seleccionados = new ArrayList<>();
            cargarDept();
            cargarCampos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarPlantilla() {
        if (Utils.isNotEmpty(seleccionados) && Utils.isNotEmptyString(nombrePlantilla)) {
            Valores valor = new Valores();
            valor.setId(idPlantilla);
            valor.setCode(Variables.tablaPlantilla + ": " + nombrePlantilla);
            valor.setValorString(new Gson().toJson(seleccionados));
            appServices.guardarValores(valor);
            plantillas = services.getValores(Variables.tablaPlantilla);
            JsfUti.messageInfo(null, "Plantilla grabada correctamente", "");
        } else {
            JsfUti.messageError(null, "Debe agregar campos para continuar y un nombre a la plantilla", "");
        }
    }

    public void cargarPlantilla(Valores valor) {

        idPlantilla = valor.getId();
        nombrePlantilla = valor.getCode().replace(Variables.tablaPlantilla + ": ", "");
        ReportFieldDet[] det = new Gson().fromJson(valor.getValorString(), ReportFieldDet[].class);
        seleccionados = new ArrayList<>(Arrays.asList(det));
    }

    public void eliminarPlantilla(Valores valor) {
        appServices.eliminarValores(valor);
        plantillas = services.getValores(Variables.tablaPlantilla);
    }

    private void cargarDept() {
        departamentos = new ArrayList<>();
        if (us.getIsUserAdmin()) {
            departamentos = services.findAllDepartamentoByServiciosDepartamento();
        } else {
            departamento = services.buscarDepartamento(new Departamento(us.getDepts().get(0)));
            departamentos.add(departamento);
            cargarUsuariosXdepartamento();
        }
    }

    public void cargarUsuariosXdepartamento() {
        if (departamento != null) {
            usuarios = services.getUserXDepts(departamento.getId());
        } else {
            JsfUti.messageWarning(null, "Debe seleccionar el departamento.", "");
        }
    }

    private void cargarCampos() {
        for (Valores v : valores) {
            try {
                Class clazz = Class.forName(v.getValorString());
                int res = v.getValorNumeric().compareTo(BigDecimal.ONE);
                Boolean principalObj = Boolean.FALSE;
                if (res == 0) {
                    clazzPrincipal = clazz;
                    principalObj = Boolean.TRUE;
                }
                List<ReportFieldDet> fieldDets = obtenerCamposClazz(clazz);
                if (Utils.isNotEmpty(fieldDets)) {
                    String det = v.getValorString().substring(v.getValorString().lastIndexOf('.') + 1);

                    tablas.add(new ReportField(v.getValorString(), principalObj, det, fieldDets));

                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ReporteDinamicMB.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private List<ReportFieldDet> obtenerCamposClazz(Class clazz) {

        List<ReportFieldDet> fieldDets = new ArrayList<>();
        try {
            List<Field> fields = Utils.getPrivateFields(clazz);
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getType().equals(List.class)) {
                    Description d = field.getAnnotation(Description.class);
                    if (d != null && !d.value().equals(Variables.omitirCampo)) {
                        Boolean esObjeto = Boolean.FALSE;
                        System.out.println("field.getName(): " + field.getName() + " field.getType().getPackage().getName(): " + field.getType().getPackage().getName());
                        if (!field.getType().getPackage().getName().startsWith("java.")) {
                            esObjeto = Boolean.TRUE;
                        }
                        fieldDets.add(new ReportFieldDet(field.getName(), d.value(), clazz.getName(), field.getType().getName(), esObjeto));
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AppEjb.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fieldDets;
    }

    private ReportFieldDet obtenerCampoRelacion(Class clazz, String campoRelacion) {

        ReportFieldDet fieldDet = null;
        try {
            List<Field> fields = Utils.getPrivateFields(clazz);
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getType().equals(List.class)) {
                    Description d = field.getAnnotation(Description.class);
                    if (d != null && !d.value().equals(Variables.omitirCampo)) {
                        Boolean esObjeto = Boolean.FALSE;
                        System.out.println("field.getName(): " + field.getName() + " field.getType().getCanonicalName(): " + field.getType().getCanonicalName());
                        if (!field.getType().getPackage().getName().startsWith("java.") && field.getType().getCanonicalName().equals(campoRelacion)) {
                            esObjeto = Boolean.TRUE;
                            fieldDet = new ReportFieldDet(field.getName(), d.value(), clazz.getName(), field.getType().getName(), esObjeto);
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(AppEjb.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return fieldDet;
    }

    public void agregarCampo(ReportField tabla, ReportFieldDet det) {
        if (!seleccionados.contains(det)) {

            try {
                if (!det.getEsObjecto()) {
                    Boolean campoEsTablaPrincipal = Boolean.FALSE;
                    for (Valores v : valores) {
                        int res = v.getValorNumeric().compareTo(BigDecimal.ONE);
                        if (res == 0 && det.getClazz().equals(v.getValorString())) {
                            campoEsTablaPrincipal = Boolean.TRUE;
                            break;
                        }
                    }

                    if (!campoEsTablaPrincipal) {
                        ReportFieldDet campoRelacion = obtenerCampoRelacion(clazzPrincipal, det.getClazz());
                        if (campoRelacion != null) {
                            Boolean campoRelacionAdd = Boolean.FALSE;
                            for (ReportFieldDet s : seleccionados) {
                                if (s.getDetailField().equals(campoRelacion.getDetailField()) && s.getClazz().equals(campoRelacion.getClazz())) {
                                    campoRelacionAdd = Boolean.TRUE;
                                    break;
                                }
                            }
                            if (!campoRelacionAdd) {
                                campoRelacion.setOperador("-");
                                campoRelacion.setValor("-");
                                campoRelacion.setTablaHecho(Boolean.TRUE);
                                seleccionados.add(campoRelacion);
                                //JsfUti.messageError(null, "Para poder agregar el campo " + det.getDetailField() + " debe agregar el campo " + campoRelacion.getDetailField() + " de la tabla solicitud de servicios", "");
                                //return;
                            }
                        }
                    }

                    det.setTablaHecho(tabla.getTablaHecho());
                    seleccionados.add(det);

                } else {
                    det.setOperador("-");
                    det.setValor("-");
                    det.setTablaHecho(tabla.getTablaHecho());
                    seleccionados.add(det);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsfUti.messageError(null, "Campo ya fue seleccionado", "");
        }
    }

    public void eliminarCampo(ReportFieldDet det) {
        if (seleccionados.contains(det)) {
            seleccionados.remove(det);
        }
    }

    public void generarReporte() {

        if (Utils.isNotEmpty(seleccionados)) {
            try {
                for (ReportFieldDet d : seleccionados) {
                    if (d.getFecha() != null) {
                        d.setValor(Utils.dateFormatPattern("yyyy-MM-dd", d.getFecha()));
                    }
                    if (d.getFecha() != null && d.getFechaHasta() != null) {
                        if (d.getOperador() != null && d.getOperador().equals("RANGO FECHA")) {
                            String d1 = Utils.dateFormatPattern("yyyy-MM-dd", d.getFecha());
                            String h2 = Utils.dateFormatPattern("yyyy-MM-dd", d.getFechaHasta());
                            d.setValor(d1 + ";" + h2);
                        }
                    }
                    if (d.getOperador() != null && d.getValor() == null) {
                        JsfUti.messageError(null, "Debe agregar un valor para el campo: " + d.getDetailField(), "");
                        return;
                    }
                    if (d.getValor() != null && d.getOperador() == null) {
                        JsfUti.messageError(null, "Debe agregar un operador para el campo: " + d.getDetailField(), "");
                        return;
                    }
                    if (d.getOperador() != null && d.getOperador().equals("RANGO FECHA") && d.getFechaHasta() == null) {
                        JsfUti.messageError(null, "Debe ingresar la fecha hasta para el campo: " + d.getDetailField(), "");
                        return;
                    }
                }
                if (Utils.isEmptyString(nombreReporte)) {
                    nombreReporte = "Reporte_" + Utils.dateFormatPattern("yyyy-MM-dd HH:mm:ss", new Date());
                    nombreReporte = nombreReporte.replace(" ", "_").replace(":", "_");
                } else {
                    nombreReporte = nombreReporte.replace(" ", "_");
                }
                DatosReporte data = new DatosReporte();
                data.setFechadesde(desde.getTime());
                data.setFechahasta(hasta.getTime());
                data.setIncluirFechas(incluirFechas);
                Map map = new HashMap();
                map.put("DESDE", Utils.dateFormatPattern("yyyy-MM-dd", desde));
                map.put("HASTA", Utils.dateFormatPattern("yyyy-MM-dd", hasta));
                if (usuario != null) {
                    map.put("NOMBRES", usuario.getNombreUsuario());
                    data.setDepartamento(departamento.getNombre());
                    data.setUsuario(usuario.getUsuarioNombre());
                } else if (departamento != null) {
                    map.put("DEPARTAMENTO", departamento.getNombre());
                    data.setDepartamento(departamento.getNombre());
                }
                map.put("NOMBRE_REPORTE", nombreReporte);
                data.setNombreArchivoPDF(nombreReporte);
                data.setCampos(seleccionados);
                Data rest = services.generarReporteDinamico(data);
                if (rest != null) {
                    if (rest.getId() == 0L) {
                        getDownloadFile(rest.getData());
                    } else {
                        JsfUti.messageError(null, rest.getData(), "");
                    }
                } else {
                    JsfUti.messageError(null, "Intente nuevamente", "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JsfUti.messageError(null, "Debe seleccionar campos para continuar", "");
        }
    }

    public void onRowEdit(RowEditEvent<ReportFieldDet> event) {

    }

    public void onRowCancel(RowEditEvent<ReportFieldDet> event) {
    }

    public List<ReportField> getTablas() {
        return tablas;
    }

    public void setTablas(List<ReportField> tablas) {
        this.tablas = tablas;
    }

    public List<ReportFieldDet> getSeleccionados() {
        return seleccionados;
    }

    public void setSeleccionados(List<ReportFieldDet> seleccionados) {
        this.seleccionados = seleccionados;
    }

    public Date getDesde() {
        return desde;
    }

    public void setDesde(Date desde) {
        this.desde = desde;
    }

    public Date getHasta() {
        return hasta;
    }

    public void setHasta(Date hasta) {
        this.hasta = hasta;
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

    public List<User> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<User> usuarios) {
        this.usuarios = usuarios;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public String getNombreReporte() {
        return nombreReporte;
    }

    public void setNombreReporte(String nombreReporte) {
        this.nombreReporte = nombreReporte;
    }

    public Boolean getIncluirFechas() {
        return incluirFechas;
    }

    public void setIncluirFechas(Boolean incluirFechas) {
        this.incluirFechas = incluirFechas;
    }

    public List<Valores> getPlantillas() {
        return plantillas;
    }

    public void setPlantillas(List<Valores> plantillas) {
        this.plantillas = plantillas;
    }

    public String getNombrePlantilla() {
        return nombrePlantilla;
    }

    public void setNombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

}
