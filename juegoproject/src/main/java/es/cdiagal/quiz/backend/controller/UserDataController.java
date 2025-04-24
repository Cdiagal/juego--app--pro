package es.cdiagal.quiz.backend.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class UserDataController extends AbstractController {
    @FXML JFXButton backButton;
    @FXML JFXButton playButton;
    @FXML Label userWellcomeLabel;
    @FXML ImageView profileImageView;
    @FXML JFXButton changeImageButton;
    @FXML Label nicknameLabel;
    @FXML Label emailUserDataLabel;
    @FXML Label levelUserDataLabel;
    @FXML Label rateUserDataLabel;
    @FXML Label pxUserDataLabel;
    @FXML JFXButton logoutButton;
    @FXML JFXButton updateButton;

    private UsuarioModel usuario;

    public UserDataController() {
        super();
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
        usuarioData();
    }



    /**
     * Muestra los datos del usuario en la interfaz.
     */
    public void usuarioData() {
        if (usuario != null) {
            userWellcomeLabel.setText("¡Hola, " + usuario.getNickname() + "!");
            nicknameLabel.setText("Alias: " + usuario.getNickname());
            emailUserDataLabel.setText("Email: " + usuario.getEmail());
            levelUserDataLabel.setText("Nivel: " + usuario.getNivel());
            rateUserDataLabel.setText("Racha: " + usuario.getRacha());
            pxUserDataLabel.setText("Puntos: " + usuario.getPuntos());
            cargarImagenUsuario();

            // Mostrar imagen de perfil si existe
            UsuarioDAO dao = new UsuarioDAO(getRutaArchivoBD());
            byte[] imagenBytes = dao.obtenerImagen(usuario.getId());
            if (imagenBytes != null) {
                InputStream is = new ByteArrayInputStream(imagenBytes);
                profileImageView.setImage(new Image(is));
            }
        }
    }

    /**
     * Metodo que pasa a la ventana update para cambiar la imagen.
     */
    @FXML
    public void onClicChangeImage() {
        try {
            Stage stage = (Stage) changeImageButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/updateUserData.fxml"));
            Scene scene = new Scene(loader.load());
    
            UpdateUserDataController controller = loader.getController();
            controller.setUsuario(usuario);
    
            stage.setTitle("Actualizar perfil");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Metodo que inicia una partida.
     */
    @FXML
    public void onClickPlay() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/gameView.fxml"));
            Scene Scene = new Scene(fxmlLoader.load());
            QuestionGameController questionGameController = fxmlLoader.getController();
            questionGameController.setUsuario(usuario);
            questionGameController.iniciarJuego();

            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setTitle("Juego");
            stage.setScene(Scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al iniciar el juego.");
        }
    }

    /**
     * Metodo que cierra la sesión del usuario.
     */
    @FXML
    public void onClicLogout() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/innit.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 600);
            stage.setTitle("BrainQuiz");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Metodo que abre la ventana de actualizar datos.
     */
    @FXML
    public void onClickOpenUpdate() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/updateUserData.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Actualizar datos");
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
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 600);
            stage.setTitle("Iniciar sesión");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga una imagen generica para el perfil.
     */

    private void cargarImagenUsuario() {
        try {
            if (usuario.getImagen() != null) {
                // Si tiene imagen, cargar desde byte[]
                ByteArrayInputStream bis = new ByteArrayInputStream(usuario.getImagen());
                Image imagen = new Image(bis);
                profileImageView.setImage(imagen);
            } else {
                // Si no tiene imagen, cargar por defecto desde resources
                InputStream is = getClass().getResourceAsStream("/images/profile.png");
                if (is == null) {
                    System.out.println("No se encontró la imagen por defecto.");
                    return;
                }
                Image imagen = new Image(is);
                profileImageView.setImage(imagen);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metodo que inicializa el cambio de idioma en el ComboBox.
     */
    @FXML
    public void initialize(){
        if(getPropertiesLanguage()==null){
            setPropertiesLanguage(loadLanguage("language", getIdiomaActual()));
        }
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

        pxUserDataLabel.setText(getPropertiesLanguage().getProperty("pxUserDataLabel"));
        rateUserDataLabel.setText(getPropertiesLanguage().getProperty("rateUserDataLabel"));
        emailUserDataLabel.setText(getPropertiesLanguage().getProperty("emailUserDataLabel"));
        nicknameLabel.setText(getPropertiesLanguage().getProperty("nicknameLabel"));
        playButton.setText(getPropertiesLanguage().getProperty("playButton"));
        logoutButton.setText(getPropertiesLanguage().getProperty("logoutButton"));
        }
    }
}
