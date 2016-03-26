package com.tl.webstore.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.tl.webstore.domain.enumeration.ShipmentType;

import com.tl.webstore.domain.enumeration.Status;

/**
 * A Shipment.
 */
@Entity
@Table(name = "shipment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "shipment")
public class Shipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ShipmentType type;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    
    @OneToOne
    private UserAddress address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShipmentType getType() {
        return type;
    }
    
    public void setType(ShipmentType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public UserAddress getAddress() {
        return address;
    }

    public void setAddress(UserAddress userAddress) {
        this.address = userAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Shipment shipment = (Shipment) o;
        if(shipment.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, shipment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
