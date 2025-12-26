/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import com.origami.session.ServletSession;
import com.origami.session.UserSession;
import com.origami.sgr.util.Utils;
import java.io.File;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Voyager
 */
@WebServlet(name = "ReporteWS", urlPatterns = {"/ReporteWS"})
public class ReporteWS extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(ReporteWS.class.getName());

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;

    private Boolean guardarPdf = false, localSave = false, download = false;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException Exception
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Map parametros;
        try {
            response.addHeader("secure-av", "Secure");
            response.addHeader("httponly-av", "HttpOnly");
            response.addHeader("X-UA-Compatible", "IE=edge,chrome=1");
            request.setCharacterEncoding("UTF-8");
            parametros = ss.getParametros();
            if (parametros == null) {
                parametros = new HashMap();
            }
            if (ss.getContentType() == null) {
                response.setContentType("application/pdf");
            } else {
                response.setContentType(ss.getContentType());
            }
            if (ss.getNombreDocumento() != null) {
                File file = new File(ss.getNombreDocumento());
                String fileName = file.getName();

                response.setContentType("application/pdf; filename=" + fileName + "; filename*= UTF-8''" + fileName + "");
                localSave = Boolean.valueOf(parametros.get("localSave") + "");
                download = Boolean.valueOf(parametros.get("download") + "");
                System.out.println("localSave>>" + localSave + " download>>>" + download);
                if (localSave || download) {
                    response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"" + "; file-download=" + fileName);
                } else {
                    if (ss.getContentType() != null && ss.getContentType().equals("rar")) {
                        ss.setNombreDocumento(ss.getNombreDocumento().concat(".").concat(ss.getContentType()));
                    }
                    // para descargar tipo de documentos guardados en el docuemntal
                    if (!ss.estaVacio()) {
                        String exten = (String) ss.getParametros().get("EXTEN");
                        if (exten != null) {
                            ss.setNombreDocumento(Utils.quitarTildes(ss.getNombreDocumento()).concat(exten));
                        }
                    }
                    System.out.println("Content-Disposition inline; filename=" + ss.getNombreDocumento());
                    response.addHeader("Content-Disposition", "inline; filename=" + ss.getNombreDocumento());
                }
                RestTemplate restTemplate = new RestTemplate();
                InputStream is = null;

                if (ss.getUrlWebService() != null) {
                    System.out.println("ss.getUrlWebService(): " + ss.getUrlWebService());
                    URL url = new URL(ss.getUrlWebService());
                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                    String decodedURLAsString = uri.toASCIIString();
                    System.out.println("ss.getUrlWebService() URL.ENCODE: " + decodedURLAsString);
                    System.out.println("ss.parametros(): " + ss.getParametros());
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + us.getToken());
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    is = new ByteArrayInputStream(restTemplate.exchange(new URI(decodedURLAsString), HttpMethod.POST, entity, byte[].class).getBody());
                }
                System.out.println("// lee archivo..");
                try (OutputStream os = response.getOutputStream()) {
                    IOUtils.copy(is, os);
                    os.flush();
                }
            }
        } catch (IOException | URISyntaxException | RestClientException e) {
            LOG.log(Level.SEVERE, "ERROR:.", e);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ReporteWS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(ReporteWS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
