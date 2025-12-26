package org.origami.ws.service.reports;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.*;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.Transparency;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.origami.ws.config.ApplicationProperties;
import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.models.Data;
import org.origami.ws.models.DatosReporte;
import org.origami.ws.models.ReportFieldDet;
import org.origami.ws.util.Utility;
import org.origami.ws.service.DecryptEncrypt;
import org.origami.ws.service.DepartamentoService;
import org.origami.ws.service.SolicitudServiciosService;
import org.origami.ws.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportDinamicService {

    @Autowired
    private ApplicationProperties appProps;
    @Autowired
    private SolicitudServiciosService solicitudServiciosService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DepartamentoService departamentoService;
    @Autowired
    private DecryptEncrypt decryptEncrypt;

    public Data generarReporteDinamico(DatosReporte datosReporte) {
        Boolean tieneParametros = Boolean.FALSE;

        for (ReportFieldDet det : datosReporte.getCampos()) {

            if (Utility.isNotEmptyString(det.getOperador()) && Utility.isNotEmptyString(det.getValor())) {
                if (det.getTablaHecho()) {
                    if (!det.getEsObjecto()) { //ES COLUMNA PROPIA DE SOLICITUD SERVICIO
                        tieneParametros = Boolean.TRUE;
                        break;
                    } else {
                        String join = "";
                        for (ReportFieldDet obj : datosReporte.getCampos()) {
                            if (obj.getClazz().equals(det.getTypeField())) {
                                if (Utility.isNotEmptyString(obj.getOperador()) && Utility.isNotEmptyString(obj.getValor())) {
                                    tieneParametros = Boolean.TRUE;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        List<Long> ids = new ArrayList<>();
        if (tieneParametros)
            ids = solicitudServiciosService.findCustomQuery(datosReporte.getCampos());
        List<SolicitudServicios> list;
        Data data;
        System.out.println("generarReporteDinamico");
        if (!tieneParametros) {
            if (datosReporte.getIncluirFechas()) {
                Date desde = new Date(datosReporte.getFechadesde());
                Date hasta = new Date(datosReporte.getFechahasta());
                if (datosReporte.getDepartamento() != null && datosReporte.getUsuario() != null) {
                    Usuario user = usuarioService.findOne(new Usuario(datosReporte.getUsuario()));
                    list = solicitudServiciosService.findByFechaDepsUsr(desde, hasta, user.getId());
                    System.out.println("datosReporte.getIncluirFechas() usuario");
                } else if (datosReporte.getDepartamento() != null && datosReporte.getUsuario() == null) {
                    Departamento dep = departamentoService.find(new Departamento(datosReporte.getDepartamento()));
                    list = solicitudServiciosService.findByFechaDeps(desde, hasta, dep.getId());
                    System.out.println("datosReporte.getIncluirFechas() departamento");
                } else {
                    list = solicitudServiciosService.findByDesdeHasta(desde, hasta);
                    System.out.println("datosReporte.getIncluirFechas() findByDesdeHasta");
                }
            } else {
                if (datosReporte.getDepartamento() != null && datosReporte.getUsuario() != null) {
                    Usuario user = usuarioService.findOne(new Usuario(datosReporte.getUsuario()));
                    list = solicitudServiciosService.findByUser(user.getId());
                    System.out.println("findByUser");
                } else if (datosReporte.getDepartamento() != null && datosReporte.getUsuario() == null) {
                    Departamento dep = departamentoService.find(new Departamento(datosReporte.getDepartamento()));
                    list = solicitudServiciosService.findByDeps(dep.getId());
                    System.out.println("findByDeps");
                } else {
                    list = solicitudServiciosService.findAll();
                    System.out.println("findAll");
                }
            }
        } else {
            list = new ArrayList<>();
            if (Utility.isEmpty(ids)) {
                return new Data(1L, "No existen datos para los parametros consultados");
            }
            for (Long id : ids) {
                list.add(solicitudServiciosService.find(new SolicitudServicios(id)));
            }
        }

        System.out.println("BcbgUtil.isNotEmpty(list): " + Utility.isNotEmpty(list));
        if (Utility.isNotEmpty(list)) {

            try {
                Style headerStyle = createHeaderStyle();
                Style detailTextStyle = createDetailTextStyle();
                Style detailNumberStyle = createDetailNumberStyle();
                DynamicReport dynaReport = getReport(headerStyle, detailTextStyle, detailNumberStyle, datosReporte.getCampos(), datosReporte.getNombreArchivoPDF());
                if (dynaReport != null) {
                    String rutaNotasXls = appProps.getRutaArchivos() + datosReporte.getNombreArchivoPDF() + ".xlsx";
                    JasperPrint jp = DynamicJasperHelper.generateJasperPrint(dynaReport, new ClassicLayoutManager(), new JRBeanCollectionDataSource(list));
                    JRXlsxExporter exporter = new JRXlsxExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jp));
                    File outputFile = new File(rutaNotasXls);
                    exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputFile));
                    SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
                    configuration.setDetectCellType(true);//Set configuration as you like it!!
                    configuration.setCollapseRowSpan(false);
                    configuration.setOnePagePerSheet(false);
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();
                    decryptEncrypt.encrypt(rutaNotasXls);
                    data = new Data(0L, rutaNotasXls);
                } else {
                    data = new Data(1L, "Ocurrio un error al generar el reporte");
                }
            } catch (JRException | ColumnBuilderException ex) {
                ex.printStackTrace();
                data = new Data(1L, "Ocurrio un error al generar el reporte");
            }
        } else {
            data = new Data(1L, "No existen datos para los parametros consultados");
        }


        return data;
    }

    private Style createHeaderStyle() {
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.ARIAL_MEDIUM);
        sb.setBorder(Border.THIN());
        sb.setBorderBottom(Border.PEN_2_POINT());
        sb.setBorderColor(Color.BLACK);
        sb.setBackgroundColor(Color.ORANGE);
        sb.setTextColor(Color.BLACK);
        sb.setTransparency(Transparency.OPAQUE);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    private Style createDetailTextStyle() {
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.DOTTED());
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.BLACK);
        sb.setPaddingLeft(5);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }

    private Style createDetailNumberStyle() {
        StyleBuilder sb = new StyleBuilder(true);
        sb.setFont(Font.VERDANA_MEDIUM);
        sb.setBorder(Border.DOTTED());
        sb.setBorderColor(Color.BLACK);
        sb.setTextColor(Color.BLACK);
        sb.setPaddingRight(5);
        sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
        return sb.build();
    }


    private AbstractColumn createColumn(String property, Class type,
                                        String title, int width, Style headerStyle, Style detailStyle)
            throws ColumnBuilderException {
        AbstractColumn columnState = ColumnBuilder.getNew()
                .setColumnProperty(property, type.getName())
                .setTitle(title)
                .setWidth(Integer.valueOf(width))
                .setStyle(detailStyle)

                .setHeaderStyle(headerStyle).build();
        return columnState;
    }

    private DynamicReport getReport(Style headerStyle, Style detailTextStyle, Style detailNumStyle, List<ReportFieldDet> dets, String reporte) throws ColumnBuilderException {
        try {
            DynamicReportBuilder report = new DynamicReportBuilder();

            for (ReportFieldDet det : dets) {
                try {
                    if (det.getTablaHecho()) {
                        AbstractColumn column = null;
                        if (!det.getEsObjecto()) {
                            column = createColumn(det.getField(), Class.forName(det.getTypeField()), det.getDetailField(), 150, headerStyle, det.getTypeField().contains("String") ? detailTextStyle : detailNumStyle);
                            report.addColumn(column);
                        } else {
                            for (ReportFieldDet obj : dets) {
                                if (obj.getClazz().equals(det.getTypeField())) {
                                    column = createColumn(det.getField() + "." + obj.getField(), Object.class, obj.getDetailField(), 150, headerStyle, obj.getTypeField().contains("String") ? detailTextStyle : detailNumStyle);
                                    report.addColumn(column);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            StyleBuilder titleStyle = new StyleBuilder(true);
            titleStyle.setFont(new Font(25, Font._FONT_ARIAL, true));
            titleStyle.setTextColor(Color.RED);
            StyleBuilder subTitleStyle = new StyleBuilder(true);
            subTitleStyle.setFont(new Font(Font.MEDIUM, Font._FONT_ARIAL, true));

            report.setTitle("Benemérito Cuerpo de Bomberos de Guayaquil");
            report.setSubtitle(reporte);
            report.setIgnorePagination(true);
            report.setUseFullPageWidth(true);
            return report.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}
