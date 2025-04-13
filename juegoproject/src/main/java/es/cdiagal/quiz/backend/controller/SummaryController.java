package es.cdiagal.quiz.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import es.cdiagal.quiz.backend.model.entities.PartidaModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class SummaryController {

    private PartidaModel partida;

    @FXML private Label labelResumen;
    @FXML private Label labelPuntos;
    @FXML private Label labelAciertos;
    @FXML private Label labelErrores;
    @FXML private Label labelDificultad;
    @FXML private Label labelDuracion;

    public void setPartida(PartidaModel partida) {
        this.partida = partida;
        mostrarResumen();
    }

    private void mostrarResumen() {
        labelPuntos.setText("Puntuación: " + partida.getPuntuacion());
        labelAciertos.setText("Aciertos: " + partida.getAciertos());
        labelErrores.setText("Errores: " + partida.getErrores());
        labelDificultad.setText("Dificultad: " + partida.getDificultad());

        long minutos = Duration.between(partida.getFecha(), LocalDateTime.now()).toMinutes();
        labelDuracion.setText("Duración: " + minutos + " minuto(s)");
    }

    @FXML
    private void nuevaPartida() {
        Stage stage = (Stage) labelResumen.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void salir() {
        Stage stage = (Stage) labelResumen.getScene().getWindow();
        stage.close();
    }
}


