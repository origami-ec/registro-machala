/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bcbg.session.ServletSession;
import org.bcbg.session.UserSession;
import org.bcbg.util.JsonDateDeserializer;
import org.bcbg.ws.BcbgEjb;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 *
 * @author Origami
 */
@WebServlet(name = "DocumentoWS", urlPatterns = {"/DocumentoWs"})
public class DocumentoWS extends HttpServlet {

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession us;

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
        try {
            Date fecha = new Date();
            String url = ss.getUrlWebService();
            System.out.println("DocumentoWS url: " + url);
            GsonBuilder builder = new GsonBuilder().setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz")
                    .registerTypeAdapter(Date.class, new JsonDateDeserializer());
            Gson gson = builder.create();

            String auth = "Bearer " + us.getToken();

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(gson.toJson(ss.getDatos() != null
                    ? ss.getDatos() : ss.getDataSource() != null ? ss.getDataSource() : ss.getData()), "UTF-8"));
            httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Authorization", auth);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
            try {
                HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
                if (httpResponse != null) {
                    response.setContentType("application/pdf");
                    response.setCharacterEncoding("UTF-8");
                    response.setHeader("Content-Disposition", "inline; filename=" + (ss.getDatos() != null ? ss.getDatos()
                    .getNombreArchivoPDF():"DocumentoWS" ) + ".pdf");
                    try ( OutputStream os = response.getOutputStream()) {
                        IOUtils.copy(httpResponse.getEntity().getContent(), os);
                        os.flush();
                    }
                }
            } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
                Logger.getLogger(BcbgEjb.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            Logger.getLogger(DocumentoWS.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DocumentoWS.class.getName()).log(Level.SEVERE, null, ex);
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
