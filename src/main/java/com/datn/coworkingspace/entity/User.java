package com.datn.coworkingspace.entity;

import com.datn.coworkingspace.enums.AuthProvider;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints =
        {@UniqueConstraint(columnNames = "userName"),
         @UniqueConstraint(columnNames = "email")})
@JsonIgnoreProperties({"roles", "password", "feedbacks", "accCustomer", "provider", "providerId", "spaces", "bookings", "comments"})
public class User extends BaseEntity{

    @Column(name = "username")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private int gender;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "is_acc_customer")
    private Boolean isAccCustomer;

    @Column(name = "provider")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonIgnoreProperties("users")
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private List<Space> spaces = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @Transient
    private String roleCode;

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getAccCustomer() { return isAccCustomer; }

    public void setAccCustomer(Boolean accCustomer) { isAccCustomer = accCustomer; }

    public String getRoleCode() { return roleCode; }

    public void setRoleCode(String roleCode) { this.roleCode = roleCode; }

    public AuthProvider getProvider() { return provider; }

    public void setProvider(AuthProvider provider) { this.provider = provider; }

    public String getProviderId() { return providerId; }

    public void setProviderId(String providerId) { this.providerId = providerId; }

    public List<Space> getSpaces() { return spaces; }

    public void setSpaces(List<Space> spaces) { this.spaces = spaces; }

    public List<Comment> getComments() { return comments; }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Booking> getBookings() { return bookings; }

    public void setBookings(List<Booking> bookings) { this.bookings = bookings; }
}
