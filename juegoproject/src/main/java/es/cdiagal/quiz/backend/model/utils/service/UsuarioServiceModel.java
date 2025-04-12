
package es.cdiagal.quiz.backend.model.utils.service;

import es.cdiagal.quiz.backend.dao.UsuarioDAO;
import es.cdiagal.quiz.backend.model.entities.UsuarioModel;

public class UsuarioServiceModel {
    private final UsuarioDAO usuarioDAO;

    public UsuarioServiceModel(String rutaBD) {
        this.usuarioDAO = new UsuarioDAO(rutaBD);
    }

    public UsuarioModel login(String nick, String password) {
        UsuarioModel usuario = usuarioDAO.buscarPorNick(nick);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    public boolean registrar(UsuarioModel usuario) {
        UsuarioModel existente = usuarioDAO.buscarPorNick(usuario.getNickname());
        if (existente == null) {
            return usuarioDAO.insertar(usuario);
        }
        return false;
    }

    public boolean actualizarPuntosYRacha(UsuarioModel usuario) {
        return usuarioDAO.actualizarPuntosYRacha(usuario);
    }

    public void verificarYActualizarNivel(UsuarioModel usuario) {
        int puntos = usuario.getPuntos();
        int nivelActual = usuario.getNivel();
        int nuevoNivel = calcularNivel(puntos);
    
        if (nuevoNivel > nivelActual) {
            usuario.setNivel(nuevoNivel);
            usuarioDAO.actualizarNivel(usuario);
        }
    }
    
    private int calcularNivel(int puntos) {
        if (puntos < 100) return 1;
        else if (puntos < 200) return 2;
        else if (puntos < 300) return 3;
        else return 4;
    }
    
}
