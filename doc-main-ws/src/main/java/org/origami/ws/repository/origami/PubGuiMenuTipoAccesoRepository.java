package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.MenuTipoAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PubGuiMenuTipoAccesoRepository extends JpaRepository<MenuTipoAcceso, Integer>, PagingAndSortingRepository<MenuTipoAcceso, Integer> {
}
