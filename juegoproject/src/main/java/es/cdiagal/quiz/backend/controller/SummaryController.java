package es.cdiagal.quiz.backend.controller;

import java.time.Duration;
import java.time.LocalDateTime;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Muestra el resumen de la partida tras finalizar.
 * @author cdiagal
 * @version 1.0.0
 */
public class SummaryController extends AbstractController {

    private PartidaModel partida;

    @FXML private Label resumenLabel;
    @FXML private Label puntosLabel;
    @FXML private Label aciertosLabel;
    @FXML private Label errorsLabel;
    @FXML private Label dificultadLabel;
    @FXML private Label duracionLabel;

    /**
     * Establece la partida finalizada y muestra sus datos.
     * @param partida modelo de la partida jugada.
     */
    public void setPartida(PartidaModel partida) {
        this.partida = partida;
        mostrarResumen();
    }

    /**
     * Muestra los resultados de la partida en pantalla.
     */
    private void mostrarResumen() {
        if (partida == null) return;

        puntosLabel.setText(getText("summary.points") + ": " + partida.getPuntuacion());
        aciertosLabel.setText(getText("summary.correct") + ": " + partida.getAciertos());
        errorsLabel.setText(getText("summary.wrong") + ": " + partida.getErrores());
        dificultadLabel.setText(getText("summary.difficulty") + ": " + partida.getDificultad());

        Duration duracion = Duration.between(partida.getFecha(), LocalDateTime.now());
        long minutos = duracion.toMinutes();
        long segundos = duracion.minusMinutes(minutos).getSeconds();
        duracionLabel.setText(getText("summary.duration") + ": " + minutos + " min " + segundos + " seg");
    }

    /**
     * Cierra la ventana actual y permite comenzar nueva partida.
     */
    @FXML
    private void nuevaPartida() {
        cerrarVentana();
    }

    /**
     * Cierra la aplicación o regresa al menú.
     */
    @FXML
    private void salir() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) resumenLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Método auxiliar para obtener textos desde el properties.
     */
    private String getText(String key) {
        return getPropertiesLanguage() != null ? getPropertiesLanguage().getProperty(key, key) : key;
    }
}
