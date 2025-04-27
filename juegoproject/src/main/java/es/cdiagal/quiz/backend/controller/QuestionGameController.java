package es.cdiagal.quiz.backend.controller;

import java.util.List;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.PreguntaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.PreguntaServiceModel;
import es.cdiagal.quiz.initApp.MainApplication;
import es.cdiagal.quiz.backend.controller.GameController;
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
    private int respuestasCorrectas = 0;
    private Timeline timeline;
    private final int tiempoTotal = 30;
    private int tiempoRestante;
    private PreguntaModel preguntaActual;
    private UsuarioModel usuario;
    private Timeline tiempoTexto;
    private javafx.scene.media.AudioClip ticTacClip;
    private boolean sonidoIniciado = false;
    private boolean juegoTerminado = false;



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
        detenerTemporizador();
        preguntas = preguntaService.obtenerPreguntasParaUsuario(usuario, 10);
        indexPregunta = 0;
        respuestasCorrectas = 0;
        juegoTerminado = false;
    
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
        preguntaActual.cargarRespuestas(optionLabel1, optionLabel2, optionLabel3, optionLabel4);

        resetEstilosOpciones();
        feedbackLabel.setText("");
        iniciarTemporizador();
    }

    /**
     * Inicia el temporizador.
     */
    private void iniciarTemporizador() {
        detenerTemporizador();

        tiempoRestante = tiempoTotal;
         // Inicializa la barra de progreso al 100% y muestra el tiempo restante
        timerBar.setProgress(1.0);
        timeLabel.setText("Tiempo: " + tiempoRestante + "s");

        // Configura la animación para la barra de progreso visual
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
        // Cuenta regresiva: cada segundo actualiza el label del tiempo.
        tiempoTexto = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            tiempoRestante--;
            timeLabel.setText("Tiempo: " + tiempoRestante + "s");

            // Sonido que se emite cuando la barra de tiempo esta por debajo de 5".
            if (tiempoRestante == 5 && ticTacClip != null && !sonidoIniciado) {
                sonidoIniciado = true;
                ticTacClip.play();
            }
            //Si se acaba el tiempo, muestra el alert de "Fin del juego".
            boolean acabado = tiempoRestante <=0;

            if (acabado) {
                detenerTemporizador();
                feedbackLabel.setText("¡Tiempo agotado!");
                usuario.reiniciarRachaCorrectasSeguidas();
            
                Platform.runLater(() -> {
            
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

        // Carga el sonido si aun no se ha cargado.
        try {
            ticTacClip = new AudioClip(getClass().getResource("/sounds/tictac.mp3").toExternalForm());
            ticTacClip.setCycleCount(AudioClip.INDEFINITE); 
            ticTacClip.setVolume(1.0);
        } catch (Exception e) {
            System.out.println("No se pudo cargar el sonido tictac: " + e.getMessage());
        }
        // Inicia la barra de progreso y el contador de texto
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

            respuestasCorrectas++;
    
            if (nivelLabel != null) nivelLabel.setText("Nivel: " + usuario.getNivel());
            if (puntosLabel != null) puntosLabel.setText("Puntos: " + usuario.getPuntos());
    
            feedbackLabel.setText("¡Correcto!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            paneSeleccionado.setStyle("-fx-background-color: #A3D9A5;");

            if(respuestasCorrectas >= 10){
                terminarJuego();
            } else {
                new Timeline(new KeyFrame(Duration.seconds(2), e -> siguientePregunta())).play();
            }
    
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
    
        } else {
            usuario.reiniciarRachaCorrectasSeguidas();
            feedbackLabel.setText("Incorrecto. La correcta era: " + letraCorrecta);
            feedbackLabel.setStyle("-fx-text-fill: red; -fx-font-weight: 900;");
            paneSeleccionado.setStyle("-fx-background-color: #F4A7A7;");

            new Timeline(new KeyFrame(Duration.seconds(2), e -> siguientePregunta())).play();
        }
    }

    private void terminarJuego() {
        // Establecer flag a true para evitar que se muestre el alert después de cada pregunta
        juegoTerminado = true;
        // Detener temporizador y mostrar la alerta de finalización
        detenerTemporizador();
        feedbackLabel.setText("¡Has completado el cuestionario!");
        usuario.reiniciarRachaCorrectasSeguidas();

        Platform.runLater(() -> {
            ButtonType continuar = new ButtonType("🔄 Continuar", ButtonData.CANCEL_CLOSE);
            ButtonType salir = new ButtonType("👋 Salir", ButtonData.OK_DONE);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Finalizado");
            alert.setHeaderText("¡Felicidades! Has completado el cuestionario.");
            alert.setContentText("¿Quieres continuar jugando o salir?");
            alert.getButtonTypes().setAll(continuar, salir);

            alert.showAndWait().ifPresent(respuesta -> {
                if (respuesta == salir) {
                    // Guardar los datos del usuario
                    guardarDatosUsuario();
                    irAUserData(); // Volver a la pantalla de usuario
                } else if (respuesta == continuar) {
                    // Reiniciar el juego o continuar con otro set de preguntas
                    iniciarJuego(); // Reinicia el juego
                }
            });
        });
    }

    private void guardarDatosUsuario() {
        // Aquí puedes llamar a tu servicio de base de datos para guardar los puntos y nivel
        UsuarioDAO dao = new UsuarioDAO(getRutaArchivoBD());
        dao.actualizarUsuario(usuario); // Actualizar usuario en la base de datos
    }

    
    //Muestra la siguiente pregunta tras responder.
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
        detenerTemporizador();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/userData.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                UserDataController controller = fxmlLoader.getController();
                controller.setUsuario(usuario);

                Stage stage = (Stage) exitButton.getScene().getWindow();
                stage.setTitle("Usuario");
                stage.setScene(scene);
                stage.sizeToScene();
                stage.show();
            } catch (Exception e) {
                System.out.println("Error al cargar la página.");
                e.printStackTrace();
            }
            
    }



    /**
     * Metodo que retorna una ventana tipo Alert con un icono
     */
    private void mostrarAlertaConIcono(String titulo, String mensaje, String iconoPath) {
        Image image = new Image(iconoPath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.setGraphic(imageView);
        alert.showAndWait();
    }

    /**
     * Dirige a userdata.
     */
    private void irAUserData() {
        // Redirige a la página de datos del usuario
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/userData.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            UserDataController controller = fxmlLoader.getController();
            controller.setUsuario(usuario);  // Cargar el usuario al controlador de la vista

            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setTitle("Usuario");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            System.out.println("Error al cargar la página.");
            e.printStackTrace();
        }
    }

    @FXML private void onClickOption1() { comprobarRespuesta(optionLabel1.getText(), optionPane1); }
    @FXML private void onClickOption2() { comprobarRespuesta(optionLabel2.getText(), optionPane2); }
    @FXML private void onClickOption3() { comprobarRespuesta(optionLabel3.getText(), optionPane3); }
    @FXML private void onClickOption4() { comprobarRespuesta(optionLabel4.getText(), optionPane4); }


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
    
            nivelLabel.setText(getPropertiesLanguage().getProperty("nivelLabel"));
            puntosLabel.setText(getPropertiesLanguage().getProperty("puntosLabel"));
            timeLabel.setText(getPropertiesLanguage().getProperty("timeLabel"));
            optionLabel1.setText(getPropertiesLanguage().getProperty("optionLabel1"));
            optionLabel2.setText(getPropertiesLanguage().getProperty("optionLabel2"));
            optionLabel3.setText(getPropertiesLanguage().getProperty("optionLabel3"));
            optionLabel4.setText(getPropertiesLanguage().getProperty("optionLabel4"));
        }
    }
}
