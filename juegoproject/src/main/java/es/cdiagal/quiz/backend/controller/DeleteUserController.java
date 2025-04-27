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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Clase que gestiona la eliminacion de un usuario.
 * @author cdiagal
 * @version 1.0.0
 */
public class DeleteUserController extends AbstractController {
    private final UsuarioDAO usuarioDAO;

    @FXML private Label deleteBigLabel;
    @FXML private TextField registerNicknameTextField;
    @FXML private PasswordField registerPasswordField;
    @FXML private PasswordField registerRepeatPasswordField;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton logoutButton;
    @FXML private JFXButton backButton;

    public DeleteUserController() {
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
        registerNicknameTextField.requestFocus();
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

        deleteBigLabel.setText(getPropertiesLanguage().getProperty("deleteBigLabel"));
        registerNicknameTextField.setPromptText(getPropertiesLanguage().getProperty("nickTextFieldPromptText"));
        registerPasswordField.setPromptText(getPropertiesLanguage().getProperty("deletePasswordField1PrompText"));
        registerRepeatPasswordField.setPromptText(getPropertiesLanguage().getProperty("deletePasswordfield2PrompText"));
        deleteButton.setText(getPropertiesLanguage().getProperty("deleteButton"));
        logoutButton.setText(getPropertiesLanguage().getProperty("logoutButton"));
        
        }
    }

    /**
     * Valida que los datos de entrada sean correctos.
     */
    private boolean validarDatos() {

        // Campos vacios.
        if (registerNicknameTextField.getText().isEmpty() || registerPasswordField.getText().isEmpty() || registerRepeatPasswordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Debes rellenar todos los campos.");
            return false;
        }
        // Contrasenias diferentes.
        if (!registerPasswordField.getText().equals(registerRepeatPasswordField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Contraseñas diferentes", "Las contraseñas no coinciden.");
            return false;
        }
        // Usuario inexistente.
        UsuarioModel usuario = usuarioDAO.buscarPorNick(registerNicknameTextField.getText());
        if (usuario == null) {
            showAlert(Alert.AlertType.ERROR, "Usuario no encontrado", "No existe ningún usuario con ese alias.");
            return false;
        }
        // Contrasenias iguales.
        String password = registerPasswordField.getText();
        if (!HashUtils.verificarPassword(password, usuario.getPassword())) {
            showAlert(Alert.AlertType.ERROR, "Contraseña incorrecta", "La contraseña no coincide con la del usuario.");
            return false;
        }
        return true;
    }

    /**
     * Gestiona la orden del boton de eliminacion de usuario.
     */
    @FXML
    protected void onClickDelete() {
        if (!validarDatos()) return;

        String nick = registerNicknameTextField.getText().trim();
        UsuarioModel usuario = usuarioDAO.buscarPorNick(nick);
        showAlert(Alert.AlertType.WARNING, "Eliminar usuario", "A continuación se va a eliminar el usuario de forma permanente \n ¿Estás seguro de continuar?");
        ButtonType confirmar = new ButtonType("Continuar");
        boolean eliminado = usuarioDAO.eliminar(usuario.getId());
        if (eliminado) {
            showAlert(Alert.AlertType.INFORMATION, "Usuario eliminado", "El usuario '" + nick + "' ha sido eliminado correctamente.");
            onClickBackButton();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error al eliminar", "Ha ocurrido un error al intentar eliminar el usuario.");
        }
    }

    /**
     * Funcion que lleva a la ventana innit.
     */
    @FXML
    protected void onClickBackButton() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("/fxml/updateUserData.fxml"));
            Scene scene = new Scene(loader.load(),853,651);
            stage.setTitle("Actualizar");
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que cierra la sesión del usuario.
     */
    @FXML
    public void onClicLogout() {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
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
     * Funcion que muestra una alerta genérica.
     */
    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Eliminar usuario");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
