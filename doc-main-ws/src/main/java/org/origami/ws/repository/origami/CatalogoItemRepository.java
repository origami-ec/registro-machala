package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Catalogo;
import org.origami.ws.entities.origami.CatalogoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CatalogoItemRepository   extends JpaRepository<CatalogoItem, Long>, PagingAndSortingRepository<CatalogoItem, Long> {

    @Query("select c from Catalogo c where c.nombre=:code")
    List<Catalogo> findBusquedaCatalogo(String code);

    List<CatalogoItem> findByCatalogo_Nombre(String code);

}
