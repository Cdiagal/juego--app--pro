package es.cdiagal.quiz.backend.controller;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.HashUtils;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador encargado de la recuperación de contraseña.
 * Permite al usuario introducir su correo electrónico para recibir una clave temporal por email.
 */
public class RecoverPasswordController extends AbstractController {
    private UsuarioModel usuario;
    private final UsuarioDAO usuarioDAO;

    @FXML private TextField recoverEmailTextField;
    @FXML private JFXButton recoverPasswordButton;
    @FXML private JFXButton backButton;
    @FXML private Label recoverLabelAdvise;
    @FXML private Label recoverBigLabel;
    /**
     * Constructor que inyecta el DAO con la ruta de la base de datos.
     */
    public RecoverPasswordController() {
        super();
        this.usuarioDAO = new UsuarioDAO(getRutaArchivoBD());
    }


    /**
     * Funcion que lleva hacia la pantalla anterior desde la que se accede a Settings.
     */
    @FXML
    protected void onClickRecoverPassword() {
        if(sendEmail()){
            try {
                Stage stage = (Stage) recoverPasswordButton.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/innit.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                stage.setTitle("BrainQuiz");
                stage.setScene(scene);
                stage.sizeToScene();
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Acción disparada cuando el usuario pulsa el botón "Enviar".
     * Verifica si el email existe, genera una clave aleatoria y simula su envío.
     */
    @FXML
    private boolean sendEmail() {
        String email = recoverEmailTextField.getText().trim();
        // Campos vacíos
        if (email.isEmpty()) {
            mostrarAlerta("Campo vacío", "Por favor, introduce tu correo electrónico.");
            return false;
        }

        // Email correcto
        if (!emailCorrecto(email)){
            mostrarAlerta("Email erróneo", "El email introducido no tiene formato válido");
            return false;
        }

        UsuarioModel usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            mostrarAlerta("Email no encontrado", "No existe ningún usuario registrado con ese correo.");
            return false;
        }

        // Generar contrasenia temporal aleatoria
        String nuevaClave = generarClaveTemporal();

        // Se hashea la contrasenia
        String hashedPassword = HashUtils.hashPassword(nuevaClave);

        // Se actualiza la contrasenia y se guarda en la BBDD.
        boolean actualizada = usuarioDAO.actualizarPasswordPorEmail(email, hashedPassword);

        if (actualizada) {
            mostrarAlerta("Clave enviada", "Se ha enviado una nueva clave temporal al correo indicado.");
            System.out.println("CLAVE TEMPORAL PARA " + email + ": " + nuevaClave);
            return true;
        } else {
            mostrarAlerta("Error", "No se pudo actualizar la contraseña. Intenta de nuevo.");
        }
        return false;
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
     * Funcion que lleva hacia la pantalla anterior desde la que se accede a Settings.
     */
    @FXML
    protected void onClickBackButton() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/updateUserData.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("BrainQuiz");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            recoverBigLabel.setText(getPropertiesLanguage().getProperty("recoverBigLabel"));
            recoverPasswordButton.setText(getPropertiesLanguage().getProperty("recoverPasswordButton"));
            recoverLabelAdvise.setText(getPropertiesLanguage().getProperty("recoverLabelAdvise_empty"));
            recoverLabelAdvise.setText(getPropertiesLanguage().getProperty("recoverLabelAdvise_match"));
        }
    }

}
