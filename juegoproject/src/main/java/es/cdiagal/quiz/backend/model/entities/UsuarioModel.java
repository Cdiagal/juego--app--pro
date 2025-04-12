
package es.cdiagal.quiz.backend.model.entities;

import java.util.Objects;

public class UsuarioModel {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private int racha;
    private int puntos;
    private int nivel;

    public UsuarioModel() {}

    public UsuarioModel(int id, String nickname, String email, String password, int racha, int puntos, int nivel) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.racha = racha;
        this.puntos = puntos;
        this.nivel = nivel;
    }

    public UsuarioModel(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.racha = 0;
        this.puntos = 0;
        this.nivel = 1;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRacha() {
        return this.racha;
    }

    public void setRacha(int racha) {
        this.racha = racha;
    }

    public int getPuntos() {
        return this.puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getNivel() {
        return this.nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UsuarioModel)) {
            return false;
        }
        UsuarioModel usuarioModel = (UsuarioModel) o;
        return id == usuarioModel.id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }



    @Override
    public String toString() {
        return id + nickname + email + password + racha + puntos;
    }

}
