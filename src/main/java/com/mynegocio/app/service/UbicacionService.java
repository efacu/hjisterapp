package com.mynegocio.app.service;

import com.mynegocio.app.domain.Ubicacion;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Ubicacion}.
 */
public interface UbicacionService {

    /**
     * Save a ubicacion.
     *
     * @param ubicacion the entity to save.
     * @return the persisted entity.
     */
    Ubicacion save(Ubicacion ubicacion);

    /**
     * Get all the ubicacions.
     *
     * @return the list of entities.
     */
    List<Ubicacion> findAll();


    /**
     * Get the "id" ubicacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ubicacion> findOne(Long id);

    /**
     * Delete the "id" ubicacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the ubicacion corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Ubicacion> search(String query);
}
