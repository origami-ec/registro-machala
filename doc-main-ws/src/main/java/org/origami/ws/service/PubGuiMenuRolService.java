package org.origami.ws.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.origami.ws.entities.origami.MenuRol;
import org.origami.ws.repository.origami.PubGuiMenuRolRepository;
import org.origami.ws.dto.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PubGuiMenuRolService {

    @Autowired
    private PubGuiMenuRolRepository repository;

    public MenuRol find(MenuRol data) {
        return repository.findOne(Example.of(data)).get();
    }

    public List<MenuRol> findAll(MenuRol data) {
        List<MenuRol> menuRols = repository.findAll(Example.of(data));
        menuRols.listIterator().forEachRemaining(this::fromJsonAcciones);
        return menuRols;
    }

    public void fromJsonAcciones(MenuRol menuRol) {
        if (menuRol.getAcciones() != null && !menuRol.getAcciones().isEmpty()) {
            Gson gson = new Gson();
            menuRol.setDocuments(gson.fromJson(menuRol.getAcciones(), new TypeToken<List<Document>>() {
            }.getType()));
            if (!menuRol.getDocuments().isEmpty()) {
                for (Document d : menuRol.getDocuments()) {
                    if (d.getEnable() != null && d.getEnable()) {
                        menuRol.setPermiso(d.getType());
                    }
                }
            }
        }
    }

}
