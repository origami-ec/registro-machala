package org.origami.ws.resource;

import org.origami.ws.entities.origami.Solicitud;
import org.origami.ws.entities.origami.TipoTramite;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.repository.origami.RolUsuarioRepository;
import org.origami.ws.repository.origami.SolicitudRepository;
import org.origami.ws.repository.origami.TipoTramiteRepository;
import org.origami.ws.repository.security.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping("/origami/api/")
public class SolicitudResource {

    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private UsuarioRepository userRepository;
    @Autowired
    private RolUsuarioRepository rolUsuarioRepository;
    @Autowired
    private TipoTramiteRepository tipoTramiteRepository;

    @RequestMapping(value = "create/solicitud", method = RequestMethod.POST)
    public ResponseEntity<?> createSolicitud(@RequestBody Solicitud solicitud) {
        try {
            if (solicitud.getUsuario() != null && solicitud.getUsuario().getId() != null) {
                TipoTramite tpt = tipoTramiteRepository.findByEstadoTrueAndActivitykey(solicitud.getActivityKey());
                solicitud.setFechaSolicitud(new Date());
                Usuario u = userRepository.findById(solicitud.getUsuario().getId()).get();
                /*solicitud.setSolNombres(u.getEnte().getNombres());
                solicitud.setSolApellidos(u.getEnte().getApellidos());
                solicitud.setSolCedula(u.getEnte().getIdentificacion());
                solicitud.setSolCelular(u.getEnte().getTelefono() != null ?
                        u.getEnte().getTelefono() :
                        u.getEnte().getTelefono() != null ?
                                u.getEnte().getTelefono() : "");
                solicitud.setSolDireccion(u.getEnte().getDireccion()
                        != null ? u.getEnte().getDireccion() : "");
                solicitud.setSolCorreo(u.getEnte().getCorreo() != null ? u.getEnte().getCorreo() :
                        u.getEnte().getCorreo() != null ? u.getEnte().getCorreo() : "");
                List<RolUsuario> rol_usuario = rolUsuarioRepository.
                        findAllByRol_NombreAndRol_IsDirector("coodinador_administrativo", Boolean.TRUE);*/

             /*   HistoricoTramites historicoTramites = remoteService.iniciarTramite(solicitud.getActivityKey(), !rol_usuario.isEmpty() ?
                        rol_usuario.get(0).getUsuario().getUsuario()
                        : "", u.getId(), tpt.getId());
                if (historicoTramites != null && historicoTramites.getId() != null) {
                    solicitud.setTramite(historicoTramites);
                }*/
            }
            return new ResponseEntity<>(solicitudRepository.save(solicitud), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
