/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.origami.sgr.restful;

import com.origami.sgr.ebilling.services.OrigamiGTService;
import com.origami.sgr.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.StreamingOutput;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author asanc
 */
@Named("imagenRP")
@Path("/imagenRP")
public class ImagenRest {

    private static final Logger LOG = Logger.getLogger(ImagenRest.class.getName());

    @Inject
    private OrigamiGTService service;

//    @GET
//    @Path("/imagen-url/{imagenUrl}")
//    @Produces("image/png")
//    public byte[] imagen(@PathParam("imagenUrl") String imagen) {
//        try {
//            System.out.println("Utils.base64ToString(imagen) " + Utils.base64ToString(imagen));
//            return (byte[]) service.methodGETwithouAuth(Utils.base64ToString(imagen), byte[].class);
//        } catch (Exception e) {
//            LOG.log(Level.SEVERE, null, e);
//        }
//        return null;
//    }
    @GET
    @Path("/imagen-url/{imagenUrl}")
    @Produces("image/png")
    public StreamingOutput imagen(@PathParam("imagenUrl") String imagen) {
        return output -> {
            InputStream is = streamGET(Utils.base64ToString(imagen));
            IOUtils.copy(is, output);
            output.flush();
        };
    }

    public InputStream streamGET(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = client.execute(get);
        return response.getEntity().getContent(); // STREAM
    }

}
