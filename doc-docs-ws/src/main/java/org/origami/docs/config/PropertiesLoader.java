package org.origami.docs.config;

import org.origami.docs.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 2)
public class PropertiesLoader implements ApplicationRunner {

    @Autowired
    private AppProps appProps;

    @Override
    public void run(ApplicationArguments var1) throws Exception{
        System.out.println("Creando directorios");
        Utils.crearDirectorio(appProps.getRutaArchivo());
        Utils.crearDirectorio(appProps.getRutaImagen());
        Utils.crearDirectorio(appProps.getRutaArchivosFD());
        Utils.crearDirectorio(appProps.getRutaArchivosNT());
    }
}
