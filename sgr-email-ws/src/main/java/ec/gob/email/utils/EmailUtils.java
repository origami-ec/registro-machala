package ec.gob.email.utils;

import java.util.Collection;

public class EmailUtils {


    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !isEmpty(l);
    }

    public static String replaceRutaArchivo(String ruta, String replace) {
        if (ruta.startsWith("ar_")) {
            System.out.println(ruta);
            ruta = ruta.replace("ar_", replace );
        }
        return ruta;
    }
}
