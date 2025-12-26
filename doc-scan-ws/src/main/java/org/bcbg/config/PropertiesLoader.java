package org.bcbg.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@Order(value = 2)
public class PropertiesLoader implements ApplicationRunner {

    @Value("${app.archivosPCLocal}")
    private String archivosPCLocal;
    @Value("${app.imagenesPCLocal}")
    private String imagenesPCLocal;
    @Value("${app.rutaArchivos}")
    private String rutaArchivos;

    @Override
    public void run(ApplicationArguments var1) throws Exception {
        System.out.println("Creando directorios");
        crearDirectorio(archivosPCLocal);
        crearDirectorio(imagenesPCLocal);
        crearDirectorio(rutaArchivos);
    }

    public static void crearDirectorio(String directory) {
        try {
            if (directory != null) {
                Path path = Paths.get(directory.endsWith("/") ? directory : directory.concat("/"));
                Path parent = path.getParent();
                if (parent != null) {
                    if (parent.toFile().exists()) {
                        if (!path.toFile().exists()) {
                            Files.createDirectory(path);
                        } else {
                            System.out.println("Exists directory " + directory);
                        }
                    } else {
                        Files.createDirectories(path);
                    }
                } else {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertiesLoader.class.getName()).log(Level.INFO, "No se pudo crear el directorio: {0}", ex.getMessage());
        }
    }
}
