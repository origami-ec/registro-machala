/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.servlets;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;
import com.origami.session.ServletSession;
import com.origami.sgr.services.interfaces.Entitymanager;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author origami-idea
 */
@WebServlet(name = "PdfFolio", urlPatterns = {"/PdfFolio"}, smallIcon = "/resources/morpheus-layout/images/loja.ico")
public class GenerarPdfFolio extends HttpServlet {

    @Inject
    ServletSession servletSession;
    @Inject
    private Entitymanager manager;

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

        OutputStream outStream;
        if (servletSession.estaVacio()) {
            response.setContentType("text/html");
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
                        + "            <p>Documento no se ecuentra disponible, contacte al administrador.</p><button id=\"j_id_a\" name=\"j_id_a\" type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" onclick=\"window.open('\\/sgr\\/procesos\\/dashBoard.xhtml?dswid=6290','_self')\"><span class=\"ui-button-text ui-c\">INICIO</span></button><script id=\"j_id_a_s\" type=\"text/javascript\">$(function(){PrimeFaces.cw(\"Button\",\"widget_j_id_a\",{id:\"j_id_a\"});});</script>\n"
                        + "     </div>"
                        + "</body>\n"
                        + "</html>");
            }

            return;
        }
        Integer desde = (Integer) servletSession.getParametros().get("desde");
        Integer hasta = (Integer) servletSession.getParametros().get("hasta");
        response.setContentType("application/pdf");
        response.addHeader("Content-disposition", "filename=" + servletSession.getNombreReporte() + ".pdf");
        try {
            outStream = response.getOutputStream();
            Document document = new Document(PageSize.A4, 25, 25, 25, 25);
            PdfWriter writer = PdfWriter.getInstance(document, outStream);
            writer.setInitialLeading(0);
            document.open();
            for (int i = desde; i <= hasta; i++) {
                System.out.println("Desde " + i);
//                if (i == desde) {
//                    PdfContentByte canvas = writer.getDirectContent();
//                    ColumnText.showTextAligned(canvas, 4, new Phrase("" + i), 20, 20, 0);
//                } else {

                document.newPage();
                HeaderFooter headerFooter = new HeaderFooter(new Phrase("" + i), false);
                headerFooter.setAlignment(HeaderFooter.ALIGN_RIGHT);
                headerFooter.disableBorderSide(Rectangle.TOP);
                headerFooter.disableBorderSide(Rectangle.BOTTOM);
                document.setHeader(headerFooter);
                writer.setPageEmpty(false);
//                }
            }
            document.addTitle(servletSession.getNombreDocumento());
            document.close();
            outStream.flush();
            outStream.close();
            servletSession.borrarDatos();
        } catch (IOException | DocumentException e) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, e);
        } finally {
        }
    }
    private static final int BLANK_THRESHOLD = 160;

    private void copyPdf(Document document, PdfWriter writer1, Integer desde, Integer hasta, ByteArrayOutputStream out, OutputStream outWriter) {
        try {
            PdfReader r = new PdfReader(out.toByteArray());
            PdfCopy writer = new PdfCopy(document, outWriter);
            writer1.freeReader(r);
            r.selectPages(desde + " - " + hasta);
            for (int i = 1; i <= r.getNumberOfPages(); i++) {
                // first check, examine the resource dictionary for /Font or
                // /XObject keys.  If either are present -> not blank.
                PdfDictionary pageDict = r.getPageN(i);
                PdfDictionary resDict = (PdfDictionary) pageDict.get(PdfName.RESOURCES);
                boolean noFontsOrImages = true;
                if (resDict != null) {
                    noFontsOrImages = resDict.get(PdfName.FONT) == null && resDict.get(PdfName.XOBJECT) == null;
                }
                System.out.println(i + " noFontsOrImages " + noFontsOrImages);
                if (!noFontsOrImages) {
                    byte bContent[] = r.getPageContent(i);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bs.write(bContent);
                    System.out.println(i + bs.size() + " > BLANK_THRESHOLD " + (bs.size() > BLANK_THRESHOLD));
                    if (bs.size() > BLANK_THRESHOLD) {
                        writer.addPage(writer.getImportedPage(r, i));
                    }
                }
            }
            writer.close();
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(GenerarPdfFolio.class.getName()).log(Level.SEVERE, null, ex);
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
