package org.origami.ws.util;

import org.apache.commons.text.RandomStringGenerator;
import org.hibernate.cfg.Environment;
import org.mp4parser.IsoFile;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {
    private static int rangoCodigo = 9999;

    @Autowired
    private static Environment env;



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

    public static Date stringTodate(String s) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(s);
        } catch (Exception e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date getFechaActual(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha DE HOY
        calendar.add(Calendar.HOUR, -5);  // numero de horas a añadir, o restar en caso de horas<0
        return calendar.getTime(); // Devuelve el objeto Date con las nuevas horas añadidas
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }


    public static Integer diferenciasMinutos(Date desde, Date hasta) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd h:mm", Locale.US);
        SimpleDateFormat sdfResultMinutos = new SimpleDateFormat("m", Locale.US);
        Integer minutos = 0;
        try {
            Date d = sdf.parse(sdf.format(desde));
            Date h = sdf.parse(sdf.format(hasta));
            Date difference = getDifferenceBetwenDates(d, h);
            minutos = Integer.parseInt(sdfResultMinutos.format(difference));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return minutos;


    }

    public static Date getDifferenceBetwenDates(Date dateInicio, Date dateFinal) {
        long milliseconds = dateFinal.getTime() - dateInicio.getTime();
        int seconds = (int) (milliseconds / 1000) % 60;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.SECOND, seconds);
        c.set(Calendar.MINUTE, minutes);
        c.set(Calendar.HOUR_OF_DAY, hours);
        return c.getTime();
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

    public static Date calcularEntregasFechas(Date fecha, int dias, int horas, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        int hoy;
        if (dias > 0) {
            do {
                cal.add(Calendar.DAY_OF_YEAR, 1);
                hoy = cal.get(Calendar.DAY_OF_WEEK);
                if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {
                    dias--;
                }
            } while (dias > 0);
        }
        if (horas > 0) {
            do {
                cal.add(Calendar.HOUR, 1);
                hoy = cal.get(Calendar.DAY_OF_WEEK);
                if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {

                    horas--;

                }
            } while (horas > 0);
        }
        if (minute > 0) {
            do {
                cal.add(Calendar.MINUTE, 1);
                hoy = cal.get(Calendar.DAY_OF_WEEK);
                if (hoy != Calendar.SATURDAY && hoy != Calendar.SUNDAY) {

                    minute--;

                }
            } while (minute > 0);
        }
        return cal.getTime();
    }

    public static String armarRutaJasper(String nombre, String rutaReporte) {
        return rutaReporte + nombre + ".jasper";
    }

    public static String colorHexadecimal() {
        Random random = new Random();
        int nextInt = random.nextInt(0xffffff + 1);
        return String.format("#%06x", nextInt);
    }

    public static Map getUrlsImagenes(String rutaImagenes) {
        Map map = new HashMap();
        map.put("HEADER_URL", rutaImagenes + "cabecera.png");
        map.put("WATERMARK_URL", rutaImagenes + "marca_agua.png");
        map.put("FOOTER_URL", rutaImagenes + "pie_pagina.png");
        return map;
    }

    public static String genCodigoVerif() {
        char[][] pairs = {{'a', 'z'}, {'0', '9'}};
        RandomStringGenerator sg = new RandomStringGenerator.Builder().withinRange(pairs).build();
        return sg.generate(15);
    }


    public static boolean isEmptyString(String l) {
        return l == null || l.isEmpty();
    }

    public static boolean isNotEmptyString(String l) {
        return !isEmptyString(l);
    }

    public static boolean validacionCorreos(String email) {
        Pattern pattern = Pattern
                .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        return mather.find();
    }

    public static String mailHtmlNotificacion(String titulo, String texto, String footerTitulo, String footerTexto) {
        return "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n"
                + "        <style> " +
                "             body {margin:0; padding:0; -webkit-text-size-adjust:none; -ms-text-size-adjust:none;} img{line-height:100%; outline:none; text-decoration:none; -ms-interpolation-mode: bicubic;} a img{border: none;} #backgroundTable {margin:0; padding:0; width:100% !important; } a, a:link{color:#2A5DB0; text-decoration: underline;} table td {border-collapse:collapse;} span {color: inherit; border-bottom: none;} span:hover { background-color: transparent; } " +
                "               .column {float: left; width: 15.33%;} .row:after{content: \"\";display: table;clear: both;} "
                + "        </style>\n"
                + "    </head>\n"
                + "    <body style=\"background: #e1e1e1;font-family:Arial, Helvetica, sans-serif; font-size:1em;\">\n"
                + "        <table id=\"backgroundTable\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"background:#e1e1e1;\">\n"
                + "            <tr>\n"
                + "                <td class=\"body\" align=\"center\" valign=\"top\" style=\"background:#e1e1e1;\" width=\"100%\">\n"
                + "                    <table cellpadding=\"0\" cellspacing=\"0\">\n"
                + "                        <tr>\n"
                + "                            <td class=\"main\" width=\"640\" align=\"center\" style=\"padding: 0 10px;\">\n"
                + "                                <br><br><br><br>\n"
                + "                                <table style=\"min-width: 100%; \" class=\"stylingblock-content-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
                "                                   <tr>" +
                "                                      <td class=\"stylingblock-content-wrapper camarker-inner\">\n"
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
                + "                                                                                 <div class=\"row\">  "
                + "                                                                                     <div class=\"column\">"
                + "                                                                                           <img src=\"https://www.bomberosguayaquil.gob.ec/wp-content/uploads/2018/02/LOGO-red.png\" width=\"75px\" style=\"float: left; margin-right: 20px;\" />"
                + "                                                                                     </div>"
                + "                                                                                     <div style=\"float: left; width: 69.33%;\">"
                + "                                                                                           <h2 style=\"text-decoration: none; color: #2b2121; text-align: center;\">" + titulo + "</h2>" +
                "                                                                                       </div>"
                + "                                                                                     <div class=\"column\">"
                + "                                                                                          <img src=\"https://www.bomberosguayaquil.gob.ec/wp-content/uploads/2018/02/LOGO-red.png\" width=\"75px\" style=\"float: right; margin-left: 20px;\" />"
                + "                                                                                      </div>"
                + "                                                                                  </div>"
                + "                                                                              <br><br>"
                + "                                                                              <tr>\n"
                + "                                                                                        <td class=\"featured-story__copy\" style=\"background: #fff;padding: 3%;\" width=\"640\" align=\"center\">\n"
                + "                                                                                            <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                                                                                <tr>\n"
                + "                                                                                                    <td style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 16px; line-height: 22px; color: #555555; padding-top: 16px;\" align=\"left\">\n"
                + "                                                                                                        " + texto + "\n"
                + "                                                                                                    </td>\n"
                + "                                                                                                </tr>\n"
                + "                                                                                            </table>\n"
                + "                                                                                        </td>"
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
                + "                        <tr>"
                + "                        <tr>\n"
                + "                            <td class=\"footer\" width=\"640\" align=\"center\" style=\"padding-top: 5px;\">\n"
                + "                                <table cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                                    <tr>\n"
                + "                                        <td align=\"center\" style=\"font-family: Geneva, Tahoma, Verdana, sans-serif; font-size: 14px; line-height: 18px; color: #738597; padding: 0 20px 40px;\">\n"
                + "                                            <br><br>\n"
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

    public static byte[] base64ToByte(String base64) {
        return Base64.decodeBase64(base64);
    }

    public static File writeByteToFile(String base64, String ruta) {
        try {
            byte[] data = Base64.decodeBase64(base64.getBytes());
            FileUtils.writeByteArrayToFile(new File(ruta), data);
            return new File(ruta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String limpiarAcentos(String cadena) {
        String limpio = null;
        if (cadena != null) {
            String valor = cadena;
            valor = valor.toUpperCase();
            // Normalizar texto para eliminar acentos, dieresis, cedillas y tildes
            limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
            // Quitar caracteres no ASCII incluyendo la enie, interrogacion que abre, exclamacion que abre, grados, U con dieresis.
            limpio = limpio.replaceAll("[^\\p{ASCII}(\u00A1)(\u00BF)(\u00B0)(U\u0308)(u\u0308)]", "");
            // Regresar a la forma compuesta, para poder comparar la enie con la tabla de valores
            limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
        }
        return limpio;
    }


    public static Long copyFileServer(String archivo, byte[] byteArchivo, Boolean video) {
        Long duracion = 60000L;
        File file = new File(archivo);
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(byteArchivo);
            os.close();
            if (video) {
                IsoFile isoFile = new IsoFile(archivo);
                double lengthInSeconds = (double)
                        isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                        isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
                System.err.println(lengthInSeconds);
                String duracionString = Double.valueOf(lengthInSeconds).toString().replace(".", "");
                System.err.println(duracionString);
                duracion = Long.parseLong(duracionString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return duracion;
    }


    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
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
}
