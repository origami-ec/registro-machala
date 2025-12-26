/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bcbg.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.lang3.StringUtils;
import org.bcbg.config.SisVars;
import org.primefaces.model.file.UploadedFile;

/**
 *
 * @author gutya
 */
public class FilesUtil implements Serializable {

    /**
     *
     */
    private static final Logger LOG = Logger.getLogger(FilesUtil.class.getName());

    public static void createDirectory(String newDirectory) {
        try {
            if (newDirectory != null) {
                Path path = Paths.get(newDirectory.endsWith("/") ? newDirectory : newDirectory.concat("/"));
                Path parent = path.getParent();
                if (parent != null) {
                    if (parent.toFile().exists()) {
                        if (!path.toFile().exists()) {
                            Files.createDirectory(path);
                            System.out.println("create directory:" + path);
                        } else {
                            System.out.println("Exists directory " + newDirectory);
                        }
                    } else {
                        Files.createDirectories(path);
                    }
                } else {
                    Files.createDirectories(path);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FilesUtil.class.getName()).log(Level.INFO, "No se pudo crear el directorio: {0}", ex.getMessage());
        }
    }

    /**
     * Return Copia el archivo en el servidor y de vualve el archivo guardado
     *
     * @param event FileUploadEvent de primefaces
     * @param ruta RUTA_FIRMA_ELECTRONICA => C:\servers_files\fd
     * @return InputStream
     * @throws java.io.IOException
     */
    public static File copyFileServer(UploadedFile event, String ruta) throws IOException {
        try {
            String archivo = new String(event.getFileName().getBytes(Charset.defaultCharset()), "UTF-8");
            String nombreArchivo = new Date().getTime() + "_" + StringUtils.stripAccents(archivo).replace(" ", "_").replace("-", "_");
            String rutaNombreArchivo = ruta + nombreArchivo;
            File file = new File(rutaNombreArchivo);

            InputStream is;
            is = event.getInputStream();
            try ( OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[1024];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            is.close(); 
            encrypt(SisVars.keyFiles, rutaNombreArchivo); 
            return new File(rutaNombreArchivo);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Copiar Archivo al servidor", e);
        } catch (Throwable ex) {
            Logger.getLogger(FilesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static File copyFileServerHidden(UploadedFile uFile, String directory, String fileName) throws IOException {
        try {
            Path path = Paths.get(directory);

            Files.createDirectories(path);
            File file = new File(directory + File.separator + fileName);
            try ( InputStream is = uFile.getInputStream();  OutputStream out = new FileOutputStream(file)) {
                byte buf[] = new byte[2048];
                int len;
                while ((len = is.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
            path = Paths.get(directory + File.separator + fileName);
            Files.setAttribute(path, "dos:hidden", true);
            return path.toFile();
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static void encrypt(Integer key, String ruta) throws Throwable {
        FileInputStream fis = new FileInputStream(ruta);
        byte data[] = new byte[fis.available()];
        fis.read(data);
        int i = 0;
        for (byte b : data) {
            data[i] = (byte) (b ^ key);
            i++;
        }
        FileOutputStream fos = new FileOutputStream(ruta);
        fos.write(data);
        fos.close();
        fis.close();
        System.out.println("Encryption Done...");
    }

    public static void decrypt(Integer key, String ruta) throws Throwable {
        FileInputStream fis = new FileInputStream(ruta);

        // Converting image into byte array,it will
        // Create a array of same size as image.
        byte data[] = new byte[fis.available()];

        // Read the array
        fis.read(data);
        int i = 0;

        // Performing an XOR operation
        // on each value of
        // byte array to Decrypt it.
        for (byte b : data) {
            data[i] = (byte) (b ^ key);
            i++;
        }

        // Opening file for writting purpose
        FileOutputStream fos = new FileOutputStream(ruta);

        // Writting Decrypted data on Image
        fos.write(data);
        fos.close();
        fis.close();
        System.out.println("Decryption Done...");
    }

  
    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();
    }

}
