
package es.cdiagal.quiz.backend.model.utils.service;

import java.util.List;

import es.cdiagal.quiz.backend.dao.PartidaDAO;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;

public class PartidaServiceModel {
    private final PartidaDAO partidaDAO;

    public PartidaServiceModel(String rutaBD) {
        this.partidaDAO = new PartidaDAO(rutaBD);
    }

    public boolean guardarPartida(PartidaModel partida) {
        return partidaDAO.insertarPartida(partida);
    }

    public List<PartidaModel> obtenerHistorialPartidas(int idUsuario) {
        return partidaDAO.obtenerPartidasPorUsuario(idUsuario);
    }
}
