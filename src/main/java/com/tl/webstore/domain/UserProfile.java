package com.tl.webstore.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.tl.webstore.domain.enumeration.Gender;

/**
 * A UserProfile.
 */
@Entity
@Table(name = "user_profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "userprofile")
public class UserProfile implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;
    
    @Column(name = "fname")
    private String fname;
    
    @Column(name = "lname")
    private String lname;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;
    
    @Column(name = "dob")
    private LocalDate dob;
    
    @OneToOne
    private UserLogin authentication;

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserRole> roles = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderHeader> orders = new HashSet<>();

    @OneToMany(mappedBy = "userProfile")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UserAddress> addresss = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }
    
    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }
    
    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getDob() {
        return dob;
    }
    
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public UserLogin getAuthentication() {
        return authentication;
    }

    public void setAuthentication(UserLogin userLogin) {
        this.authentication = userLogin;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> userRoles) {
        this.roles = userRoles;
    }

    public Set<OrderHeader> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderHeader> orderHeaders) {
        this.orders = orderHeaders;
    }

    public Set<UserAddress> getAddresss() {
        return addresss;
    }

    public void setAddresss(Set<UserAddress> userAddresss) {
        this.addresss = userAddresss;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserProfile userProfile = (UserProfile) o;
        if(userProfile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, userProfile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
            "id=" + id +
            ", email='" + email + "'" +
            ", fname='" + fname + "'" +
            ", lname='" + lname + "'" +
            ", imageUrl='" + imageUrl + "'" +
            ", gender='" + gender + "'" +
            ", dob='" + dob + "'" +
            '}';
    }
}
