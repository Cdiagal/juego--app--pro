
package es.cdiagal.quiz.backend.model.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class PartidaModel {
    private int id;
    private int idUsuario;
    private int puntuacion;
    private int aciertos;
    private int errores;
    private int dificultad;
    private LocalDateTime fecha;

    public PartidaModel() {}

    public PartidaModel(int id, int idUsuario, int puntuacion, int aciertos, int errores, int dificultad, LocalDateTime fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
        this.aciertos = aciertos;
        this.errores = errores;
        this.dificultad = dificultad;
        this.fecha = fecha;
    }

    public PartidaModel(int idUsuario, int puntuacion, int aciertos, int errores, int dificultad, LocalDateTime fecha) {
        this.idUsuario = idUsuario;
        this.puntuacion = puntuacion;
        this.aciertos = aciertos;
        this.errores = errores;
        this.dificultad = dificultad;
        this.fecha = fecha;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getPuntuacion() {
        return this.puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public int getAciertos() {
        return this.aciertos;
    }

    public void setAciertos(int aciertos) {
        this.aciertos = aciertos;
    }

    public int getErrores() {
        return this.errores;
    }

    public void setErrores(int errores) {
        this.errores = errores;
    }

    public int getDificultad() {
        return this.dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PartidaModel)) {
            return false;
        }
        PartidaModel partidaModel = (PartidaModel) o;
        return id == partidaModel.id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    

    @Override
    public String toString() {
        return id + idUsuario + puntuacion + aciertos + errores + dificultad + fecha.toString();
    }

}
