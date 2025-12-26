package org.origami.ws.models;

import org.origami.ws.entities.origami.TareasActivas;
import org.origami.ws.entities.origami.TareasHistoricas;
import org.origami.ws.entities.security.Usuario;

import java.util.List;

public class UsuarioTareasHistoricas {
    private Usuario usuario;
    private List<TareasHistoricas> tareasHis;
    private List<TareasActivas> tareasAct;

    public UsuarioTareasHistoricas() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<TareasHistoricas> getTareasHis() {
        return tareasHis;
    }

    public void setTareasHis(List<TareasHistoricas> tareasHis) {
        this.tareasHis = tareasHis;
    }

    public List<TareasActivas> getTareasAct() {
        return tareasAct;
    }

    public void setTareasAct(List<TareasActivas> tareasAct) {
        this.tareasAct = tareasAct;
    }
}
