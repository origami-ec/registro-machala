package org.origami.ws.repository.origami;

import org.origami.ws.entities.origami.CatalogoItem;
import org.origami.ws.entities.origami.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface DepartamentoRepository
        extends JpaRepository<Departamento, Long>, PagingAndSortingRepository<Departamento, Long> {

    List<Departamento> findByCodigoAndEstado(String nombre, Boolean estado);

    @Query(value = "select c from CatalogoItem c  inner join c.catalogo cc where cc.nombre=:nombre and c.estado=:estado")
    List<CatalogoItem> findByTiposUnidades(String nombre, String estado);

    @Query("SELECT d FROM Departamento d ORDER BY d.nombre asc")
    List<Departamento> getDepartamentoPadre();

    @Query("SELECT d FROM Departamento d  ORDER BY d.nombre asc")
    List<Departamento> getDeartamentoHijos();

    @Query("SELECT d FROM Departamento d WHERE d.estado=true ORDER BY d.nombre asc")
    List<Departamento> getDeartamentoHijosByEstado();

    @Query(value = "SELECT new org.origami.ws.entities.origami.Departamento(d.id, d.nombre, d.estado, d.codigo) FROM Departamento d ORDER BY d.nombre ASC")
    List<Departamento> findAllDepartamentos();

    @Query(value = "SELECT DISTINCT new org.origami.ws.entities.origami.Departamento(d.id, d.nombre,  d.estado, d.codigo) FROM RolUsuario ru JOIN ru.rol r JOIN r.departamento d WHERE d.estado= true ORDER BY d.id ASC")
    List<Departamento> findAllDepartamentosWithUser();

    @Query(value = "SELECT DISTINCT s.departamento FROM ServiciosDepartamento s WHERE s.estado = true ORDER BY s.departamento.nombre")
    List<Departamento> findAllCustomDepartamentoByServicioDepartamento();


    Optional<Departamento> findByIdBcbgAndTipo(Long idBcbg, Long tipo);

}
