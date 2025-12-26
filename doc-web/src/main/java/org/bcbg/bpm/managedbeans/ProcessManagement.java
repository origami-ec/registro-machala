package org.bcbg.bpm.managedbeans;

import org.bcbg.config.SisVars;
import org.bcbg.entities.Departamento;
import org.bcbg.entities.TipoTramite;
import org.bcbg.managedbeans.UsuariosMB;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.activiti.engine.repository.ProcessDefinition;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Origami
 *
 */
@Named
@ViewScoped
public class ProcessManagement extends BpmManageBeanBaseRoot implements Serializable {

    private List<TipoTramite> procedures = null, selProcedures;
    private List<ProcessDefinition> process = null;
    private StreamedContent stream;
    private TipoTramite tipoTramite;
    private List<Departamento> departamentos;
    private String imagen;

    @PostConstruct
    public void iniView() {
        tipoTramite = new TipoTramite();
        //tipoTramite.setTramiteVentanillaUnica(Boolean.FALSE);
        procedures = appServices.getTipoTramites();
        departamentos = appServices.getListDepartamentosHijos();
    }

    public List<ProcessDefinition> getProcessDeployments() {
        try {
            if (procedures != null) {
                process = new ArrayList<>();
                List<ProcessDefinition> pro = this.getProcesosDesplegados();
                pro.forEach((p) -> {
                    for (TipoTramite t : procedures) {
                        if (p.getKey().equals(t.getActivitykey().trim())) {
                            process.add(p);
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
        return process;
    }

    public void loadProcess() {
        try {
            if (selProcedures != null) {
                if (!selProcedures.isEmpty()) {
                    selProcedures.forEach((p) -> {
                        this.loadProcessByClassPath(p.getArchivoBpmn());
                    });
                    JsfUti.update("formMain:tdatos:dtprocessDep");
                    JsfUti.messageInfo(null, "Nota", "Se desplegaron " + selProcedures.size() + " proceso(s) satisfactoriamente.");
                } else {
                    JsfUti.messageWarning(null, "Advertencia", "Debe elegir al menos un proceso del listado");
                }
            } else {
                JsfUti.messageWarning(null, "Advertencia", "Debe elegir al menos un proceso del listado");
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void cargarProceso(TipoTramite tipo) {
        try {
            if (!tipo.getEstado()) {
                JsfUti.messageWarning(null, "Advertencia", "No se puede cargar proceso Inactivo.");
                return;
            }
            if (tipo.getArchivoBpmn() != null) {
                this.loadProcessByClassPath(tipo.getArchivoBpmn());
                JsfUti.update("formMain:tdatos:dtprocessDep");
                JsfUti.messageInfo(null, "Nota", "Se desplego el proceso (" + tipo.getActivitykey() + ") satisfactoriamente.");
            } else {
                JsfUti.messageWarning(null, "Advertencia", "No se encuentra ruta de archivo bpmn.");
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgDiagram(String id, String name) {
        try {
            stream = this.imagenProceso(id, name);
            JsfUti.update("frmDiagrama");
            JsfUti.executeJS("PF('dlgDiagrama'));");
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void showDlgProcess() {
        JsfUti.update("formProcess");
        JsfUti.executeJS("PF('dlgProcess').show();");
    }

    public void editProcess(TipoTramite gtp) {
        tipoTramite = gtp;
        this.showDlgProcess();
    }

    public void newProcess() {
        tipoTramite = new TipoTramite();
        this.showDlgProcess();
    }

    public void saveProcess() {
        try {

            Boolean tipo = tipoTramite.getInterno() == null ? Boolean.FALSE : tipoTramite.getInterno();
            tipoTramite.setInterno(tipo);

            if (this.validar()) {

                /*   if (validacionAbr()) {
                    JsfUti.messageWarning(null, "Mensaje:", "El código de la abreviatura del trámite ya existe o no puede ser mayor a 3 caracteres");
                    return;
                }*/
                TipoTramite t = (TipoTramite) service.methodPOST(tipoTramite, SisVars.ws + "create/tipoTramite", TipoTramite.class);
                if (t != null && t.getId() != null) {
                    procedures = appServices.getTipoTramites();
                    JsfUti.messageInfo(null, "Mensaje:", "Transaccion con éxito.");
                    JsfUti.update("formMain");
                    JsfUti.executeJS("PF('dlgProcess').hide();");
                } else {
                    JsfUti.messageError(null, "Mensaje:", "Error al crear.");
                }
            } else {
                JsfUti.messageWarning(null, "Advertencia", "Todos lo campos deben ser ingresados.");
            }
        } catch (Exception e) {
            Logger.getLogger(ProcessManagement.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public boolean validar() {
        if (tipoTramite.getDescripcion().isEmpty()) {
            return false;
        }
        if (tipoTramite.getAbreviatura().isEmpty()) {
            return false;
        }
        if (!tipoTramite.getInterno()) {
            return true;
        }
        if (tipoTramite.getActivitykey().isEmpty()) {
            return false;
        }
        if (tipoTramite.getArchivoBpmn().isEmpty()) {
            return false;
        }
        if (tipoTramite.getColor() == null || tipoTramite.getColor().isEmpty()) {
            return false;
        }
        return true;
    }

    public boolean validacionAbr() {

        TipoTramite ti = new TipoTramite();
        List<TipoTramite> lista = new ArrayList<>();
        if (tipoTramite.getAbreviatura().length() > 3) {
            return true;
        }

        if (tipoTramite.getId() == null) {
            ti = (TipoTramite) service.methodGET(SisVars.ws + "tiposTramite/abr/" + tipoTramite.getAbreviatura(), TipoTramite.class);
            if (ti == null) {
                return false;
            } else if (Objects.equals(tipoTramite.getId(), ti.getId())) {
                return false;
            } else {
                return true;
            }
        } else {
            lista = (List<TipoTramite>) service.methodListGET(SisVars.ws + "tipo/tramite/validacion/" + tipoTramite.getId() + "/" + tipoTramite.getAbreviatura(), TipoTramite[].class);
            if (lista.isEmpty()) {
                return false;
            } else {
                Boolean valida = Boolean.FALSE;
                for (TipoTramite l : lista) {
                    if (!Objects.equals(l.getId(), tipoTramite.getId())) {
                        valida = true;
                        break;
                    } else {
                        valida = false;
                    }
                }
                return valida;
            }
        }
    }

    @Override
    public void handleFileUpload(FileUploadEvent event) {
        try {
            imagen = event.getFile().getFileName();
            File f = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
            tipoTramite.setUrlImagen("resource/image/" + Utils.getFilterRuta(f.getAbsolutePath()));
        } catch (IOException ex) {
            JsfUti.messageWarning(null, "Intente nuevamente", "");
            Logger.getLogger(UsuariosMB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public List<TipoTramite> getProcedures() {
        return procedures;
    }

    public void setProcedures(List<TipoTramite> procedures) {
        this.procedures = procedures;
    }

    public List<TipoTramite> getSelProcedures() {
        return selProcedures;
    }

    public void setSelProcedures(List<TipoTramite> selProcedures) {
        this.selProcedures = selProcedures;
    }

    public StreamedContent getStream() {
        return stream;
    }

    public void setStream(StreamedContent stream) {
        this.stream = stream;
    }

    public TipoTramite getTipoTramite() {
        return tipoTramite;
    }

    public void setTipoTramite(TipoTramite tipoTramite) {
        this.tipoTramite = tipoTramite;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

//</editor-fold>
}
