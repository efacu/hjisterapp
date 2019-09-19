package com.mynegocio.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ProvinciaSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ProvinciaSearchRepositoryMockConfiguration {

    @MockBean
    private ProvinciaSearchRepository mockProvinciaSearchRepository;

}
