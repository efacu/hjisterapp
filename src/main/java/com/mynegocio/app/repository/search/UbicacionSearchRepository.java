package com.mynegocio.app.repository.search;
import com.mynegocio.app.domain.Ubicacion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Ubicacion} entity.
 */
public interface UbicacionSearchRepository extends ElasticsearchRepository<Ubicacion, Long> {
}
