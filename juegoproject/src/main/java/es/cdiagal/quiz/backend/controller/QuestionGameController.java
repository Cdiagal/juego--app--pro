package es.cdiagal.quiz.backend.controller;

import java.util.List;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.PreguntaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.PreguntaServiceModel;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Clase que gestiona toda la lógica visual del juego.
 * @author cdiagal
 * @version 1.0.0
 */
public class QuestionGameController extends AbstractController {
    private GameController gameController;
    private final PreguntaServiceModel preguntaService;
    private List<PreguntaModel> preguntas;
    private int indexPregunta = 0;
    private Timeline timeline;
    private final int tiempoTotal = 30;
    private int tiempoRestante;
    private PreguntaModel preguntaActual;
    private UsuarioModel usuario;
    private Timeline tiempoTexto;
    private javafx.scene.media.AudioClip ticTacClip;
    private boolean sonidoIniciado = false;



    @FXML private Label questionLabel;
    @FXML private Label optionLabel1, optionLabel2, optionLabel3, optionLabel4;
    @FXML private ProgressBar timerBar;
    @FXML private Label feedbackLabel;
    @FXML private Pane optionPane1, optionPane2, optionPane3, optionPane4;
    @FXML private Label puntosLabel;
    @FXML private Label nivelLabel;
    @FXML private Label bonusLabel;
    @FXML private JFXButton exitButton;
    @FXML private Label timeLabel;

    public QuestionGameController() {
        super();
        this.preguntaService = new PreguntaServiceModel(getRutaArchivoBD());
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void iniciarJuego() {
        preguntas = preguntaService.obtenerPreguntasParaUsuario(usuario, 10);
        indexPregunta = 0;
    
        // Mostrar puntos y nivel desde el inicio
        if (puntosLabel != null) {
            puntosLabel.setText("Puntos: " + usuario.getPuntos());
        }
        if (nivelLabel != null) {
            nivelLabel.setText("Nivel: " + usuario.getNivel());
        }
    
        mostrarPregunta();
    }
    

    private void mostrarPregunta() {
        if (indexPregunta >= preguntas.size()) {
            feedbackLabel.setText("¡Has completado el cuestionario!");
            detenerTemporizador();
            return;
        }

        preguntaActual = preguntas.get(indexPregunta);
        questionLabel.setText(preguntaActual.getEnunciado());
        List<String> opciones = preguntaActual.getOpcionesMezcladas();

        optionLabel1.setText(opciones.get(0));
        optionLabel2.setText(opciones.get(1));
        optionLabel3.setText(opciones.get(2));
        optionLabel4.setText(opciones.get(3));

        resetEstilosOpciones();
        feedbackLabel.setText("");
        iniciarTemporizador();
    }

    /**
     * Inicia el temporizador.
     */
    private void iniciarTemporizador() {
        detenerTemporizador();

        tiempoRestante = (usuario != null && usuario.getRachaCorrectasSeguidas() > 0)
            ? Math.min(tiempoTotal + 2, 60)
            : tiempoTotal;

        timerBar.setProgress(1.0);
        timeLabel.setText("Tiempo: " + tiempoRestante + "s");


        timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(timerBar.progressProperty(), 1.0)),
            new KeyFrame(Duration.seconds(tiempoRestante), new KeyValue(timerBar.progressProperty(), 0.0))
        );

        // Cambiar el color de la barra según el progreso
        timerBar.progressProperty().addListener((obs, oldVal, newVal) -> {
            double progress = newVal.doubleValue();
            if (progress > 0.5) {
                timerBar.setStyle("-fx-accent: green;");
            } else if (progress > 0.2) {
                timerBar.setStyle("-fx-accent: orange;");
            } else {
                timerBar.setStyle("-fx-accent: red;");
            }
        });

        tiempoTexto = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tiempoRestante--;
            timeLabel.setText("Tiempo: " + tiempoRestante + "s");

            if (tiempoRestante == 5 && ticTacClip != null && !sonidoIniciado) {
                sonidoIniciado = true;
                ticTacClip.play();
            }
            if (tiempoRestante <= 0) {
                detenerTemporizador();
                feedbackLabel.setText("¡Tiempo agotado!");
                usuario.reiniciarRachaCorrectasSeguidas();
            
                Platform.runLater(() -> {
                    ImageView iconX = new ImageView(new Image(getClass().getResource("/images/close_red.png").toExternalForm()));
                    iconX.setFitWidth(16);
                    iconX.setFitHeight(16);
            
                    ImageView iconRetry = new ImageView(new Image(getClass().getResource("/images/retry.png").toExternalForm()));
                    iconRetry.setFitWidth(16);
                    iconRetry.setFitHeight(16);
            
                    ButtonType salir = new ButtonType("Salir", ButtonData.CANCEL_CLOSE);
                    ButtonType reintentar = new ButtonType("Reintentar", ButtonData.OK_DONE);
            
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Fin de la partida");
                    alert.setHeaderText("Se ha agotado el tiempo");
                    alert.setContentText("¿Quieres volver a intentarlo o salir?");
                    alert.getButtonTypes().setAll(salir, reintentar);
            
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(getClass().getResource("/images/alert_icon.png").toExternalForm()));
            
                    alert.showAndWait().ifPresent(respuesta -> {
                        if (respuesta == salir) {
                            if (gameController != null) {
                                gameController.salirDelJuego();
                            }
                        } else if (respuesta == reintentar) {
                            iniciarJuego();
                        }
                    });
                });
            }
            
        }));
        tiempoTexto.setCycleCount(tiempoRestante);

        try {
            ticTacClip = new AudioClip(getClass().getResource("/sounds/tictac.mp3").toExternalForm());
            ticTacClip.setCycleCount(AudioClip.INDEFINITE); 
            ticTacClip.setVolume(1.0);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el sonido tictac: " + e.getMessage());
        }

        timeline.play();
        tiempoTexto.play();
    }

    /**
     * Detiene el temporizador
     */
    private void detenerTemporizador() {
        if (timeline != null) timeline.stop();
        if (tiempoTexto != null) tiempoTexto.stop();
        if (ticTacClip != null && ticTacClip.isPlaying()) {
            ticTacClip.stop();
        }
        sonidoIniciado = false;
    }

    /**
     * Analiza la respuesta dada por el jugador y determina si es correcta, incorrecta y si tiene bonus.
     */
    private void comprobarRespuesta(String respuestaUsuario, Pane paneSeleccionado) {
        detenerTemporizador();
    
        String letraCorrecta = preguntaActual.getRespuestaCorrecta().trim().substring(0, 1).toUpperCase();
        String letraUsuario = respuestaUsuario.trim().substring(0, 1).toUpperCase();
    
        if (letraUsuario.equals(letraCorrecta)) {
            usuario.incrementarPuntosPorAcierto();
            usuario.incrementarRachaCorrectasSeguidas();
            usuario.actualizarNivelPorPuntos();
    
            if (nivelLabel != null) nivelLabel.setText("Nivel: " + usuario.getNivel());
            if (puntosLabel != null) puntosLabel.setText("Puntos: " + usuario.getPuntos());
    
            feedbackLabel.setText("¡Correcto!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            paneSeleccionado.setStyle("-fx-background-color: #A3D9A5;");
    
            if (usuario.getRachaCorrectasSeguidas() == 5) {
                tiempoRestante = Math.min(tiempoRestante + 2, 60);
                timeLabel.setText("Tiempo: " + tiempoRestante + "s");
    
                bonusLabel.setText("+2 BONUS");
                bonusLabel.setStyle("-fx-fill: green; -fx-font-weight: bold;");
                bonusLabel.setVisible(true);
    
                ScaleTransition scale = new ScaleTransition(Duration.seconds(0.3), bonusLabel);
                scale.setFromX(1);
                scale.setFromY(1);
                scale.setToX(1.6);
                scale.setToY(1.6);
    
                FadeTransition fade = new FadeTransition(Duration.seconds(1.2), bonusLabel);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);
                fade.setDelay(Duration.seconds(0.3));
    
                scale.setOnFinished(e -> fade.play());
                scale.play();
            }
    
            new Timeline(new KeyFrame(Duration.seconds(2), e -> siguientePregunta())).play();
        } else {
            usuario.reiniciarRachaCorrectasSeguidas();
            feedbackLabel.setText("Incorrecto. La correcta era: " + letraCorrecta);
            feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-weight: 900;");
            paneSeleccionado.setStyle("-fx-background-color: #F4A7A7;");
    
            ImageView iconX = new ImageView(new Image(getClass().getResource("/images/close_red.png").toExternalForm()));
            iconX.setFitWidth(16);
            iconX.setFitHeight(16);
    
            ImageView iconRetry = new ImageView(new Image(getClass().getResource("/images/retry.png").toExternalForm()));
            iconRetry.setFitWidth(16);
            iconRetry.setFitHeight(16);
    
            ButtonType salir = new ButtonType("Salir", ButtonData.CANCEL_CLOSE);
            ButtonType reintentar = new ButtonType("Reintentar", ButtonData.OK_DONE);
    
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Fin de la partida");
            alert.setHeaderText("Respuesta incorrecta");
            alert.setContentText("Has perdido. ¿Quieres volver a intentarlo o salir?");
            alert.getButtonTypes().setAll(salir, reintentar);
    
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResource("/images/alert_icon.png").toExternalForm()));
    
            ((javafx.scene.control.Button) alert.getDialogPane().lookupButton(salir)).setGraphic(iconX);
            ((javafx.scene.control.Button) alert.getDialogPane().lookupButton(reintentar)).setGraphic(iconRetry);
    
            alert.showAndWait().ifPresent(respuesta -> {
                if (respuesta == salir && gameController != null) {
                    gameController.salirDelJuego();
                } else if (respuesta == reintentar) {
                    iniciarJuego();
                }
            });
        }
    }
    

    private void siguientePregunta() {
        indexPregunta++;
        mostrarPregunta();
    }

    /**
     * Reestablece los pane al color una vez que se contesta.
     */
    private void resetEstilosOpciones() {
        String estiloOriginal = "-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 5;";
        optionPane1.setStyle(estiloOriginal);
        optionPane2.setStyle(estiloOriginal);
        optionPane3.setStyle(estiloOriginal);
        optionPane4.setStyle(estiloOriginal);
    }

    /**
     * Funcion que sale del juego.
     */
    @FXML
    public void onClickExitGameButton(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar salida");
        alert.setHeaderText("¿Quieres salir del juego?");
        alert.setContentText("Perderás el progreso actual de esta partida.");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");

        alert.getButtonTypes().setAll(botonSi, botonNo);

        alert.showAndWait().ifPresent(respuesta -> {
            if (respuesta == botonSi) {
                // Salir del juego
                if (gameController != null) {
                    gameController.salirDelJuego();
                }

                // Cargar la vista de userData.fxml
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/userData.fxml"));
                    Scene escena = new Scene(loader.load());
                    
                    // Pasar el usuario a la vista
                    UserDataController controller = loader.getController();
                    controller.setUsuario(usuario);

                    Stage stage = (Stage) timerBar.getScene().getWindow();
                    stage.setTitle("Perfil de usuario");
                    stage.setScene(escena);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML private void onClickOption1() { comprobarRespuesta(optionLabel1.getText(), optionPane1); }
    @FXML private void onClickOption2() { comprobarRespuesta(optionLabel2.getText(), optionPane2); }
    @FXML private void onClickOption3() { comprobarRespuesta(optionLabel3.getText(), optionPane3); }
    @FXML private void onClickOption4() { comprobarRespuesta(optionLabel4.getText(), optionPane4); }
}
