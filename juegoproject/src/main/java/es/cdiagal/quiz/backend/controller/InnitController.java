package es.cdiagal.quiz.backend.controller;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.model.entities.Bandera;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InnitController extends AbstractController{
    @FXML protected AnchorPane innitAnchorPane;
    @FXML protected JFXButton loginButton;
    @FXML protected JFXButton registerButton;
    @FXML protected ComboBox<Bandera> languageComboBox;
    
    
    /**
     * Metodo que inicializa el cambio de idioma en el ComboBox.
     */
    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll(
            new Bandera("español", new Image("/images/sp.png")),
            new Bandera("english", new Image("/images/uk.png")),
            new Bandera("français", new Image("/images/fr.png"))
        );

    // Mostrar solo banderas en el desplegable
        languageComboBox.setCellFactory(listView -> crearCeldasIdioma());

    // Mostrar solo la bandera seleccionada
        languageComboBox.setButtonCell(crearCeldasIdioma());
    
    // Seleccionar la actual
    for (Bandera idioma : languageComboBox.getItems()) {
        if (idioma.getNombre().equalsIgnoreCase(AbstractController.getIdiomaActual())) {
            languageComboBox.setValue(idioma);
            break;
        }
    }

    }
    /**
     * Metodo que crea las celdas del ComboBox con imagenes.
     */
    private ListCell<Bandera> crearCeldasIdioma() {
        return new ListCell<>() {
            private final ImageView imageView = new ImageView();
        
            @Override
            protected void updateItem(Bandera item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item.getIcono());
                    imageView.setFitWidth(30);
                    imageView.setFitHeight(30);
                    setGraphic(imageView);
                }
            }
        };
    }


    /**
     * Funcion que cambia el idioma de los objetos de la interfaz.
     */
    @FXML
    public void onClicChangeLanguage() {
        Bandera idiomaSeleccionado = (Bandera) languageComboBox.getValue();
        AbstractController.setIdiomaActual(idiomaSeleccionado.getNombre());
        setPropertiesLanguage(loadLanguage("language", idiomaSeleccionado.getNombre()));

        loginButton.setText(getPropertiesLanguage().getProperty("loginButton"));
        registerButton.setText(getPropertiesLanguage().getProperty("registerButton"));
    }

    /**
     * Funcion que abre la ventana login.
     */
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

    /**
     * Funcion que abre la ventana de registro de usuario.
     */
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
