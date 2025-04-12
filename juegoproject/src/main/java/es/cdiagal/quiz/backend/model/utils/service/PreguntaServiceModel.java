
package es.cdiagal.quiz.backend.model.utils.service;

import java.util.List;

import es.cdiagal.quiz.backend.dao.PreguntaDAO;
import es.cdiagal.quiz.backend.model.entities.PreguntaModel;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;

public class PreguntaServiceModel {
    private final PreguntaDAO preguntaDAO;

    public PreguntaServiceModel(String rutaBD) {
        this.preguntaDAO = new PreguntaDAO(rutaBD);
    }

    public List<PreguntaModel> obtenerPreguntasParaUsuario(UsuarioModel usuario, int cantidad) {
        int dificultad = calcularDificultadDesdeUsuario(usuario);
        return preguntaDAO.obtenerPreguntasPorDificultad(dificultad, cantidad);
    }

    private int calcularDificultadDesdeUsuario(UsuarioModel usuario) {
        int puntos = usuario.getPuntos();

        if (puntos < 100) return 1;
        else if (puntos < 200) return 2;
        else if (puntos < 300) return 3;
        else return 4;
    }
}
