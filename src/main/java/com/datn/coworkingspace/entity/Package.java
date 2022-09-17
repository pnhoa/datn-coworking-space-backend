package com.datn.coworkingspace.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "package")
public class Package extends BaseEntity {

    private String type;

    private String note;

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
}
