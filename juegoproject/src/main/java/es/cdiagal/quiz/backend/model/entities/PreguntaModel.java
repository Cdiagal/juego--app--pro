
package es.cdiagal.quiz.backend.model.entities;
import java.util.Objects;


/**
 * Clase que gestiona la logica de las preguntas.
 * @author cdiagal 
 * @version 1.0.0
 */

public class PreguntaModel {
    private int id;
    private String enunciado;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private String respuestaCorrecta;
    private int dificultad;


    /**
     * Constructor vacio.
     */
    public PreguntaModel() {}

    /**
     * Constructor con todos los atributos de la clase.
     * @param id
     * @param enunciado
     * @param opcionA
     * @param opcionB
     * @param opcionC
     * @param opcionD
     * @param respuestaCorrecta
     * @param dificultad
     */
    public PreguntaModel(int id, String enunciado, String opcionA, String opcionB, 
                    String opcionC, String opcionD, String respuestaCorrecta, int dificultad) {
        this.id = id;
        this.enunciado = enunciado;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
        this.dificultad = dificultad;
    }

    /**
     * Constructor con los atributos necesarios para gestionar la BBDD.
     */
    public PreguntaModel(String enunciado, String opcionA, String opcionB, String opcionC, String opcionD, String respuestaCorrecta, int dificultad) {
        this.enunciado = enunciado;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta;
        this.dificultad = dificultad;
    }


    // Getters y setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnunciado() {
        return this.enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getOpcionA() {
        return this.opcionA;
    }

    public void setOpcionA(String opcionA) {
        this.opcionA = opcionA;
    }

    public String getOpcionB() {
        return this.opcionB;
    }

    public void setOpcionB(String opcionB) {
        this.opcionB = opcionB;
    }

    public String getOpcionC() {
        return this.opcionC;
    }

    public void setOpcionC(String opcionC) {
        this.opcionC = opcionC;
    }

    public String getOpcionD() {
        return this.opcionD;
    }

    public void setOpcionD(String opcionD) {
        this.opcionD = opcionD;
    }

    public String getRespuestaCorrecta() {
        return this.respuestaCorrecta;
    }

    public void setRespuestaCorrecta(String respuestaCorrecta) {
        this.respuestaCorrecta = respuestaCorrecta;
    }

    public int getDificultad() {
        return this.dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }
    
    

    /**
     * Metodos equals() y hashCode().
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PreguntaModel)) {
            return false;
        }
        PreguntaModel preguntaModel = (PreguntaModel) o;
        return id == preguntaModel.id ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Metodo toString().
     */
    @Override
    public String toString() {
        return "ID: " + id + " Enunciado: " + enunciado + " Opcion A: " + opcionA + " Opcion B: " + opcionB + 
        " Opcion C: " + opcionC + " Opcion D: " + opcionD + " Respuesta correcta: " + respuestaCorrecta + 
        " Dificultad :" + dificultad;
    }
    
}
