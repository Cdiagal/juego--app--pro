package es.cdiagal.quiz.backend.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.HashUtils;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class UpdateUserDataController extends AbstractController {

    private final UsuarioDAO usuarioDAO;
    private UsuarioModel usuario;
    private byte[] imagenSeleccionada;

    @FXML protected Label registerEmailLabel;
    @FXML protected Label registerPasswordLabel;
    @FXML protected Label registerNicknameLabel;
    @FXML private Label passwordRegisterAdviseLabel;
    @FXML private Label nicknameRegisterAdviseLabel;
    @FXML private Label emailRegisterAdviseLabel;
    @FXML private TextField registerNicknameTextField;
    @FXML private TextField registerEmailTextField;
    @FXML private TextField registerRepeatEmailTextField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField registerRepeatPasswordField;
    @FXML private ImageView updateImageView;
    @FXML private Label updateUserTextAdvise;
    @FXML private JFXButton deleteUserButton;
    @FXML private JFXButton backButton;
    @FXML private JFXButton updateButton;
    @FXML private JFXButton changeImageButton;

    public UpdateUserDataController() {
        super();
        this.usuarioDAO = new UsuarioDAO(getRutaArchivoBD());
    }

    

    public void setUsuario(UsuarioModel usuario) {
        if (usuario == null) {
            mostrarAlerta("Error", "Usuario no disponible.");
            return;
        }
        this.usuario = usuario; 
        registerNicknameTextField.setText(usuario.getNickname());
        registerEmailTextField.setText(usuario.getEmail());
        cargarImagenUsuario();
    }

    private void cargarImagenUsuario() {
        byte[] bytes = usuario.getImagen();
        if (bytes != null) {
            updateImageView.setImage(new Image(new ByteArrayInputStream(bytes)));
        } else {
            updateImageView.setImage(new Image(getClass().getResourceAsStream("/images/profile.png")));
        }
        aplicarClipCircular(updateImageView);
    }

    private void aplicarClipCircular(ImageView imageView) {
        if (imageView != null) {
            double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
            Circle clip = new Circle(radius, radius, radius);
            imageView.setClip(clip);
        } else {
            System.out.println("Error: ImageView es null");
        }
    }

    @FXML
    public void onClicChangeImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = fileChooser.showOpenDialog(new Stage());
        if (archivo != null) {
            try {
                imagenSeleccionada = Files.readAllBytes(archivo.toPath());
                Image imagen = new Image(new FileInputStream(archivo));
                updateImageView.setImage(imagen);
                aplicarClipCircular(updateImageView);
                usuario.setImagen(imagenSeleccionada);
                boolean imagenActualizada = usuarioDAO.actualizarImagen(usuario.getId(), imagenSeleccionada);
                if (imagenActualizada) {
                    mostrarAlerta("Imagen cargada", "Imagen seleccionada correctamente. Recuerda guardar para aplicarla.");
                } else {
                    mostrarAlerta("Error", "No se pudo actualizar la imagen en la base de datos.");
                }
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo leer la imagen seleccionada.");
            }
        }
    }

    @FXML
    private void onClickUpdateUser() {
        if (!validarDatosUpdate()) {
            return;
        }

        boolean actualizado = usuarioDAO.actualizarUsuario(usuario);

        if (actualizado) {
            mostrarAlerta("Datos actualizados", "Tus datos se han guardado correctamente.");
        } else {
            mostrarAlerta("Error", "No se pudo actualizar tu información.");
        }
        
        try {
            Stage stage = (Stage) updateButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/userData.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            UserDataController userDataController = fxmlLoader.getController();
            userDataController.setUsuario(usuario);
            userDataController.usuarioData();
            
            stage.setTitle("Iniciar sesión");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Funcion que lleva hacia la pantalla de Eliminar usuario.
     */
    @FXML
    protected void onClickDeleteUser() {
        try {
            Stage stage = (Stage) deleteUserButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/deleteUser.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 600);
            stage.setTitle("Eliminar usuario");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion que lleva hacia la pantalla anterior desde la que se accede a Settings.
     */
    @FXML
    protected void onClickBackButton() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/userData.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Iniciar sesión");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private boolean validarDatosUpdate(){
        if(registerNicknameTextField.getText().isEmpty()){
            nicknameRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("nicknameRegisterAdviseLabel"));
            return false;
        }

        if(registerPasswordField == null || registerRepeatPasswordField == null
            || registerRepeatPasswordField.getText().isEmpty() || registerPasswordField.getText().isEmpty()){
                passwordRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("passwordRegisterAdviseLabel_null"));
                return false;
        }

        if(!registerPasswordField.getText().equals(registerRepeatPasswordField.getText())){
            passwordRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("passwordRegisterAdviseLabel_match"));
            return false;
        } else {
            passwordRegisterAdviseLabel.setText("");
        }
        
        if(registerEmailTextField.getText() == null || registerRepeatEmailTextField.getText() == null
            || registerEmailTextField.getText().isEmpty() || registerRepeatEmailTextField.getText().isEmpty()){
                emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_empty"));
                return false;
        }

        if(!registerEmailTextField.getText().equals(registerRepeatEmailTextField.getText())){
            emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_match"));
            return false;
        } else {
            emailRegisterAdviseLabel.setText("");
        }
        
        if(!emailCorrecto(registerEmailTextField.getText()) || !emailCorrecto(registerRepeatEmailTextField.getText())){
            emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_valid"));
            return false;
        } else {
            emailRegisterAdviseLabel.setText("");
        }

        if (!registerPasswordField.getText().isEmpty()) {
            if (!registerPasswordField.getText().equals(registerRepeatPasswordField.getText())) {
                mostrarAlerta("Error", "Las contraseñas no coinciden.");
                return false;
            }
            // Hashear la nueva contraseña
            String hashedPassword = HashUtils.hashPassword(registerPasswordField.getText());
            usuario.setPassword(hashedPassword);  // Asignar la contraseña hasheada
        }

        usuario.setNickname(registerNicknameTextField.getText());
        usuario.setEmail(registerEmailTextField.getText());

        // Si hay una imagen seleccionada, asignarla al usuario
        if (imagenSeleccionada != null) {
            usuario.setImagen(imagenSeleccionada);
        }
        return true;
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
    public void initialize() {
        if (updateImageView != null) {
            aplicarClipCircular(updateImageView);
        } else {
            System.out.println("Error: El ImageView no se ha inicializado.");
        }
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

            registerNicknameLabel.setText(getPropertiesLanguage().getProperty("registerNicknameLabel"));
            registerNicknameTextField.setPromptText(getPropertiesLanguage().getProperty("registerNicknameLabelPrompText"));
            nicknameRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("nicknameRegisterAdviseLabel"));
            registerEmailLabel.setText(getPropertiesLanguage().getProperty("registerEmailLabel"));
            registerEmailTextField.setPromptText(getPropertiesLanguage().getProperty("registerEmailTextFieldPrompText"));
            registerRepeatEmailTextField.setPromptText(getPropertiesLanguage().getProperty("registerRepeatEmailTextFieldPrompText"));
            emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_null"));
            emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_empty"));
            emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_match"));
            emailRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("emailRegisterAdviseLabel_valid"));
            registerPasswordLabel.setText(getPropertiesLanguage().getProperty("registerPasswordLabel"));
            registerPasswordField.setPromptText(getPropertiesLanguage().getProperty("registerPasswordFieldPrompText"));
            registerRepeatPasswordField.setPromptText(getPropertiesLanguage().getProperty("registerRepeatPasswordFieldPrompText"));
            passwordRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("passwordRegisterAdviseLabel_null"));
            passwordRegisterAdviseLabel.setText(getPropertiesLanguage().getProperty("passwordRegisterAdviseLabel_match"));
        
        }
    }

}
