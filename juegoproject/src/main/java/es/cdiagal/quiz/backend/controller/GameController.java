package es.cdiagal.quiz.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.PartidaServiceModel;
import es.cdiagal.quiz.backend.controller.QuestionGameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Controller de partida que gestiona la lógica de inicio, continuación y finalización de una partida.
 * @author cdiagal
 * @version 1.0.0
 */

public class GameController extends AbstractController {
    private QuestionGameController questionGameController;
    private final PartidaServiceModel partidaService;
    private PartidaModel partida;
    private UsuarioModel usuario;
    private int idUltimaPreguntaActual;

    @FXML private AnchorPane rootPane;
    /**
     * Constructor: inyecta la ruta de BD al servicio.
     */
    public GameController() {
        super(); // PATH_DB establecido en AbstractController
        this.partidaService = new PartidaServiceModel(getRutaArchivoBD());
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    /**
     * Inicia el flujo de la partida para el usuario.
     */
    public void iniciarJuego(UsuarioModel usuario) {
        this.usuario = usuario;

        PartidaModel partidaActiva = partidaService.obtenerPartidaActiva(usuario.getId());
        if (partidaActiva != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Partida activa");
            alert.setHeaderText("Tienes una partida activa.");
            alert.setContentText("¿Quieres continuarla o empezar una nueva?");

            ButtonType buttonContinuar = new ButtonType("Continuar");
            ButtonType buttonNueva = new ButtonType("Nueva");
            ButtonType buttonCerrar = new ButtonType("Cerrar");
            alert.getButtonTypes().setAll(buttonContinuar, buttonNueva, buttonCerrar);

            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.isPresent()) {
                ButtonType elegido = resultado.get();
                if (elegido == buttonContinuar) {
                    
                    this.partida = partidaActiva;
                    continuarPartida();
                }
                else if (elegido == buttonNueva) {
                    
                    crearPartidaNueva();
                }
                
            } else {
                crearPartidaNueva();
            }
            
            iniciarLogicaJuego();
        }
    }

    /**
     * Funcion que gestiona la creacion de una partida nueva.
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

    /**
     * Continua una partida empezada.
     */
    private void continuarPartida() {
        iniciarLogicaJuego();
    }

    /**
     * Arranca la interfaz de juego (preguntas) cargando el FXML correspondiente.
     */
    private void iniciarLogicaJuego() {
        try {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/questionGame.fxml"));
            Parent root = loader.load();
            // Pasa el usuario y comienza la lógica de preguntas
            QuestionGameController controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setGameController(this);
            this.questionGameController = controller;

            Scene scene = new Scene(root);
            stage.setTitle("Quiz");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo iniciar el juego.");
        }
    }

    private void finalizarPartida() {
        LocalDateTime ahora = LocalDateTime.now();
        long duracionSegundos = java.time.Duration.between(partida.getFecha(), ahora).getSeconds();

        if (duracionSegundos < 60 && partida.getId() != 0) {
            partidaService.eliminarPartida(partida.getId());
            System.out.println("Partida descartada (menos de 1 minuto).");
        } else {
            partida.setIdUltimaPregunta(idUltimaPreguntaActual);
            partida.setFecha(ahora);
            partidaService.actualizarPartida(partida);
            System.out.println("Partida guardada correctamente.");
        }
    }

    /**
     * Sale del juego, detiene temporizador y regresa a la vista de datos de usuario.
     */
    @FXML
    public void salirDelJuego() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Salir del juego");
        confirmacion.setHeaderText("¿Seguro que quieres salir?");
        confirmacion.setContentText("Se guardará tu progreso solo si has jugado al menos 1 minuto.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            finalizarPartida();

            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userData.fxml"));
                    Parent root = loader.load();

                    UserDataController controller = loader.getController();
                    controller.setUsuario(usuario);
                    controller.usuarioData();

                    Stage stage = (Stage) rootPane.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Datos del usuario");
                    stage.sizeToScene();
                    stage.show();

                    showAlert(Alert.AlertType.INFORMATION, "Partida finalizada", "¡Gracias por jugar!");
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "No se pudo regresar a datos de usuario.");
                }
            });
        }
    }



    /**
     * Método utilitario para mostrar alertas.
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

   
}
