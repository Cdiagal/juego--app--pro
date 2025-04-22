package es.cdiagal.quiz.backend.controller;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.PreguntaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.PreguntaServiceModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.List;

public class QuestionGameController extends AbstractController {
    private final PreguntaServiceModel preguntaService;

    private List<PreguntaModel> preguntas;
    private int indexPregunta = 0;
    private Timeline timeline;
    private final int tiempoTotal = 10; // segundos
    private int tiempoRestante;
    private PreguntaModel preguntaActual;
    private UsuarioModel usuario;

    @FXML private Label questionLabel;
    @FXML private Label optionLabel1, optionLabel2, optionLabel3, optionLabel4;
    @FXML private ProgressBar timerBar;
    @FXML private Label feedbackLabel;
    @FXML private Pane optionPane1, optionPane2, optionPane3, optionPane4;

    /**
     * Inyecta el servicio de preguntas usando la ruta centralizada del archivo de base de datos.
     */
    public QuestionGameController() {
        super();
        this.preguntaService = new PreguntaServiceModel(getRutaArchivoBD());
    }

    /**
     * Método para iniciar el juego una vez se ha establecido el usuario.
     * Obtiene las preguntas y muestra la primera.
     */
    public void iniciarJuego() {
        preguntas = preguntaService.obtenerPreguntasParaUsuario(usuario, 10);
        indexPregunta = 0;
        mostrarPregunta();
    }

    /**
     * Establece el usuario que va a jugar.
     * @param usuario el usuario actual
     */
    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
    }

    /**
     * Muestra la pregunta actual en la interfaz.
     * Si no quedan preguntas, termina el cuestionario.
     */
    private void mostrarPregunta() {
        if (indexPregunta >= preguntas.size()) {
            feedbackLabel.setText("¡Has completado el cuestionario!");
            detenerTemporizador();
            return;
        }

        preguntaActual = preguntas.get(indexPregunta);
        questionLabel.setText(preguntaActual.getEnunciado());
        List<String> opciones = preguntaActual.getOpcionesMezcladas();

        // Asigna las opciones a los labels
        optionLabel1.setText(opciones.get(0));
        optionLabel2.setText(opciones.get(1));
        optionLabel3.setText(opciones.get(2));
        optionLabel4.setText(opciones.get(3));

        resetEstilosOpciones();
        feedbackLabel.setText("");
        iniciarTemporizador();
    }

    /**
     * Inicia el temporizador de cuenta regresiva para la pregunta actual.
     * Si se agota el tiempo, avanza a la siguiente pregunta automáticamente.
     */
    private void iniciarTemporizador() {
        detenerTemporizador();
        tiempoRestante = tiempoTotal;
        timerBar.setProgress(1.0);

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            tiempoRestante--;
            timerBar.setProgress((double) tiempoRestante / tiempoTotal);

            if (tiempoRestante <= 0) {
                feedbackLabel.setText("¡Tiempo agotado!");
                siguientePregunta();
            }
        }));
        timeline.setCycleCount(tiempoTotal);
        timeline.play();
    }

    /**
     * Detiene el temporizador activo si lo hay.
     */
    private void detenerTemporizador() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Comprueba si la respuesta seleccionada es correcta.
     * Muestra feedback visual y textual según el resultado.
     * @param respuestaUsuario Texto de la opción seleccionada (p. ej., "A. París")
     * @param paneSeleccionado Pane asociado a la opción clicada (para marcar color)
     */
    private void comprobarRespuesta(String respuestaUsuario, Pane paneSeleccionado) {
        detenerTemporizador();

        String respuestaCorrecta = preguntaActual.getRespuestaCorrecta();
        String respuestaUsuarioLimpia = respuestaUsuario.substring(0, 1); // Extrae la letra A, B, C o D

        if (respuestaCorrecta.equalsIgnoreCase(respuestaUsuarioLimpia)) {
            feedbackLabel.setText("¡Correcto!");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            paneSeleccionado.setStyle("-fx-background-color: #A3D9A5;");
        } else {
            feedbackLabel.setText("Incorrecto. La correcta era: " + respuestaCorrecta);
            feedbackLabel.setStyle("-fx-text-fill: red;");
            paneSeleccionado.setStyle("-fx-background-color: #F4A7A7;");
        }

        Timeline wait = new Timeline(new KeyFrame(Duration.seconds(2), e -> siguientePregunta()));
        wait.setCycleCount(1);
        wait.play();
    }

    /**
     * Avanza a la siguiente pregunta del cuestionario.
     */
    private void siguientePregunta() {
        indexPregunta++;
        mostrarPregunta();
    }

    /**
     * Restaura el estilo original de los paneles de opción.
     * Se usa antes de mostrar una nueva pregunta.
     */
    private void resetEstilosOpciones() {
        String estiloOriginal = "-fx-background-color: white; -fx-border-color: #CCCCCC; -fx-border-radius: 5;";
        optionPane1.setStyle(estiloOriginal);
        optionPane2.setStyle(estiloOriginal);
        optionPane3.setStyle(estiloOriginal);
        optionPane4.setStyle(estiloOriginal);
    }

    /**
     * Métodos vinculados al clic de cada opción.
     * Llaman a comprobarRespuesta con el texto y pane correspondiente.
     */
    @FXML private void onClickOption1() {
        comprobarRespuesta(optionLabel1.getText(), optionPane1);
    }

    @FXML private void onClickOption2() {
        comprobarRespuesta(optionLabel2.getText(), optionPane2);
    }

    @FXML private void onClickOption3() {
        comprobarRespuesta(optionLabel3.getText(), optionPane3);
    }

    @FXML private void onClickOption4() {
        comprobarRespuesta(optionLabel4.getText(), optionPane4);
    }
}
