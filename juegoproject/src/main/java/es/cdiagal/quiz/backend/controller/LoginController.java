package es.cdiagal.quiz.backend.controller;

import com.jfoenix.controls.JFXButton;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
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

    private final UsuarioDAO usuarioDAO = new UsuarioDAO(getRutaArchivoBD());

    @FXML protected JFXButton backButton;
    @FXML protected Label loginLabel;
    @FXML protected TextField userLoginTextfield;
    @FXML protected PasswordField loginPasswordfield;
    @FXML protected JFXButton acceptLoginButton;
    @FXML protected Hyperlink recoveryLink;
    @FXML protected Text loginTextAdvise;



    

    /**
     * Funcion que abre la ventana userData despues de comprobar los datos.
     */
    @FXML 
    public void onClicAcceptLogin(){
        UsuarioModel usuarioValidado = validarDatosLogin();
        if (usuarioValidado != null) {
            try {
                Stage stage = (Stage) acceptLoginButton.getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/fxml/userData.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 451,600);

                UserDataController userDataController = fxmlLoader.getController();
                userDataController.setUsuario(usuarioValidado);

                stage.setTitle("Información");
                stage.setScene(scene);
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
            stage.setTitle("Recuperar contraseña");
            stage.setScene(scene);
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
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("innit.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 450, 600);
            stage.setTitle("BrainQuiz");
            stage.setScene(scene);
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
        if (userLoginTextfield == null || userLoginTextfield.getText().isEmpty() ||
             loginPasswordfield == null || loginPasswordfield.getText().isEmpty()) {
                loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise"));
            return null;
        }

        UsuarioModel usuarioLogin = usuarioDAO.buscarPorNick(userLoginTextfield.getText());
        if (usuarioLogin == null) {
            loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise_errorUser"));
            return null;
        }

        if (!usuarioLogin.getPassword().equals(loginPasswordfield.getText())) {
            loginTextAdvise.setText(getPropertiesLanguage().getProperty("loginTextAdvise_errorPassword"));
            return null;
        }

        loginTextAdvise.setText("¡Usuario validado correctamente!");
        return usuarioLogin;
    }
}
