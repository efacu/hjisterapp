package com.mynegocio.app.service.impl;

import com.mynegocio.app.service.UbicacionService;
import com.mynegocio.app.domain.Ubicacion;
import com.mynegocio.app.repository.UbicacionRepository;
import com.mynegocio.app.repository.search.UbicacionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Ubicacion}.
 */
@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final Logger log = LoggerFactory.getLogger(UbicacionServiceImpl.class);

    private final UbicacionRepository ubicacionRepository;

    private final UbicacionSearchRepository ubicacionSearchRepository;

    public UbicacionServiceImpl(UbicacionRepository ubicacionRepository, UbicacionSearchRepository ubicacionSearchRepository) {
        this.ubicacionRepository = ubicacionRepository;
        this.ubicacionSearchRepository = ubicacionSearchRepository;
    }

    /**
     * Save a ubicacion.
     *
     * @param ubicacion the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Ubicacion save(Ubicacion ubicacion) {
        log.debug("Request to save Ubicacion : {}", ubicacion);
        Ubicacion result = ubicacionRepository.save(ubicacion);
        ubicacionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ubicacions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ubicacion> findAll() {
        log.debug("Request to get all Ubicacions");
        return ubicacionRepository.findAll();
    }


    /**
     * Get one ubicacion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ubicacion> findOne(Long id) {
        log.debug("Request to get Ubicacion : {}", id);
        return ubicacionRepository.findById(id);
    }

    /**
     * Delete the ubicacion by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ubicacion : {}", id);
        ubicacionRepository.deleteById(id);
        ubicacionSearchRepository.deleteById(id);
    }

    /**
     * Search for the ubicacion corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ubicacion> search(String query) {
        log.debug("Request to search Ubicacions for query {}", query);
        return StreamSupport
            .stream(ubicacionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
