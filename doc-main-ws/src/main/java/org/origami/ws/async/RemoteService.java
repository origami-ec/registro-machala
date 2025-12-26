package org.origami.ws.async;

import org.origami.ws.config.ApplicationProperties;
import org.origami.ws.dto.Correo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemoteService {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ApplicationProperties applicationProperties;

    @Async
    public void enviarCorreo(Correo correo) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Correo> requestEntity = new HttpEntity<>(correo, headers);
            ResponseEntity<String> response = restTemplate.exchange(applicationProperties.getUrlZull() + "enviarCorreo",
                    HttpMethod.POST, requestEntity, String.class);
            System.out.println("STATUS CORREO " + response.getStatusCode());
            if (response.getStatusCode() == HttpStatus.OK) {
                //   return response.getBody();
            }
            //return null;
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
    }

}
