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

import com.tl.webstore.domain.enumeration.Status;

/**
 * A Tracking.
 */
@Entity
@Table(name = "tracking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "tracking")
public class Tracking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    
    @Column(name = "details")
    private String details;
    
    @ManyToOne
    @JoinColumn(name = "order_header_id")
    private OrderHeader orderHeader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tracking tracking = (Tracking) o;
        if(tracking.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tracking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tracking{" +
            "id=" + id +
            ", status='" + status + "'" +
            ", details='" + details + "'" +
            '}';
    }
}
