package es.cdiagal.quiz.backend.controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashController {

    @FXML
    protected StackPane splashStackPane;

    @FXML
    protected ImageView logoImage;

    @FXML
    public void initialize() {
        FadeTransition fade = new FadeTransition(Duration.seconds(2), logoImage);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(1)); // espera antes de comenzar
        fade.setOnFinished(event -> abrirVentanaPrincipal());
        fade.play();
    }
    
    @FXML
    private void abrirVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/innit.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("BrainQuiz");
            stage.setScene(new Scene(root));
            stage.show();

            // Cerrar splash
            Stage actual = (Stage) logoImage.getScene().getWindow();
            actual.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

