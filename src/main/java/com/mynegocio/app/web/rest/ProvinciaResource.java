package com.mynegocio.app.web.rest;

import com.mynegocio.app.domain.Provincia;
import com.mynegocio.app.service.ProvinciaService;
import com.mynegocio.app.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mynegocio.app.domain.Provincia}.
 */
@RestController
@RequestMapping("/api")
public class ProvinciaResource {

    private final Logger log = LoggerFactory.getLogger(ProvinciaResource.class);

    private static final String ENTITY_NAME = "provincia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProvinciaService provinciaService;

    public ProvinciaResource(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }

    /**
     * {@code POST  /provincias} : Create a new provincia.
     *
     * @param provincia the provincia to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provincia, or with status {@code 400 (Bad Request)} if the provincia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/provincias")
    public ResponseEntity<Provincia> createProvincia(@RequestBody Provincia provincia) throws URISyntaxException {
        log.debug("REST request to save Provincia : {}", provincia);
        if (provincia.getId() != null) {
            throw new BadRequestAlertException("A new provincia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Provincia result = provinciaService.save(provincia);
        return ResponseEntity.created(new URI("/api/provincias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /provincias} : Updates an existing provincia.
     *
     * @param provincia the provincia to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provincia,
     * or with status {@code 400 (Bad Request)} if the provincia is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provincia couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/provincias")
    public ResponseEntity<Provincia> updateProvincia(@RequestBody Provincia provincia) throws URISyntaxException {
        log.debug("REST request to update Provincia : {}", provincia);
        if (provincia.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Provincia result = provinciaService.save(provincia);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provincia.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /provincias} : get all the provincias.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of provincias in body.
     */
    @GetMapping("/provincias")
    public List<Provincia> getAllProvincias() {
        log.debug("REST request to get all Provincias");
        return provinciaService.findAll();
    }

    /**
     * {@code GET  /provincias/:id} : get the "id" provincia.
     *
     * @param id the id of the provincia to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provincia, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/provincias/{id}")
    public ResponseEntity<Provincia> getProvincia(@PathVariable Long id) {
        log.debug("REST request to get Provincia : {}", id);
        Optional<Provincia> provincia = provinciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(provincia);
    }

    /**
     * {@code DELETE  /provincias/:id} : delete the "id" provincia.
     *
     * @param id the id of the provincia to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/provincias/{id}")
    public ResponseEntity<Void> deleteProvincia(@PathVariable Long id) {
        log.debug("REST request to delete Provincia : {}", id);
        provinciaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/provincias?query=:query} : search for the provincia corresponding
     * to the query.
     *
     * @param query the query of the provincia search.
     * @return the result of the search.
     */
    @GetMapping("/_search/provincias")
    public List<Provincia> searchProvincias(@RequestParam String query) {
        log.debug("REST request to search Provincias for query {}", query);
        return provinciaService.search(query);
    }

}
