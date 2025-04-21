package es.cdiagal.quiz.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;



import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.PartidaServiceModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class GameController extends AbstractController {
    private PartidaModel partida;
    private PartidaServiceModel partidaService;
    private UsuarioModel usuario;
    private int idUltimaPreguntaActual;



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

    /**
     * Metodo que carga el estado de una partida.
     */
    private void continuarPartida() {
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
            partida.setIdUltimaPregunta(idUltimaPreguntaActual);
            partida.setFecha(ahora); 
            partidaService.actualizarPartida(partida);
            System.out.println("Partida guardada correctamente.");
        }
    }

    /**
     * Metodo que sale del juego y vuelve a la pantalla UserData.
     */
    @FXML
    private void salirDelJuego() {
        finalizarPartida();
        try {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userData.fxml"));
        Scene scene = new Scene(loader.load(), 450, 600);

        UserDataController userDataController = loader.getController();
        userDataController.setUsuario(usuario);
        userDataController.usuarioData();

        stage.setTitle("Datos del Usuario");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Partida finalizada");
        alert.setHeaderText(null);
        alert.setContentText("¡Gracias por jugar!");
        alert.showAndWait();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

