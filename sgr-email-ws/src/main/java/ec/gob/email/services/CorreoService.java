package ec.gob.email.services;

import ec.gob.email.config.ApplicationProperties;
import ec.gob.email.model.CorreoDTO;
import ec.gob.email.repository.CorreoArchivoRepository;
import ec.gob.email.repository.CorreoRepository;
import ec.gob.email.entity.Correo;
import ec.gob.email.entity.CorreoArchivo;
import ec.gob.email.utils.EmailUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;

@Service
public class CorreoService {

    private String contentType = CTYPE_TEXT;

    private static final String CTYPE_TEXT = "text/html; charset=utf-8";
    @Autowired
    private ApplicationProperties applicationProperties;
    @Autowired
    private CorreoRepository repository;
    @Autowired
    private CorreoArchivoRepository correoArchivoRepository;

    public Map<String, List> findAll(Correo data, Pageable pageable) {
        Map<String, List> map = new HashMap<>();
        Page<Correo> result;
        ExampleMatcher customExampleMatcher = ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                .withIgnoreCase();
        result = repository.findAll(Example.of(data, customExampleMatcher), pageable);
        List<String> pages = new ArrayList<>();
        pages.add(String.valueOf(result.getTotalPages()));
        pages.add(String.valueOf(repository.count(Example.of(data, customExampleMatcher))));
        map.put("result", result.getContent());
        map.put("pages", pages);
        return map;
    }

    @Async
    public void reenviarCorreo(Correo correo) {
        List<File> files = new ArrayList<>();
        File file;
        List<CorreoArchivo> listCorreo = correoArchivoRepository.findByCorreo_Id(correo.getId());
        if (listCorreo != null && !listCorreo.isEmpty()) {
            for (CorreoArchivo correoArchivo : listCorreo) {
                file = new File(EmailUtils.replaceRutaArchivo(correoArchivo.getRutaArchivo(), applicationProperties.getRutaArchivos()));
                if (file.exists()) {
                    files.add(file);
                } else {
                    System.out.println("archivo no encontrado :C verificar el borrado de archivos");
                }
            }
        }
        enviarCorreo(correo, files);
    }

    @Async
    public void saveCorreoAndCorreoArchivo(CorreoDTO correoDTO, Boolean enviado) {
        if (correoDTO != null) {
            Correo correo = new Correo();
            correo.setDestinatario(correoDTO.getDestinatario());
            correo.setMensaje(correoDTO.getMensaje());
            correo.setEnviado(enviado);
            correo.setAsunto(correoDTO.getAsunto());
            correo.setFechaEnvio(new Date());
            correo.setTramite(correoDTO.getNumeroTramite());
            correo.setExcepcion(correoDTO.getError());
            correo.setOrigen(correoDTO.getConfiguracion().getUsuario());
            correo.setUsuario(correoDTO.getUsuario());
            correo = repository.save(correo);
            if (EmailUtils.isNotEmpty(correoDTO.getAdjuntos()) && correo.getId() != null) {
                for (File f : correoDTO.getAdjuntos()) {
                    CorreoArchivo correoArchivo = new CorreoArchivo();
                    correoArchivo.setCorreo(correo);
                    correoArchivo.setNombreArchivo(f.getName());
                    correoArchivo.setRutaArchivo(f.getAbsolutePath());
                    correoArchivo.setTipoArchivo(FilenameUtils.getExtension(f.getAbsolutePath()));
                    correoArchivoRepository.save(correoArchivo);
                }
            }
            //return correo;
        }
    }

    @Async
    public void actualizarReenvio(Correo correo) {
        repository.save(correo);
    }

    @Async
    public void enviarCorreo(CorreoDTO correo) {
        try {

            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", applicationProperties.getCorreoHost());
            if (Boolean.parseBoolean(applicationProperties.getSsl())) {
                props.setProperty("mail.smtp.ssl.enable", "true");
            } else {
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            props.setProperty("mail.smtp.port", applicationProperties.getCorreoPort());
            props.setProperty("mail.smtp.user", applicationProperties.getCorreo());
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(applicationProperties.getCorreo(), applicationProperties.getRazonSocial()));
            mimeMessage.setSubject(correo.getAsunto());
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(correo.getDestinatario()));
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(mensaje);
            texto.setContent(correo.getMensaje(), contentType);
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            if (EmailUtils.isNotEmpty(correo.getAdjuntos())) {
                MimeBodyPart file;
                for (File f : correo.getAdjuntos()) {
                    file = new MimeBodyPart();
                    file.attachFile(f);
                    multipart.addBodyPart(file);
                }
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(applicationProperties.getCorreo(), applicationProperties.getCorreoClave());
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
            saveCorreoAndCorreoArchivo(correo, Boolean.TRUE);
        } catch (MessagingException | IOException ex) {
            System.out.println(ex);
            saveCorreoAndCorreoArchivo(correo, Boolean.FALSE);
        }
    }

    @Async
    public void enviarCorreo(Correo correo, List<File> adjuntos) {
        try {

            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", applicationProperties.getCorreoHost());
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", applicationProperties.getCorreoPort());
            props.setProperty("mail.smtp.user", applicationProperties.getCorreo());
            props.setProperty("mail.smtp.auth", "true");

            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(applicationProperties.getCorreo(), applicationProperties.getRazonSocial()));
            mimeMessage.setSubject(correo.getAsunto());
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(correo.getDestinatario()));
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(mensaje);
            texto.setContent(correo.getMensaje(), contentType);
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            if (EmailUtils.isNotEmpty(adjuntos)) {
                MimeBodyPart file;
                for (File f : adjuntos) {
                    file = new MimeBodyPart();
                    file.attachFile(f);
                    multipart.addBodyPart(file);
                }
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(applicationProperties.getCorreo(), applicationProperties.getCorreoClave());
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
            correo.setEnviado(Boolean.TRUE);
            actualizarReenvio(correo);
        } catch (MessagingException | IOException ex) {
            System.out.println(ex);
            correo.setEnviado(Boolean.FALSE);
            actualizarReenvio(correo);
        }
    }

    @Async
    public void enviarCorreoV2(CorreoDTO correo) {
        try {
            //INGRESO DE LAS PROPIEDADES DE LA CONEXION
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "smtp");
            props.setProperty("mail.smtp.host", correo.getConfiguracion().getServidor());
            if (Boolean.parseBoolean(correo.getConfiguracion().getSsl())) {
                props.setProperty("mail.smtp.ssl.enable", "true");
            } else {
                props.setProperty("mail.smtp.starttls.enable", "true");
            }
            props.setProperty("mail.smtp.port", correo.getConfiguracion().getPuerto().toString());
            props.setProperty("mail.smtp.user", correo.getConfiguracion().getUsuario());
            props.setProperty("mail.smtp.auth", "true");
            props.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");

            //INSTANCIA DE LA SESSION
            Session session = Session.getInstance(props, null);
            //CUERPO DEL MENSAJE
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(correo.getConfiguracion().getUsuario(), correo.getConfiguracion().getRazonSocial()));
            mimeMessage.setSubject(correo.getAsunto());
            mimeMessage.setSentDate(new Date());
            mimeMessage.addRecipients(Message.RecipientType.TO, InternetAddress.parse(correo.getDestinatario()));
            //TEXTO DEL MENSAJE
            MimeBodyPart texto = new MimeBodyPart();
            //texto.setText(mensaje);
            texto.setContent(correo.getMensaje(), contentType);
            //CONTENEDOR DE LAS PARTES
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //ADJUNTAR LOS ARCHIVO EN PARTES
            if (EmailUtils.isNotEmpty(correo.getAdjuntos())) {
                MimeBodyPart file;
                for (File f : correo.getAdjuntos()) {
                    file = new MimeBodyPart();
                    file.attachFile(f);
                    multipart.addBodyPart(file);
                }
            }
            //AGREGAR MULTIPART EN CUERPO DEL MENSAJE
            mimeMessage.setContent(multipart);
            // ENVIAR MENSAJE
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(correo.getConfiguracion().getUsuario(), correo.getConfiguracion().getClave());
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            }
            saveCorreoAndCorreoArchivo(correo, Boolean.TRUE);
        } catch (MessagingException | IOException ex) {
            System.out.println(ex);
            correo.setError(ex.toString());
            saveCorreoAndCorreoArchivo(correo, Boolean.FALSE);
        }
    }

}
