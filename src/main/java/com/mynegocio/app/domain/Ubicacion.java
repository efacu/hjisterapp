package com.mynegocio.app.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "ubicacion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "ubicacion")
public class Ubicacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "calle")
    private String calle;

    @Column(name = "codigo_postal")
    private String codigoPostal;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "state_province")
    private String stateProvince;

    @ManyToOne
    @JsonIgnoreProperties("ubicacions")
    private Provincia provincia;

    @OneToOne
    @JoinColumn(unique = true)
    private Department nombreUbicacion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCalle() {
        return calle;
    }

    public Ubicacion calle(String calle) {
        this.calle = calle;
        return this;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public Ubicacion codigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
        return this;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public Ubicacion ciudad(String ciudad) {
        this.ciudad = ciudad;
        return this;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public Ubicacion stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public Ubicacion provincia(Provincia provincia) {
        this.provincia = provincia;
        return this;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Department getNombreUbicacion() {
        return nombreUbicacion;
    }

    public Ubicacion nombreUbicacion(Department department) {
        this.nombreUbicacion = department;
        return this;
    }

    public void setNombreUbicacion(Department department) {
        this.nombreUbicacion = department;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ubicacion)) {
            return false;
        }
        return id != null && id.equals(((Ubicacion) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ubicacion{" +
            "id=" + getId() +
            ", calle='" + getCalle() + "'" +
            ", codigoPostal='" + getCodigoPostal() + "'" +
            ", ciudad='" + getCiudad() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            "}";
    }
}
