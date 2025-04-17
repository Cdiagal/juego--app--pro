package es.cdiagal.quiz.backend.model.entities;

import javafx.scene.image.Image;

public class Bandera {
    private final String nombre;
    private final Image icono;

    public Bandera(String nombre, Image icono){
        this.nombre = nombre;
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public Image getIcono() {
        return icono;
    }

    @Override
    public String toString() {
        return "";
    }
}
