package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PersonaRepository extends JpaRepository<Persona, Long>, PagingAndSortingRepository<Persona, Long> {
    Persona findTopByIdentificacionOrderByIdDesc(String ciRuc);
}
