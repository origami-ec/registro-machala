package org.origami.docs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrigamiDocsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrigamiDocsApplication.class, args);
    }
   /* @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Guayaquil"));
    }*/
}
