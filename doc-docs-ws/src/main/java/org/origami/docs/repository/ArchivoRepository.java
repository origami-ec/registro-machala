package org.origami.docs.repository;

import org.origami.docs.entity.Archivo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface ArchivoRepository extends MongoRepository<Archivo, String> {

    @Query("{ 'numTramite' : ?0, 'tipoIndexacion' : ?1, 'detalleDocumento' : { $regex : ?2 } ,  'detalles.detalle': { $regex: ?3 } }")
    List<Archivo> busquedaByIndexacion(String numTramite, String tipoIndex, String detDocumento, String detalleIndex);


    @Query("{ 'numTramite' : ?0, 'tipoIndexacion' : ?1, 'detalleDocumento' : { $regex : ?2 } }")
    List<Archivo> busquedaByIndexacion(String numTramite, String tipoIndex, String detDocumento);

    @Query("{ 'numTramite' : ?0, 'tipoIndexacion' : ?1}")
    List<Archivo> busquedaByIndexacion(String numTramite, String tipoIndex);

    @Query("{ 'numTramite' : ?0}")
    List<Archivo> busquedaByIndexacion(String numTramite);

    @Query("{ 'tipoIndexacion' : ?0}")
    List<Archivo> busquedaByTipoIndexacion(String tipoIndexacion);

    @Query("{ 'tipoIndexacion' : ?0, 'detalleDocumento' : { $regex : ?1 }}")
    List<Archivo> busquedaByTipoIndexacion(String tipoIndexacion, String detalleDocumento);

    @Query("{ 'tipoIndexacion' : ?0, 'detalleDocumento' : { $regex : ?1 } ,  'detalles.detalle': { $regex: ?2 } }")
    List<Archivo> busquedaByTipoIndexacion(String tipoIndexacion, String detalleDocumento, String detalleIndex);

    //@Query("{ 'tipoIndexacion' : ?0,  'detalles.detalle' : { $regex: ?1 } }")
    @Query("{ 'tipoIndexacion' : ?0,  'detalles.detalle' : ?1 }")
    List<Archivo> busquedaByTipoIndexacionDets(String tipoIndexacion, String detalleIndex);

    @Query("{ 'numTramite' : ?0, 'tipoIndexacion' : ?1,  'detalles.detalle': { $regex: ?2 } }")
    List<Archivo> busquedaByTramiteTipoIndexacionDets(String numTramite, String tipoIndexacion, String detalleIndex);

    List<Archivo> findAllByTipoIndexacion(String tipoIndex);
    
    Archivo findTopByNumTramiteOrderByFechaCreacionDesc(String numTramite);


}
