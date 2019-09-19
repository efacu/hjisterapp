package com.mynegocio.app.service.impl;

import com.mynegocio.app.service.ProvinciaService;
import com.mynegocio.app.domain.Provincia;
import com.mynegocio.app.repository.ProvinciaRepository;
import com.mynegocio.app.repository.search.ProvinciaSearchRepository;
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
 * Service Implementation for managing {@link Provincia}.
 */
@Service
@Transactional
public class ProvinciaServiceImpl implements ProvinciaService {

    private final Logger log = LoggerFactory.getLogger(ProvinciaServiceImpl.class);

    private final ProvinciaRepository provinciaRepository;

    private final ProvinciaSearchRepository provinciaSearchRepository;

    public ProvinciaServiceImpl(ProvinciaRepository provinciaRepository, ProvinciaSearchRepository provinciaSearchRepository) {
        this.provinciaRepository = provinciaRepository;
        this.provinciaSearchRepository = provinciaSearchRepository;
    }

    /**
     * Save a provincia.
     *
     * @param provincia the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Provincia save(Provincia provincia) {
        log.debug("Request to save Provincia : {}", provincia);
        Provincia result = provinciaRepository.save(provincia);
        provinciaSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the provincias.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Provincia> findAll() {
        log.debug("Request to get all Provincias");
        return provinciaRepository.findAll();
    }


    /**
     * Get one provincia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Provincia> findOne(Long id) {
        log.debug("Request to get Provincia : {}", id);
        return provinciaRepository.findById(id);
    }

    /**
     * Delete the provincia by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Provincia : {}", id);
        provinciaRepository.deleteById(id);
        provinciaSearchRepository.deleteById(id);
    }

    /**
     * Search for the provincia corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Provincia> search(String query) {
        log.debug("Request to search Provincias for query {}", query);
        return StreamSupport
            .stream(provinciaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
