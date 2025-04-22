package es.cdiagal.quiz.backend.model.utils.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {

    private static final String REMITENTE = "tucorreo@gmail.com";
    private static final String CONTRASENA_APP = "tu_contraseña_de_aplicacion";

    public static boolean enviarClaveTemporal(String destinatario, String claveTemporal) {
        // Configuración del servidor SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Autenticación
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CONTRASENA_APP);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Tu nueva clave temporal");
            mensaje.setText("Hola,\n\nTu nueva clave temporal es:\n\n" + claveTemporal + "\n\n¡Inicia sesión y cámbiala pronto!");

            Transport.send(mensaje);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
