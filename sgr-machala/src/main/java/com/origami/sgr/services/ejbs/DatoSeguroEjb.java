/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.services.ejbs;

import com.origami.sgr.entities.CatEnte;
import com.origami.sgr.entities.ServicioExterno;
import com.origami.sgr.models.DatoSeguro;
import com.origami.sgr.services.interfaces.DatoSeguroServices;
import com.origami.sgr.services.interfaces.Entitymanager;
import com.origami.sgr.util.Querys;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.codec.binary.Base64;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;

/**
 *
 * @author CarlosLoorVargas
 */
@Stateless(name = "datoSeguro")
public class DatoSeguroEjb implements DatoSeguroServices {

    private static final Logger LOG = Logger.getLogger(DatoSeguroEjb.class.getName());
    
    @EJB
    protected Entitymanager manager;
    
    protected JSONArray iCivil, iCias;
    protected JSONObject child, info;
    protected String field;
    protected ServicioExterno se;
    protected DatoSeguro ds = null;

    @Override
    public DatoSeguro getDatos(String cedula, boolean empresa, Integer intentos) {
        HttpsURLConnection cx;
        try {
            if (!empresa) {
                se = (ServicioExterno) manager.find(Querys.getServicioExternoByIdent, new String[]{"identificador"}, new Object[]{"ERRC"});
            } else {
                se = (ServicioExterno) manager.find(Querys.getServicioExternoByIdent, new String[]{"identificador"}, new Object[]{"ERSC"});
            }
            if (se == null) {
                return null;
            }
            cx = getAuthConex(se.getUrl() + cedula + "/" + se.getComplemento(), se.getUsuario(), se.getClave());
            if (cx != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader((cx.getInputStream())));
                String output;
                while ((output = br.readLine()) != null) {
                    JSONObject jasonObjet = new JSONObject(output);
                    if (empresa) {
                        info = jasonObjet.getJSONObject("DatosTramite").getJSONObject("InformacionCivil");
                        if(jasonObjet.getJSONObject("DatosTramite").getJSONObject("InformacionSuperCias") != null){
                            iCias = jasonObjet.getJSONObject("DatosTramite").getJSONObject("InformacionSuperCias").getJSONObject("Accionistas").getJSONObject("Companias").getJSONArray("Compania");
                        ds = this.iterate(null, iCias, info, empresa);
                        }
                    } else {
                        if (!jasonObjet.getJSONObject("DatosTramite").isNull("InformacionCivil")) {
                            iCivil = jasonObjet.getJSONObject("DatosTramite").getJSONArray("InformacionCivil");
                            ds = this.iterate(iCivil, null, info, empresa);
                        }
                    }
                }
            } else {
                return null;
            }
        } catch (IOException | JSONException e) {
            intentos++;
            if (intentos < 3) {
                this.getDatos(cedula, empresa, intentos);
            } else {
                ds = null;
                Logger.getLogger(DatoSeguroEjb.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return ds;
    }

    @Override
    public URLConnection configureConnection(URLConnection con) {
        try {
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            con.setConnectTimeout(30000);//30000
            con.setReadTimeout(40000);//40000
            if (con instanceof HttpsURLConnection) {
                HttpsURLConnection conHttps = (HttpsURLConnection) con;
                TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    @Override
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }};

                HostnameVerifier allHostsValid = new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                };
                try {
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                    con = conHttps;
                } catch (NoSuchAlgorithmException | KeyManagementException e) {
                    Logger.getLogger(DatoSeguroEjb.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DatoSeguroEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return con;
    }

    @Override
    public CatEnte getEnteFromDatoSeguro(DatoSeguro data) {
        CatEnte ente = null;
        String fields[];
        Integer num;
        String nombre = "";

        try {
            if (data != null) {
                ente = (CatEnte) manager.find(Querys.getEnteByIdent, new String[]{"ciRuc"}, new Object[]{data.getIdentificacion()});
                if (ente != null) {
                    ente.setDireccion(data.getDireccion());
                    return ente;
                } else {
                    ente = new CatEnte();
                }

                fields = data.getDescripcion().split(" ");
                num = fields.length;

                switch (num) {
                    case 3:
                        ente.setNombres(fields[2]);
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        break;
                    case 4:
                        ente.setNombres(fields[2] + " " + fields[3]);
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        break;
                    case 5:
                        ente.setNombres(fields[2] + " " + fields[3] + " " + fields[4]);
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        break;
                    default:
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        for (int i = 2; i < num; i++) {
                            nombre = nombre + fields[i];
                            if (i != num - 1) {
                                nombre = nombre + " ";
                            }
                        }
                        ente.setNombres(nombre);
                        break;
                }

                ente.setCiRuc(data.getIdentificacion());
                ente.setDireccion(data.getDireccion());
                ente.setFechaNacimiento(data.getFecNacto());
                ente.setCorreo1(data.getEmail());
                ente.setTelefono1(data.getTelefono());
                ente = (CatEnte) manager.persist(ente);
            }
        } catch (Exception e) {
            ente = null;
            LOG.log(Level.SEVERE, "getEnteFromDatoSeguro", e);
        }
        return ente;
    }

    @Override
    public CatEnte llenarEnte(DatoSeguro data, CatEnte ente, Boolean cabiarCiRuc) {
        String fields[];
        Integer num;
        String nombre = "";

        try {
            if (data != null) {
                data.setDescripcion(verificarContenido(data.getDescripcion()));
                fields = data.getDescripcion().split(" ");
                num = fields.length;

                switch (num) {
                    case 3:
                        ente.setNombres(fields[2]);
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        break;
                    case 4:
                        ente.setNombres(fields[2] + " " + fields[3]);
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        break;
                    case 5:
                        ente.setNombres(fields[2] + " " + fields[3] + " " + fields[4]);
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        break;
                    default:
                        ente.setApellidos(fields[0] + " " + fields[1]);
                        for (int i = 2; i < num; i++) {
                            nombre = nombre + fields[i];
                            if (i != num - 1) {
                                nombre = nombre + " ";
                            }
                        }
                        ente.setNombres(nombre);
                        break;
                }
                if (cabiarCiRuc) {
                    ente.setCiRuc(data.getIdentificacion());
                }
                if (data.getDireccion() != null) {
                    ente.setDireccion(verificarContenido(data.getDireccion()));
                }
                
                if(data.getCondicion().equalsIgnoreCase("FALLECIDO")){
                    ente.setEstado("F");
                }
                ente.setFechaNacimiento(data.getFecNacto());
            }
        } catch (Exception e) {
            ente = null;
            LOG.log(Level.SEVERE, "Llenar Ente", e);
        }
        return ente;
    }
    

    private DatoSeguro iterate(JSONArray civil, JSONArray cias, JSONObject obj, boolean empresa) {
        DateFormat sdf;
        try {
            if (civil != null && civil.length() > 0) {
                ds = new DatoSeguro();
                for (int i = 0; i < civil.length(); i++) {
                    child = civil.getJSONObject(i);
                    field = child.getString("NombreCampo");
                    if (field.equalsIgnoreCase("CEDULA")) {
                        ds.setIdentificacion(child.get("Valor").toString());
                    }
                    if (field.equalsIgnoreCase("NOMBRE")) {
                        ds.setDescripcion(child.getString("Valor"));
                    }
                    if (field.equalsIgnoreCase("GENERO")) {
                        ds.setGenero(child.getString("Valor"));
                    }
                    if (field.equalsIgnoreCase("CONDICIONCIUDADANO")) {
                        ds.setCondicion(child.getString("Valor"));
                    }
                    if (field.equalsIgnoreCase("FECHANACIMIENTO")) {
                        sdf = new SimpleDateFormat("dd/MM/yyyy");
                        ds.setFecNacto(sdf.parse(child.getString("Valor")));
                    }
                    if (field.equalsIgnoreCase("NACIONALIDAD")) {
                        ds.setNacionalidad(child.getString("Valor"));
                    }
                    if (field.equalsIgnoreCase("ESTADOCIVIL")) {
                        ds.setEstadoCivil(child.getString("Valor"));
                    }
                    if (field.equalsIgnoreCase("CONYUGE")) {
                        ds.setConyuge(child.getString("Valor"));
                    }
                }
            }
            if (empresa) {
                ds = new DatoSeguro();
                if (obj.getString("Valor") != null) {
                    ds.setIdentificacion(obj.getString("Valor"));
                } else {
                    return null;
                }
                for (int i = 0; i < cias.length(); i++) {
                    child = cias.getJSONObject(i);
                    field = child.getString("NombreCampo");
                    if (field.equalsIgnoreCase("CIAFCONSTITUCION")) {
                        if (child.getString("Valor") != null) {
                            ds.setFecConst(child.getString("Valor"));
                        }
                    }
                    if (field.equalsIgnoreCase("CIAOBJETOSOCIAL")) {
                        if (child.getString("Valor") != null) {
                            ds.setObjSocial(child.getString("Valor"));
                        }
                    }

                }
            }

        } catch (JSONException | ParseException e) {
            Logger.getLogger(DatoSeguroEjb.class.getName()).log(Level.SEVERE, null, e);
            ds = null;
        }
        return ds;
    }

    private HttpsURLConnection getAuthConex(String urlp, String user, String pass) {
        HttpsURLConnection cx = null;
        try {
            URL url = new URL(urlp);
            cx = (HttpsURLConnection) configureConnection(url.openConnection());
            if (cx != null) {
                String userpass = user + ":" + pass;
                String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
                cx.setRequestProperty("Content-Type", "application/json");
                cx.setRequestProperty("Authorization", basicAuth);
                if (cx.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                    return null;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DatoSeguroEjb.class.getName()).log(Level.SEVERE, null, e);
        }
        return cx;
    }

    /**
     * 
     * @param descripcion
     * @return 
     */
    private String verificarContenido(String descripcion) {
        Charset utf8 = Charset.forName("UTF-8");
        String Buffer = new String(descripcion.getBytes(), utf8);
        return Buffer;
    }

}
