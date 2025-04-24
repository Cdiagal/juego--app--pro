package es.cdiagal.quiz.backend.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import es.cdiagal.quiz.backend.controller.abstractas.AbstractController;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;
import es.cdiagal.quiz.backend.model.utils.service.HashUtils;
import es.cdiagal.quiz.initApp.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
public class UpdateUserDataController extends AbstractController {

    private final UsuarioDAO usuarioDAO;
    private UsuarioModel usuario;
    private byte[] imagenSeleccionada;

    @FXML private TextField nicknameField;
    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField repeatPasswordField;
    @FXML private ImageView profileImageView;
    @FXML private Label updateUserTextAdvise;
    @FXML private Button deleteUserButton;

    public UpdateUserDataController() {
        super();
        this.usuarioDAO = new UsuarioDAO(getRutaArchivoBD());
    }

    @FXML
    public void initialize() {
        aplicarClipCircular(profileImageView);
    }

    public void setUsuario(UsuarioModel usuario) {
        this.usuario = usuario;
        nicknameField.setText(usuario.getNickname());
        emailField.setText(usuario.getEmail());

        cargarImagenUsuario();
    }

    private void cargarImagenUsuario() {
        byte[] bytes = usuario.getImagen();
        if (bytes != null) {
            profileImageView.setImage(new Image(new ByteArrayInputStream(bytes)));
        } else {
            profileImageView.setImage(new Image(getClass().getResourceAsStream("/images/default-profile.png")));
        }
        aplicarClipCircular(profileImageView);
    }

    private void aplicarClipCircular(ImageView imageView) {
        double radius = Math.min(imageView.getFitWidth(), imageView.getFitHeight()) / 2;
        Circle clip = new Circle(radius, radius, radius);
        imageView.setClip(clip);
    }

    @FXML
    public void onClickUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen de perfil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File archivo = fileChooser.showOpenDialog(new Stage());
        if (archivo != null) {
            try {
                imagenSeleccionada = Files.readAllBytes(archivo.toPath());
                Image imagen = new Image(new FileInputStream(archivo));
                profileImageView.setImage(imagen);
                aplicarClipCircular(profileImageView);
                usuario.setImagen(imagenSeleccionada);
                usuarioDAO.actualizarImagen(usuario.getId(), imagenSeleccionada);
                mostrarAlerta("Imagen cargada", "Imagen seleccionada correctamente. Recuerda guardar para aplicarla.");
            } catch (IOException e) {
                mostrarAlerta("Error", "No se pudo leer la imagen seleccionada.");
            }
        }
    }

    @FXML
    private void actualizarDatos() {
        String nuevoNickname = nicknameField.getText().trim();
        String nuevoEmail = emailField.getText().trim();
        String nuevaPass = newPasswordField.getText();
        String repetirPass = repeatPasswordField.getText();

        if (nuevoNickname.isEmpty() || nuevoEmail.isEmpty()) {
            mostrarAlerta("Campos vacíos", "El nickname y el email no pueden estar vacíos.");
            return;
        }

        if (!nuevoEmail.matches("^[A-Za-z0-9%+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            mostrarAlerta("Email inválido", "Introduce un correo electrónico válido.");
            return;
        }

        if (!nuevaPass.isEmpty() || !repetirPass.isEmpty()) {
            if (!nuevaPass.equals(repetirPass)) {
                mostrarAlerta("Error", "Las contraseñas no coinciden.");
                return;
            }
            usuario.setPassword(HashUtils.hashPassword(nuevaPass));
        }

        usuario.setNickname(nuevoNickname);
        usuario.setEmail(nuevoEmail);

        if (imagenSeleccionada != null) {
            usuario.setImagen(imagenSeleccionada);
        }

        boolean actualizado = usuarioDAO.actualizarUsuario(usuario);

        if (actualizado) {
            mostrarAlerta("Datos actualizados", "Tus datos se han guardado correctamente.");
        } else {
            mostrarAlerta("Error", "No se pudo actualizar tu información.");
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    protected void onClickDeleteUser(){
        try {
            Stage stage = (Stage) deleteUserButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("deleteUser.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 500);
            stage.setTitle("Eliminar usuario");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            updateUserTextAdvise.setText("Error al cargar la pantalla de eliminar usuario.");
            e.printStackTrace();
        }
    }
} 
