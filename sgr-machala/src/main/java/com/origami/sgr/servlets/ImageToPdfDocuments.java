/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.origami.session.ServletSession;
import com.origami.sgr.util.Utils;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author Anyelo
 */
@WebServlet(name = "docsPdf", urlPatterns = {"/docsPdf"})
public class ImageToPdfDocuments extends HttpServlet {

    @Inject
    private ServletSession ss;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            if (ss == null || ss.estaVacio()) {
                try (PrintWriter salida = response.getWriter()) {
                    salida.println("<!DOCTYPE html>\n"
                            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                            + "<head id=\"j_id_3\">\n"
                            + "     <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n"
                            + "     <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                            + "     <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" />\n"
                            + "     <meta name=\"mobile-web-app-capable\" content=\"yes\" />"
                            + "     <link type=\"text/css\" rel=\"stylesheet\" href=\"/sgr/javax.faces.resource/theme.css.xhtml?ln=primefaces-morpheus-blue\" />"
                            + "     <link type=\"text/css\" rel=\"stylesheet\" href=\"/sgr/javax.faces.resource/fa/font-awesome.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                            + "     <link rel=\"stylesheet\" type=\"text/css\" href=\"/sgr/javax.faces.resource/components.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                            + "     <script type=\"text/javascript\" src=\"/sgr/javax.faces.resource/jquery/jquery.js.xhtml?ln=primefaces&amp;v=7.0\">"
                            + "     </script><script type=\"text/javascript\" src=\"/sgr/javax.faces.resource/core.js.xhtml?ln=primefaces&amp;v=7.0\"></script><script type=\"text/javascript\" src=\"/sgr/javax.faces.resource/components.js.xhtml?ln=primefaces&amp;v=7.0\"></script><link rel=\"stylesheet\" type=\"text/css\" href=\"/sgr/javax.faces.resource/css/layout-blue.css.xhtml?ln=morpheus-layout\" /><script type=\"text/javascript\">if(window.PrimeFaces){PrimeFaces.settings.locale='es_EC';PrimeFaces.settings.legacyWidgetNamespace=true;PrimeFaces.settings.projectStage='Development';}</script>\n"
                            + "     <title>Documento</title>"
                            + "</head>"
                            + "<body class=\"exception-body\">\n"
                            + "     <div class=\"exception-panel\"><img style=\"width: 256px !important;\" id=\"j_id_8\" src=\"/sgr/javax.faces.resource/images/pdf.svg.xhtml?ln=morpheus-layout\" alt=\"\" />\n"
                            + "            <div class=\"line\"></div>\n"
                            + "            <h1>Documento no encontrado</h1>\n"
                            + "            <p>Documento no se ecuentra disponible, conacte al administrador.</p><button id=\"j_id_a\" name=\"j_id_a\" type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"window.open('\\/sgr\\/procesos\\/dashBoard.xhtml?dswid=6290','_self')\"><span class=\"ui-button-text ui-c\">INICIO</span></button><script id=\"j_id_a_s\" type=\"text/javascript\">$(function(){PrimeFaces.cw(\"Button\",\"widget_j_id_a\",{id:\"j_id_a\"});});</script>\n"
                            + "     </div>"
                            + "</body>\n"
                            + "</html>");
                }
                return;
            }
            response.setContentType("application/pdf");
            /*TbLibrerias bases = (TbLibrerias) ss.getParametros().get("libreria");
            if (bases != null) {
                response.addHeader("Content-disposition", "filename=" + ss.getParametros().get("idTransaccion") + ".pdf");
                Map<String, Object> paramt = new HashMap<>();
                paramt.put("idTransaccion", ss.getParametros().get("idTransaccion"));
                //List<TbBlob> blobs = de.findAll(TbBlob.class, paramt);
                List<TbBlob> blobs = null;
                PDDocument document = new PDDocument();
                if (Utils.isNotEmpty(blobs)) {
                    for (TbBlob blob : blobs) {
                        PDPage page = new PDPage(new PDRectangle(blob.getWidthBlob(), blob.getHeightBlob()));
                        PDImageXObject img = PDImageXObject.createFromByteArray(document, blob.getObjBlob(), 
                        String name = (blob.getFileName() == null ? blob.getOrdSalida() : blob.getFileName()) + ".png";
                        PDImageXObject img = PDImageXObject.createFromByteArray(document, blob.getObjBlob(), name);
                        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                            contentStream.drawImage(img, 0, 0);
                        }
                        document.addPage(page);
                    }
                    document.save(response.getOutputStream());
                    document.close();
                }
            }*/
        } catch (IOException e) {
            System.out.println("Pagina PNG!");
            //Logger.getLogger(ImageToPdfDocuments.class.getName()).log(Level.SEVERE, null, e);
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
        } catch (IOException | SQLException | ServletException ex) {
            System.out.println("Pagina PNG!");
            //Logger.getLogger(ImageToPdfDocuments.class.getName()).log(Level.SEVERE, null, ex);
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

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Generador de pdf de imagenes.";
    }

}
