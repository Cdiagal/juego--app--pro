package es.cdiagal.quiz.backend.controller;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.Bandera;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InnitController extends AbstractController{
    private final UsuarioDAO usuarioDAO;

    @FXML protected AnchorPane innitAnchorPane;
    @FXML protected JFXButton loginButton;
    @FXML protected JFXButton registerButton;
    @FXML protected ComboBox<Bandera> languageComboBox;
    
    public InnitController() {
        super();
        this.usuarioDAO = new UsuarioDAO(getRutaArchivoBD());
    }
    
    /**
     * Metodo que inicializa el cambio de idioma en el ComboBox.
     */
    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll(
        new Bandera("es", new Image(getClass().getResourceAsStream("/images/sp.png"))),
        new Bandera("en", new Image(getClass().getResourceAsStream("/images/uk.png"))),
        new Bandera("fr", new Image(getClass().getResourceAsStream("/images/fr.png")))
        );
    // Seleccionar la actual
    for (Bandera idioma : languageComboBox.getItems()) {
        if (idioma.getNombre().equalsIgnoreCase(AbstractController.getIdiomaActual())) {
            languageComboBox.setValue(idioma);
            break;
        }
    }

    //Carga el idioma de los elementos de la ventana.
    changeLanguage();

    // Mostrar solo banderas en el desplegable
        languageComboBox.setCellFactory(listView -> crearCeldasIdioma());

    // Mostrar solo la bandera seleccionada
        languageComboBox.setButtonCell(crearCeldasIdioma());


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
        Bandera idiomaSeleccionado = languageComboBox.getValue();
        if (idiomaSeleccionado == null) return;
        setIdiomaActual(idiomaSeleccionado.getNombre());
        setPropertiesLanguage(loadLanguage("language", idiomaSeleccionado.getNombre()));
        changeLanguage();
        
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
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
            } catch (Exception e) {
                System.out.println("Error al cargar la página.");
                e.printStackTrace();
            }
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

            loginButton.setText(getPropertiesLanguage().getProperty("loginButton"));
            registerButton.setText(getPropertiesLanguage().getProperty("registerButton"));
        }
    }
}
