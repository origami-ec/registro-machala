package org.bcbg.util;
 
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.validator.routines.UrlValidator;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.bcbg.config.SisVars;
import org.bcbg.entities.HistoricoTramites;
import org.bcbg.entities.User;
import static org.bcbg.util.FilesUtil.encrypt;
import org.bcbg.ws.AppEjb;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.file.UploadedFile;
import org.primefaces.model.filter.FilterConstraint;
import org.primefaces.util.LocaleUtils;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 *
 * @author User
 */
public class Utils {

    private static final int[] PATTERN = {2, 1, 2, 1, 2, 1, 2, 1, 2};
    private static final int[] CASO_9 = {4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] CASO_6 = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final String NUMERIC_REGEX = "^[0-9]+$";
    private static final String DECIMAL_REGEX = "^[+]?\\d+([.]\\d+)?$";
    private static final String EMAIL_REGEX = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[A-Za-z]{1,})$";

    public static BigDecimal bigdecimalTo2Decimals(BigDecimal inNumber) {
        String temp = inNumber.toString();
        BigDecimal outNumber;
        int indice = temp.indexOf('.');
        if (((inNumber.toString().length() - 1) - indice) > 2) {
            String tempNew = temp.substring(0, indice + 3);
            outNumber = new BigDecimal(tempNew);
            if (((temp.length()) - (indice + 1)) >= 3) {
                if (Integer.parseInt(temp.substring(tempNew.length(), tempNew.length() + 1)) >= 5) {
                    outNumber = outNumber.add(new BigDecimal("0.01"));
                }
            }
        } else {
            outNumber = inNumber;
        }
        return outNumber;
    }

    public static List<String> separadorComas(String correos) {
        List<String> correosResulList = new ArrayList<>();
        String temp = correos;
        int indice = temp.indexOf(',');
        if (indice > 0) {
            do {
                String correo1 = temp.substring(0, indice);
                correosResulList.add(correo1);
                String correoRestante = temp.substring(indice + 1, temp.length());
                temp = correoRestante;
                indice = temp.indexOf(',');

            } while (indice > 0);
            correosResulList.add(temp);
        } else {
            correosResulList.add(correos);
        }
        return correosResulList;
    }

    public static Boolean isRepetido(Collection<String> val, Object nuevo) {
        boolean i = false;
        for (String x : val) {
            if (x.equals(nuevo)) {
                i = true;
                return i;
            }
        }
        return i;
    }

    public static synchronized boolean validarEmailConExpresion(String email) {
        return validatePattern(EMAIL_REGEX, email);
    }

    public static Long restarFechas(Date fechaMenor, Date fechaMayor) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(fechaMenor);
        cal2.setTime(fechaMayor);
        long milis1 = cal1.getTimeInMillis();
        long milis2 = cal2.getTimeInMillis();
        long diff = milis2 - milis1;
        long diffDays = diff / (24 * 60 * 60 * 1000);
        /*
        long diffHour = diff / (60 * 60 * 1000);
        diffHour = diffHour - (diffDays * 24);
        if (diffHour > 0) {
            diffDays++;
        }
         */
        return diffDays;
    }

    public static synchronized boolean validateCCRuc(final String identificacion) {
        if (identificacion == null) {
            return false;
        }
        if (identificacion.trim().isEmpty()) {
            return false;
        }
        if (!validateNumberPattern(identificacion)) {
            return false;
        }
        if (identificacion.length() != 10 & identificacion.length() != 13) {
            return false;
        }
        int[] coeficientes = null;
        int indiceDigitoVerificador = 9;
        int modulo = 11;

        if ((identificacion.length() == 13) && !identificacion.substring(10, 13).equals("001")) {
            return false;
        }
        if (identificacion.charAt(2) == '9') {
            coeficientes = CASO_9;
        } else if (identificacion.charAt(2) == '6') {
            coeficientes = CASO_6;
            indiceDigitoVerificador = 8;
        } else if (identificacion.charAt(2) < '6') {
            coeficientes = PATTERN;
            modulo = 10;
        }
        return verify(identificacion.toCharArray(), coeficientes, indiceDigitoVerificador, modulo);
    }

    private static boolean verify(final char[] array, final int[] coeficientes,
            final int indiceDigitoVerificador, final int modulo) {
        if (coeficientes == null) {
            return false;
        }
        int sum = 0;
        int aux;
        for (int i = 0; i < coeficientes.length; i++) {
            aux = new Integer(String.valueOf(array[i])) * coeficientes[i];
            if ((modulo == 10) && (aux > 9)) {
                aux -= 9;
            }
            sum += aux;
        }
        int mod = sum % modulo;
        mod = mod == 0 ? modulo : mod;
        final int res = (modulo - mod);
        Integer valorVerificar = null;
        if (array.length == 13) {
            valorVerificar = Integer.valueOf(String.valueOf(array[array.length - (13 - indiceDigitoVerificador)]));
        } else if (array.length == 10) {
            valorVerificar = Integer.valueOf(String.valueOf(array[array.length - (10 - indiceDigitoVerificador)]));
        }
        return res == valorVerificar;
    }

    public static synchronized boolean validateNumberPattern(final String valor) {
        return validatePattern(NUMERIC_REGEX, valor);
    }

    public static synchronized boolean validateDecimalPattern(final String valor) {
        return validatePattern(DECIMAL_REGEX, valor);
    }

    public static synchronized boolean validatePattern(final String patron, final String valor) {
        final Pattern patter = Pattern.compile(patron);
        final Matcher matcher = patter.matcher(valor);
        return matcher.matches();
    }

    public static Archivo crearDocumentoArchivo(InputStream is, String ruta, String nombreArchivo, String tipoArchivo) throws IOException {
        File file = new File(ruta);
        try ( OutputStream out = new FileOutputStream(file)) {
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            is.close();
        }
        Archivo documento = new Archivo();
        documento.setNombre(nombreArchivo);
        documento.setTipo(tipoArchivo);
        documento.setRuta(ruta);
        return documento;
    }

    public static Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    public static Date sumarDiasFechaSinWeekEnd(Date fecha, int dias) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int hoy;
        do {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            hoy = cal.get(Calendar.DAY_OF_WEEK);
            if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                dias--;
            }
        } while (dias > 0);
        return cal.getTime();
    }

    public static String pasarUtf(String cadena) throws UnsupportedEncodingException {
        if (cadena != null) {
            byte[] bytes = cadena.getBytes("ISO-8859-1");
            cadena = new String(bytes, "UTF-8");
        }
        return cadena;
    }

    public static String randomNumericString() {
        int i = (int) (Math.random() * 100000);
        return String.valueOf(i);
    }

    public static Integer getDateValues(String formatValue, Date value) {
        Integer res = 0;
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        f.format(value);
        switch (formatValue.toUpperCase()) {
            case "Y":
                res = f.getCalendar().get(Calendar.YEAR);
                break;
            case "M":
                res = f.getCalendar().get(Calendar.MONTH);
                break;
            case "D":
                res = f.getCalendar().get(Calendar.DAY_OF_MONTH);
                break;
        }
        return res;
    }

    public static String completarCadenaConCeros(String cadena, Integer longitud) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String ceros = "";
        for (int i = 0; i < longitud; i++) {
            ceros = ceros + "0";
        }
        int tamanio = cadena.length();
        ceros = ceros.substring(0, longitud - tamanio);
        cadena = ceros + cadena;
        return cadena;
    }

    public static String convertirMesALetra(Integer fechames) {
        String mes;
        switch (fechames) {
            case 0:
                mes = "Enero";
                break;
            case 1:
                mes = "Febrero";
                break;
            case 2:
                mes = "Marzo";
                break;
            case 3:
                mes = "Abril";
                break;
            case 4:
                mes = "Mayo";
                break;
            case 5:
                mes = "Junio";
                break;
            case 6:
                mes = "Julio";
                break;
            case 7:
                mes = "Agosto";
                break;
            case 8:
                mes = "Septiembre";
                break;
            case 9:
                mes = "Octubre";
                break;
            case 10:
                mes = "Noviembre";
                break;
            case 11:
                mes = "Diciembre";
                break;
            default:
                mes = "";
        }
        return mes;
    }

    public static String quitarSaltos(String cadena) {
        return cadena.replace("\r", "").replace("\n", "");
    }

    public static boolean isDecimal(String cad) {
        try {
            Double.parseDouble(cad);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isValidDate(Object inDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(inDate.toString().trim());
            } catch (ParseException pe) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmpty(Collection l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection l) {
        return !Utils.isEmpty(l);
    }

    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !Utils.isEmptyString(l);
    }

    /**
     * Si el String es nulo returna vacio, caso contrario el mismo valor.
     *
     * @param nombres Object
     * @return Object
     */
    public static String isEmpty(String nombres) {
        if (nombres == null || nombres.trim().isEmpty()) {
            return "";
        }
        return nombres;
    }

    /**
     * Verifica que el valor numerico no sea nulo <code>value</code> y retorna
     * el mismo valor de <code>value</code> caso contrario retorna -1.
     *
     * @param <T> Object
     * @param value Valor a verificar.
     * @return si el valor de <code>value</code> es nulo retorna -1 caso
     * contrario el valor de <code>value</code>
     */
    public static <T> T isNull(T value) {
        if (value == null || value.toString().trim().length() < 0) {
            return (T) new BigInteger("-1");
        }
        return (T) value;
    }

    /**
     * Verifica si <code>value</code> es nulo y retorna <code>true</code>, caso
     * contrario retorna <code>false</code>.
     *
     * @param value Tipo de Dato Númerico de cualquier tipo primitivo o objecto.
     * @return True si el null caso contrario false.
     */
    public static Boolean isNumberNull(Number value) {
        if (value == null || value.longValue() < 0L) {
            return true;
        }
        return false;
    }

    public static <T> T get(final List<T> values, int idx) {
        if (values.size() > idx) {
            return values.get(idx);
        }
        return null;
    }

    public static <T> T get(final Collection<T> values, int idx) {
        if (values.size() > idx) {
            List<T> result = new ArrayList<>(values);
            return result.get(idx);
        }
        return null;
    }

    public static Date validateDate(String fecha) throws ParseException {
        SimpleDateFormat parser = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = parser.parse(fecha);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        date = formatter.parse(formattedDate);
        return date;
    }

    /**
     * Le da formato a la fecha con el pattern que se le pasa como parametro
     *
     * @param pattern Formato que se desea obtener.
     * @param fechaFin Fecha a dar formato.
     * @return Fecha con el formato esperado.
     */
    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
    }

    public static boolean isNum(String nom) {
        try {
            Long.parseLong(nom);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public static Integer getAnio(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.YEAR);
    }

    public static Integer getMes(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.MONTH);
    }

    public static Integer getDia(Date fechaIngreso) {
        Calendar c = Calendar.getInstance();
        c.setTime(fechaIngreso);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public static Date getFechaInit() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static HashMap sortByValues(HashMap map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static String encriptaEnMD5(String stringAEncriptar) {
        return DigestUtils.md5Hex(stringAEncriptar);
    }

    public static String encriptSHAHex(String dato) {
        System.out.println("/encrriptSHA1 " + DigestUtils.sha512Hex(dato));
        return DigestUtils.sha512Hex(dato);
    }

    public static int boolean2int(Boolean x) {
        if (x) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param <T> Object
     * @param object Lista de objecto principal
     * @param previousValues lista a retornar
     * @param duplicateArray Lista a comparar si estan repetidos.
     * @param compare 0 para realizar comparacion en binario, para hacer la
     * comparacion como texto 1
     * @return Object
     */
    public static <T> List<T> verificarRepetidos(final List<T> object, final List<T> previousValues, final List<T> duplicateArray, final int compare) {
        Iterator<T> iterator = object.iterator();
        while (iterator.hasNext()) {
            T next = iterator.next();
            int count = 0;
            for (T t : duplicateArray) {
                if (compare == 0) {
                    if (next.equals(t)) {
                        count++;
                    }
                } else {
                    if (String.valueOf(next).equalsIgnoreCase(String.valueOf(t))) {
                        count++;
                    }
                }
            }
            if (count == 0 && !previousValues.contains(next)) {
                previousValues.add(next);
            }

        }
        return previousValues;
    }

    public static String getValorUuid() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String quitarTildes(String cadena) {
        if (cadena == null) {
            return "";
        } else {
            cadena = Normalizer.normalize(cadena, Normalizer.Form.NFD);
            cadena = cadena.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            return cadena;
        }

    }

    /**
     * TRANSFORMA LA PRIMERA LETRA EN MAYUSCULA
     *
     * @param text Texto a procesar.
     * @return Texto con la primera letra en mayucula y despues de cada _ la
     * pasa a mayucula
     */
    public static String transformUpperCase(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        String[] aux = text.split("_");
        if (aux.length > 1) {
            return text;
        }
        String result = "";
        int count = 0;
        for (String string : aux) {
            if (count > 0) {
                result += string.substring(0, 1).toUpperCase().concat(string.substring(1));
            } else {
                result = string;
            }
            count++;
        }
        return result;
    }

    public static List<String> separarCaracteresList(String texto, String split) {
        if (texto == null) {
            return null;
        }
        String[] array = texto.split(split);
        List<String> resultList = new ArrayList<>();
        for (String car : array) {
            resultList.add(car);
        }
        return resultList;
    }

    /**
     * Realiza un split a los apellidos si encuenta un de o del lo ubica como
     * parte de uno de los apellidos.
     *
     * @param apellidos
     * @return
     */
    public static List<String> obtenerApellidos(String apellidos) {
        if (apellidos == null) {
            return null;
        }
        // Reemplazamos todos los espacion en blanco por uno solo
        apellidos = reemplazarEspacionEnBlanco(apellidos, " ");
        String[] split = apellidos.split(" ");
        int countApellidos = 2;
        List<String> result = new LinkedList<>();
        String cadenaAux = "";

        for (String cadena : split) {
            if (countApellidos != 0) {
                switch (split.length) {
                    case 2:
                        result.add(cadena);
                        countApellidos--;
                        break;
                    default:
                        if (cadena.equalsIgnoreCase("del") || cadena.equalsIgnoreCase("de")) {
                            cadenaAux = cadena + " ";
                            continue;
                        }
                        result.add(cadenaAux + cadena);
                        cadenaAux = "";
                        countApellidos--;
                        break;
                }
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * Busca los apellidos en toda la cadena si existe en la cadena 'de' o 'del'
     * los concatena a apellidos siguiente.
     *
     * @param nombresCompletos Nombres y apellidos completos
     * @param inicioApellidos0 Si es false indica que empezara a recorrer desde
     * la ultima cadena hacia atras
     * @return Los 2 apellidos
     */
    public static List<String> obtenerApellidos(String nombresCompletos, boolean inicioApellidos0) {
        if (nombresCompletos == null) {
            return null;
        }
        // Reemplazamos todos los espacion en blanco por uno solo
        nombresCompletos = reemplazarEspacionEnBlanco(nombresCompletos, " ");
        String[] split = nombresCompletos.split(" ");
        int countApellidos = 2;
        List<String> result = new LinkedList<>();
        String cadenaAux = "";
        if (inicioApellidos0) {
            for (String cadena : split) {
                if (countApellidos != 0) {
                    switch (split.length) {
                        case 4:
                            result.add(cadena);
                            countApellidos--;
                            break;
                        default:
                            if (cadena.equalsIgnoreCase("del") || cadena.equalsIgnoreCase("de")) {
                                cadenaAux = cadena + " ";
                                continue;
                            }
                            result.add(cadenaAux + cadena);
                            cadenaAux = "";
                            countApellidos--;
                            break;
                    }
                } else {
                    break;
                }
            }
        } else {
            for (int i = split.length - 1; i >= 0; i--) {
                if (countApellidos > -1 || (split[i].equalsIgnoreCase("del") || split[i].equalsIgnoreCase("de"))) {
                    switch (split.length) {
                        case 4:
                            if (countApellidos > 0) {
                                result.add(0, split[i]);
                                countApellidos--;
                            }
                            break;
                        default:
                            if (split[i].equalsIgnoreCase("del") || split[i].equalsIgnoreCase("de")) {
                                cadenaAux = split[i] + " ";
                                System.out.println("Cadena " + cadenaAux);
                                if (result.size() > 0) {
                                    result.set(0, cadenaAux + result.get(0));
                                    cadenaAux = "";
                                    countApellidos--;
                                }
                            } else {
                                result.add(0, cadenaAux + split[i]);
                                cadenaAux = "";
                                countApellidos--;
                            }
                            break;
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public static String reemplazarEspacionEnBlanco(String cadena, String caracterNuevo) {
        return cadena.replaceAll("\\s+", caracterNuevo);
    }

    public static String convertirFechaLetra(Date fecha) {
        String mesLetra = Utils.convertirMesALetra(Utils.getMes(fecha));
        String fechaLetra = Utils.getDia(fecha).toString() + " de "
                + mesLetra.substring(0, 1).toUpperCase() + mesLetra.substring(1)
                + " del " + Utils.getAnio(fecha).toString();
        return fechaLetra;
    }

    public static File copyFileServer(List<UploadedFile> files, String directorio) throws IOException {
        try {
            Path path = Paths.get(SisVars.rutaRepositorioArchivo);
            Files.createDirectories(path);
            for (UploadedFile uFile : files) {
                File file = new File(SisVars.rutaRepositorioArchivo + "/" + uFile.getFileName());
//                System.out.println(file.getName() + " >> " + file.length()); // tamanio de foto.
                try ( InputStream is = uFile.getInputStream();  OutputStream out = new FileOutputStream(file)) {
                    byte buf[] = new byte[2048];
                    int len;
                    while ((len = is.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
                return path.toFile();
            }
        } catch (IOException e) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Copiar Archivo al servidor", e);
        }
        return null;
    }

    public static String createDirectoryIfNotExist(String directorio) {
        Path path = Paths.get(directorio);
        if (path.toFile().exists()) {
            return directorio;
        } else {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return directorio;
    }

    public static String completarCadenaL(String cadena, int longitud, String addCadena) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String temp = addCadena;
        for (int i = 0; i < longitud; i++) {
            addCadena = addCadena + temp;
        }
        int tamanio = cadena.length();
        addCadena = addCadena.substring(0, longitud - tamanio);
        cadena = addCadena + cadena;
        return cadena;
    }

    public static String completarCadenaR(String cadena, int longitud, String addCadena) {
        if (cadena == null) {
            return "";
        }
        if (cadena.length() > longitud) {
            return cadena.substring(0, longitud);
        }
        String temp = addCadena;
        for (int i = 0; i < longitud; i++) {
            addCadena = addCadena + temp;
        }
        int tamanio = cadena.length();
        addCadena = addCadena.substring(0, longitud - tamanio);
        cadena = cadena + addCadena;
        return cadena;
    }

    public static HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory(String user, String pass) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient(user, pass));
        return clientHttpRequestFactory;
    }

    public static HttpClient httpClient(String user, String pass) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        HttpClient client1 = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        return client1;
    }

    public static String colorRandom() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }

    public static Integer convertirLetraAMes(String mes) {
        Integer mesNumerito;
        switch (mes) {
            case "Ene":
            case "ENERO":
                mesNumerito = 1;
                break;
            case "Feb":
            case "FEBRERO":
                mesNumerito = 2;
                break;
            case "Mar":
            case "MARZO":
                mesNumerito = 3;
                break;
            case "Abr":
            case "ABRIL":
                mesNumerito = 4;
                break;
            case "May":
            case "MAYO":
                mesNumerito = 5;
                break;
            case "Jun":
            case "JUNIO":
                mesNumerito = 6;
                break;
            case "Jul":
            case "JULIO":
                mesNumerito = 7;
                break;
            case "Ago":
            case "AGOSTO":
                mesNumerito = 8;
                break;
            case "Sep":
            case "SEPTIEMBRE":
                mesNumerito = 9;
                break;
            case "Oct":
            case "OCTUBRE":
                mesNumerito = 10;
                break;
            case "Nov":
            case "NOVIEMBRE":
                mesNumerito = 11;
                break;
            default:
                mesNumerito = 12;
        }
        return mesNumerito;
    }

    public static String removeLastChar(String str) {
        return removeLastChars(str, 1);
    }

    public static String removeLastChars(String str, int chars) {
        return str.substring(0, str.length() - chars);
    }

    public static List<Field> getPrivateFields(Class<?> theClass) {
        List<Field> privateFields = new ArrayList<>();

        Field[] fields = theClass.getDeclaredFields();

        for (Field field : fields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                privateFields.add(field);
            }
        }
        return privateFields;
    }

    public static String quitarTildeEspacioToMinuscula(String cadena) {
        if (cadena == null) {
            return "";
        } else {
            cadena = Normalizer.normalize(cadena, Normalizer.Form.NFD);
            return cadena.replaceAll("\\s", "").replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").toLowerCase();
        }
    }

    public static String getFilterRuta(String ruta) {
//        char[] unidad = ruta.toCharArray();
//        char a = unidad[0];
//        if ((a + "").equals("C") || (a + "").equals("D") || (a + "").equals("F")) {
//            unidad[1] = '_';
//        }
//        ruta = new String(unidad).replace("\\", "-").replace("/", "-");
        ruta = ruta.replace("\\", "/");
        if (ruta.startsWith(SisVars.rutaRepositorioFirmado)) {
            ruta = ruta.replace(SisVars.rutaRepositorioFirmado, "fd_");
        } else if (ruta.startsWith(SisVars.rutaFirmasElectronicas)) {
            ruta = ruta.replace(SisVars.rutaFirmasElectronicas, "fe_");
        } else if (ruta.startsWith(SisVars.rutaRepositorioArchivo)) {
            ruta = ruta.replace(SisVars.rutaRepositorioArchivo, "ar_");
        } else if (ruta.startsWith(SisVars.rutaRepositorioDocumental)) {
            ruta = ruta.replace(SisVars.rutaRepositorioDocumental, "doc_");
        } else if (ruta.startsWith(SisVars.rutaRepositorioNotas)) {
            ruta = ruta.replace(SisVars.rutaRepositorioNotas, "not_");
        }
        return ruta;
    }

    public static String replaceRutaArchivo(String ruta) {

        if (ruta.startsWith("fd_")) {
            ruta = ruta.replace("fd_", SisVars.rutaRepositorioFirmado);
        } else if (ruta.startsWith("fe_")) {
            ruta = ruta.replace("fe_", SisVars.rutaFirmasElectronicas);
        } else if (ruta.startsWith("ar_")) {
            ruta = ruta.replace("ar_", SisVars.rutaRepositorioArchivo);
        } else if (ruta.startsWith("doc_")) {
            ruta = ruta.replace("doc_", SisVars.rutaRepositorioDocumental);
        } else if (ruta.startsWith("not_")) {
            ruta = ruta.replace("not_", SisVars.rutaRepositorioNotas);
        }

        return ruta;
    }

    public static String mailHtmlFinTramite(HistoricoTramites historicoTramites, User u) {
        return Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo() + "-" + historicoTramites.getTipoTramite().getDescripcion(),
                "<strong>¡Hola! </strong><br><strong>" + u.getRecursoHumano().getPersona().getDetalleNombre() + "</strong>"
                + " se notifica que el trámite número: " + historicoTramites.getCodigo() + " ha finalizado.",
                "<strong>Gracias por la Atención Brindada</strong><br>",
                "Este correo fue enviado de forma automática y no requiere respuesta.");
    }

    public static String mailHtmlFinTramite(HistoricoTramites historicoTramites, String nombres) {
        return Utils.mailHtmlNotificacion("Trámite N° " + historicoTramites.getCodigo(),
                "<strong>¡Hola! " + nombres + "</strong>"
                + " se notifica que el trámite número: " + historicoTramites.getCodigo() + " ha finalizado.",
                "<strong>Gracias por la Atención Brindada</strong><br>",
                "Este correo fue enviado de forma automática y no requiere respuesta.");
    }

    public static String mailHtmlRestablecimientoPassw(String passw, String nombreUser) {
        return mailHtmlNotificacion("RESTABLECIMIENTO DE CONTRASEÑA", "<strong>¡Hola! " + nombreUser + "</strong> "
                + "inicie sesión el el sistema de ventanilla Inteligente y cambie la contraseña establecida por defecto. <br>"
                + "Contraseña: <strong>" + passw + "</strong>", "<strong>Gracias por la Atención Brindada</strong><br>",
                "Este correo fue enviado de forma automática y no requiere respuesta.");
    }

    public static String mailHtmlNotificacion(String titulo, String texto, String footerTitulo, String footerTexto) {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"utf-8\">\n"
                + "        <style>\n"
                + "\n"
                + "            body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; }\n"
                + "\n"
                + "        </style>\n"
                + "    </head>\n"
                + "    <body style=\"font-family:Arial, Helvetica, sans-serif; font-size:1em;\">\n"
                + "        <table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" >\n"
                + "            <tr>\n"
                + "                <td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#FFFFFF;\" width=\"100%\">\n"
                + "                    <table cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                        <tr>\n"
                + "                            <td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\">\n"
                + "                                <br><br><br><br>\n"
                + "                                <table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"><tr><td class=\"stylingblock-content-wrapper camarker-inner\">\n"
                + "                                    <table class=\"featured-story featured-story--top\" cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                <tr>\n"
                + "                                                    <td style=\"padding-bottom: 20px;\">\n"
                + "                                                        <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                            <tr>\n"
                + "                                                                <td class=\"featured-story__inner\" style=\"background: #fff;\">\n"
                + "                                                                    <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                        <tr>\n"
                + "                                                                            <td class=\"featured-story__content-inner\" style=\"padding: 32px 30px 45px;\">\n"
                + "                                                                                <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                                 <center><img src=\"https://www.bomberosguayaquil.gob.ec/wp-content/uploads/2018/02/LOGO-red.png\" width=\"75px\"/></center>"
                + "                                                                                    <h2 style=\"text-align:center; text-decoration: none; color: #464646;\">\n"
                + "                                                                                                      " + titulo + "</h2>\n"
                + "                                                                                    <tr>\n"
                + "                                                                                        <td class=\"featured-story__copy\" style=\"background: #fff;\" width=\"640\" align=\"center\">\n"
                + "                                                                                            <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                                                <tr>\n"
                + "                                                                                                    <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">\n"
                + "                                                                                                        " + texto + "\n"
                + "                                                                                                    </td>\n"
                + "                                                                                                </tr>\n"
                + "                                                                                            </table>\n"
                + "                                                                                        </td>\n"
                + "                                                                                    </tr>\n"
                + "                                                                                </table>\n"
                + "                                                                            </td>\n"
                + "                                                                        </tr>\n"
                + "                                                                    </table>\n"
                + "                                                                </td>\n"
                + "                                                            </tr>\n"
                + "                                                        </table>\n"
                + "                                                    </td>\n"
                + "                                                </tr>\n"
                + "                                            </table>\n"
                + "                                        </td>\n"
                + "                                    </tr>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                        <tr>\n"
                + "                            <td class=\"footer\" width=\"640\" align=\"center\" style=\"padding-top: 10px;\">\n"
                + "                                <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                    <tr>\n"
                + "                                        <td align=\"center\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 14px; line-height: 18px; color: #738597; padding: 0 20px 40px;\">\n"
                + "                                            <br>      <br>\n"
                + "                                            <strong>" + footerTitulo + "</strong>\n"
                + "                                            <br>\n"
                + "                                            " + footerTexto + "\n"
                + "                                        </td>\n"
                + "                                    </tr>\n"
                + "                                </table>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                    </table>\n"
                + "                </td>\n"
                + "            </tr>\n"
                + "        </table>\n"
                + "    </body>\n"
                + "</html>";
    }

    public static Boolean isAjaxRequest() {
        FacesContext fc = FacesContext.getCurrentInstance();
        return fc.isPostback();
    }

    public static String getUrlViewFile(String archivo) {
        return SisVars.wsMedia + "resource/pdf/" + Utils.getFilterRuta(archivo) + "/descarga/" + SisVars.VIEW_DOC;
    }
    
    public static String getUrlViewImage(String archivo) {
        return SisVars.wsMedia + "resource/image/" + Utils.getFilterRuta(archivo);
    }

    public static String getUrlDownloadFile(String archivo) { 
         String[] tipo = archivo.split("\\.");
        String lastOne = tipo[tipo.length-1];
        String ruta = "";
        switch (lastOne) {
            case "pdf":
                ruta = SisVars.wsMedia + "resource/pdf/" + Utils.getFilterRuta(archivo) + "/descarga/" + SisVars.DOWNLOAD_DOC;
                break;
            case "xlsx" :
            case "xls" :
                ruta = SisVars.wsMedia + "resource/xlxs/" + Utils.getFilterRuta(archivo);
                break;
            case "docs" :
            case "docx" :
            case "doc" :
            case "txt" :
            case "csv" :
                ruta = SisVars.wsMedia + "resource/docs/" + Utils.getFilterRuta(archivo) + "/descarga/" + SisVars.DOWNLOAD_DOC;    
                break;
            case "tiff":
            case "bmp":
            case "wmf":
            case "dib":
            case "gif":
            case "jpg":
                ruta = SisVars.wsMedia + "resource/descargarImage/" + Utils.getFilterRuta(archivo);    
                break;
            default:
                ruta = SisVars.wsMedia + "resource/image/" + Utils.getFilterRuta(archivo);    
                break;
        }
        return ruta;
    }

    public static int anioActual() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static boolean validacionCorreos(String email) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public static String conversionBase64(String fileName) {
        try {
            File file = new File(fileName);
            byte[] bytes = loadFile(file);
            Base64.Encoder e = Base64.getEncoder();
            byte[] encoded = e.encode(bytes);
            String encodedString = new String(encoded);
            return encodedString;
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] loadFile(File file) {
        try {
            InputStream is = new FileInputStream(file);

            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

            is.close();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] convertToBytes(Object object) throws IOException {
        try ( ByteArrayOutputStream bos = new ByteArrayOutputStream();  ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    public static Date getDayMonday() {
        Date dayMonday;
        Calendar c = Calendar.getInstance();
        switch (c.get(Calendar.DATE)) {
            case Calendar.MONDAY:
                dayMonday = new Date();
                break;
            case Calendar.TUESDAY:
                dayMonday = DateUtils.addDays(new Date(), -1);
                break;
            case Calendar.WEDNESDAY:
                dayMonday = DateUtils.addDays(new Date(), -2);
                break;
            case Calendar.THURSDAY:
                dayMonday = DateUtils.addDays(new Date(), -3);
                break;
            case Calendar.FRIDAY:
                dayMonday = DateUtils.addDays(new Date(), -4);
                break;
            default:
                LocalDate dt = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                dayMonday = java.util.Date.from(dt.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                break;
        }
        System.out.println("//UTILS DIA " + c.get(Calendar.DATE));
        System.out.println("//FECHA " + dayMonday);
        return dayMonday;
    }

    public static boolean filter(FacesContext context, Collection<FilterMeta> filterBy, Object o) {
        boolean matching = true;

        for (FilterMeta filter : filterBy) {
            FilterConstraint constraint = filter.getConstraint();
            Object filterValue = filter.getFilterValue();

            try {
                Object columnValue = String.valueOf(ReflexionEntity.getCampo(o, filter.getField()));
                matching = constraint.isMatching(context, columnValue, filterValue, LocaleUtils.getCurrentLocale());
            } catch (Exception e) {
                matching = false;
            }

            if (!matching) {
                break;
            }
        }

        return matching;
    }

    /**
     * Este metodo recorre todos los campos de la clase y permite armar la URL
     * para ser enviada al servidor solo con los campos que sean diferente de
     * null =) se debe usar despues del path de la URL x ejemplo persona/find
     *
     * @param obj Es la clase inicializada
     * @param clazz es el .class del Objecto
     * @param startIndex Sirve para iniciar la busqueda con null caso contrario
     * es un objecto dentro de la clase
     * @return los parametros para la url
     */
    public static String armarUrlCamposObj(Object obj, Class clazz, String startIndex) {
        try {
            if (obj != null) {

                String urlParametros = startIndex == null ? "?" : "";
                List<Field> fields = Utils.getPrivateFields(clazz);
//            Collections.sort(fields, (a, b) -> {
//                return (b.getType().getPackageName().substring(0) ).compareTo((a.getType().getPackageName()).substring(0));
//            });
                //  fields.sort((Field a, Field b) -> b.getType().getPackageName().compareTo(a.getType().getPackageName()));
                Object value;
                for (Field field : fields) {
                    field.setAccessible(true);
                    value = field.get(obj);
                    if (!field.getType().equals(List.class)) {
                        if (value != null) {
                            if (field.getType().getPackage().getName().startsWith("java.lang")) {
                                if (startIndex == null) {
                                    urlParametros = urlParametros + field.getName() + "=" + value + "&";
                                } else {
                                    urlParametros = urlParametros + (startIndex + "." + field.getName()) + "=" + value + "&";
                                }
                            } else {//ES UN OBJECTO
                                urlParametros = urlParametros + armarUrlCamposObj(value, value.getClass(), (startIndex == null ? "" : (startIndex + ".")) + field.getName());
                            }
                        }
                    }
                }
                if (urlParametros.length() > 1) {//SIGNIFICA QUE NINGUN CAMPO ES NULO
                    if (urlParametros.substring(0, urlParametros.length() - 1).contains("&") && startIndex == null) {
                        urlParametros = Utils.removeLastChar(urlParametros).replace(" ", "%20");//ELIMINA EL ULTIMO & DE LA URL =o

                    }
                    return urlParametros;
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(AppEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static String getDate(Date fecha) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return dateFormat.format(fecha);
    }

    public static Integer numeroPaginas(String archivo) {
        try {
            FilesUtil.decrypt(SisVars.keyFiles, archivo); 
            PDDocument document = PDDocument.load(new File(archivo));
            Integer i = document.getNumberOfPages();
            document.close();
            FilesUtil.encrypt(SisVars.keyFiles, archivo); 
            return i;
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        } catch (Throwable ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
         return 0;
    }

    public static Map<String, String> getDatos(String valor) {
        Map<String, String> map = new HashMap<>();
        String apellidos = "";
        String nombres = "";
        String[] datos = valor.split(" ");
        for (int i = 0; i < datos.length; i++) {
            switch (i) {
                case 0:
                case 1:
                    apellidos = apellidos + datos[i] + " ";
                    break;
                default:
                    nombres = nombres + datos[i] + " ";
            }
        }
        map.put("nombre", nombres.trim());
        map.put("apellido", apellidos.trim());
        return map;
    }

     public static boolean isValidUrl(String url){
            UrlValidator defaultValidator = new UrlValidator();
      return defaultValidator.isValid(url);
     }
     public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
         Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
}
     
   public static void setEnv(Map<String, String> newenv) throws Exception {
        try {
          Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
          Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
          theEnvironmentField.setAccessible(true);
          Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
          env.putAll(newenv);
          Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
          theCaseInsensitiveEnvironmentField.setAccessible(true);
          Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
          cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
          Class[] classes = Collections.class.getDeclaredClasses();
          Map<String, String> env = System.getenv();
          for(Class cl : classes) {
            if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
              Field field = cl.getDeclaredField("m");
              field.setAccessible(true);
              Object obj = field.get(env);
              Map<String, String> map = (Map<String, String>) obj;
              map.clear();
              map.putAll(newenv);
            }
          }
        }
      }

}
