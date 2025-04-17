package es.cdiagal.quiz.backend.controller;

import es.cdiagal.quiz.backend.model.entities.UsuarioModel;

public class UserDataController {
    private UsuarioModel usuario;

    public void setUsuario(UsuarioModel usuario){
        this.usuario = usuario;
        //usuarioData();
    }
    /**
    public void usuarioData(){
        if(usuario != null){
            userDataUserTextField.setText(usuario.getUsuarioNickName());
            userDataEmailTextField.setText(usuario.getEmail());
            userDataLevelTextField.setText(usuario.getNivel());
        }
        }
        */
}
