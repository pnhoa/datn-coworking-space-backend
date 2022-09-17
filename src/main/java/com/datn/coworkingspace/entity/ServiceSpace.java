package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "service_space")
public class ServiceSpace extends BaseEntity {
    // service of space
    private String title;

    private String note;

    @OneToMany(mappedBy = "serviceSpace", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("serviceSpace")
    private Set<SubSpace> subSpaces = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "space_id")
    @JsonIgnoreProperties("serviceSpaces")
    private Space space;

    public ServiceSpace() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<SubSpace> getSubSpaces() { return subSpaces; }

    public void setSubSpaces(Set<SubSpace> subSpaces) { this.subSpaces = subSpaces; }

    public Space getSpace() {  return space; }

    public void setSpace(Space space) { this.space = space; }
}
