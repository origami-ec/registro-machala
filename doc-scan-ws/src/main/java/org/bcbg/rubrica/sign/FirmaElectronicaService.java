/*
 * Copyright (C) 2020
 * Authors: Ricardo Arguello, Misael Fernández
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.*
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.bcbg.rubrica.sign;

import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.IOUtils;
import org.bcbg.model.DocumentosScan;
import org.bcbg.model.FirmaDocDesk;
import org.bcbg.model.FirmaDocumento;
import org.bcbg.model.FirmaElectronica;
import org.bcbg.rubrica.certificate.CertUtils;
import org.bcbg.rubrica.exceptions.EntidadCertificadoraNoValidaException;
import org.bcbg.rubrica.keystore.Alias;
import org.bcbg.rubrica.keystore.KeyStoreProviderFactory;
import org.bcbg.rubrica.sign.pdf.PDFSigner;
import org.bcbg.rubrica.sign.pdf.PdfUtil;
import org.bcbg.rubrica.utils.*;
import org.bcbg.rubrica.validaciones.DocumentoUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.bcbg.rubrica.certificate.CertEcUtils;

import static org.bcbg.rubrica.certificate.CertUtils.getTokens;
import static org.bcbg.rubrica.certificate.CertUtils.seleccionarAlias;

import org.bcbg.rubrica.certificate.to.Documento;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import org.bcbg.rubrica.keystore.FileKeyStoreProvider;
import org.bcbg.rubrica.keystore.KeyStoreProvider;
import org.bcbg.service.DecryptEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.List;

/**
 * Metodo de pruebas funcionales
 *
 * @author mfernandez
 */
@Service
public class FirmaElectronicaService {

    @Value("${app.origamiGT}")
    private String origamiGT;
    @Value("${app.paramVisor}")
    private String paramVisor;
    @Value("${app.rutaArchivos}")
    private String rutaArchivos;
    @Value("${app.nombre}")
    private String nombreApp;
    @Value("${app.ext}")
    private String ext;

    @Autowired
    private DecryptEncrypt decryptEncrypt;

    public List<Alias> getAllTokens() {
        try {
            String tipoKeyStoreProvider = "TOKEN";
            KeyStore ks = KeyStoreProviderFactory.getKeyStore("", tipoKeyStoreProvider);

            if (ks != null) {
                return getTokens(ks);
            }

        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return new ArrayList<>();
    }


    private static Properties parametros(FirmaElectronica firmaElectronica) {

        Properties params = new Properties();
        params.setProperty(PDFSigner.SIGNING_LOCATION, firmaElectronica.getUbicacion());
        params.setProperty(PDFSigner.SIGNING_REASON, firmaElectronica.getMotivo());
        params.setProperty(PDFSigner.SIGN_TIME, ZonedDateTime.ofInstant(firmaElectronica.getFechaEmision().toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        params.setProperty(PDFSigner.LAST_PAGE, firmaElectronica.getNumeroPagina().toString());
        params.setProperty(PDFSigner.TYPE_SIG, firmaElectronica.getTipoFirma());
        params.setProperty(PDFSigner.INFO_QR, "Firmado digitalmente con RUBRICA\nhttps://minka.gob.ec/rubrica/rubrica");
        params.setProperty(PDFSigner.FONT_SIZE, "4.5");
        // Posicion firma
        if (firmaElectronica.getTipoFirma().equalsIgnoreCase("QR")) {
            params.setProperty(PDFSigner.FONT_SIZE, "3.0");
        }
        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_X, firmaElectronica.getPosicionX1());
        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_Y, firmaElectronica.getPosicionY1());

        return params;
    }

    public FirmaElectronica validarCertificado(FirmaElectronica firmaElectronica) throws IOException, KeyStoreException {

        try {
            System.out.println(rutaArchivos + firmaElectronica.getArchivo());
            System.out.println(rutaArchivos + firmaElectronica.getArchivo() + ext);
            // ARCHIVO
            File hideFile = new File(rutaArchivos + firmaElectronica.getArchivo());
            File showFile = new File(rutaArchivos + firmaElectronica.getArchivo() + ext);
            boolean success = hideFile.renameTo(showFile);
            if (success) {
                try {
                    KeyStoreProvider ksp = new FileKeyStoreProvider(rutaArchivos + firmaElectronica.getArchivo() + ext);
                    KeyStore keyStore = ksp.getKeystore(firmaElectronica.getClave().toCharArray());
                    // TOKEN
                    //KeyStore keyStore = KeyStoreProviderFactory.getKeyStore(PASSWORD);

                    String alias = seleccionarAlias(keyStore);
                    X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate(alias);
                    firmaElectronica.setUid(Utils.getUID(x509Certificate));
                    firmaElectronica.setCn(Utils.getCN(x509Certificate));
                    firmaElectronica.setEmision(CertEcUtils.getNombreCA(x509Certificate));
                    firmaElectronica.setFechaEmision(x509Certificate.getNotBefore());
                    firmaElectronica.setFechaExpiracion(x509Certificate.getNotAfter());
                    firmaElectronica.setIsuser(x509Certificate.getIssuerX500Principal().getName());

                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
                    TemporalAccessor accessor = dateTimeFormatter.parse(TiempoUtils.getFechaHoraServidor());
                    Date fechaHoraISO = Date.from(Instant.from(accessor));

                    //Validad certificado revocado
                    Date fechaRevocado = UtilsCrlOcsp.validarFechaRevocado(x509Certificate);
                    if (fechaRevocado != null && fechaRevocado.compareTo(fechaHoraISO) <= 0) {
                        firmaElectronica.setEstadoFirma("Certificado revocado: " + fechaRevocado);
                    }
                    if (fechaHoraISO.compareTo(x509Certificate.getNotBefore()) <= 0 || fechaHoraISO.compareTo(x509Certificate.getNotAfter()) >= 0) {
                        firmaElectronica.setEstadoFirma("Certificado caducado");
                    }
                    if (firmaElectronica.getEstadoFirma() == null) {
                        firmaElectronica.setEstadoFirma("Certificado emitido por entidad certificadora acreditada:  " + (Utils.verifySignature(x509Certificate) ? "Si" : "No"));
                    }
                    firmaElectronica.setFirmaCaducada(Boolean.FALSE);
                    if (firmaElectronica.getFechaExpiracion() != null) {
                        Date hoy = new Date();
                        Date expiracion = firmaElectronica.getFechaExpiracion();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(expiracion);
                        calendar.add(Calendar.MONTH, -1); // restar un mes a la fecha

                        Date expiracionProx = calendar.getTime();
                        if (hoy.after(expiracion)) { //LA FIRMA YA SE ENCUENTRA CADUCADA
                            firmaElectronica.setFirmaCaducada(Boolean.TRUE);
                            firmaElectronica.setEstadofirmaCaducada("Su firma electrónica esta vencida por favor renuevela: Fecha de expiración: "
                                    + dateFormatPattern("yyyy-MM-dd", expiracion));
                        } else if (hoy.after(expiracionProx)) {
                            firmaElectronica.setEstadofirmaCaducada("Su firma electrónica esta próxima a caducar: Fecha de expiración: "
                                    + dateFormatPattern("yyyy-MM-dd", expiracion) + " - Faltan: " + diasRestantes(expiracion) + " días.");
                        }
                    }
                } catch (KeyStoreException | EntidadCertificadoraNoValidaException | InvalidKeyException e) {
                    e.printStackTrace();
                    firmaElectronica.setEstadoFirma("Clave incorrecta");
                }
                showFile.renameTo(hideFile);

            } else {
                firmaElectronica.setEstadoFirma("No se pudo validar");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmaElectronica;
    }

    public FirmaElectronica firmarDocumentoArchivo(FirmaElectronica firmaElectronica, Boolean esDesk) throws Exception {
        String firmaRuta;
        // ARCHIVO
        File hideFile = new File(rutaArchivos + firmaElectronica.getArchivo());
        File showFile = null;// SI ES NULO ES POR LA APP DE ESCRITORIO
        Boolean desencripta = Boolean.FALSE;
        if (!esDesk) { //SE AGREGA PARA CONDICIONAR SI ES WEB O NO XK EN LA WEB LAS FIRMAS ESTAN EL SERVIDOR
            showFile = new File(rutaArchivos + firmaElectronica.getArchivo() + ext);
            esDesk = hideFile.renameTo(showFile);
            firmaRuta = rutaArchivos + firmaElectronica.getArchivo() + ext;
            decryptEncrypt.decrypt(firmaElectronica.getArchivoFirmar());
            desencripta = Boolean.TRUE;
        } else {
            firmaRuta = firmaElectronica.getAliasToken();
        }

        if (esDesk) {
            // File has been renamed
            KeyStoreProvider ksp = new FileKeyStoreProvider(firmaRuta);
            KeyStore keyStore = ksp.getKeystore(firmaElectronica.getClave().toCharArray());
            if (firmaElectronica.getNumeroPagina() == null) {
                PDDocument doc = PDDocument.load(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())));
                firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
            }
            byte[] docByteArry = DocumentoUtils.loadFile(replaceRutaArchivo(firmaElectronica.getArchivoFirmar()));
            Properties parametros = parametros(firmaElectronica);

            Signer signer = Utils.documentSigner(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())));
            String alias = seleccionarAlias(keyStore);

            PrivateKey key = (PrivateKey) keyStore.getKey(alias, firmaElectronica.getClave().toCharArray());
            X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();

            if (x509CertificateUtils.validarX509Certificate((X509Certificate) keyStore.getCertificate(alias))) {//validación de firmaEC
                try {
                    Certificate[] certChain = keyStore.getCertificateChain(alias);

                    byte[] signed = signer.sign(docByteArry, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, key, certChain, parametros);
                    System.out.println("final firma\n-------");
                    String nombreDocumento = FileUtils.crearNombreFirmado(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), FileUtils.getExtension(signed));
                    java.io.FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
                    if (showFile == null) {
                        abrirDocumento(nombreDocumento);
                    }
                    fos.write(signed);
                    fos.close();
                    File file = new File(nombreDocumento);
                    firmaElectronica.setArchivoFirmado(nombreDocumento);
                    firmaElectronica.setUrlArchivoFirmado(origamiGT + Constantes.apiArchivos + "ar_" + file.getName());
                    guardarDocumentoFirmado(firmaElectronica, nombreDocumento);
                    System.out.println("nombreDocumento: "   +nombreDocumento);
                    if (showFile != null)
                        showFile.renameTo(hideFile);
                    if (desencripta) {
                        decryptEncrypt.encrypt(firmaElectronica.getArchivoFirmar());
                        decryptEncrypt.encrypt(nombreDocumento);
                    }
                    return firmaElectronica;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public FirmaElectronica firmarDocumentoDesk(FirmaDocDesk firmaDocDesk) {
        System.out.println(firmaDocDesk.toString());
        FirmaElectronica firmaElectronica = new FirmaElectronica();
        firmaElectronica.setMotivo(firmaElectronica.getMotivo() != null ? firmaElectronica.getMotivo() : "");
        firmaElectronica.setEsToken(firmaDocDesk.getTipo().equals("TOKEN") ? Boolean.TRUE : Boolean.FALSE);
        firmaElectronica.setClave(firmaDocDesk.getClave());
        firmaElectronica.setNumeroPagina(Integer.valueOf(firmaDocDesk.getPagina()));
        firmaElectronica.setFechaEmision(new Date());
        firmaElectronica.setPosicionX1(firmaDocDesk.getPosicionX());
        firmaElectronica.setPosicionY1(firmaDocDesk.getPosicionY());
        firmaElectronica.setTipoFirma("QR");
        firmaElectronica.setUbicacion(firmaDocDesk.getUbicacion() != null ? firmaDocDesk.getUbicacion() : "");
        firmaElectronica.setAliasToken(firmaDocDesk.getToken() != null ? firmaDocDesk.getToken() : "");
        firmaElectronica.setArchivoFirmar(firmaDocDesk.getArchivo().trim());
        try {
            if (firmaDocDesk.getTipo().equals("TOKEN")) {
                firmaElectronica = firmarDocumentoToken(firmaElectronica);
            } else {
                firmaElectronica = firmarDocumentoArchivo(firmaElectronica, Boolean.TRUE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return firmaElectronica;
    }


    public FirmaElectronica firmarDocumentoToken(FirmaElectronica firmaElectronica) throws Exception {
        if (firmaElectronica.getNumeroPagina() == null) {
            PDDocument doc = PDDocument.load(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())));
            firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
        }

        Properties parametros = parametros(firmaElectronica);
        KeyStore ks = KeyStoreProviderFactory.getKeyStore("", "TOKEN");
        String alias = firmaElectronica.getAliasToken();
        X509Certificate x509Certificate = CertUtils.getCert(ks, alias);
        X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();
        if (x509CertificateUtils.validarX509Certificate(x509Certificate) && x509Certificate != null && alias != null) {
            System.out.println("x509CertificateUtils");
            byte[] docSigned = firmar(ks, alias, new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), "".toCharArray(),
                    parametros);
            System.out.println("final firma\n-------");
            String nombreDocumento = FileUtils.crearNombreFirmado(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), FileUtils.getExtension(docSigned));
            java.io.FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
            abrirDocumento(nombreDocumento);
            fos.write(docSigned);
            fos.close();
            File file = new File(nombreDocumento);
            firmaElectronica.setArchivoFirmado(file.getName());
            firmaElectronica.setUrlArchivoFirmado(origamiGT + Constantes.apiArchivos + "ar_" + file.getName());
            guardarDocumentoFirmado(firmaElectronica, nombreDocumento);
        }


        return firmaElectronica;
    }


    public Documento verificarDocumento(FirmaElectronica firmaElectronica) throws Exception {
        decryptEncrypt.decrypt(firmaElectronica.getArchivoFirmado());
        File document = new File(firmaElectronica.getArchivoFirmado());
        Documento documento = Utils.verificarDocumento(document);
        decryptEncrypt.encrypt(firmaElectronica.getArchivoFirmado());
        return documento;
    }


    public String replaceRutaArchivo(String ruta) {
        if (ruta.startsWith("ar_")) {
            ruta = ruta.replace("ar_", rutaArchivos);
        }
        return ruta;
    }

    public FirmaDocumento generarDocumentoLocal(FirmaDocumento local) {
        if (local.getClave() == null || local.getClave().isEmpty()) {
            local.setEstado("Escriba una contraseña");
            return local;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String archivo = rutaArchivos + local.getMotivo() + ".pdf";
        InputStream is = null;
        File firma = new File(local.getArchivo());
        if (firma.exists()) {
            File target = new File(archivo);
            try {
                // System.out.println(appProps.getSgr() + "generarPDF/" + local.getDocumento());
                ResponseEntity<Resource> responseEntity = restTemplate.exchange(origamiGT + Constantes.apiArchivos + "generarPDF/" + local.getDocumento(), HttpMethod.GET, new HttpEntity<>(headers), Resource.class);
                is = responseEntity.getBody().getInputStream();
                try (InputStream inputStream = responseEntity.getBody().getInputStream()) {
                    org.apache.commons.io.FileUtils.copyInputStreamToFile(inputStream, target);
                }
                FirmaElectronica firmaElectronica = new FirmaElectronica();
                firmaElectronica.setUbicacion(nombreApp);
                firmaElectronica.setMotivo(local.getMotivo());
                firmaElectronica.setArchivoFirmar(archivo);
                firmaElectronica.setClave(local.getClave());
                firmaElectronica.setArchivo(local.getArchivo());
                firmaElectronica.setTipoFirma("QR");
                firmaElectronica.setUrlQr("");
                firmaElectronica.setPosicionX1(local.getPosicionX1().toString());
                firmaElectronica.setPosicionY1(local.getPosicionY1().toString());
                firmaElectronica.setNumeroPagina(local.getNumeroPagina());
                firmaElectronica = firmarDocumentoArchivo(firmaElectronica, Boolean.FALSE);
                byte[] docByteArry = DocumentoUtils.loadFile((firmaElectronica.getArchivoFirmado()));
                if (docByteArry != null) {
                    System.out.println("docByteArry != null");
                    try {
                        local.setArchivoFirmado(Base64.getEncoder().encodeToString(docByteArry));
                        System.out.println("local: " + local.getArchivoFirmado().length());
                        HttpEntity<Object> request = new HttpEntity<>(local, headers);
                        ResponseEntity<FirmaDocumento> response = restTemplate.exchange(origamiGT + Constantes.apiArchivos + "actualizarDocumento", HttpMethod.POST, request, FirmaDocumento.class);
                        if (response.hasBody()) {
                            FirmaDocumento dl = response.getBody();
                            if (!local.getDocumento().equals(dl.getDocumento())) {
                                local.setEstado("OK");
                            } else {
                                local.setEstado("Intente nuevamente");
                            }
                        } else {
                            local.setEstado("No existe conexión con SUGIR");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    local.setEstado("El documento no pudo ser firmado");
                }
                if (is != null) {
                    IOUtils.closeQuietly(is);
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (target != null) {
                    try {
                        FileDeleteStrategy.FORCE.delete(target);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                return local;
                //return firmaElectronica;
            } catch (Exception e) {
                if (e != null && e.getMessage() != null && e.getMessage().contains("keystore password was incorrect")) {
                    local.setEstado("Clave incorrecta");
                } else {
                    local.setEstado("Intente nuevamente");
                }
                e.printStackTrace();
            }
        } else {
            local.setEstado("No se pudo encontrar su firma electronica");
        }
        return local;

    }

    public FirmaDocumento subirDocumento(FirmaDocumento local) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            if (local.getSubirArchivo() != null && !local.getSubirArchivo().isEmpty()) {
                byte[] docByteArry = DocumentoUtils.loadFile((local.getSubirArchivo()));
                if (docByteArry != null) {

                    local.setArchivoFirmado(Base64.getEncoder().encodeToString(docByteArry));
                    HttpEntity<Object> request = new HttpEntity<>(local, headers);
                    ResponseEntity<FirmaDocumento> response = restTemplate.exchange(origamiGT + Constantes.apiArchivos + "actualizarDocumento", HttpMethod.POST, request, FirmaDocumento.class);
                    if (response.hasBody()) {
                        FirmaDocumento dl = response.getBody();
                        if (!local.getDocumento().equals(dl.getDocumento())) {
                            local.setEstado("OK");
                        } else {
                            local.setEstado("Intente nuevamente");
                        }
                    } else {
                        local.setEstado("No existe conexión con SUGIR");
                    }
                } else {
                    local.setEstado("El documento no pudo ser firmado");
                }
            } else {
                local.setEstado("Debe escoger el archivo a subir");
            }

            return local;
            //return firmaElectronica;
        } catch (Exception e) {
            if (e != null && e.getMessage() != null && e.getMessage().contains("keystore password was incorrect")) {
                local.setEstado("Clave incorrecta");
            } else {
                local.setEstado("Intente nuevamente");
            }
            e.printStackTrace();
        }

        return local;

    }

    @Async
    public void guardarDocumentoFirmado(FirmaElectronica documentoFirmado, String rutaDocFirmado) {
        try {
            Path path = Paths.get(rutaDocFirmado);
            byte[] arr = Files.readAllBytes(path);
            documentoFirmado.setArchivoDesk(arr);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> request = new HttpEntity<>(documentoFirmado, headers);
            restTemplate.exchange(origamiGT + Constantes.apiDocumentos, HttpMethod.POST, request, FirmaDocumento.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DocumentosScan guardarDocumentoScan(DocumentosScan documentosScan) {
        try {

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> request = new HttpEntity<>(documentosScan, headers);
            ResponseEntity<DocumentosScan> response = restTemplate.exchange(origamiGT + Constantes.apiDocumentosScan, HttpMethod.POST, request, DocumentosScan.class);
            if (response != null && response.getBody() != null) {
                //String command = "\"C:\\Origami\\Visor.exe\" " + response.getBody().getId() + ";" + paramVisor;
                String command = "\"C:\\origami\\Visor.exe\" " + response.getBody().getId() + ";" + paramVisor;
                System.out.println("command: " + command);
                Process p = Runtime.getRuntime().exec(command);
                p.waitFor();

                /*String command = "C:/Origami/Visor.exe " + response.getBody().getId() + ";" + paramVisor;
                System.out.println("command: " + command);
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
                Process p = pb.start();
                p.waitFor();*/

                /*ProcessBuilder processBuilder = new ProcessBuilder();
                Path workingDir = Paths.get("C:\\Origami\\");
                processBuilder.directory(workingDir.toFile()); // Edited here
                processBuilder.command("Visor.exe "+ response.getBody().getId() + ";" + paramVisor);
                try {
                    processBuilder.start();
                   // processBuilder.waitFor();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }*/
                
                /*ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd  C:/Origami/ && Visor.exe "+ response.getBody().getId() + ";" + paramVisor);
                builder.redirectErrorStream(true);
                Process p = builder.start();
                BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while (true) {
                    line = r.readLine();
                    if (line == null) { break; }
                    System.out.println(line);
                }*/
                
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public int cambiarHora(Date fecha) {
        System.out.println("fecha con la que entra? " + fecha);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        String newDate = calendar.get(Calendar.YEAR) + "-0" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":00";

        String cmd = "date \"+%d-%m-%C%y %H:%M:%S\" --set \"" + newDate.trim() + "\"";
        System.out.println("cmd: " + cmd);
        String s;
        Process p;
        try {
            if (isLinux()) {
                System.out.println("fecha " + newDate.trim());
                p = Runtime.getRuntime().exec(new String[]{"date", "--set", newDate.trim()});
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                while ((s = br.readLine()) != null)
                    System.out.println("line: " + s);
                p.waitFor();
                System.out.println("exit: " + p.exitValue());
                p.destroy();
                return p.exitValue();
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static boolean isLinux() {
        String osName = System.getProperty("os.name");
        return (osName.toUpperCase().indexOf("LINUX") == 0);
    }


    public static byte[] firmar(KeyStore keyStore, String alias, File documento, char[] clave, Properties params) throws Exception {
        byte[] docByteArry = FileUtils.fileConvertToByteArray(documento);
        Signer signer = Utils.documentSigner(documento);
        PrivateKey key = null;
        try {
            key = (PrivateKey) keyStore.getKey(alias, clave);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Certificate[] certChain = keyStore.getCertificateChain(alias);
        if (key != null) {
            try {
                return signer.sign(docByteArry, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, key, certChain, params);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public void abrirDocumento(String documento) {
        try {
            FileUtils.abrirDocumento(documento);
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String dateFormatPattern(String pattern, Date fechaFin) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(fechaFin);
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
}
