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
package ec.gob.service.firmaec.rubrica.sign;

import ec.gob.service.firmaec.model.FirmaElectronica;
import ec.gob.service.firmaec.rubrica.sign.pdf.PDFSigner;
import ec.gob.service.firmaec.rubrica.sign.pdf.PdfUtil;
import ec.gob.service.firmaec.rubrica.utils.X509CertificateUtils;
import ec.gob.service.firmaec.rubrica.validaciones.DocumentoUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import ec.gob.service.firmaec.rubrica.certificate.CertEcUtils;
import static ec.gob.service.firmaec.rubrica.certificate.CertUtils.seleccionarAlias;
import ec.gob.service.firmaec.rubrica.certificate.to.Documento;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import ec.gob.service.firmaec.rubrica.keystore.FileKeyStoreProvider;
import ec.gob.service.firmaec.rubrica.keystore.KeyStoreProvider;
import ec.gob.service.firmaec.rubrica.utils.FileUtils;
import ec.gob.service.firmaec.rubrica.utils.TiempoUtils;
import ec.gob.service.firmaec.rubrica.utils.Utils;
import ec.gob.service.firmaec.rubrica.utils.UtilsCrlOcsp;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * Metodo de pruebas funcionales
 *
 * @author mfernandez
 */
@Service
public class FirmaElectronicaService {

    private static Properties parametros(FirmaElectronica firmaElectronica) throws IOException {
        //QR
        //SUPERIOR IZQUIERDA
        //String llx = "10";
        //String lly = "830";
        //INFERIOR IZQUIERDA
        //String llx = "100";
        //String lly = "91";
        //INFERIOR DERECHA
        //String llx = "419";
        //String lly = "90";
        //String llx = "419";
        //String lly = "50";
        //INFERIOR CENTRADO
        //String llx = "260";
        //String lly = "91";
        //QR
        //SUPERIOR IZQUIERDA
        //String llx = "10";
        //String lly = "830";
        //String urx = String.valueOf(Integer.parseInt(llx) + 110);
        //String ury = String.valueOf(Integer.parseInt(lly) - 36);
        //INFERIOR CENTRADO
        //String llx = "190";
        //String lly = "85";
        //String urx = String.valueOf(Integer.parseInt(llx) + 260);
        //String ury = String.valueOf(Integer.parseInt(lly) - 36);
        //INFERIOR CENTRADO (ancho pie pagina)
        //String llx = "100";
        //String lly = "80";&
        //String urx = String.valueOf(Integer.parseInt(llx) + 430);
        //String ury = String.valueOf(Integer.parseInt(lly) - 25);
        //INFERIOR DERECHA
        String llx = "10"; //MOVIMIENTO DE IZQUIERDA A DERECHA Y VICEVERSA
        String lly = "150"; //MOVIMIENTO DE ARRIBA - ABAJO Y VICCEVERSA
        String fdllx = "10";
        String fdlly = "100";
        String urx = firmaElectronica.getPositionURX() == null ? String.valueOf(Integer.parseInt(llx) + 100) : firmaElectronica.getPositionURX();
        String ury = firmaElectronica.getPositionURY() == null ? String.valueOf(Integer.parseInt(lly) - 100) : firmaElectronica.getPositionURY();

        Properties params = new Properties();
        //llx mueves de marge left de izquierda a derecha
        //  String llx =firmaElectronica.getPosicionX1();//!= null ? String.valueOf((-2)*(Integer.parseInt(firmaElectronica.getPosicionX2())-200)) : "10"; //MOVIMIENTO DE IZQUIERDA A DERECHA Y VICEVERSA -Y-
        // String lly =firmaElectronica.getPosicionY1() ;//!= null ? String.valueOf((-2)*(Integer.parseInt(firmaElectronica.getPosicionY2())-800)) : "830"; //MOVIMIENTO DE ARRIBA - ABAJO Y VICCEVERSA    -X-
        //String fdllx = firmaElectronica.getPosicionX1(); //!= null ? firmaElectronica.getPosicionX2() : "10";
        //String fdlly = firmaElectronica.getPosicionY1() ;//!= null ? firmaElectronica.getPosicionY2() : "100";
        //String fdlly= firmaElectronica.getPosicionY1() ;//!= null ? firmaElectronica.getPosicionX2() : "10";
        //agrandar firma
        //  String urx =firmaElectronica.getPosicionX2();//null ? String.valueOf((-2)*Integer.parseInt(firmaElectronica.getPosicionX1())+100): "500";//String.valueOf(Integer.parseInt(llx) + 50);
        //mover hacia arriba cuando se seleccione arriba
        //  String ury = firmaElectronica.getPosicionY2();//String.valueOf((-1)*Integer.parseInt(firmaElectronica.getPosicionY2()));//!= null ? String.valueOf((-2)*Integer.parseInt(firmaElectronica.getPosicionY1())-300): "300";//String.valueOf(Integer.parseInt(lly) - 36);
        params.setProperty(PDFSigner.SIGNING_LOCATION, firmaElectronica.getUbicacion());
        params.setProperty(PDFSigner.SIGNING_REASON, firmaElectronica.getMotivo());
        params.setProperty(PDFSigner.SIGN_TIME, TiempoUtils.getFechaHoraServidor());
        params.setProperty(PDFSigner.LAST_PAGE, firmaElectronica.getNumeroPagina().toString());
        params.setProperty(PDFSigner.TYPE_SIG, firmaElectronica.getTipoFirma());
        params.setProperty(PDFSigner.INFO_QR, firmaElectronica.getUrlQr());
        params.setProperty(PDFSigner.FONT_SIZE, "4.5");
        // Posicion firma
        if (firmaElectronica.getTipoFirma().equalsIgnoreCase("QR")) {
            urx = String.valueOf((Integer.parseInt(firmaElectronica.getPosicionX1()) + 150));
            ury = String.valueOf((Integer.parseInt(firmaElectronica.getPosicionY1()) - 90));
            params.setProperty(PDFSigner.FONT_SIZE, "3.0");
        }
        System.out.println("urx " + urx + " ury " + ury);
        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_X, firmaElectronica.getPosicionX1());
        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_Y, firmaElectronica.getPosicionY1());
        params.setProperty(PdfUtil.POSITION_ON_PAGE_UPPER_RIGHT_X, urx);
        params.setProperty(PdfUtil.POSITION_ON_PAGE_UPPER_RIGHT_Y, ury);
//        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_X, llx);
//        params.setProperty(PdfUtil.POSITION_ON_PAGE_LOWER_LEFT_Y, lly);
//        params.setProperty(PdfUtil.POSITION_ON_PAGE_UPPER_RIGHT_X, urx);
//        params.setProperty(PdfUtil.POSITION_ON_PAGE_UPPER_RIGHT_Y, ury);
//
////        params.setProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_LEFT_X, fdllx);
////        params.setProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_RIGHT_Y, fdlly);
//        params.setProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_LEFT_X, firmaElectronica.getPosicionX1());
//        params.setProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_RIGHT_Y, firmaElectronica.getPosicionY1());
        return params;
    }

    public FirmaElectronica validarCertificado(FirmaElectronica firmaElectronica) throws IOException, KeyStoreException, Exception {
        // ARCHIVO
        KeyStoreProvider ksp = new FileKeyStoreProvider(firmaElectronica.getArchivo());
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
            firmaElectronica.setEstadoFirma("Certificado emitido por entidad certificadora acreditada? " + Utils.verifySignature(x509Certificate));
        }
        return firmaElectronica;
    }

    public FirmaElectronica firmarDocumento(FirmaElectronica firmaElectronica) throws KeyStoreException, Exception {

        ////// LEER PDF:
        if (firmaElectronica.getNumeroPagina() == null) {
            PDDocument doc = PDDocument.load(new File(firmaElectronica.getArchivoFirmar()));
            firmaElectronica.setNumeroPagina(doc.getNumberOfPages());
        }
        Properties parametros = parametros(firmaElectronica);
//        if (firmaElectronica.getFirmaDigital() != null && !firmaElectronica.getFirmaDigital().isEmpty()) {
//            firmaDigital(firmaElectronica, parametros);
//        }
        byte[] docByteArry = DocumentoUtils.loadFile((firmaElectronica.getArchivoFirmar()));
        // ARCHIVO
        KeyStoreProvider ksp = new FileKeyStoreProvider(firmaElectronica.getArchivo());
        KeyStore keyStore = ksp.getKeystore(firmaElectronica.getClave().toCharArray());
        // TOKEN
        //KeyStore keyStore = KeyStoreProviderFactory.getKeyStore(PASSWORD);

        byte[] signed = null;
        Signer signer = Utils.documentSigner(new File((firmaElectronica.getArchivoFirmar())));
        String alias = seleccionarAlias(keyStore);
        PrivateKey key = (PrivateKey) keyStore.getKey(alias, firmaElectronica.getClave().toCharArray());

        X509CertificateUtils x509CertificateUtils = new X509CertificateUtils();
        if (x509CertificateUtils.validarX509Certificate((X509Certificate) keyStore.getCertificate(alias))) {//validación de firmaEC
            try {
                Certificate[] certChain = keyStore.getCertificateChain(alias);

                signed = signer.sign(docByteArry, SignConstants.SIGN_ALGORITHM_SHA1WITHRSA, key, certChain, parametros);
                System.out.println("final firma\n-------");
                ////// Permite guardar el archivo en el equipo y luego lo abre
                String nombreDocumento = FileUtils.crearNombreFirmado(new File((firmaElectronica.getArchivoFirmar())), FileUtils.getExtension(signed));
                java.io.FileOutputStream fos = new java.io.FileOutputStream(nombreDocumento);
                //Abrir documento
                /*  new java.util.Timer().schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        try {
                            //FileUtils.abrirDocumento(nombreDocumento);
                            System.out.println(nombreDocumento);
                            verificarDocumento(nombreDocumento);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            //System.exit(0);
                        }
                    }
                }, 3000); //espera 3 segundos*/
                fos.write(signed);
                fos.close();
                //Abrir documento
                File file = new File(nombreDocumento);
                System.out.println("nombreDocumento: " + nombreDocumento);
                firmaElectronica.setArchivoFirmado(nombreDocumento);
//                firmaElectronica.setUrlArchivoFirmado(urlMedia + "ar_" + file.getName() + "/descarga/inline");
                return firmaElectronica;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public FirmaElectronica firmarDocumentoDigital(FirmaElectronica firmaElectronica) {
        try {
            String urx = String.valueOf((Integer.parseInt(firmaElectronica.getPosicionX1()) + 150));
            String ury = String.valueOf((Integer.parseInt(firmaElectronica.getPosicionY1()) - 90));
            Properties params = new Properties();
//            params.setProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_LEFT_X, firmaElectronica.getPosicionX1());
//            params.setProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_RIGHT_Y, firmaElectronica.getPosicionY2());
            return firmaDigital(firmaElectronica, params);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public Documento verificarDocumento(FirmaElectronica firmaElectronica) throws Exception {
        File document = new File(firmaElectronica.getArchivoFirmado());
        Documento documento = Utils.verificarDocumento(document);
        document.delete();
        return documento;
    }

    //pruebas de fecha-hora
    private static void fechaHora(int segundos) throws KeyStoreException, Exception {
        tiempo(segundos);//espera en segundos
        do {
            try {
                System.out.println("getFechaHora() " + TiempoUtils.getFechaHora());
                System.out.println("getFechaHoraServidor() " + TiempoUtils.getFechaHoraServidor());
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } while (tiempo);
        System.exit(0);
    }

    private static boolean tiempo = true;

    private static void tiempo(int segundos) {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                tiempo = false;
            }
        }, segundos * 1000); //espera 3 segundos
    }
    //pruebas de fecha-hora

    public FirmaElectronica firmaDigital(FirmaElectronica firmaElectronica, Properties parametros) {
        try {
//            File archFirmar = new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar()));
//            String nombreDocumento = FileUtils.crearNombreFirmado(new File(replaceRutaArchivo(firmaElectronica.getArchivoFirmar())), ".pdf");
//
//            String outputFilePath = nombreDocumento;
//            OutputStream fos = new FileOutputStream(new File(outputFilePath));
//
//            PdfReader pdfReader = new PdfReader(replaceRutaArchivo(firmaElectronica.getArchivoFirmar()));
//            PdfStamper pdfStamper = new PdfStamper(pdfReader, fos);
//
//            Image image = Image.getInstance(firmaElectronica.getFirmaDigital());
//            image.scaleAbsolute(150, 50); //Scale image's width and height
//            image.setAbsolutePosition(Integer.parseInt(parametros.getProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_LEFT_X).trim()), Integer.parseInt(parametros.getProperty(PdfUtil.POSITION_ON_PAGE_FD_LOWER_RIGHT_Y).trim())); //Set position for image in PDF
//
//            PdfContentByte pdfContentByte = pdfStamper.getOverContent(firmaElectronica.getNumeroPagina());
//            pdfContentByte.addImage(image);
//            System.out.println("Image added in " + outputFilePath);
//            pdfStamper.close();
//            firmaElectronica.setArchivoFirmado(nombreDocumento);
//            firmaElectronica.setUrlArchivoFirmado(urlMedia + "ar_" + new File(nombreDocumento).getName() + "/descarga/inline");
            return firmaElectronica;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public String replaceRutaArchivo(String ruta) {
//
//        if (ruta.startsWith("ar_")) {
//            ruta = ruta.replace("ar_", rutaArchivos);
//        }
//
//        return ruta;
//    }
}
