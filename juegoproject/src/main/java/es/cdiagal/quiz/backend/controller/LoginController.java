package es.cdiagal.quiz.backend.controller;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.HashUtils;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController extends AbstractController{
    
    private final UsuarioDAO usuarioDAO;

    @FXML protected JFXButton backButton;
    @FXML protected Label loginLabel;
    @FXML protected TextField userLoginTextfield;
    @FXML protected PasswordField userLoginPasswordfield;
    @FXML protected JFXButton acceptLoginButton;
    @FXML protected Hyperlink recoveryLink;
    @FXML protected Text loginTextAdvise;



    /**
     * Inyección de dependencia del DAO usando la ruta unificada de la BD.
     */
    public LoginController(){
        super();
        this.usuarioDAO = new UsuarioDAO(getRutaArchivoBD());
    }

    /**
     * Metodo que inicializa el cambio de idioma en el ComboBox.
     */
    @FXML
    public void initialize(){
        if(getPropertiesLanguage()==null){
            setPropertiesLanguage(loadLanguage("language", getIdiomaActual()));
        }
        userLoginTextfield.requestFocus();
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

        loginLabel.setText(getPropertiesLanguage().getProperty("loginLabel"));
        userLoginTextfield.setPromptText(getPropertiesLanguage().getProperty("userLoginTextfieldPrompText"));
        userLoginPasswordfield.setPromptText(getPropertiesLanguage().getProperty("userLoginPasswordfieldPrompText"));
        loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise_errorUser"));
        loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise_errorPassword"));
        acceptLoginButton.setText(getPropertiesLanguage().getProperty("acceptLoginButton"));
        recoveryLink.setText(getPropertiesLanguage().getProperty("recoveryLink"));
        }
    }

    /**
     * Funcion que abre la ventana userData despues de comprobar los datos.
     */
    @FXML
    public void onClicAcceptLogin(){
        UsuarioModel usuarioValidado = validarDatosLogin();
        if (usuarioValidado != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/userData.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                UserDataController userDataController = fxmlLoader.getController();
                userDataController.setUsuario(usuarioValidado);
                userDataController.usuarioData();

                Stage stage = (Stage) acceptLoginButton.getScene().getWindow();
                stage.setTitle(getPropertiesLanguage().getProperty("userDataTitle"));
                stage.setScene(scene);
                stage.sizeToScene();
                stage.show();
            } catch (Exception e) {
                System.out.println("Error al cargar la página.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Funcion que lleva hacia la pantalla de recuperacion de contraseña.
     */
    @FXML
    public void onClicRecoverPassword(){
        try {
            Stage stage = (Stage) recoveryLink.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/recoverPassword.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 451,600);
            stage.setTitle(getPropertiesLanguage().getProperty("recoverPasswordTitle"));
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            System.out.println("Error al cargar la página.");
            e.printStackTrace();
        }
    }

        /**
     * Funcion que lleva hacia la pantalla anterior desde la que se accede a Settings.
     */
    @FXML
    protected void onClickBackButton(){
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/innit.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 600);
            stage.setTitle("BrainQuiz");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Funcion que valida todos los datos posibles para que pueda acceder un nuevo
     * usuario con sus credenciales.
     * 
     * @return nuevo login.
     */
    private UsuarioModel validarDatosLogin() {
        // Campos vacios
        if (userLoginTextfield == null || userLoginTextfield.getText().isEmpty() ||
        userLoginPasswordfield == null || userLoginPasswordfield.getText().isEmpty()) {
                loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise"));
            return null;
        }
        // Usuario inexistente
        UsuarioModel usuarioLogin = usuarioDAO.buscarPorNick(userLoginTextfield.getText());
        if (usuarioLogin == null) {
            loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise_errorUser"));
            return null;
        }
        // Contraseña incorrecta (con hash verificado)
        if (!HashUtils.verificarPassword(userLoginPasswordfield.getText(), usuarioLogin.getPassword())) {
            loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise_errorPassword"));
            return null;
        }
        // validacion exitosa
        loginTextAdvise.setText("¡Usuario validado correctamente!");
        return usuarioLogin;
    }


    
}
