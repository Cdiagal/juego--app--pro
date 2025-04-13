
package es.cdiagal.quiz.backend.model.utils.service;

import java.util.List;

import es.cdiagal.quiz.backend.dao.PartidaDAO;
import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.PartidaModel;
import es.cdiagal.quiz.backend.model.entities.SummaryModel;

public class PartidaServiceModel {
    private final PartidaDAO partidaDAO;
    private final UsuarioDAO usuarioDAO;

    public PartidaServiceModel(String rutaDB){
        this.partidaDAO = new PartidaDAO(rutaDB);
        this.usuarioDAO = new UsuarioDAO(rutaDB);
    }


    public boolean guardarPartida(PartidaModel partida) {
        return partidaDAO.insertarPartida(partida);
    }

    public boolean actualizarPartida(PartidaModel partida) {
        return partidaDAO.actualizarPartida(partida);
    }

    public boolean eliminarPartida(int idPartida) {
        return partidaDAO.eliminarPartida(idPartida);
    }

    public List<PartidaModel> obtenerHistorialPartidas(int idUsuario) {
        return partidaDAO.obtenerPartidasPorUsuario(idUsuario);
    }

    public PartidaModel obtenerPartidaActiva(int idUsuario) {
        return partidaDAO.obtenerPartidaActiva(idUsuario);
    }

    public SummaryModel calcularResumenUsuario(int idUsuario) {
        int totalPuntos = partidaDAO.sumarPuntos(idUsuario);
        int partidasJugadas = partidaDAO.contarPartidas(idUsuario);
        int mejorPuntuacion = partidaDAO.obtenerMejorPuntuacion(idUsuario);
        int nivel = usuarioDAO.obtenerNivel(idUsuario);

        
        int puntosParaSiguienteNivel = 100 + (nivel * 100);
        double progreso = Math.min(1.0, (double) totalPuntos / puntosParaSiguienteNivel);

        return new SummaryModel(totalPuntos, partidasJugadas, mejorPuntuacion, nivel, progreso);
    }

}
