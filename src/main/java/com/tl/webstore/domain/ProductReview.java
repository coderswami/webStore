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
 * A ProductReview.
 */
@Entity
@Table(name = "product_review")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "productreview")
public class ProductReview implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;
    
    @NotNull
    @Column(name = "description", nullable = false)
    private String description;
    
    @NotNull
    @Column(name = "rating", nullable = false)
    private Double rating;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    private UserProfile user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductReview productReview = (ProductReview) o;
        if(productReview.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, productReview.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ProductReview{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", rating='" + rating + "'" +
            '}';
    }
}
