/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bcbg.documental.managedbeans;

import com.google.gson.Gson;
import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.Color;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.Nota;
import org.bcbg.models.Data;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Messages;
import org.bcbg.util.Utils;
import org.primefaces.extensions.event.ImageAreaSelectEvent;
import org.primefaces.model.CroppedImage;

/**
 *
 * @author ORIGAMI
 */
@Named
@ViewScoped
public class EditarDocumentoMB extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private UserSession us;
    private Date fecha;
    private Boolean crearNota, verTodasNotas, recortarImagen;
    private ArchivoDocs archivo;
    private Nota nota, notaSeleccionada;
    private Integer indexImagen, indexNota;
    private List<Color> colores;
    private Color color;
    private CroppedImage croppedImage;
    private String newImageName;
    private String tipoAnotacion;
    
    private List<Data> sellos;
    private Data sello;
    
    @PostConstruct
    public void init() {
        if (ss.tieneParametro("archivoId")) {
            nota = new Nota();
            fecha = new Date();
            verTodasNotas = Boolean.FALSE;
            String archivoId = (String) ss.retornarValor("archivoId");
            archivo = documentalService.consultarArchivo(new ArchivoDocs(archivoId));
            colores = documentalService.getColores();
            sellos = documentalService.getSellos();
            for(Data d: sellos){
                System.out.println("d. "+d.getData());
            }
            System.out.println("archivoId: " + archivoId);
        } else {
            JsfUti.messageError(null, "No existe archivo", "Intente nuevamente");
            JsfUti.redirect(JsfUti.getHostContextUrl() + "/procesos/documentos/origamiDocs");
        }

    }

    public void selectEndListener(ImageAreaSelectEvent e) {
           
            switch(tipoAnotacion){
                case SisVars.nota:
                    nota.setAlto( SisVars.maxAltoNota);
                    nota.setAncho(SisVars.maxAnchoNota);
                    break;
                case SisVars.linea:
                    nota.setAlto(SisVars.maxAltoLinea);
                    nota.setAncho(e.getWidth());
                    break;
                case SisVars.remarcado:
                    nota.setAlto(SisVars.maxAltoRemarcado);
                    nota.setAncho(e.getWidth());
                    break;
                case SisVars.sello:
                    nota.setAlto(e.getHeight());
                    nota.setAncho(e.getWidth());
                    break;
                default:
                    nota.setAlto(e.getHeight());
                    nota.setAncho(e.getWidth());
                    break;
            }
            
            
            
            nota.setTipo(tipoAnotacion);
           
            nota.setFecha(Utils.getDate(new Date()));
            nota.setUsuario(us.getUsuarioDocs());
            nota.setPosicionX1(e.getX1());
            nota.setPosicionX2(e.getX2());
            nota.setPosicionY1(e.getY1());
            nota.setPosicionY2(e.getY2());

            indexImagen = Integer.parseInt(e.getComponent().getClientId().replace("dtPdfs:", "").replace(":areaSelect", ""));
            System.out.println("indexImagen: " + indexImagen);
            System.out.println("e.getWidth(): " + e.getWidth());
            System.out.println("e.getHeight(): " + e.getHeight());

            JsfUti.executeJS("PF('dlgNota').show()");
            JsfUti.update("formNota"); 

    }

    public void actualizarIndexacion() {
        if (Utils.isEmptyString(archivo.getDetalleDocumento())) {
            JsfUti.messageError(null, Messages.descripcionArchivo, "");
            return;
        }
        for (IndexacionCampo ic : archivo.getDetalles()) {
            if (ic.getObligatorio()) {
                if (Utils.isEmptyString(ic.getDetalle())) {
                    JsfUti.messageError(null, Messages.camposObligatorios, "");
                    return;
                }
            }
        }
        archivo = documentalService.actualizarDatosIndex(archivo);
        JsfUti.messageInfo(null, Messages.correcto, "");
    }

    public void grabarNota() {
        if (nota != null && Utils.isEmptyString(nota.getTitulo())) {
            JsfUti.messageError(null, "Debe ingresar el título de la anotación", "");
            return;
            }
        if(tipoAnotacion.equals(SisVars.nota)){
            
            if (nota != null && Utils.isEmptyString(nota.getNota())) {
                JsfUti.messageError(null, "Debe ingresar una descripción", "");
                return;
            }
        }else if(tipoAnotacion.equals(SisVars.sello)){
         if(sello == null ){
             JsfUti.messageError(null, "Debe escoger un sello", "");
                return;
         }else{
             nota.setNota(sello.getData()); ///URL DEL SELLO
         }
        }else{
            nota.setNota("");
        }
        if(!tipoAnotacion.equals(SisVars.sello)){ //SI ES SELLO NO LLEVA COLOR
             if (color == null || Utils.isEmptyString(color.getColor())) {
            JsfUti.messageError(null, "Debe escoger un color", "");
            return;
        }
        }else{
            color = new Color();
            color.setColor("#FFFFFF");
        }
       
        nota.setColor(color.getColor());
        System.out.println("nota: " + nota.toString());
        Data rest = documentalService.guardarNota(archivo.getId(), nota, indexImagen);
        if (rest != null) {
            if (rest.getId() == 0) {
                archivo = new Gson().fromJson(rest.getData(), ArchivoDocs.class);
                JsfUti.messageInfo(null, tipoAnotacion + " guardada correctamente", "");
                JsfUti.executeJS("PF('dlgNota').hide()");
                //JsfUti.update("dtPdfs:" + indexImagen + ":pngNota");
                //JsfUti.update("dtPdfs");
                List<Nota> notas = archivo.getImagenes().get(indexImagen).getNotas();
                visualizarNota(notas.get(notas.size() - 1));
                JsfUti.messageInfo(null, Messages.correcto, "");
            } else {
                JsfUti.messageInfo(null, "Intente nuevamente", rest.getStatus());
                JsfUti.executeJS("PF('dlgNota').hide()");
            }

        } else {
            JsfUti.messageError(null, "No se pudo guardar la anotación" , Messages.intenteNuevamente);
        }
    }

    public void visualizarNota(Nota nota) {
        notaSeleccionada = nota;
        verTodasNotas = Boolean.FALSE;
        JsfUti.update("dtPdfs");

    }

    public void eliminarNota(Nota notaEliminar, int indexNota, int indexImagen) {
        Data rest = documentalService.eliminarNota(archivo.getId(), notaEliminar, indexImagen, indexNota);
        if (rest != null) {
            if (rest.getId() == 0) {
                archivo = new Gson().fromJson(rest.getData(), ArchivoDocs.class);
                System.out.println("eliminarNota - archivo.getId(): " + archivo.getId());
                JsfUti.messageInfo(null, "Nota eliminada correctamente", "");
                JsfUti.update("dtPdfs:" + indexNota + ":pngNota");
                //JsfUti.update("dtPdfs");
            } else {
                JsfUti.messageInfo(null, "Intente nuevamente", rest.getStatus());
                JsfUti.executeJS("PF('dlgNota').hide()");
            }

        } else {
            JsfUti.messageError(null, "No se pudo eliminar la nota", "Intente nuevamente");
        }
    }

    public void crop() {
        if (croppedImage == null) {
            return;
        }

        setNewImageName(getRandomImageName());
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        String newFileName = externalContext.getRealPath("") + File.separator + "resources" + File.separator + "demo"
                + File.separator + "images" + File.separator + "crop" + File.separator + getNewImageName() + ".jpg";

        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(croppedImage.getBytes(), 0, croppedImage.getBytes().length);
            imageOutput.close();
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cropping failed."));
            return;
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Cropping finished."));
    }

    private String getRandomImageName() {
        int i = (int) (Math.random() * 100000);

        return String.valueOf(i);
    }

    public void limpiarNotaSeleccionada() {
      
        notaSeleccionada = null;
    }

    public void cerrarDialogoNota() {
        JsfUti.executeJS("PF('dlgNota').hide()");
    }

    public void visualizarDocumento() {
            if (!archivo.getFormato().startsWith("IMG")) {
            getPDF(archivo.getRuta());
        } else {
            JsfUti.redirectNewTab(Utils.getUrlDownloadFile(archivo.getRuta()));
        }
    }

    public void imprimirNotas() {
        ArchivoDocs rest = documentalService.imprimirNotas(archivo);
        if (rest != null && Utils.isNotEmptyString(rest.getRutaNotas())) {
            getPDF(rest.getRutaNotas());
        } else {
            JsfUti.messageError(null, Messages.intenteNuevamente, "");
        }
    }

    public ArchivoDocs getArchivo() {
        return archivo;
    }

    public void setArchivo(ArchivoDocs archivo) {
        this.archivo = archivo;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Nota getNotaSeleccionada() {
        return notaSeleccionada;
    }

    public void setNotaSeleccionada(Nota notaSeleccionada) {
        this.notaSeleccionada = notaSeleccionada;
    }

    public Boolean getCrearNota() {
        return crearNota;
    }

    public void setCrearNota(Boolean crearNota) {
        this.crearNota = crearNota;
    }

    public Boolean getVerTodasNotas() {
        return verTodasNotas;
    }

    public void setVerTodasNotas(Boolean verTodasNotas) {
        this.verTodasNotas = verTodasNotas;
    }

    public List<Color> getColores() {
        return colores;
    }

    public void setColores(List<Color> colores) {
        this.colores = colores;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public CroppedImage getCroppedImage() {
        return croppedImage;
    }

    public void setCroppedImage(CroppedImage croppedImage) {
        this.croppedImage = croppedImage;
    }

    public String getNewImageName() {
        return newImageName;
    }

    public void setNewImageName(String newImageName) {
        this.newImageName = newImageName;
    }

    public Boolean getRecortarImagen() {
        return recortarImagen;
    }

    public void setRecortarImagen(Boolean recortarImagen) {
        this.recortarImagen = recortarImagen;
    }

    public String getTipoAnotacion() {
        return tipoAnotacion;
    }

    public void setTipoAnotacion(String tipoAnotacion) {
        this.tipoAnotacion = tipoAnotacion;
    }

    public List<Data> getSellos() {
        return sellos;
    }

    public void setSellos(List<Data> sellos) {
        this.sellos = sellos;
    }

    public Data getSello() {
        return sello;
    }

    public void setSello(Data sello) {
        this.sello = sello;
    }
    
    
    
    

}
