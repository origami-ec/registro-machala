package org.origami.ws.service;

import org.origami.ws.config.ApplicationProperties;
import org.origami.ws.dto.PersonaDinardapDTO;
import org.origami.ws.entities.origami.Persona;
import org.origami.ws.repository.origami.PersonaRepository;
import org.origami.ws.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Date;

@Service
public class ServicioDINARDAP {

    @Autowired
    private ApplicationProperties appProps;
    @Autowired
    private PersonaRepository personaRepository;

    public PersonaDinardapDTO datosDINARDAP(String identificacion) {
        try {
            RestTemplate restTemplate = new RestTemplate(Utility.getClientHttpRequestFactory(appProps.getUsuarioDinardap(),
                    appProps.getClaveDinardap()));
            System.out.println(appProps.getUrlDinardap() + identificacion);
            System.out.println(appProps.getUsuarioDinardap() + " ---- " + appProps.getClaveDinardap());
            URI uri = new URI(appProps.getUrlDinardap() + identificacion);
            ResponseEntity<PersonaDinardapDTO> contribuyente = restTemplate.getForEntity(uri, PersonaDinardapDTO.class);
            if (contribuyente.getBody() != null) {
                System.out.println(contribuyente.getBody().toString());
                if (contribuyente.getBody().getFechaExpedicionLong() != null)
                    contribuyente.getBody().setFechaExpedicion(new Date(contribuyente.getBody().getFechaExpedicionLong()));
                if (contribuyente.getBody().getFechaNacimientoLong() != null)
                    contribuyente.getBody().setFechaNacimiento(new Date(contribuyente.getBody().getFechaNacimientoLong()));
                PersonaDinardapDTO persona = contribuyente.getBody();
                try {
                    Persona cliente = personaRepository.findTopByIdentificacionOrderByIdDesc(identificacion);
                    if (cliente != null) {
                        persona.setCorreo(cliente.getCorreo());
                        persona.setTelefono(cliente.getCelular());
                        persona.setDireccion(cliente.getDireccion());
                    } else {
                        Persona p = new Persona();
                        p.setApellidos(persona.getApellidos());
                        p.setNombres(persona.getNombres());
                        p.setIdentificacion(identificacion);
                        p.setFechaCre(new Date());
                        personaRepository.save(p);
                    }
                    System.out.println(persona.getCondicionCiudadano());
                    calculoEdad(persona);
                    System.out.println("persona.getEdad(): " + persona.getEdad());
                } catch (Exception e) {
                    System.out.println("Error al conusltar al origamiGT");
                    e.printStackTrace();
                }
                return persona;
            }
        } catch (URISyntaxException | RestClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void calculoEdad(PersonaDinardapDTO persona) {
        if (persona != null) {
            if (persona.getFechaNacimiento() != null) {
                Integer year = Utility.getAnio(persona.getFechaNacimiento());
                int month = Utility.getMes(persona.getFechaNacimiento()) + 1;
                Integer day = Utility.getDia(persona.getFechaNacimiento());
                LocalDate today = LocalDate.now();
                LocalDate birthday = LocalDate.of(year, month, day);
                Integer edad = Utility.calculateAge(birthday, today);
                persona.setEdad(edad);
            }
        }
    }


}
