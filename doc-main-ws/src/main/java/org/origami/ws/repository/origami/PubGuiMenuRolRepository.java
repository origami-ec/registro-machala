package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.MenuRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PubGuiMenuRolRepository extends JpaRepository<MenuRol, Long>, PagingAndSortingRepository<MenuRol, Long> {
    List<MenuRol> findAllByMenu_Id(Integer idMenu);
}
