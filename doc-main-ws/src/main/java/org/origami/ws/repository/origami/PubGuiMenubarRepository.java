package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Menubar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PubGuiMenubarRepository extends JpaRepository<Menubar, Integer>, PagingAndSortingRepository<Menubar, Integer> {

}
