package org.origami.zull.repository;


import org.origami.zull.entity.EquipoAutorizado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoAutorizadoRepository extends JpaRepository<EquipoAutorizado, Integer> {

    List<EquipoAutorizado> findAllByExcluir(int excluir);

}
