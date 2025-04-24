
package es.cdiagal.quiz.backend.model.entities;

import java.util.List;
import java.util.Objects;

/**
 * Clase que controla toda la logica de Usuario.
 */
public class UsuarioModel {
    private int id;
    private String nickname;
    private String email;
    private String password;
    private int racha;
    private int puntos;
    private int nivel;
    private byte[] imagen;
    private int rachaCorrectasSeguidas; 

    /**
     * Constructor vacio de la clase.
     */
    public UsuarioModel() {}


    /**
     * Constructor con todas las propiedades de la clase.
     * @param id
     * @param nickname
     * @param email
     * @param password
     * @param racha
     * @param puntos
     * @param nivel
     */
    public UsuarioModel(int id, String nickname, String email, String password, int racha, int puntos, int nivel) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.racha = racha;
        this.puntos = puntos;
        this.nivel = nivel;
    }

    /**
     * Constructor que contiene los atributos de la clase para gestionar 
     * @param nickname
     * @param email
     * @param password
     */
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

    public byte[] getImagen() {
        return this.imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public int getRachaCorrectasSeguidas() {
        return rachaCorrectasSeguidas;
    }

    public void setRachaCorrectasSeguidas(int rachaCorrectasSeguidas) {
        this.rachaCorrectasSeguidas = rachaCorrectasSeguidas;
    }
    
    /**
     * Metodo que incrementa puntos por cada acierto +1.
     */
    public void incrementarPuntosPorAcierto() {
        this.puntos += 1;
        this.rachaCorrectasSeguidas++;
    
        // Bonus de +2 puntos cada 5 respuestas correctas consecutivas
        if (this.rachaCorrectasSeguidas % 5 == 0) {
            this.puntos += 2;
        }
    }
    

    /**
     * Reinciia la racha cuando el ususario falla.
     */
    public void reiniciarRachaCorrectasSeguidas() {
        this.rachaCorrectasSeguidas = 0;
    }

    /**
     * Actualiza el nivel del usuario en funcion de los puntos que tenga.
     */
    public void actualizarNivelPorPuntos() {
        if (this.puntos <= 100) {
            this.nivel = 1; // Fácil
        } else if (this.puntos <= 500) {
            this.nivel = 2; // Medio
        } else if (this.puntos <= 1000) {
            this.nivel = 3; // Difícil
        }
    }
    

    /**
     * Gestiona la posicion del usuario en el ranking por puntos.
     */
    public int getPosicionRanking(List<UsuarioModel> usuarios) {
        usuarios.sort((u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == this.id) {
                return i + 1;
            }
        }
        return -1;
    }

    
    public void incrementarRachaCorrectasSeguidas() {
        this.rachaCorrectasSeguidas++;
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
