package com.mynegocio.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Provincia.
 */
@Entity
@Table(name = "provincia")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "provincia")
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "nombre_provincia")
    private String nombreProvincia;

    @ManyToOne
    @JsonIgnoreProperties("provincias")
    private Country country;

    @OneToMany(mappedBy = "provincia")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Ubicacion> nombreProvincias = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public Provincia nombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
        return this;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    public Country getCountry() {
        return country;
    }

    public Provincia country(Country country) {
        this.country = country;
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<Ubicacion> getNombreProvincias() {
        return nombreProvincias;
    }

    public Provincia nombreProvincias(Set<Ubicacion> ubicacions) {
        this.nombreProvincias = ubicacions;
        return this;
    }

    public Provincia addNombreProvincia(Ubicacion ubicacion) {
        this.nombreProvincias.add(ubicacion);
        ubicacion.setProvincia(this);
        return this;
    }

    public Provincia removeNombreProvincia(Ubicacion ubicacion) {
        this.nombreProvincias.remove(ubicacion);
        ubicacion.setProvincia(null);
        return this;
    }

    public void setNombreProvincias(Set<Ubicacion> ubicacions) {
        this.nombreProvincias = ubicacions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Provincia)) {
            return false;
        }
        return id != null && id.equals(((Provincia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Provincia{" +
            "id=" + getId() +
            ", nombreProvincia='" + getNombreProvincia() + "'" +
            "}";
    }
}
