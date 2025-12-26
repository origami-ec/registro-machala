/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.restful;

import com.google.gson.Gson;
import com.origami.sgr.entities.AclUser;
import com.origami.sgr.entities.CtlgItem;
import com.origami.sgr.entities.DocumentoFirma;
import com.origami.sgr.entities.SolicitudServicios;
import com.origami.sgr.restful.models.UsuarioRest;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.services.interfaces.RegCertificadoService;
import com.origami.sgr.services.interfaces.RegistroPropiedadServices;
import com.origami.sgr.services.interfaces.VentanillaPubLocal;
import com.origami.sgr.util.Querys;
import com.origami.sgr.util.Utils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Anyelo
 */
@Path(value = "api/")
@Produces({"application/json"})
public class SgrRest implements Serializable {

    private static final Logger LOG = Logger.getLogger(SgrRest.class.getName());
    private static final long serialVersionUID = 1L;

    @Inject
    private Entitymanager em;
    @Inject
    private VentanillaPubLocal vp;
    @Inject
    private RegCertificadoService rcs;
    @Inject
    private RegistroPropiedadServices rps;

    @POST
    @Path(value = "/iniciarSesion")
    @Consumes(MediaType.APPLICATION_JSON)
    public UsuarioRest iniciarSesion(String data) {
        Gson gson = new Gson();
        UsuarioRest rest = gson.fromJson(data, UsuarioRest.class);
        AclUser u = (AclUser) em.find(Querys.getUsuariobyUserClave, new String[]{"user", "clave"}, new Object[]{rest.getUsuario(), rest.getClave()});
        if (u != null) {
            rest.setMensaje("OK");
        } else {
            rest.setMensaje("Usuario o contraseña incorrecto");
        }
        return rest;
    }

    @POST
    @Path(value = "/actualizarDocumento")
    @Consumes(MediaType.APPLICATION_JSON)
    public DocumentoFirma actualizarDocumento(String data) {
        Gson gson = new Gson();
        CtlgItem estado;
        DocumentoFirma rest = gson.fromJson(data, DocumentoFirma.class);
        DocumentoFirma bd = em.find(DocumentoFirma.class, rest.getId());
        if (bd != null && bd.getTipo() != null) {
            if (bd.getEstado().getCodename().equals("firma_pendiente")) {
                String nameTask = rps.getNameTaskFromNumTramite(bd.getNumTramite());
                if (Utils.isNotEmptyString(nameTask)) {
                    nameTask = nameTask.toLowerCase();
                }
                //Boolean finalizaTarea = Boolean.FALSE;
                Long oid = null;
                SolicitudServicios sb = em.find(SolicitudServicios.class, bd.getReferencia());
                //oid = rps.guardarArchivo(Base64.getDecoder().decode(rest.getArchivoFirmado()));
                sb.setDocumento(oid);
                Map map = new HashMap();
                // rps.finalizarTareaFirmaFuncionario(bd);//CONTINUAR TAREA

                em.persist(sb);
                rest.setDocumento(oid);
                map = new HashMap();
                map.put("codename", "firma_realizada");
                estado = (CtlgItem) em.findObjectByParameter(CtlgItem.class, map);
                bd.setEstado(estado);
                bd.setDocumento(oid);
                bd.setFechaFirma(new Date());
                em.persist(bd);

            }
        }
        return rest;
    }

    @GET
    @Path("/documentosPendienteFirma/{usuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DocumentoFirma> firmarDocumento(@PathParam("usuario") String usuario) {
        try {
            Map map = new HashMap();
            map.put("codename", "firma_pendiente");
            CtlgItem ci = (CtlgItem) em.findObjectByParameter(CtlgItem.class, map);

            List<DocumentoFirma> documentos = em.findAll(Querys.getDocumentosPendienteFirma,
                    new String[]{"estado", "usuario"}, new Object[]{ci.getId(), usuario});
            if (Utils.isNotEmpty(documentos)) {
                DocumentoFirma fd = documentos.get(0);
                List<DocumentoFirma> documentosFirma = em.findAll(Querys.getDocumentosUsuarioEstadoTramiteFirma,
                        new String[]{"estado", "usuario", "numTramite"}, new Object[]{ci.getId(), fd.getUsuario(), fd.getNumTramite()});
                return documentosFirma;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @GET
    @Path("/generarPDF/{oid}")
    @Produces("application/pdf")
    public byte[] generarDocumentoPdf(@PathParam("oid") String oid) {
        System.out.println("oid: " + oid);
        byte[] pdfContents = null;
        if (oid != null) {
            File f = vp.generarPDF(Long.valueOf(oid));

            if (f != null) {
                try {
                    pdfContents = Files.readAllBytes(f.toPath());
                    f.delete();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        return pdfContents;
    }

    @GET
    @Path("validacion/certificado/{code}")
    @Produces("application/pdf")
    public byte[] validacionDocumento(@PathParam("code") String validationCode) throws IOException, SQLException {
        try {
            String ruta = rcs.findByValidationCode2(validationCode);
            File archivo = new File(ruta);
            if (archivo.exists()) {
                try {
                    return Files.readAllBytes(archivo.toPath());
                } catch (IOException e) {
                    LOG.log(Level.SEVERE, null, e);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return null;
    }

}
