/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.managedbeans;

import com.itextpdf.text.pdf.PdfReader;
import org.bcbg.bpm.managedbeans.BpmManageBeanBaseRoot;
import org.bcbg.config.SisVars;
import org.bcbg.entities.SolicitudDocumentos;
import org.bcbg.entities.firmaelectronica.DocumentoElectronico;
import org.bcbg.entities.firmaelectronica.FirmaElectronica;
import org.bcbg.models.FirmaElectronicaModel;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.FilesUtil;
import org.bcbg.util.JsfUti;
import org.bcbg.util.Utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import org.bcbg.ws.BcbgService;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class FirmaElectronicaDocMB extends BpmManageBeanBaseRoot implements Serializable {

    @Inject
    private BcbgService service;
    @Inject
    private UserSession us;
    @Inject
    protected ServletSession ss;
    private FirmaElectronica firmaElectronica, firmaTemp;
    private DocumentoElectronico documento;
    private Boolean validar;
    private File file;
    private List<File> files;
    private UploadedFile uploadedFile;
    private List<SolicitudDocumentos> documentos, documentosNuevos;
    private StreamedContent streamedContent;
    private Date fechaFirmar;
    private String motivo, localizacion;
    private Integer numeroPagina;

    @PostConstruct
    private void init() {
        try {
            loadModel();

        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }
    }

    private void loadModel() {
        validar = Boolean.FALSE;
        firmaElectronica = new FirmaElectronica();
        documento = new DocumentoElectronico();
        documentos = new ArrayList<>();
        documentosNuevos = new ArrayList<>();
        files = new ArrayList<>();
        fechaFirmar = new Date();
        firmaTemp = new FirmaElectronica();
        ss.borrarDatos();

    }

    //<editor-fold defaultstate="collapsed" desc="Validacion de firma">
    public void validarDocumento() {
        if (Utils.isNotEmptyString(firmaElectronica.getArchivoFirmado())) {
            validar = Boolean.TRUE;
            documento = (DocumentoElectronico) service.methodPOST(firmaElectronica, SisVars.wsFirmaEC + "firmaElectronica/verificarDocumento", DocumentoElectronico.class);
        } else {
            validar = Boolean.FALSE;
            JsfUti.messageError(null, "Debe subir un documento para continuar", "");
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            File file = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);
            firmaElectronica.setArchivo(event.getFile().getFileName());
            firmaElectronica.setArchivoFirmado(file.getAbsolutePath());
            JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");

        } catch (IOException e) {
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
        }
    }
//</editor-fold>

    public void handleFileUpload2(FileUploadEvent event) {
        try {
            
            uploadedFile = event.getFile();
            
            file = FilesUtil.copyFileServer(event.getFile(), SisVars.rutaRepositorioArchivo);

            PdfReader reader = new PdfReader(uploadedFile.getInputStream());
            numeroPagina = reader.getNumberOfPages();
            reader.close();
            JsfUti.messageInfo(null, "Información", "El archivo se subió correctamente.");
            JsfUti.update("formMain");
        } catch (IOException e) {
            System.out.println("Exception " + e.getMessage());
            JsfUti.messageError(null, "Ocurrió un error al subir el archivo", "");
        }
    }

    public void agregarFirmarDocumento(boolean imagenPDF) {
        try {
            if (fechaFirmar == null) {
                JsfUti.messageWarning(null, "Debe seleccionar la fecha con la que requiere ver la firma", "");
                return;
            }
            if (numeroPagina == null) {
                JsfUti.messageWarning(null, "Debe ingresar el número de la página a firmar", "");
                return;
            }
            Integer numeroReal = Utils.numeroPaginas(file.getAbsolutePath());
            if (numeroPagina > numeroReal) {
                JsfUti.messageWarning(null, "El número de página de ser menor al número de páginas del documento #Páginas: " + numeroReal, "");
                return;
            }
            if (numeroPagina <= 0) {
                JsfUti.messageWarning(null, "El número de página de ser mayor al número de páginas del documento #Páginas: " + numeroReal, "");
                return;
            }
            if (file != null && uploadedFile != null) {

                if (us.getFirmaElectronica() != null) {
                    FirmaElectronica fe = us.getFirmaElectronica();
                    fe.setArchivoFirmar(file.getAbsolutePath());
                    fe.setMotivo(motivo);
                    fe.setNumeroPagina(numeroPagina);
                    fe.setUrlQr("");

                    FirmaElectronicaModel firma = new FirmaElectronicaModel();
                    firma.setFechaCreacion(new Date());
                    firma.setFechaFirmar(new Date());
                    firma.setFirmaElectronica(fe);

                    ss.instanciarParametros();

                    ss.agregarParametro("firmaElectronicaModel", firma);
                    ss.agregarParametro("imagenPDF", imagenPDF);
                    ss.agregarParametro("fechaFirma", fechaFirmar);
                    if (!imagenPDF) {
                        JsfUti.openDialogFrame("/resources/dialog/dlgFirmaElectronicaFecha.xhtml", "95%", "95%");
                    } else {
                        JsfUti.openDialogFrame("/resources/dialog/dlgFirmaElectronicaFecha.xhtml", "95%", "95%");
                    }

                } else {
                    JsfUti.messageError(null, "Debe cargar y validar su firma electrónica para continuar", "");
                }
            } else {
                JsfUti.messageError(null, "Debe subir un documento para continuar", "");
            }
        } catch (Exception e) {
            System.out.println("exception file " + e.getMessage());
        }
    }

    public void archivoFirmado(SelectEvent event) {
        try {
            cerrrDlogo();
            FirmaElectronica fe = (FirmaElectronica) event.getObject();
            firmaTemp = new FirmaElectronica();
            firmaTemp = fe;
            System.out.println("firmaTemp: " + firmaTemp.toString());
            if (fe != null) {
                File f = new File(fe.getArchivoFirmar());
                if (f.exists()) {
                    f.delete();
                }
                //  agregarDocumento(Boolean.TRUE, fe.getArchivoFirmado());

                // streamedContent = createStream(fe.getArchivoFirmado());
                // /servers_files/archivos/1609864384530_
                String[] nombreArchivo = fe.getArchivoFirmado().split("_");
                String archivoDescarga = "";
                for (int i = 2; i < nombreArchivo.length; i++) {
                    archivoDescarga = archivoDescarga + " " + nombreArchivo[i];
                }
                ss.setNombreDocumento(archivoDescarga);
                ss.setUrlWebService(fe.getUrlArchivoFirmado());
                PrimeFaces.current().executeScript("PF('DlgoDocumento').hide()");
                PrimeFaces.current().ajax().update("formDocumento");
                PrimeFaces.current().ajax().update("formMain");
                JsfUti.redirectNewTab(fe.getUrlArchivoFirmado());
            } else {
                JsfUti.messageError(null, "Error al firmar electrónicamente", "Intente nuevamente");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception archivoFirmado " + e.getMessage());
        }
    }

    public void agregarDocumento(Boolean tieneFirma, String archivo) {
        if (file != null && uploadedFile != null) {
            SolicitudDocumentos documento = new SolicitudDocumentos();
            documento.setEstado("A");
            documento.setFechaCreacion(new Date());
            documento.setNombreArchivo(uploadedFile.getFileName());
            documento.setTipoArchivo(uploadedFile.getContentType());
            documento.setTieneFirmaElectronica(tieneFirma);
            documento.setRutaArchivo(!archivo.isEmpty() ? archivo : file.getAbsolutePath());
            documento.setUsuario(us.getName_user());
            documento.setSolicitud(null);
            documentosNuevos.add(documento);
            JsfUti.executeJS("PF('DlgoDocumento').hide()");
            file = null;
            uploadedFile = null;
        } else {
            JsfUti.messageError(null, "Debe subir un documento para continuar", "");
        }
    }

    public void eliminarDocumento(SolicitudDocumentos doc) {

        service.methodPUT(doc, SisVars.ws + "solicitudDocumento/eliminar", String.class);
        JsfUti.update("formMain");
        JsfUti.messageInfo(null, "Su registro se elimino correctamente", null);

    }

    public void cerrrDlogo() {
        JsfUti.executeJS("PF('DlgoDocumento').hide()");
        JsfUti.update("formDocumento");
    }

    public void getFileContent(String file) throws FileNotFoundException, IOException {

        //    byte[] document = pruebaDocu(file);
        //    streamedContent = new DefaultStreamedContent().builder().stream(() -> new ByteArrayInputStream(document)).contentType("application/pdf").build();
    }

    public String generateRandomIdForNotCaching() {
        return java.util.UUID.randomUUID().toString();
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public FirmaElectronica getFirmaElectronica() {
        return firmaElectronica;
    }

    public void setFirmaElectronica(FirmaElectronica firmaElectronica) {
        this.firmaElectronica = firmaElectronica;
    }

    public DocumentoElectronico getDocumento() {
        return documento;
    }

    public void setDocumento(DocumentoElectronico documento) {
        this.documento = documento;
    }

    public Boolean getValidar() {
        return validar;
    }

    public void setValidar(Boolean validar) {
        this.validar = validar;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public List<SolicitudDocumentos> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<SolicitudDocumentos> documentos) {
        this.documentos = documentos;
    }

    public List<SolicitudDocumentos> getDocumentosNuevos() {
        return documentosNuevos;
    }

    public void setDocumentosNuevos(List<SolicitudDocumentos> documentosNuevos) {
        this.documentosNuevos = documentosNuevos;
    }

    public StreamedContent getStreamedContent() {

        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public FirmaElectronica getFirmaTemp() {
        return firmaTemp;
    }

    public void setFirmaTemp(FirmaElectronica firmaTemp) {
        this.firmaTemp = firmaTemp;
    }

    public Date getFechaFirmar() {
        return fechaFirmar;
    }

    public void setFechaFirmar(Date fechaFirmar) {
        this.fechaFirmar = fechaFirmar;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getNumeroPagina() {
        return numeroPagina;
    }

    public void setNumeroPagina(Integer numeroPagina) {
        this.numeroPagina = numeroPagina;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

//</editor-fold>
}
