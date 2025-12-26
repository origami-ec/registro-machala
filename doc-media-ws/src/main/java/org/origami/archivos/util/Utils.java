package org.origami.archivos.util;

import org.origami.archivos.config.AppProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.util.DateConverter;
import org.apache.tomcat.util.http.fileupload.FileUtils;

@Service
public class Utils {

    @Autowired
    private AppProps appProps;

    public String replaceRutaArchivo(String ruta) {
        System.out.println(ruta);
        if (ruta.startsWith("fd_")) {
            ruta = ruta.replace("fd_", appProps.getArchivoFirmados());
        } else if (ruta.startsWith("ar_")) {
            ruta = ruta.replace("ar_", appProps.getArchivos());
        } else if (ruta.startsWith("imgtemp_")) {
            ruta = ruta.replace("imgtemp_", appProps.getImagenes());
        } else if (ruta.startsWith("doc_")) {
            ruta = ruta.replace("doc_", appProps.getDocumentos());
        } else if (ruta.startsWith("not_")) {
            ruta = ruta.replace("not_", appProps.getNotas());
        } else if (ruta.startsWith("ma_")) {
            ruta = ruta.replace("ma_", appProps.getManuales());
        } else {
            ruta = appProps.getArchivos() + ruta;
        }

        return ruta;
    }

    public String addRutaArchivo(String nombre) {
        try {
            return appProps.getDocumentos() + nombre;
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
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
                            System.out.println("Create directory: " + path);
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
            Logger.getLogger(Utils.class.getName()).log(Level.INFO, "No se pudo crear el directorio: {0}", ex.getMessage());
        }
    }

    public static void borrarDirectorio(String path) {
        try {
            FileUtils.deleteDirectory(new File(path));
            System.out.println("Delete directory: " + path);
        } catch (IOException e) {
            System.out.println(e);
        }
    }
    public static String base64ToString(String string) {
        byte[] decoded = Base64.getDecoder().decode(string.getBytes());
        return new String(decoded, StandardCharsets.UTF_8);
    }
    
    // Conversor de fecha PDF (ejemplo: D:20230105154500-05'00')
    public static Date parsePdfDate(String pdfDate) {
        try {
            return DateConverter.toCalendar(pdfDate).getTime();
        } catch (Exception e) {
            return null;
        }
    }
}
