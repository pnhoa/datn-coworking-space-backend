package com.datn.coworkingspace.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "space_description")
public class SpaceDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date openingDate;

    private String shortDescription;

    private String description;

    @OneToOne(cascade = CascadeType.ALL,
            mappedBy = "spaceDescription",
            fetch = FetchType.LAZY,
            optional = false)
    private Space space;

    public SpaceDescription() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }
}
