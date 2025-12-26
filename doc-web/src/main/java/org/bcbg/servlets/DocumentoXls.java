/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.servlets;

import org.bcbg.session.ServletSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * Modificado de la clase #Documento de Joao Sanga.
 *
 * @author Angel Navarro
 */
@WebServlet(name = "DocumentoXls", urlPatterns = {"/DocumentoExcel"})
public class DocumentoXls extends HttpServlet {

    private Map parametros;

    @Inject
    ServletSession servletSession;

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

        //ServletSession servletSession = (ServletSession) request.getSession().getAttribute("servletSession");
        JasperPrint jasperPrint;
        OutputStream outStream;

        parametros = servletSession.getParametros();
        parametros.put(JRParameter.IS_IGNORE_PAGINATION, true);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        if (servletSession.getNombreDocumento() == null) {
            response.addHeader("Content-disposition", "filename=" + servletSession.getNombreReporte() + "-"
                    + new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime()) + ".xlsx");
        } else {
            response.addHeader("Content-disposition", "filename=" + servletSession.getNombreDocumento() + ".xlsx");
        }
        try {
            request.setCharacterEncoding("UTF-8");
            String ruta;
            if (servletSession.getNombreSubCarpeta() == null) {
                ruta = getServletContext().getRealPath("//reportes//" + servletSession.getNombreReporte() + ".jasper");
            } else {
                ruta = getServletContext().getRealPath("//reportes//" + servletSession.getNombreSubCarpeta() + "//" + servletSession.getNombreReporte() + ".jasper");
            }

            JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList());
            if (servletSession.getDataSource() != null) {
                dataSource = new JRBeanCollectionDataSource(servletSession.getDataSource());
            }
            jasperPrint = JasperFillManager.fillReport(ruta, parametros, dataSource);

            outStream = response.getOutputStream();

            JRXlsxExporter exporter = new JRXlsxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));

            // Configuraciones del Reporte
            SimpleXlsxReportConfiguration xlsReportConfiguration = new SimpleXlsxReportConfiguration();

            xlsReportConfiguration.setAutoFitPageHeight(true);
            xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
            xlsReportConfiguration.setOnePagePerSheet(false);
            xlsReportConfiguration.setDetectCellType(true);
            xlsReportConfiguration.setWrapText(true);
            xlsReportConfiguration.setRemoveEmptySpaceBetweenColumns(true);
            xlsReportConfiguration.setCollapseRowSpan(Boolean.TRUE);
            xlsReportConfiguration.setIgnoreAnchors(Boolean.TRUE);
            xlsReportConfiguration.setMaxRowsPerSheet(1045000);
            xlsReportConfiguration.setIgnoreGraphics(Boolean.TRUE);
            xlsReportConfiguration.setOverrideHints(Boolean.TRUE);

            exporter.setConfiguration(xlsReportConfiguration);
            if (servletSession.getOnePagePerSheet()) {
                xlsReportConfiguration.setOnePagePerSheet(servletSession.getOnePagePerSheet());
                xlsReportConfiguration.setWhitePageBackground(servletSession.getFondoBlanco());
            }
            // Configuraciones de exportacion.
            //SimpleXlsExporterConfiguration xlsExporterConfiguration = new SimpleXlsExporterConfiguration();
            exporter.exportReport();

            servletSession.borrarDatos();

        } catch (Exception e) {
            Logger.getLogger(DocumentoXls.class.getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(DocumentoXls.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DocumentoXls.class.getName()).log(Level.SEVERE, null, ex);
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
