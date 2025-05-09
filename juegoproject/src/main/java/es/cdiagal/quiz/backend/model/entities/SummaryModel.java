package es.cdiagal.quiz.backend.model.entities;

import java.util.Objects;
/**
 * Clase que gestiona toda la logica del resumen de las partidas jugadas.
 * @author cdiagal
 * @version 1.0.0
 */
public class SummaryModel {
    
    private int totalPuntos;
    private int partidasJugadas;
    private int mejorPuntuacion;
    private int nivel;
    private double progresoSiguienteNivel; // Valor entre 0.0 y 1.0

    /**
     * Constructor vacio 
     */
    public SummaryModel() {
    }

    /**
     * Constructor con todas las propiedades de la clase.
     * @param totalPuntos
     * @param partidasJugadas
     * @param mejorPuntuacion
     * @param nivel
     * @param progresoSiguienteNivel
     */
    public SummaryModel(int totalPuntos, int partidasJugadas, int mejorPuntuacion, int nivel, double progresoSiguienteNivel) {
        this.totalPuntos = totalPuntos;
        this.partidasJugadas = partidasJugadas;
        this.mejorPuntuacion = mejorPuntuacion;
        this.nivel = nivel;
        this.progresoSiguienteNivel = progresoSiguienteNivel;
    }

    // Getters y setters

    public int getTotalPuntos() {
        return totalPuntos;
    }

    public void setTotalPuntos(int totalPuntos) {
        this.totalPuntos = totalPuntos;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public int getMejorPuntuacion() {
        return mejorPuntuacion;
    }

    public void setMejorPuntuacion(int mejorPuntuacion) {
        this.mejorPuntuacion = mejorPuntuacion;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public double getProgresoSiguienteNivel() {
        return progresoSiguienteNivel;
    }

    public void setProgresoSiguienteNivel(double progresoSiguienteNivel) {
        this.progresoSiguienteNivel = progresoSiguienteNivel;
    }


    /**
     * Metodo toString().
     */
    @Override
    public String toString() {
        return "Resumen{" +
                "totalPuntos=" + totalPuntos +
                ", partidasJugadas=" + partidasJugadas +
                ", mejorPuntuacion=" + mejorPuntuacion +
                ", nivel=" + nivel +
                ", progresoSiguienteNivel=" + progresoSiguienteNivel +
                '}';
    }

    /**
     * Metodos equals() y hashCode().
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SummaryModel)) {
            return false;
        }
        SummaryModel summaryModel = (SummaryModel) o;
        return totalPuntos == summaryModel.totalPuntos && partidasJugadas == summaryModel.partidasJugadas && mejorPuntuacion == summaryModel.mejorPuntuacion && nivel == summaryModel.nivel && progresoSiguienteNivel == summaryModel.progresoSiguienteNivel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPuntos, partidasJugadas, mejorPuntuacion, nivel, progresoSiguienteNivel);
    }
    
}
