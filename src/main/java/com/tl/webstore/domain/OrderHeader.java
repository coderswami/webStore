package com.tl.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.tl.webstore.domain.enumeration.OrderType;

import com.tl.webstore.domain.enumeration.Status;

/**
 * A OrderHeader.
 */
@Entity
@Table(name = "order_header")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "orderheader")
public class OrderHeader implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OrderType type;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
    
    @NotNull
    @Column(name = "order_total", nullable = false)
    private Double orderTotal;
    
    @Column(name = "cookie")
    private String cookie;
    
    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @OneToMany(mappedBy = "orderHeader")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderItem> items = new HashSet<>();

    @OneToOne
    private Payment payment;

    @OneToOne
    private Shipment shipment;

    @OneToMany(mappedBy = "orderHeader")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Tracking> trackings = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderType getType() {
        return type;
    }
    
    public void setType(OrderType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }
    
    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getCookie() {
        return cookie;
    }
    
    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> orderItems) {
        this.items = orderItems;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public Set<Tracking> getTrackings() {
        return trackings;
    }

    public void setTrackings(Set<Tracking> trackings) {
        this.trackings = trackings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderHeader orderHeader = (OrderHeader) o;
        if(orderHeader.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderHeader.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderHeader{" +
            "id=" + id +
            ", type='" + type + "'" +
            ", status='" + status + "'" +
            ", orderTotal='" + orderTotal + "'" +
            ", cookie='" + cookie + "'" +
            '}';
    }
}
