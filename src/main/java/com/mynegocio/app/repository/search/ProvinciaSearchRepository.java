package com.mynegocio.app.repository.search;
import com.mynegocio.app.domain.Provincia;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Provincia} entity.
 */
public interface ProvinciaSearchRepository extends ElasticsearchRepository<Provincia, Long> {
}
