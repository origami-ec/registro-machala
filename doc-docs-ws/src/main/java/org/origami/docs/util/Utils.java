package org.origami.docs.util;

import org.apache.commons.io.FilenameUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
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
                            System.out.println("Create directory:" + path);
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

    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !isEmpty(l);
    }

    public static String isEmpty(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return "";
        }
        return nombres;
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }

    public static String getDate(Date fecha) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(fecha);
    }

    public String getExtensionFile(String archivo) {
        return com.google.common.io.Files.getFileExtension(archivo);
    }

    public static int diasRestantes(Date fecha) {
        DateFormat dd = new SimpleDateFormat("dd/MM/yyyy");
        int dias = 0;
        boolean activo = false;
        Calendar calendar;
        Date aux;
        do {
            calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, dias);
            aux = calendar.getTime();
            if (dd.format(aux).equals(dd.format(fecha)))
                activo = true;
            else
                dias++;
        } while (!activo);
        return dias;
    }

    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
    }

    public static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static String verificarArchivo(File file, String tipo) {
        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        System.out.println("mimeType: " + mimeType);
        if (mimeType == null) {
            mimeType = tipo;
        }
        if (mimeType.startsWith("application/pdf"))
            return "PDF";
        if (mimeType.startsWith("image"))
            return "IMG";
        if (mimeType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml"))
            return "WORD";
        if (mimeType.startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml"))
            return "EXCEL";
        if (mimeType.startsWith("text/plain"))
            return "TXT";
        return "";
    }

    public static Date getFechaMongo() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date()); // Configuramos la fecha DE HOY
        calendar.add(Calendar.HOUR, -5);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }

    public static Integer getAnio(Date fechaIngreso) {
        if (fechaIngreso == null) {
//            System.out.println("Retorna Anio Actual si es nulo.");
            return getAnio(new Date());
        }
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.YEAR);
    }

    /**
     * Gets mes.
     *
     * @param fechaIngreso the fecha ingreso
     * @return the anio
     */
    public static Integer getMes(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * Gets mes.
     *
     * @param fechaIngreso the fecha ingreso
     * @return the anio
     */
    public static Integer getDia(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static String getFilterRuta(String ruta) {
        ruta = Base64.getEncoder().encodeToString(ruta.getBytes());
        return ruta;
    }

    public static String convertirArchivoABase64(String rutaArchivo) {
        try {
            // Crear un objeto File a partir de la ruta del archivo
            File archivo = new File(rutaArchivo);

            // Leer el contenido del archivo en un array de bytes
            byte[] fileContent = Files.readAllBytes(archivo.toPath());

            // Convertir el array de bytes a una cadena en Base64
            return Base64.getEncoder().encodeToString(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
