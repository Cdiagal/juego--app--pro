package es.cdiagal.quiz.backend.controller;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserDataController extends AbstractController{
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
    
    private UsuarioModel usuario;

    public UserDataController() {
        super();
    }
    public void setUsuario(UsuarioModel usuario){
        this.usuario = usuario;
        usuarioData();
    }
    
    public void usuarioData(){
        if(usuario != null){
            userWellcomeLabel.setText("¡Bienvenid@, " + usuario.getNickname() + "!");
            nicknameLabel.setText("Alias: " + usuario.getNickname());
            emailUserDataLabel.setText("Email: " + usuario.getEmail());
            levelUserDataLabel.setText("Nivel: " + usuario.getNivel());
            rateUserDataLabel.setText("Racha: " + usuario.getRacha());
            pxUserDataLabel.setText("Puntos: " + usuario.getPuntos());
        }
    }

    /**
     * Metodo que cambia la imagen de perfil del usuario.
     */
    @FXML
    public void onClicChangeImage(){

    }
    
    /**
     * Metodo que inicia una partida.
     */
    @FXML
    public void onClickPlay(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/game.fxml"));
            Scene Scene = new Scene(fxmlLoader.load(), 600, 500); // ajusta si necesitas
    
            GameController gameController = fxmlLoader.getController();
            gameController.iniciarJuego(usuario); // pasar el usuario al juego
    
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
    public void onClicLogout(){
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
     * Funcion que lleva hacia la pantalla anterior desde la que se accede a Settings.
     */
    @FXML
    protected void onClickBackButton(){
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
}
