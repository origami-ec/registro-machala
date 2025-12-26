/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.restful;

import org.bcbg.config.SisVars;
import org.bcbg.session.ServletSession;
import org.bcbg.ws.AppServices;
import java.io.InputStream;
import java.io.OutputStream;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;

/**
 * REST Web Service
 *
 * @author ORIGAMI1
 */
@Path("pdfDocumentos")
@RequestScoped
public class PDFDocumentos {

    @Inject
    private AppServices appServices;

    public PDFDocumentos() {
    }

    @GET
    @Path("/pdf/{ruta}")
    @Produces("application/pdf")
    public StreamingOutput consultarDocumento(@PathParam("ruta") String ruta) {
        InputStream is = appServices.getInputStreamReporte(SisVars.wsMedia + "resource/pdf/" + ruta + "/descarga/inline");
        return (OutputStream output) -> {
            IOUtils.copy(is, output);
        };
    }

}
