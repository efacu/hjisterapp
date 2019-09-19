package com.mynegocio.app.service;

import com.mynegocio.app.domain.Provincia;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Provincia}.
 */
public interface ProvinciaService {

    /**
     * Save a provincia.
     *
     * @param provincia the entity to save.
     * @return the persisted entity.
     */
    Provincia save(Provincia provincia);

    /**
     * Get all the provincias.
     *
     * @return the list of entities.
     */
    List<Provincia> findAll();


    /**
     * Get the "id" provincia.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Provincia> findOne(Long id);

    /**
     * Delete the "id" provincia.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the provincia corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @return the list of entities.
     */
    List<Provincia> search(String query);
}
