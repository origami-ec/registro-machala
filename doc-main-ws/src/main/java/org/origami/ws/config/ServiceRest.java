package org.origami.ws.config;

import org.origami.ws.util.Utility;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ServiceRest implements Serializable {


    public Object methodGET(String url, Class resultClazz, String user, String pass) {
        try {
            RestTemplate restTemplate = new RestTemplate(Utility.getClientHttpRequestFactory(user, pass));
            URI uri = new URI(url);
            ResponseEntity<Object> response = restTemplate.getForEntity(uri, resultClazz);
            return response.getBody();
        } catch (URISyntaxException | RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object methodPOST(String url, String json, Class resultClazz, String user, String pass) {
        try {
            RestTemplate restTemplate = new RestTemplate(Utility.getClientHttpRequestFactory(user, pass));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<>(json, httpHeaders);
            URI uri = new URI(url);
            ResponseEntity<Object> response = restTemplate.postForEntity(uri, entity, resultClazz);
            return response.getBody();
        } catch (URISyntaxException | RestClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List methodListGET(String url, Class resultClazz, String user, String pass) {
        try {
            RestTemplate restTemplate = new RestTemplate(Utility.getClientHttpRequestFactory(user, pass));
            Object[] obj = (Object[]) restTemplate.getForObject(new URI(url), resultClazz);
            if (obj != null) {
                return Arrays.asList(obj);
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
