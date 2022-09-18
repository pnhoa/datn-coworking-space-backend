package com.datn.coworkingspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "package")
public class Package extends BaseEntity {

    // package of space (Day, Month,....)
    private String type;

    private String note;

    @OneToMany(mappedBy = "packageSubSpace", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("packageSubSpace")
    private Set<SubSpace> subSpaces = new HashSet<>();

    public Package() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<SubSpace> getSubSpaces() { return subSpaces; }

    public void setSubSpaces(Set<SubSpace> subSpaces) { this.subSpaces = subSpaces; }
}
