package org.origami.docs.service;

import org.origami.docs.config.AppProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;

@Service
public class DecryptEncrypt {

    @Autowired
    private AppProps appProps;

    public  void decrypt(String ruta) {
        try {
            Integer key = Integer.parseInt((System.getenv("keyFiles") != null ? System.getenv("keyFiles") : appProps.getKeyFiles()));;
            FileInputStream fis = new FileInputStream(ruta);
            byte data[] = new byte[fis.available()];

            // Read the array
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
            System.out.println("Decryption Done...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public   void encrypt(String ruta) {
        try {
            Integer key = Integer.parseInt((System.getenv("keyFiles") != null ? System.getenv("keyFiles") : appProps.getKeyFiles()));;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
