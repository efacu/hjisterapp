package com.mynegocio.app.web.rest;

import com.mynegocio.app.Hjisterapp1App;
import com.mynegocio.app.domain.Ubicacion;
import com.mynegocio.app.repository.UbicacionRepository;
import com.mynegocio.app.repository.search.UbicacionSearchRepository;
import com.mynegocio.app.service.UbicacionService;
import com.mynegocio.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.mynegocio.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link UbicacionResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = Hjisterapp1App.class)
public class UbicacionResourceIT {

    private static final String DEFAULT_CALLE = "AAAAAAAAAA";
    private static final String UPDATED_CALLE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_POSTAL = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_POSTAL = "BBBBBBBBBB";

    private static final String DEFAULT_CIUDAD = "AAAAAAAAAA";
    private static final String UPDATED_CIUDAD = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private UbicacionService ubicacionService;

    /**
     * This repository is mocked in the com.mynegocio.app.repository.search test package.
     *
     * @see com.mynegocio.app.repository.search.UbicacionSearchRepositoryMockConfiguration
     */
    @Autowired
    private UbicacionSearchRepository mockUbicacionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restUbicacionMockMvc;

    private Ubicacion ubicacion;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UbicacionResource ubicacionResource = new UbicacionResource(ubicacionService);
        this.restUbicacionMockMvc = MockMvcBuilders.standaloneSetup(ubicacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubicacion createEntity(EntityManager em) {
        Ubicacion ubicacion = new Ubicacion()
            .calle(DEFAULT_CALLE)
            .codigoPostal(DEFAULT_CODIGO_POSTAL)
            .ciudad(DEFAULT_CIUDAD)
            .stateProvince(DEFAULT_STATE_PROVINCE);
        return ubicacion;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ubicacion createUpdatedEntity(EntityManager em) {
        Ubicacion ubicacion = new Ubicacion()
            .calle(UPDATED_CALLE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .ciudad(UPDATED_CIUDAD)
            .stateProvince(UPDATED_STATE_PROVINCE);
        return ubicacion;
    }

    @BeforeEach
    public void initTest() {
        ubicacion = createEntity(em);
    }

    @Test
    @Transactional
    public void createUbicacion() throws Exception {
        int databaseSizeBeforeCreate = ubicacionRepository.findAll().size();

        // Create the Ubicacion
        restUbicacionMockMvc.perform(post("/api/ubicacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isCreated());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeCreate + 1);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getCalle()).isEqualTo(DEFAULT_CALLE);
        assertThat(testUbicacion.getCodigoPostal()).isEqualTo(DEFAULT_CODIGO_POSTAL);
        assertThat(testUbicacion.getCiudad()).isEqualTo(DEFAULT_CIUDAD);
        assertThat(testUbicacion.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);

        // Validate the Ubicacion in Elasticsearch
        verify(mockUbicacionSearchRepository, times(1)).save(testUbicacion);
    }

    @Test
    @Transactional
    public void createUbicacionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ubicacionRepository.findAll().size();

        // Create the Ubicacion with an existing ID
        ubicacion.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUbicacionMockMvc.perform(post("/api/ubicacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ubicacion in Elasticsearch
        verify(mockUbicacionSearchRepository, times(0)).save(ubicacion);
    }


    @Test
    @Transactional
    public void getAllUbicacions() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        // Get all the ubicacionList
        restUbicacionMockMvc.perform(get("/api/ubicacions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ubicacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE.toString())))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL.toString())))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD.toString())))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())));
    }
    
    @Test
    @Transactional
    public void getUbicacion() throws Exception {
        // Initialize the database
        ubicacionRepository.saveAndFlush(ubicacion);

        // Get the ubicacion
        restUbicacionMockMvc.perform(get("/api/ubicacions/{id}", ubicacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ubicacion.getId().intValue()))
            .andExpect(jsonPath("$.calle").value(DEFAULT_CALLE.toString()))
            .andExpect(jsonPath("$.codigoPostal").value(DEFAULT_CODIGO_POSTAL.toString()))
            .andExpect(jsonPath("$.ciudad").value(DEFAULT_CIUDAD.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUbicacion() throws Exception {
        // Get the ubicacion
        restUbicacionMockMvc.perform(get("/api/ubicacions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUbicacion() throws Exception {
        // Initialize the database
        ubicacionService.save(ubicacion);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockUbicacionSearchRepository);

        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // Update the ubicacion
        Ubicacion updatedUbicacion = ubicacionRepository.findById(ubicacion.getId()).get();
        // Disconnect from session so that the updates on updatedUbicacion are not directly saved in db
        em.detach(updatedUbicacion);
        updatedUbicacion
            .calle(UPDATED_CALLE)
            .codigoPostal(UPDATED_CODIGO_POSTAL)
            .ciudad(UPDATED_CIUDAD)
            .stateProvince(UPDATED_STATE_PROVINCE);

        restUbicacionMockMvc.perform(put("/api/ubicacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUbicacion)))
            .andExpect(status().isOk());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);
        Ubicacion testUbicacion = ubicacionList.get(ubicacionList.size() - 1);
        assertThat(testUbicacion.getCalle()).isEqualTo(UPDATED_CALLE);
        assertThat(testUbicacion.getCodigoPostal()).isEqualTo(UPDATED_CODIGO_POSTAL);
        assertThat(testUbicacion.getCiudad()).isEqualTo(UPDATED_CIUDAD);
        assertThat(testUbicacion.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);

        // Validate the Ubicacion in Elasticsearch
        verify(mockUbicacionSearchRepository, times(1)).save(testUbicacion);
    }

    @Test
    @Transactional
    public void updateNonExistingUbicacion() throws Exception {
        int databaseSizeBeforeUpdate = ubicacionRepository.findAll().size();

        // Create the Ubicacion

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUbicacionMockMvc.perform(put("/api/ubicacions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ubicacion)))
            .andExpect(status().isBadRequest());

        // Validate the Ubicacion in the database
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ubicacion in Elasticsearch
        verify(mockUbicacionSearchRepository, times(0)).save(ubicacion);
    }

    @Test
    @Transactional
    public void deleteUbicacion() throws Exception {
        // Initialize the database
        ubicacionService.save(ubicacion);

        int databaseSizeBeforeDelete = ubicacionRepository.findAll().size();

        // Delete the ubicacion
        restUbicacionMockMvc.perform(delete("/api/ubicacions/{id}", ubicacion.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ubicacion> ubicacionList = ubicacionRepository.findAll();
        assertThat(ubicacionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ubicacion in Elasticsearch
        verify(mockUbicacionSearchRepository, times(1)).deleteById(ubicacion.getId());
    }

    @Test
    @Transactional
    public void searchUbicacion() throws Exception {
        // Initialize the database
        ubicacionService.save(ubicacion);
        when(mockUbicacionSearchRepository.search(queryStringQuery("id:" + ubicacion.getId())))
            .thenReturn(Collections.singletonList(ubicacion));
        // Search the ubicacion
        restUbicacionMockMvc.perform(get("/api/_search/ubicacions?query=id:" + ubicacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ubicacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].calle").value(hasItem(DEFAULT_CALLE)))
            .andExpect(jsonPath("$.[*].codigoPostal").value(hasItem(DEFAULT_CODIGO_POSTAL)))
            .andExpect(jsonPath("$.[*].ciudad").value(hasItem(DEFAULT_CIUDAD)))
            .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ubicacion.class);
        Ubicacion ubicacion1 = new Ubicacion();
        ubicacion1.setId(1L);
        Ubicacion ubicacion2 = new Ubicacion();
        ubicacion2.setId(ubicacion1.getId());
        assertThat(ubicacion1).isEqualTo(ubicacion2);
        ubicacion2.setId(2L);
        assertThat(ubicacion1).isNotEqualTo(ubicacion2);
        ubicacion1.setId(null);
        assertThat(ubicacion1).isNotEqualTo(ubicacion2);
    }
}
