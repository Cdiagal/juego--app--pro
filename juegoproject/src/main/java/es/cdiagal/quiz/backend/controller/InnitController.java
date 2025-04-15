package es.cdiagal.quiz.backend.controller;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.initApp.MainApplication;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InnitController {
    @FXML protected AnchorPane innitAnchorPane;
    @FXML protected JFXButton loginButton;
    @FXML protected JFXButton registerButton;
    
 




    @FXML
    public void onClicOpenLoginScene (){
        try {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 451,600);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
        } catch (Exception e) {
            System.out.println("Error al cargar la página.");
            e.printStackTrace();
        }
    }

    
    @FXML
    public void onClicOpenRegisterScene(){
        try {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/register.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 451,600);
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
            } catch (Exception e) {
                System.out.println("Error al cargar la página.");
                e.printStackTrace();
            }
    }
}
