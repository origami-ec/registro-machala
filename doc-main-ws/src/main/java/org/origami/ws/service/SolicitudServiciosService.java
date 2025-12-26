package org.origami.ws.service;

import org.origami.ws.dto.SolicitudServiciosDTO;
import org.origami.ws.entities.origami.HistoricoTramites;
import org.origami.ws.entities.origami.SolicitudServicios;
import org.origami.ws.entities.origami.TipoTramite;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.mappers.SolicitudServicioMapper;
import org.origami.ws.models.ReportFieldDet;
import org.origami.ws.repository.origami.SolicitudServiciosRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SolicitudServiciosService {
    @Autowired
    private DataSource origamiDS;
    @Autowired
    private SolicitudServiciosRepository serviciosRepository;
    @Autowired
    private AppServices appServices;
    @Autowired
    private HistoricoTramiteService tramiteService;
    @Autowired
    private SolicitudServicioMapper serviceMapper;
    @Autowired
    private TipoTramiteService tipoTramiteService;
    @Autowired
    private UsuarioService usuarioService;

    protected Map<String, List> map;

    public SolicitudServiciosService() {
        map = new HashMap<>();
        //matcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING);
    }

    public SolicitudServicios find(SolicitudServicios data) {
        SolicitudServicios ss = serviciosRepository.findOne(Example.of(data)).get();
        if (ss != null && ss.getUsuarioCreacion() != null) {
            Usuario u = usuarioService.findOne(new Usuario(ss.getUsuarioCreacion()));
            u.setClave(null);
            u.setPassword(null);
            ss.setUsuarioIngreso(u);
        }
        return ss;

    }


    public List<SolicitudServicios> findByFechaDepsUsr(Date desde, Date hasta, Long user) {
        List<SolicitudServicios> list = serviciosRepository.findAllByFechaDepsUser(desde, hasta, user);
        return initUsuarioIngreso(list);
    }


    public List<SolicitudServicios> findByFechaDeps(Date desde, Date hasta, Long departamento) {
        List<SolicitudServicios> list = serviciosRepository.findAllByFechaDeps(desde, hasta, departamento);
        return initUsuarioIngreso(list);
    }

    public List<SolicitudServicios> findByDesdeHasta(Date desde, Date hasta) {
        List<SolicitudServicios> list = serviciosRepository.findAllByFecha(desde, hasta);
        return initUsuarioIngreso(list);
    }

    public List<SolicitudServicios> findAll() {
        List<SolicitudServicios> list = serviciosRepository.findAll();
        return initUsuarioIngreso(list);
    }

    public List<SolicitudServicios> findByDeps(Long departamentoID) {
        List<SolicitudServicios> list = serviciosRepository.findAllByTipoServicio_Departamento_Id(departamentoID);
        return initUsuarioIngreso(list);
    }

    public List<SolicitudServicios> findByUser(Long userId) {
        List<SolicitudServicios> list = serviciosRepository.findAllByUsuarioCreacion(userId);
        return initUsuarioIngreso(list);
    }

    private List<SolicitudServicios> initUsuarioIngreso(List<SolicitudServicios> list) {
        if (Utility.isNotEmpty(list)) {
            for (SolicitudServicios ss : list) {
                if (ss != null && ss.getUsuarioCreacion() != null) {
                    Usuario u = usuarioService.findOne(new Usuario(ss.getUsuarioCreacion()));
                    u.setClave(null);
                    u.setPassword(null);
                    ss.setUsuarioIngreso(u);
                }
            }
        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    public Map<String, List> find(SolicitudServicios data, Pageable pageable) {
        Page<SolicitudServicios> result = serviciosRepository.findAll(Example.of(data), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(serviciosRepository.count(Example.of(data))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    public Map<String, List> find(SolicitudServiciosDTO data, Pageable pageable) {
        Page<SolicitudServicios> result = null;
        SolicitudServicios solicitudServicios = serviceMapper.toEntity(data);
       /* ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();*/
        if (data.getFecha() != null && data.getFecha1() != null) {
            String desde = data.getFecha();
            String hasta = data.getFecha1() != null ? data.getFecha1() : desde;

            Date desdeDate = Utility.stringTodate(desde);
            Date hastaDate = Utility.addDays(Utility.getFechaActual(Utility.stringTodate(hasta)), 1);
            if (solicitudServicios.getUsuarioCreacion() != null) {
                result = serviciosRepository.findAllByFechaCreacionBetweenAndUsuarioCreacion(desdeDate, hastaDate, solicitudServicios.getUsuarioCreacion(), pageable);
            }
            if (solicitudServicios.getTipoServicio().getDepartamento() != null
                    && solicitudServicios.getTipoServicio().getDepartamento().getId() != null) {
                result = serviciosRepository.findAllByFechaCreacionBetweenAndDepartamentoId(desdeDate, hastaDate, solicitudServicios.getTipoServicio().getDepartamento().getId(), pageable);
            }
        } else {
            result = serviciosRepository.findAll(Example.of(solicitudServicios), pageable);
        }
        serviciosRepository.findAll(pageable);
        List<SolicitudServicios> list = result.getContent();
        for (SolicitudServicios ss : list) {
            if (ss.getUsuarioCreacion() != null) {
                Usuario usuarioIngreso = usuarioService.usuarioXid(new Usuario(ss.getUsuarioCreacion()));
                ss.setUsuarioIngreso(usuarioIngreso);
            }
        }
        List<SolicitudServiciosDTO> solicitudServiciosDTOList = serviceMapper.toDTO(result.getContent());
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(serviciosRepository.count(Example.of(solicitudServicios))));
        map.put("result", solicitudServiciosDTOList);
        map.put("pages", pages);
        return map;

    }

    public SolicitudServicios registrar(SolicitudServicios data) {
        try {
            int dias = 0;
            int horas = 0;
            int minute = 0;
            HistoricoTramites tramites = data.getTramite();
            Usuario u = usuarioService.findOne(new Usuario(data.getUsuarioIngreso().getId(), null, null, null));
            tramites.setCorreoNotificacion(data.getCorreoNotificacion());
            tramites.setTipoTramite(tipoTramiteService.find(new TipoTramite(tramites.getTipoTramite().getId())));
            tramites = tramiteService.registrar(tramites, data.getTipoServicio(), data.getSolicitudInterna());
            tramiteService.guardarObservacionInicial(tramites, u.getUsuarioNombre());


            if (data.getTipoServicio() != null) {
                dias = data.getTipoServicio().getDiasRespuesta() != null ? data.getTipoServicio().getDiasRespuesta() : 0;
                horas = data.getTipoServicio().getHora() != null ? data.getTipoServicio().getHora() : 0;
                minute = data.getTipoServicio().getMinutos() != null ? data.getTipoServicio().getMinutos() : 0;
                tramites.setFechaEntrega(appServices.diasEntregaTramite(tramites.getFechaIngreso(), dias, horas, minute));
            } else {
                dias = tramites.getTipoTramite().getDias() != null ? tramites.getTipoTramite().getDias() : 0;
                horas = tramites.getTipoTramite().getHoras() != null ? tramites.getTipoTramite().getHoras() : 0;
                minute = tramites.getTipoTramite().getMinutos() != null ? tramites.getTipoTramite().getMinutos() : 0;
                tramites.setFechaEntrega(appServices.diasEntregaTramite(tramites.getFechaIngreso(), dias, horas, minute));

            }

            tramites = tramiteService.actualizar(tramites);
            data.setTramite(tramites);
            data.setUsuarioCreacion(data.getUsuarioIngreso().getId());
            return serviciosRepository.save(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SolicitudServicios actualizar(SolicitudServicios data) {
        return serviciosRepository.save(data);

    }

    public void eliminar(SolicitudServicios data) {
        serviciosRepository.delete(data);
    }

    public SolicitudServiciosDTO notificarSolicitudPago(SolicitudServiciosDTO servicios) {
        SolicitudServicios solicitudServicios = serviceMapper.toEntity(servicios);

        return servicios;
    }

    public List<Long> findCustomQuery(List<ReportFieldDet> campos) {
        List<Long> ids = new ArrayList<>();
        Connection con = null;
        try {
            con = origamiDS.getConnection();
            String sql = "select ss.id from solicitud_servicio ss\n" +
                    "left outer join departamento dep on ss.departamento_id = dep.id\n" +
                    "left outer join persona per on ss.ente_solicitante_id = per.id\n" +
                    "left outer join servicio_departamento sd on ss.tipo_servicio_id = sd.id\n" +
                    "left outer join tipo_tramite tt on ss.tipo_tramite_id = tt.id\n" +
                    "left outer join historico_tramite ht on ss.tramite_id = ht.id\n" +
                    "left outer join usuario us on ss.usuario_ingreso_id = us.id ";

            StringBuilder where = new StringBuilder("where ");
            String operador;

            for (ReportFieldDet det : campos) {//NO FUNCIONA SI LA COLUMNA EN LA BASE TIENE UN NOMBRE DIFERENTE
                System.out.println("det: " + det.toString());
                operador = null;
                try {
                    if (det.getOperador() != null && det.getValor() != null) {
                        switch (det.getOperador()) {
                            case "IGUAL":
                                operador = " = ";
                                break;
                            case "CONTIENE":
                                operador = " like  ";
                                break;
                            case "MENOR IGUAL QUE":
                                operador = " <= ";
                                break;
                            case "MAYOR IGUAL QUE":
                                operador = " >= ";
                                break;
                            case "RANGO FECHA":
                                operador = " BETWEEN ";
                                break;
                            case "-":
                                operador = "-";
                                break;

                        }
                        if (operador != null) {
                            if (det.getTablaHecho()) {
                                String field = "";
                                StringBuilder valor = new StringBuilder();
                                if (!det.getEsObjecto()) { //ES COLUMNA PROPIA DE SOLICITUD SERVICIO

                                    for (String w : det.getField().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
                                        if (field.isEmpty()) {
                                            field = w.toLowerCase();
                                        } else {
                                            field = field + "_" + w.toLowerCase();
                                        }
                                    }
                                    System.out.println("det.getValor(): " + det.getValor());
                                    switch (det.getTypeField()) {
                                        case "java.lang.String":
                                            if (!operador.contains("like"))
                                                valor.append("'").append(det.getValor()).append("'");
                                            else
                                                valor.append("'%").append(det.getValor()).append("%'");
                                            break;
                                        case "java.lang.Long":
                                        case "java.lang.Boolean": //NO HAY BOLEANOS
                                            valor.append(det.getValor());
                                            break;
                                        case "java.util.Date":
                                            field = "CONVERT(date," + field + ")";
                                            if (!operador.contains("BETWEEN")) {
                                                valor.append("CONVERT(date,").append("'").append(det.getValor()).append("'").append(")");
                                            } else {
                                                String[] fechas = det.getValor().split(";");
                                                String desde = fechas[0];
                                                String hasta = fechas[1];
                                                valor.append("CONVERT(date,").append("'").append(desde).append("'").append(")");
                                                valor.append(" AND ");
                                                valor.append("CONVERT(date,").append("'").append(hasta).append("'").append(")");
                                            }
                                            break;
                                    }
                                    //field.setLength(field.length() - 1);
                                    where.append(field).append(operador).append(valor).append(" AND ");
                                } else {
                                    String join = "";
                                    for (ReportFieldDet obj : campos) {
                                        if (obj.getClazz().equals(det.getTypeField())) {
                                            if (obj.getOperador() != null && obj.getValor() != null) {
                                                switch (obj.getOperador()) {
                                                    case "IGUAL":
                                                        operador = " = ";
                                                        break;
                                                    case "CONTIENE":
                                                        operador = " like  ";
                                                        break;
                                                    case "MENOR IGUAL QUE":
                                                        operador = " <= ";
                                                        break;
                                                    case "MAYOR IGUAL QUE":
                                                        operador = " >= ";
                                                        break;
                                                    case "RANGO FECHA":
                                                        operador = " BETWEEN ";
                                                        break;
                                                }
                                                switch (obj.getClazz()) {
                                                    case "org.bcbg.entities.HistoricoTramites":
                                                        join = "ht.";
                                                        break;
                                                    case "org.bcbg.entities.ServiciosDepartamento":
                                                        join = "sd.";
                                                        break;
                                                    case "org.bcbg.entities.Departamento":
                                                        join = "dep.";
                                                        break;
                                                    case "org.bcbg.entities.Persona":
                                                        join = "per.";
                                                        break;
                                                    case "org.bcbg.entities.TipoTramite":
                                                        join = "tt.";
                                                        break;
                                                    case "org.bcbg.entities.User":
                                                        join = "us.";
                                                        break;
                                                }
                                                System.out.println("objeto: " + join + "." + obj.getField());

                                                for (String w : obj.getField().split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
                                                    if (field.isEmpty()) {
                                                        field = w.toLowerCase();
                                                    } else {
                                                        field = field + "_" + w.toLowerCase();
                                                    }

                                                }
                                                switch (obj.getTypeField()) {
                                                    case "java.lang.String":
                                                        if (!operador.contains("like"))
                                                            valor.append("'").append(obj.getValor()).append("'");
                                                        else
                                                            valor.append("'%").append(obj.getValor()).append("%'");
                                                        break;
                                                    case "java.lang.Long":
                                                    case "java.lang.Boolean": //NO HAY BOLEANOS
                                                        valor.append(obj.getValor());
                                                        break;
                                                    case "java.util.Date":
                                                        field = "CONVERT(date," + field + ")";
                                                        if (!operador.contains("BETWEEN")) {
                                                            valor.append("CONVERT(date,").append("'").append(obj.getValor()).append("'").append(")");
                                                        } else {
                                                            String[] fechas = obj.getValor().split(";");
                                                            String desde = fechas[0];
                                                            String hasta = fechas[1];
                                                            valor.append("CONVERT(date,").append("'").append(desde).append("'").append(")");
                                                            valor.append(" AND ");
                                                            valor.append("CONVERT(date,").append("'").append(hasta).append("'").append(")");
                                                        }
                                                        break;
                                                }
                                                //field.setLength(field.length() - 1);
                                                where.append(join).append(field).append(operador).append(valor).append(" AND ");
                                            }

                                        }
                                    }
                                }

                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            where.setLength(where.length() - 4);
            sql = sql + where;
            System.out.println("sql: " + sql);
            ResultSet rs;
            try (Statement ps = con.createStatement()) {

                rs = ps.executeQuery(sql);
                System.out.println(rs.toString());
                while (rs.next()) {
                    Long id = rs.getLong(1);
                    System.out.println(id);
                    ids.add(id);
                }
                System.out.println("resultado: " + ids.size());
            }
            /*List<Field> fields = BcbgUtil.getPrivateFields(clazz);
            for (Field field : fields) {
                field.setAccessible(true);
                if (!field.getType().equals(List.class)) {
                    Column d = field.getAnnotation(Column.class);
                    if (d != null && !d.name().equals(Variables.omitirCampo)) {
                        Boolean esObjeto = Boolean.FALSE;
                        if (!field.getType().getPackage().getName().startsWith("java.") && field.getType().getCanonicalName().equals(campoRelacion)) {
                            esObjeto = Boolean.TRUE;

                        }
                    }
                }
            }*/
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null)
                    con.close();
            } catch (SQLException ex) {
                Logger.getLogger(SolicitudServiciosService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ids;
    }

}
