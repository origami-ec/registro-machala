/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.ws;

import java.io.File;
import java.nio.file.Files;
import org.bcbg.config.SisVars;
import org.bcbg.documental.models.Archivador;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.commons.io.FileUtils;
import org.bcbg.documental.models.ArchivoDocs;
import org.bcbg.documental.models.ArchivoIndexDto;
import org.bcbg.documental.models.Color;
import org.bcbg.documental.models.Formato;
import org.bcbg.documental.models.ImagenNota;
import org.bcbg.documental.models.Indexacion;
import org.bcbg.documental.models.IndexacionCampo;
import org.bcbg.documental.models.Nota;
import org.bcbg.documental.models.Tesauro;
import org.bcbg.documental.models.TipoDato;
import org.bcbg.entities.Documentos;
import org.bcbg.models.Data;
import org.bcbg.session.UserSession;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author Origami
 */
@Stateless
public class DocumentalEjb implements DocumentalService {

    @Inject
    private BcbgService service;
    @Inject
    private UserSession us;

    @Override
    public DefaultTreeNode getTreeNodes() {
        try {
            DefaultTreeNode root = new DefaultTreeNode(new Archivador("Menu Principal", "A", new Date(), 0));
            DefaultTreeNode node;
            List<Archivador> list = service.methodListGET(SisVars.ws + "archivadores/padres", Archivador[].class);
            for (Archivador arc : list) {
                node = new DefaultTreeNode(arc, root);
                this.fillSons(node, arc.getId());
            }
            return root;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void fillSons(DefaultTreeNode node, Long archivador) {
        DefaultTreeNode temp;
        List<Archivador> list = service.methodListGET(SisVars.ws + "archivadores/hijos/" + archivador, Archivador[].class);
        for (Archivador arc : list) {
            temp = new DefaultTreeNode(arc, node);
            this.fillSons(temp, arc.getId());
        }
    }

    @Override
    public ArchivoDocs guardarArchivo(UploadedFile archivo, ArchivoIndexDto archivoIndex) {
        return (ArchivoDocs) service.methodUploadFile(us.getUsuarioDocs(), archivoIndex, archivo.getContentType(), archivo.getFileName(), archivo.getContent(), SisVars.wsDocs + "archivo/cargarDocumento", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs guardarArchivo(String contentType, String nameFile, byte[] content, ArchivoIndexDto archivoIndex) {
        return (ArchivoDocs) service.methodUploadFile(us.getUsuarioDocs(), archivoIndex, contentType, nameFile, content, SisVars.wsDocs + "archivo/cargarDocumento", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs guardarArchivo(File archivo, ArchivoIndexDto archivoIndex) {
        String contentType;
        byte[] bytes = null;
        try {
            contentType = Files.probeContentType(archivo.toPath());
            bytes = FileUtils.readFileToByteArray(archivo);
        } catch (Exception e) {
            contentType = "";
            e.printStackTrace();
        }
        if (bytes != null) {
            return (ArchivoDocs) service.methodUploadFile(us.getUsuarioDocs(), archivoIndex, contentType, archivo.getName(), bytes, SisVars.wsDocs + "archivo/cargarDocumento", ArchivoDocs.class);
        } else {
            return null;
        }
    }

    @Override
    public ArchivoDocs actualizarDatosIndex(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.wsDocs + "archivo/actualizarDatosIndex", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs imprimirNotas(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.wsDocs + "archivo/imprimirNotas", ArchivoDocs.class);
    }

    @Override
    public List<ArchivoDocs> busquedaAvanzada(ArchivoIndexDto index) {
        return service.methodListPOST(index, SisVars.wsDocs + "archivo/busquedaAvanzada", ArchivoDocs[].class);
    }

    @Override
    public Data guardarNota(String archivoId, Nota nota, Integer indexImagen) {
        ImagenNota imagenNota = new ImagenNota(archivoId, SisVars.notaAgregar, nota);
        imagenNota.setIndiceImagen(indexImagen);
        return (Data) service.methodPOST(imagenNota, SisVars.wsDocs + "archivo/agregarNota", Data.class);
    }

    @Override
    public Data eliminarNota(String archivoId, Nota nota, Integer indexImagen, Integer indexNota) {
        ImagenNota imagenNota = new ImagenNota(archivoId, SisVars.notaEliminar, nota);
        imagenNota.setIndiceImagen(indexImagen);
        imagenNota.setIndiceNota(indexNota);
        return (Data) service.methodPOST(imagenNota, SisVars.wsDocs + "archivo/agregarNota", Data.class);
    }

    @Override
    public ArchivoDocs consultarArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.wsDocs + "archivo/consultar", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs convertirArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.wsDocs + "archivo/convertir", ArchivoDocs.class);
    }

    @Override
    public ArchivoDocs eliminarArchivo(ArchivoDocs archivo) {
        return (ArchivoDocs) service.methodPOST(archivo, SisVars.wsDocs + "archivo/eliminar", ArchivoDocs.class);
    }

    @Override
    public List<Color> getColores() {
        return (List<Color>) service.methodListGET(SisVars.wsDocs + "colores", Color[].class);
    }

    @Override
    public Formato guardarFormato(Formato formato) {
        return (Formato) service.methodPOST(formato, SisVars.wsDocs + "formato/grabar", Formato.class);
    }

    @Override
    public List<Formato> getFormatos() {
        return (List<Formato>) service.methodListGET(SisVars.wsDocs + "formatos", Formato[].class);
    }

    @Override
    public List<TipoDato> getTipoDatos() {
        return (List<TipoDato>) service.methodListGET(SisVars.wsDocs + "tipoDatos", TipoDato[].class);
    }

    @Override
    public List<IndexacionCampo> getIndicesPredeterminados() {
        return (List<IndexacionCampo>) service.methodListGET(SisVars.wsDocs + "indicePredeterminados", IndexacionCampo[].class);
    }

    @Override
    public List<Indexacion> getIndices() {
        return (List<Indexacion>) service.methodListGET(SisVars.wsDocs + "indexacion/consultar", Indexacion[].class);
    }

    @Override
    public Indexacion guardarIndexacion(Indexacion indexacion) {
        return (Indexacion) service.methodPOST(indexacion, SisVars.wsDocs + "indexacion/grabar", Indexacion.class);
    }

    @Override
    public Indexacion validarIndexacionFormulario(Indexacion indexacion) {
        return (Indexacion) service.methodPOST(indexacion, SisVars.wsDocs + "indexacion/validar", Indexacion.class);
    }

    @Override
    public List<Data> getSellos() {
        return (List<Data>) service.methodListGET(SisVars.wsDocs + "archivo/sellos", Data[].class);
    }

    @Override
    public List<Documentos> getDocumentosScan() {
        return (List<Documentos>) service.methodListGET(SisVars.ws + "documents/consultar/" + us.getName_user(), Documentos[].class);
    }

    @Override
    public Documentos guardarDocumento(Documentos documentos) {
        return (Documentos) service.methodPOST(documentos, SisVars.ws + "documents/grabar", Documentos.class);
    }

    @Override
    public Tesauro validarIndexacionTesauro(Tesauro data) {
        return (Tesauro) service.methodPOST(data, SisVars.wsDocs + "tesauro/validar", Tesauro.class);
    }

    @Override
    public Tesauro guardarTesauro(Tesauro data) {
        return (Tesauro) service.methodPOST(data, SisVars.wsDocs + "tesauro/grabar", Tesauro.class);
    }

    @Override
    public List<Tesauro> getTesauros() {
        return (List<Tesauro>) service.methodListGET(SisVars.ws + "tesauro/consultar", Tesauro[].class);
    }
}
