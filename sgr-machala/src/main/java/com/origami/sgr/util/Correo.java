/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.util;

import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Anyelo
 */
public class Correo implements Serializable {

    public void enviarCorreo() {
        String correoEnvia = "email1@gmail.com";
        String claveCorreo = "claveEmail1";

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.user", correoEnvia);

        try {
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correoEnvia, "EMPRESA PUBLICA MUNICIPAL DEL REGISTRO DE LA PROPIEDAD DEL CANTON PORTOVIEJO EP"));
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse("correo1@hotmail.com"));
            mimeMessage.setSubject("HA RECIBIDO UNA FACTURA ELECTRONICA");
            //PARTE DEL MESAJE
            MimeBodyPart text = new MimeBodyPart();
            text.setText("ESTIMADO CLIENTE:\n"
                    + "ENCUENTRE LOS ARCHIVOS DE SU FACTURA ELECTRONICA EN LOS ADJUNTOS O ACCEDIENDO A NUESTRA PAGINA WEB: \n"
                    + "http://www.registropropiedadportoviejo.gob.ec\n"
                    + "USUARIO: <SU NUMERO DE CEDULA/RUC/PASAPORTE>\n"
                    + "PASSWORD: 123 (O LA QUE HAYA PERSONALIZADO EN SESIONES ANTERIORES)");
            //ADJUNTO XML DEL MENSAJE
            MimeBodyPart xml = new MimeBodyPart();
            xml.attachFile("C:/servers_files/caja5/2401201701136008641000120010020000031351514419217.xml");
            //ADJUNTO PDF DEL MENSAJE
            MimeBodyPart pdf = new MimeBodyPart();
            pdf.attachFile("C:/servers_files/caja5/2401201701136008641000120010020000031351514419217.pdf");
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(text);
            multipart.addBodyPart(xml);
            multipart.addBodyPart(pdf);
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            //ENVIAR MENSAJE
            Transport transport = session.getTransport("smtp");
            transport.connect(correoEnvia, claveCorreo);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            System.out.println("//Correo no enviado");
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, e);
        }
        System.out.println("//Correo enviado");
    }

    /*public static void main(String[] args) {
     Correo correoTexto = new Correo();
     correoTexto.enviarCorreo();
     }*/
}
