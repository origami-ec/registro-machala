package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PubGuiMenuRepository extends JpaRepository<Menu, Integer>, PagingAndSortingRepository<Menu, Integer> {

    List<Menu> findAllByMenuPadreIsNull();

    List<Menu> findAllByMenuPadreIsNullOrderByNumPosicion();

}
