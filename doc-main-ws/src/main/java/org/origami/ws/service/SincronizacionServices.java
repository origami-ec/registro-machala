package org.origami.ws.service;

import org.origami.ws.async.RemoteService;
import org.origami.ws.dto.Correo;
import org.origami.ws.entities.databcbg.CompaniaBCBG;
import org.origami.ws.entities.databcbg.DepartamentoBCBG;
import org.origami.ws.entities.origami.Departamento;
import org.origami.ws.entities.origami.TareasActivas;
import org.origami.ws.entities.origami.Valores;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.repository.databcbg.CompaniaBCBGRepository;
import org.origami.ws.repository.databcbg.DepartamentoBCBGRepository;
import org.origami.ws.repository.origami.DepartamentoRepository;
import org.origami.ws.repository.origami.ValoresRepository;
import org.origami.ws.util.Utility;
import org.origami.ws.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SincronizacionServices {

    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private DepartamentoBCBGRepository departamentoBCBGRepository;
    @Autowired
    private CompaniaBCBGRepository companiaBCBGRepository;
    @Autowired
    private TareasActivasService tareasActivasService;
    @Autowired
    private ValoresRepository valoresRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private RemoteService remoteService;

    public void actualizarDatosDepartamento() {
        List<DepartamentoBCBG> departamentosBCBG = departamentoBCBGRepository.findAll();
        Departamento depOrg;
        for (DepartamentoBCBG depsBcbg : departamentosBCBG) {
            depOrg = new Departamento();
            Optional<Departamento> depOp = departamentoRepository.findByIdBcbgAndTipo(depsBcbg.getId(), Constantes.tipoLugarDep);
            if (depOp.isPresent()) {
                depOrg = depOp.get();
            }
            depOrg.setIdBcbg(depsBcbg.getId());
            depOrg.setTipo(Constantes.tipoLugarDep);
            depOrg.setNombre(depsBcbg.getNombre());
            depOrg.setCodigo(depsBcbg.getCodigo());
            depOrg.setEstado(!depsBcbg.getEstado());
            departamentoRepository.save(depOrg);
        }
        List<CompaniaBCBG> companiasBCBG = companiaBCBGRepository.findAll();
        for (CompaniaBCBG c : companiasBCBG) {
            depOrg = new Departamento();
            Optional<Departamento> depOp = departamentoRepository.findByIdBcbgAndTipo(c.getId(), Constantes.tipoLugarComp);
            if (depOp.isPresent()) {
                depOrg = depOp.get();
            }
            depOrg.setIdBcbg(c.getId());
            depOrg.setTipo(Constantes.tipoLugarComp);
            depOrg.setNombre(c.getNombre());
            depOrg.setCodigo(c.getCodigo());
            depOrg.setEstado(!c.getEstado());
            departamentoRepository.save(depOrg);
        }
        System.out.println("actualizarDatosDepartamento - ok");
    }


    public void tramitesXcaducar() {
        Valores v = valoresRepository.findByCode("DIAS_NOTIFICACION_TRAMITE_CADUCA");

        List<TareasActivas> tareasActivas = tareasActivasService.findAllTareasActivas();
        for (TareasActivas t : tareasActivas) {
            // if (t.getFechaEntrega()) {
            Usuario u = usuarioService.findOne(new Usuario(t.getAssignee()));
            if (u != null) {
                String message = Utility.mailHtmlNotificacion("Trámite: <strong>" + t.getCodigo() + "</strong>",
                        "Se le notifica que el trámite " + t.getCodigo() + " de " + t.getNombreSolicitante() + " esta próximo a caducar.",
                        "Gracias por la atención brindada",
                        "Este correo fue enviado de forma automática y no requiere respuesta.");
                Correo correo = new Correo();
                correo.setDestinatario(u.getRecursoHumano().getPersona().getCorreo());
                correo.setAsunto("Trámite: " + t.getCodigo());
                correo.setMensaje(message);
                remoteService.enviarCorreo(correo);
            }

            //  }
        }
    }


}
