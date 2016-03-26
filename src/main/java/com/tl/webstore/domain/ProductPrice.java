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

/**
 * A ProductPrice.
 */
@Entity
@Table(name = "product_price")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productprice")
public class ProductPrice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "list_price", nullable = false)
    private Double listPrice;
    
    @Column(name = "discount")
    private Double discount;
    
    @Column(name = "sales_price")
    private Double salesPrice;
    
    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    private Currency currency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getListPrice() {
        return listPrice;
    }
    
    public void setListPrice(Double listPrice) {
        this.listPrice = listPrice;
    }

    public Double getDiscount() {
        return discount;
    }
    
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }
    
    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Boolean getActive() {
        return active;
    }
    
    public void setActive(Boolean active) {
        this.active = active;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductPrice productPrice = (ProductPrice) o;
        if(productPrice.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productPrice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductPrice{" +
            "id=" + id +
            ", listPrice='" + listPrice + "'" +
            ", discount='" + discount + "'" +
            ", salesPrice='" + salesPrice + "'" +
            ", active='" + active + "'" +
            '}';
    }
}
