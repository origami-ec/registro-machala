package org.origami.ws.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.apache.commons.io.FileUtils;
import org.origami.ws.config.ApplicationProperties;
import org.origami.ws.dto.DataTramite;
import org.origami.ws.dto.SolicitudServiciosDTO;
import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.entities.origami.Notificacion;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.origami.ws.entities.origami.Tramites;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.mappers.ReporteBandejaTareasMapper;
import org.origami.ws.mappers.ReporteTareasRealizadasMapper;
import org.origami.ws.models.Data;
import org.origami.ws.models.DatosNotificacion;
import org.origami.ws.models.DatosReporte;
import org.origami.ws.models.UsuarioTareasHistoricas;
import org.origami.ws.repository.origami.NotificacionRepository;
import org.origami.ws.repository.origami.SolicitudServiciosRepository;
import org.origami.ws.repository.origami.TareasActivasRepository;
import org.origami.ws.repository.origami.TareasHistoricasRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class ReporteService {

    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private TareasActivasRepository tar;
    @Autowired
    private ReporteBandejaTareasMapper rbtm;
    @Autowired
    private TareasHistoricasRepository thr;
    @Autowired
    private ReporteTareasRealizadasMapper rtrm;
    @Autowired
    private SolicitudServiciosRepository ssr;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private NotificacionRepository notr;
    @Autowired
    private UsuarioResponsableService usuarioResponsableService;
    @Autowired
    private DepartamentoService departamentoService;

    public byte[] generarPDF(String resourceLocation, Object dataSource, Map parameters) {
        try {
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(dataSource));
            JasperPrint jasperPrint;
            File file = ResourceUtils.getFile(resourceLocation);
            jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, jrBeanCollectionDataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param resourceLocation Ruta del reporte
     * @param dataSource       Objeto para el reporte
     * @param parameters       Map de parametros
     * @param nombreReporte    Nombre sin PDF del reporte a guardar
     * @return OK SI se creo
     * @throws FileNotFoundException x si se cae
     * @throws JRException           x si se cae
     */

    public String generarGuardarPDF(String resourceLocation, Object dataSource, Map parameters, String nombreReporte) {

        String ruta = applicationProperties.getRutaArchivos() + nombreReporte.replace(".pdf", "") + ".pdf";

        try {
            File file = ResourceUtils.getFile(resourceLocation);
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(dataSource));
            JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, jrBeanCollectionDataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint, ruta);
            //System.out.println("ruta: " + ruta);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return ruta;
    }

    public String generarGuardarPDF2(String resourceLocation, Object dataSource, Map parameters, String nombreReporte) {
        String ruta = applicationProperties.getRutaArchivos() + nombreReporte.replace(".pdf", "") + ".pdf";
        try {

            File file = ResourceUtils.getFile(resourceLocation);
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(Arrays.asList(dataSource));
            JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, jrBeanCollectionDataSource);

            //Export to pdf
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(ruta));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            exporter.setConfiguration(configuration);
            exporter.exportReport();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ruta;

    }

    public byte[] generarPDF(String resourceLocation, List dataSource, Map parameters) {
        try {
            File file = ResourceUtils.getFile(resourceLocation);
            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(dataSource);
            JasperPrint jasperPrint = JasperFillManager.fillReport(file.getAbsolutePath(), parameters, jrBeanCollectionDataSource);
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public byte[] getReporteTicketTramite(Long solicitud) {
        try {
            System.out.println("getReporteTicketTramite: " + solicitud);
            SolicitudServicios ss = ssr.getById(solicitud);
            Usuario u = usuarioService.findOne(new Usuario(ss.getUsuarioCreacion(), null, null, null));
            System.out.println(ss.toString());
            System.out.println(ss.getTipoTramite().toString());
            DataTramite tramite = new DataTramite(ss.getTipoTramite() != null ? ss.getDepartamento().getNombre() :
                    ss.getTipoServicio().getDepartamento().getNombre(),
                    ss.getTramite().getCodigo(),
                    ss.getRepresentante(),
                    ss.getFechaCreacion(),
                    ss.getTipoServicio() != null ? ss.getTipoServicio().getNombre() : ss.getTipoTramite().getDescripcion(),
                    ss.getTipoServicio() != null ? ss.getTipoServicio().getNombre() : "", u.getNombreUsuario(),
                    ss.getCampoReferencia() != null ? ss.getCampoReferencia(): "");
            tramite.setObservacion(ss.getDescripcionInconveniente() != null ? ss.getDescripcionInconveniente() : "");
            tramite.setAsunto(ss.getAsunto() != null ? ss.getAsunto() : "");
            Map par = new HashMap();
            par.putAll(Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
            return this.generarPDF(Utility.armarRutaJasper("TicketTramite", applicationProperties.getRutaReportes()), tramite, par);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getReporteReimprimirTicketTramite(Long solicitud) {
        try {
            SolicitudServicios ss = ssr.getOne(solicitud);
            Usuario u = usuarioService.findOne(new Usuario(ss.getUsuarioCreacion(), null, null, null));
            DataTramite tramite = new DataTramite(ss.getTipoTramite() != null ? ss.getTipoTramite().getDepartamento().getNombre() :
                    ss.getTipoServicio().getDepartamento().getNombre(),
                    ss.getTramite().getCodigo(),
                    ss.getRepresentante(),
                    new Date(),
                    ss.getTipoServicio() != null ? ss.getTipoServicio().getNombre() : ss.getTipoTramite().getDescripcion(),
                    ss.getTipoServicio() != null ? ss.getTipoServicio().getNombre() : "", u.getNombreUsuario(),
                    ss.getCampoReferencia() != null ? ss.getCampoReferencia(): ""
            );
            Map par = new HashMap();
            par.putAll(Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
            return this.generarPDF(Utility.armarRutaJasper("ReimprimirTicketTramite", applicationProperties.getRutaReportes()), tramite, par);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public SolicitudServicios getInformeSolicitud(Data data) {
        try {
            Notificacion n = notr.getOne(data.getId());
            DatosNotificacion dn = new DatosNotificacion();
            dn.setFecha(n.getFecha());
            dn.setCodigo(n.getCodigo());
            dn.setContenido(n.getContenido());
            dn.setDepartamento(n.getTramite().getTipoTramite().getDepartamento().getNombre());
            dn.setDocumento(n.getTipoNotificacion().getDescripcion());
            String nombreReporte = "Notificacion";
            if (data.getUnicode() != null) {
                if (data.getUnicode().equals("memo")) {
                    nombreReporte = "memoVistoBueno";
                }
            }
            byte[] reporte = this.generarPDF(Utility.armarRutaJasper(nombreReporte, applicationProperties.getRutaReportes()), dn,
                    Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
            String nombreArchivo = new Date().getTime() + data.getId() + ".pdf";
            FileUtils.writeByteArrayToFile(new File(applicationProperties.getRutaArchivos() + nombreArchivo), reporte);
            SolicitudServicios s = ssr.getOne(Long.parseLong(data.getData()));
            return ssr.save(s);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] getReporteBandejaTareas(DatosReporte data) {
        try {
            String desde = Utility.dateFormatPattern("yyyy-MM-dd", new Date(data.getFechadesde()));
            String hasta = Utility.dateFormatPattern("yyyy-MM-dd", new Date(data.getFechahasta()));
            data.getParametros().putAll(Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
            if (data.getUsuario() != null) {
                return this.generarPDF(Utility.armarRutaJasper("BandejaTareas", applicationProperties.getRutaReportes()),
                        rbtm.toListDTO(tar.getTareasByUsuario(desde, hasta, data.getUsuario(), data.getCandidato())), data.getParametros());
            } else {
                List<UsuarioTareasHistoricas> listUsuarioTH = new ArrayList<>();
                UsuarioTareasHistoricas usuarioTH;
                Departamento dep = departamentoService.find(new Departamento(data.getDepartamento()));
                List<Usuario> listUsuarios = usuarioResponsableService.usersXdepartamentos(dep.getId());
                if (!listUsuarios.isEmpty()) {
                    for (Usuario u : listUsuarios) {
                        usuarioTH = new UsuarioTareasHistoricas();
                        usuarioTH.setTareasAct(tar.getTareasByUsuario(desde, hasta, u.getUsuarioNombre(), "%" + u.getUsuarioNombre() + "%"));
                        usuarioTH.setUsuario(u);
                        listUsuarioTH.add(usuarioTH);
                    }
                }
                return this.generarPDF(Utility.armarRutaJasper("BandejaTareasDepartamento", applicationProperties.getRutaReportes()),
                        listUsuarioTH, data.getParametros());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getReporteTareasRealizadas(DatosReporte data) {
        try {
            String desde = Utility.dateFormatPattern("yyyy-MM-dd", new Date(data.getFechadesde()));
            String hasta = Utility.dateFormatPattern("yyyy-MM-dd", new Date(data.getFechahasta()));

            data.getParametros().putAll(Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
            if (data.getUsuario() != null) {
                return this.generarPDF(Utility.armarRutaJasper("BandejaTareas", applicationProperties.getRutaReportes()),
                        rtrm.toListDTO(thr.getTareasRealizadasByUsuarioYFechas(data.getUsuario(),
                                desde, hasta)), data.getParametros());
            } else {
                List<UsuarioTareasHistoricas> listUsuarioTH = new ArrayList<>();
                UsuarioTareasHistoricas usuarioTH;
                Departamento dep = departamentoService.find(new Departamento(data.getDepartamento()));
                List<Usuario> listUsuarios = usuarioResponsableService.usersXdepartamentos(dep.getId());
                if (!listUsuarios.isEmpty()) {
                    for (Usuario u : listUsuarios) {
                        usuarioTH = new UsuarioTareasHistoricas();
                        usuarioTH.setTareasHis(thr.getTareasRealizadasByUsuarioYFechas(u.getUsuarioNombre(), desde, hasta));
                        usuarioTH.setUsuario(u);
                        listUsuarioTH.add(usuarioTH);
                    }
                }
                return this.generarPDF(Utility.armarRutaJasper("BandejaTareasDepartamento", applicationProperties.getRutaReportes()),
                        listUsuarioTH, data.getParametros());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getReporteAnalisis(List<Tramites> data, String nombreReporte) {
        try {
            return this.generarPDF(Utility.armarRutaJasper(nombreReporte, applicationProperties.getRutaReportes()), data, Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public byte[] getReporteSolicitudServicio(SolicitudServiciosDTO data, String nombreReporte) {
        try {
            System.out.println(data.toString());
            return this.generarPDF(Utility.armarRutaJasper(nombreReporte, applicationProperties.getRutaReportes()), data, Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public byte[] getReporteNotificacion(Long notificacion) {
        try {
            Notificacion data = notr.getOne(notificacion);
            DatosNotificacion dn = new DatosNotificacion();
            dn.setFecha(data.getFecha());
            dn.setCodigo(data.getCodigo());
            dn.setContenido(data.getContenido());
            dn.setDepartamento(data.getTramite().getTipoTramite().getDepartamento().getNombre());
            dn.setDocumento(data.getTipoNotificacion().getDescripcion());
            return this.generarPDF(Utility.armarRutaJasper("Notificacion", applicationProperties.getRutaReportes()), dn,
                    Utility.getUrlsImagenes(applicationProperties.getRutaImagenes()));
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }


}
