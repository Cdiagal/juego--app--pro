package es.cdiagal.quiz.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;



import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.PartidaServiceModel;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameController extends AbstractController {
    private PartidaModel partida;
    private PartidaServiceModel partidaService;
    private UsuarioModel usuario;


    @FXML
    private AnchorPane rootPane;


    // Se ejecuta al iniciar el controlador o escena
    public void iniciarJuego(UsuarioModel usuario) {
        this.usuario = usuario;
        this.partidaService = new PartidaServiceModel(getRutaArchivoBD());

        PartidaModel partidaActiva = partidaService.obtenerPartidaActiva(usuario.getId());

        if (partidaActiva != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Partida activa");
            alert.setHeaderText("Tienes una partida activa.");
            alert.setContentText("¿Quieres continuarla o empezar una nueva?");
            ButtonType btnContinuar = new ButtonType("Continuar");
            ButtonType btnNueva = new ButtonType("Nueva");
            alert.getButtonTypes().setAll(btnContinuar, btnNueva);

            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.isPresent() && resultado.get() == btnContinuar) {
                this.partida = partidaActiva;
                continuarPartida();
            } else {
                crearPartidaNueva();
            }
        } else {
            crearPartidaNueva();
        }
    }

    /**@FXML
    private void salirDelJuego() {
        finalizarPartida();
        
        Window rootPane;
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    */

    private void crearPartidaNueva() {
        this.partida = new PartidaModel(
            usuario.getId(),
            0, 0, 0,
            usuario.getNivel(),
            LocalDateTime.now()
        );
        partidaService.guardarPartida(partida);
        iniciarLogicaJuego();
    }

    private void continuarPartida() {
        // Aquí puedes cargar el estado si guardaste más datos (por ejemplo, pregunta actual)
        iniciarLogicaJuego();
    }

    private void iniciarLogicaJuego() {
        // Este método inicia el juego en sí:
        // - Carga preguntas
        // - Muestra la primera
        // - Reinicia temporizador
        // etc.
    }

    private void finalizarPartida() {
        LocalDateTime ahora = LocalDateTime.now();
        long duracionSegundos = java.time.Duration.between(partida.getFecha(), ahora).getSeconds();
    
        if (duracionSegundos < 60) {
            if (partida.getId() != 0) {
                partidaService.eliminarPartida(partida.getId());
            }
            System.out.println("Partida descartada (menos de 1 minuto).");
        } else {
            partida.setFecha(ahora); 
            partidaService.actualizarPartida(partida);
            System.out.println("Partida guardada correctamente.");
        }
    }
    
}

