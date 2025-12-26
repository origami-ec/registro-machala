package gob.ec.dinardap.controller;

import ec.gob.ws.DINARDAPService.Entidad;
import gob.ec.dinardap.dinardap.ServicioDINARDAP;
import gob.ec.dinardap.entities.PubPersona;
import gob.ec.dinardap.models.RespuestaDinarp;
import gob.ec.dinardap.repository.PubPersonaRepository;
import gob.ec.dinardap.repository.ValoresRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dinardap/")
public class PersonaController {

    @Autowired
    private ValoresRepository valoresRepository;
    @Autowired
    private PubPersonaRepository pubPersonaRepository;
    @Autowired
    private ServicioDINARDAP servicioDINARDAP;

    @RequestMapping(value = "/identificacion/{identificacion}", method = RequestMethod.GET)
    public ResponseEntity<PubPersona> findPersonByCi(@PathVariable(value = "identificacion") String identificacion) {
        try {
            return new ResponseEntity<>(pubPersonaRepository.findByCedRuc(identificacion), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);

    }

    @RequestMapping(value = "aplicacion/{aplicacion}/identificacion/{identificacion}", method = RequestMethod.GET)
    public ResponseEntity<PubPersona> findEnte(@PathVariable(value = "aplicacion") String aplicacion,
            @PathVariable(value = "identificacion") String identificacion) {
        try {
            String paqueteSri = valoresRepository.findByCode("DINARDAP_SRI").getValorString();
            String paqueteDemografico = valoresRepository.findByCode("DINARDAP_DEMOGRAFICO").getValorString();
            String codigo = "", parametro = "";
            Boolean acept;
            switch (aplicacion) {
                case "SIGERI":
                case "VENTANILLA-WEB":
                case "VENTANILLA-MOVIL":
                case "ENTREGA-TRAMITE":
                    acept = Boolean.TRUE;
                    break;
                default:
                    acept = Boolean.FALSE;
                    break;
            }
            String codigoPaquete = identificacion.length() == 10 ? "C" : "J";
            switch (codigoPaquete) {
                case "C":
                    codigo = paqueteDemografico;
                    parametro = "identificacion";
                    break;
                case "J":
                    codigo = paqueteSri;
                    parametro = "identificacion";
                    break;
                default:
                    acept = Boolean.FALSE;
                    break;

            }
            if (acept) {
                PubPersona pubPersona = servicioDINARDAP.datosDINARDAP(identificacion, codigo, parametro);
                pubPersona.setTipoDocumento(codigoPaquete);
                return new ResponseEntity<>(pubPersona, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "consulta/paquete/{paquete}/parametro/{parametro}/documento/{documento}", method = RequestMethod.GET)
    public ResponseEntity<List<Entidad>> consultaDinarpNativa(@PathVariable(value = "paquete") String paquete,
            @PathVariable(value = "parametro") String parametro, @PathVariable(value = "documento") String documento) {
        try {

            List<Entidad> entidades = servicioDINARDAP.datosNativosDINARP(paquete, parametro, documento);
            return new ResponseEntity<>(entidades, HttpStatus.OK);

        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "especifico/documento/{documento}", method = RequestMethod.GET)
    public ResponseEntity<RespuestaDinarp> consutaDinarpEspecifico(@PathVariable(value = "documento") String documento) {
        try {
            return new ResponseEntity<>(servicioDINARDAP.datosEspecificosDINARP(documento), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
