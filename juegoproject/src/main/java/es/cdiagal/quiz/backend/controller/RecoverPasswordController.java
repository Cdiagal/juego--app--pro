package es.cdiagal.quiz.backend.controller;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controlador encargado de la recuperación de contraseña.
 * Permite al usuario introducir su correo electrónico para recibir una clave temporal por email.
 */
public class RecoverPasswordController extends AbstractController {

    private final UsuarioDAO usuarioDAO;

    @FXML
    private TextField recoverEmailTextField;

    /**
     * Constructor que inyecta el DAO con la ruta de la base de datos.
     */
    public RecoverPasswordController() {
        super();
        this.usuarioDAO = new UsuarioDAO(getRutaArchivoBD());
    }

    /**
     * Acción disparada cuando el usuario pulsa el botón "Enviar".
     * Verifica si el email existe, genera una clave aleatoria y simula su envío.
     */
    @FXML
    private void onEnviarClicked() {
        String email = recoverEmailTextField.getText().trim();
        // Campos vacíos
        if (email.isEmpty()) {
            mostrarAlerta("Campo vacío", "Por favor, introduce tu correo electrónico.");
            return;
        }

        // Email correcto
        if (!emailCorrecto(email)){
            mostrarAlerta("Email erróneo", "El email introducido no tiene formato válido");
        }

        UsuarioModel usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            mostrarAlerta("Email no encontrado", "No existe ningún usuario registrado con ese correo.");
            return;
        }

        // Generar clave temporal aleatoria
        String nuevaClave = generarClaveTemporal();

        // Simula el envío por correo con un mensaje como si lo hiciera.
        System.out.println("CLAVE TEMPORAL PARA " + email + ": " + nuevaClave);

        // Se actualiza la contrasenia y se guarda en la BBDD.
        boolean actualizada = usuarioDAO.actualizarPasswordPorEmail(email, nuevaClave);

        if (actualizada) {
            mostrarAlerta("Clave enviada", "Se ha enviado una nueva clave temporal al correo indicado.");
            System.out.println("CLAVE TEMPORAL PARA " + email + ": " + nuevaClave); // simulación del envío
        } else {
            mostrarAlerta("Error", "No se pudo actualizar la contraseña. Intenta de nuevo.");
        }
    }

    /**
     * Genera una clave aleatoria segura de 8 caracteres.
     * @return la clave generada
     */
    private String generarClaveTemporal() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder clave = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            clave.append(caracteres.charAt(index));
        }

        return clave.toString();
    }

    /**
     * Muestra una alerta simple al usuario.
     * @param titulo Título de la alerta
     * @param mensaje Mensaje a mostrar
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    /**
     * Funcion que comprueba que el email introducido cumple con el formato establecido para ser un email.
     * @param email del usuario
     * @return validacion de formato.
     */
    private boolean emailCorrecto(String email){
        String regex = "^[A-Za-z0-9%+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Metodo que inicializa el cambio de idioma en el ComboBox.
     */
    @FXML
    public void initialize(){
        if(getPropertiesLanguage()==null){
            setPropertiesLanguage(loadLanguage("language", getIdiomaActual()));
        }
        recoverEmailTextField.requestFocus();
        changeLanguage();
    }

    
    /**
     * Funcion que cambia el idioma de las etiquetas y objetos de la ventana
     */
    @FXML
    public void changeLanguage() {
        String language = AbstractController.getIdiomaActual();

        if(getPropertiesLanguage() == null){
            setPropertiesLanguage(loadLanguage("language", language));
        }
        if(getPropertiesLanguage() != null){

            recoverEmailTextField.setPromptText(getPropertiesLanguage().getProperty("recoverEmailTextFieldPromptText"));

        }
    }

}
