/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.ec.dinardap.dinardap;

import gob.ec.dinardap.entities.PubPersona;
import gob.ec.dinardap.repository.ValoresRepository;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import ec.gob.ws.DINARDAPService.Columna;
import ec.gob.ws.DINARDAPService.Consultar;
import ec.gob.ws.DINARDAPService.ConsultarFaultException;
import ec.gob.ws.DINARDAPService.ConsultarResponse;
import ec.gob.ws.DINARDAPService.Entidad;
import ec.gob.ws.DINARDAPService.Fila;
import ec.gob.ws.DINARDAPService.Interoperador;
import ec.gob.ws.DINARDAPService.Parametro;
import ec.gob.ws.DINARDAPService.Parametros;
import gob.ec.dinardap.models.RespuestaDinarp;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author gutya
 */
@Service
public class ServicioDINARDAP {

    @Autowired
    private ValoresRepository valoresRepository;

    /**
     * @param identificacion
     * @param codigoPaquete
     * @param parametro
     * @return
     */
    public PubPersona datosDINARDAP(String identificacion,
            String codigoPaquete, String parametro) {
        PubPersona persona;
        try {
            String paqueteSri = valoresRepository.findByCode("DINARDAP_SRI").getValorString();
            String paqueteDemografico = valoresRepository.findByCode("DINARDAP_DEMOGRAFICO").getValorString();
            String urlInterOperatividad = valoresRepository.findByCode("DINARDAP_URL").getValorString();
            String userInterOperatividad = valoresRepository.findByCode("DINARDAP_USUARIO").getValorString();
            String passInterOperatividad = valoresRepository.findByCode("DINARDAP_CLAVE").getValorString();

            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(Interoperador.class);
            factory.setAddress(urlInterOperatividad);
            factory.setUsername(userInterOperatividad);
            factory.setPassword(passInterOperatividad);

            Interoperador port = (Interoperador) factory.create();
            Client client = ClientProxy.getClient(port);

            if (client != null) {
                HTTPConduit conduit = (HTTPConduit) client.getConduit();
                HTTPClientPolicy policy = new HTTPClientPolicy();
                policy.setAllowChunking(false);
                conduit.setClient(policy);

            }
            Parametro paramCodigoPaquete = new Parametro();
            paramCodigoPaquete.setNombre("codigoPaquete");
            paramCodigoPaquete.setValor(codigoPaquete);

            Parametro paramIdent = new Parametro();
            paramIdent.setNombre(parametro);
            paramIdent.setValor(identificacion);

            Parametros parametros = new Parametros();
            parametros.getParametro().add(paramCodigoPaquete);
            parametros.getParametro().add(paramIdent);

            if (codigoPaquete.equals(paqueteSri)) {
                //System.out.println("holi ");
                Parametro fuenteDatos = new Parametro();
                fuenteDatos.setNombre("fuenteDatos");
                fuenteDatos.setValor("");
                parametros.getParametro().add(fuenteDatos);
            }

            Consultar consultar = new Consultar();
            consultar.setParametros(parametros);
            ConsultarResponse response = port.consultar(consultar);

            List<Entidad> entidades = response.getPaquete().getEntidades().getEntidad();
            persona = new PubPersona();
            Map<String, String> datos;
            //filas
            for (Entidad entidad : entidades) {
                //System.out.println("Información de:" + entidad.getNombre());
                for (Fila fila : entidad.getFilas().getFila()) {
                    //columnas
                    for (Columna columna : fila.getColumnas().getColumna()) {
                        //se obtiene los valores de cada columna
                        System.out.println(columna.getCampo() + " = " + columna.getValor());
                        switch (columna.getCampo()) {
                            case "cedula":
                            case "numeroRuc":
                                persona.setCedRuc(columna.getValor());
                                break;
                            case "nombre":
                                if (codigoPaquete.equals(paqueteDemografico)) {
                                    datos = getDatos(columna.getValor());
                                    persona.setNombres(datos.get("nombre"));
                                    persona.setApellidos(datos.get("apellido"));
                                }
                                break;
                            case "razonSocial":
                                persona.setNombres(columna.getValor());
                                break;
                            case "nombreComercial":
                                persona.setApellidos(columna.getValor());
                                break;
                            case "callesDomicilio":
                            case "direccionCorta":
                                persona.setDireccion(columna.getValor());
                                break;
                            case "estadoCivil":
                                persona.setEstadoCivil(columna.getValor());
                                break;
                            case "fechaNacimiento":
                                persona.setFechaNacimientoLong(new SimpleDateFormat("dd/MM/yyyy").parse(columna.getValor()).getTime());
                                break;
                            case "email":
                                persona.setCorreo1(columna.getValor());
                                break;
                            case "telefonoDomicilio":
                                persona.setTelefono1(columna.getValor());
                                break;
                            case "telefonoTrabajo":
                                persona.setTelefono2(columna.getValor());
                                break;
                            case "conyuge":
                                if (columna.getValor() != null && !columna.getValor().isEmpty()) {
                                    datos = getDatos(columna.getValor());
                                    persona.setNombreConyuge(datos.get("nombre"));
                                    persona.setApellidoConyuge(datos.get("apellido"));
                                }
                                break;
                            case "fechaExpedicion":
                                if (codigoPaquete.equals(paqueteDemografico)) {
                                    persona.setFechaExpedicionLong(new SimpleDateFormat("dd/MM/yyyy").parse(columna.getValor()).getTime());
                                }
                                break;
                            case "fechaInicioActividades":
                                if (codigoPaquete.equals(paqueteSri)) {
                                    persona.setFechaExpedicionLong(new SimpleDateFormat("yyyy-MM-dd").parse(columna.getValor()).getTime());
                                }
                                break;
                            case "condicionCiudadano":
                                if (codigoPaquete.equals(paqueteDemografico)) {
                                    persona.setCondicionCiudadano(columna.getValor());
                                }
                                break;
                            case "fechaDefuncion":
                                if (codigoPaquete.equals(paqueteDemografico)) {
                                    persona.setFechaDefuncion(columna.getValor());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        } catch (ConsultarFaultException | ParseException ex) {
            System.out.println(ex);
            persona = null;
        }
        return persona;
    }

    private Map<String, String> getDatos(String valor) {
        Map<String, String> map = new HashMap<>();
        String apellidos = "";
        String nombres = "";
        String[] datos = valor.split(" ");
        if (datos.length > 2) {
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
        } else {
            for (int i = 0; i < datos.length; i++) {
                switch (i) {
                    case 0:
                        apellidos = apellidos + datos[i] + " ";
                        break;
                    case 1:
                        nombres = nombres + datos[i] + " ";
                        break;
                    default:
                        nombres = " ";
                }
            }
        }

        map.put("nombre", nombres.trim());
        map.put("apellido", apellidos.trim());
        return map;
    }

    public List<Entidad> datosNativosDINARP(String paquete, String parametro, String documento) {
        try {
            String urlInterOperatividad = valoresRepository.findByCode("DINARDAP_URL").getValorString();
            String userInterOperatividad = valoresRepository.findByCode("DINARDAP_USUARIO").getValorString();
            String passInterOperatividad = valoresRepository.findByCode("DINARDAP_CLAVE").getValorString();
            String paqueteSri = valoresRepository.findByCode("DINARDAP_SRI").getValorString();

            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(Interoperador.class);
            factory.setAddress(urlInterOperatividad);
            factory.setUsername(userInterOperatividad);
            factory.setPassword(passInterOperatividad);

            Interoperador port = (Interoperador) factory.create();
            Client client = ClientProxy.getClient(port);

            if (client != null) {
                HTTPConduit conduit = (HTTPConduit) client.getConduit();
                HTTPClientPolicy policy = new HTTPClientPolicy();
                policy.setAllowChunking(false);
                conduit.setClient(policy);

            }
            Parametro paramCodigoPaquete = new Parametro();
            paramCodigoPaquete.setNombre("codigoPaquete");
            paramCodigoPaquete.setValor(paquete);

            Parametro paramIdent = new Parametro();
            paramIdent.setNombre(parametro);
            paramIdent.setValor(documento);

            Parametros parametros = new Parametros();
            parametros.getParametro().add(paramCodigoPaquete);
            parametros.getParametro().add(paramIdent);

            if (paquete.equals(paqueteSri)) {
                Parametro fuenteDatos = new Parametro();
                fuenteDatos.setNombre("fuenteDatos");
                fuenteDatos.setValor("");
                parametros.getParametro().add(fuenteDatos);
            }

            Consultar consultar = new Consultar();
            consultar.setParametros(parametros);
            ConsultarResponse response = port.consultar(consultar);

            return response.getPaquete().getEntidades().getEntidad();

        } catch (ConsultarFaultException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public RespuestaDinarp datosEspecificosDINARP(String documento) {
        try {
            RespuestaDinarp modelo = new RespuestaDinarp();
            String urlInterOperatividad = valoresRepository.findByCode("DINARDAP_URL").getValorString();
            String userInterOperatividad = valoresRepository.findByCode("DINARDAP_USUARIO").getValorString();
            String passInterOperatividad = valoresRepository.findByCode("DINARDAP_CLAVE").getValorString();
            String paqueteDemografico = valoresRepository.findByCode("DINARDAP_DEMOGRAFICO").getValorString();
            String paqueteBiometrico = valoresRepository.findByCode("DINARDAP_BIOMETRICO").getValorString();

            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(Interoperador.class);
            factory.setAddress(urlInterOperatividad);
            factory.setUsername(userInterOperatividad);
            factory.setPassword(passInterOperatividad);

            Interoperador port = (Interoperador) factory.create();
            Client client = ClientProxy.getClient(port);

            if (client != null) {
                HTTPConduit conduit = (HTTPConduit) client.getConduit();
                HTTPClientPolicy policy = new HTTPClientPolicy();
                policy.setAllowChunking(false);
                conduit.setClient(policy);

            }
            Parametro paramCodigoPaquete = new Parametro();
            paramCodigoPaquete.setNombre("codigoPaquete");
            paramCodigoPaquete.setValor(paqueteDemografico);

            Parametro paramIdent = new Parametro();
            paramIdent.setNombre("identificacion");
            paramIdent.setValor(documento);

            Parametros parametros = new Parametros();
            parametros.getParametro().add(paramCodigoPaquete);
            parametros.getParametro().add(paramIdent);

            Consultar consultar = new Consultar();
            consultar.setParametros(parametros);
            ConsultarResponse response = port.consultar(consultar);

            List<Entidad> entidades = response.getPaquete().getEntidades().getEntidad();

            //filas
            for (Entidad entidad : entidades) {
                //System.out.println("Información de:" + entidad.getNombre());
                for (Fila fila : entidad.getFilas().getFila()) {
                    //columnas
                    for (Columna columna : fila.getColumnas().getColumna()) {
                        //se obtiene los valores de cada columna
                        switch (columna.getCampo()) {
                            case "actaDefuncion":
                                modelo.setActaDefuncion(columna.getValor());
                                break;
                            case "anioInscripcionNacimiento":
                                modelo.setAnioInscripcionNacimiento(columna.getValor());
                                break;
                            case "cedula":
                                modelo.setCedula(columna.getValor());
                                break;
                            case "condicionCiudadano":
                                modelo.setCondicionCiudadano(columna.getValor());
                                break;
                            case "conyuge":
                                modelo.setConyuge(columna.getValor());
                                break;
                            case "estadoCivil":
                                modelo.setEstadoCivil(columna.getValor());
                                break;
                            case "fechaDefuncion":
                                modelo.setFechaDefuncion(columna.getValor());
                                break;
                            case "fechaInscripcionDefuncion":
                                modelo.setFechaInscripcionDefuncion(columna.getValor());
                                break;
                            case "fechaMatrimonio":
                                modelo.setFechaMatrimonio(columna.getValor());
                                break;
                            case "fechaNacimiento":
                                modelo.setFechaNacimiento(columna.getValor());
                                break;
                            case "individualDactilar":
                                modelo.setIndividualDactilar(columna.getValor());
                                break;
                            case "nombre":
                                modelo.setNombre(columna.getValor());
                                break;
                            case "nombreMadre":
                                modelo.setNombreMadre(columna.getValor());
                                break;
                            case "nombrePadre":
                                modelo.setNombrePadre(columna.getValor());
                                break;
                            case "profesion":
                                modelo.setProfesion(columna.getValor());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            paramCodigoPaquete = new Parametro();
            paramCodigoPaquete.setNombre("codigoPaquete");
            paramCodigoPaquete.setValor(paqueteBiometrico);

            paramIdent = new Parametro();
            paramIdent.setNombre("identificacion");
            paramIdent.setValor(documento);

            parametros = new Parametros();
            parametros.getParametro().add(paramCodigoPaquete);
            parametros.getParametro().add(paramIdent);

            consultar = new Consultar();
            consultar.setParametros(parametros);
            response = port.consultar(consultar);

            entidades = response.getPaquete().getEntidades().getEntidad();

            for (Entidad entidad : entidades) {
                //System.out.println("Información de:" + entidad.getNombre());
                for (Fila fila : entidad.getFilas().getFila()) {
                    //columnas
                    for (Columna columna : fila.getColumnas().getColumna()) {
                        //se obtiene los valores de cada columna
                        switch (columna.getCampo()) {
                            case "foto":
                                modelo.setFoto(columna.getValor());
                                break;
                            case "firma":
                                modelo.setFirma(columna.getValor());
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            return modelo;
        } catch (ConsultarFaultException ex) {
            System.out.println(ex);
            return null;
        }
    }

}
