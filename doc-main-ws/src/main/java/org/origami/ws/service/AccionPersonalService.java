package org.origami.ws.service;

import org.origami.ws.entities.rrhh.AccionPersonal;
import org.origami.ws.entities.security.Usuario;
import org.origami.ws.models.Data;
import org.origami.ws.repository.rrhh.AccionPersonalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccionPersonalService {

    @Autowired
    private AccionPersonalRepository repository;
    @Autowired
    private UsuarioService usuarioService;

    public Data verificarUsuario(Usuario usuario){
        AccionPersonal accionPersonal = repository.findByDestinoIdOrderByIdDesc(usuario.getUsuarioRRHH());
        Data data = new Data();
        if(accionPersonal!=null){
            System.out.println(accionPersonal);
            Boolean disponible;
            if(accionPersonal.getFechaHasta()==null){//El usuario NO esta en bomberos
                disponible = Boolean.FALSE;
            }else if(new Date().before(accionPersonal.getFechaHasta())){
                disponible = Boolean.FALSE;
            }else{
                disponible = Boolean.TRUE;
            }
            if(!disponible){
                Usuario reemplazo = usuarioService.usuarioXrrhh(accionPersonal.getOrgigenId());
                data.setId(1L);
                data.setData("No se le pueden asignar trámites por motivo de " + accionPersonal.getTipoDesc()
                        + ". en su lugar puede escoger a " + reemplazo.getNombreUsuario());
            }else{
                data.setId(0L); //El usuario esta disponible
                data.setData("El usuario esta disponble");
            }
        }else{
            data.setId(0L); //El usuario esta disponible
            data.setData("El usuario esta disponble");
        }


        return data;

    }



}
