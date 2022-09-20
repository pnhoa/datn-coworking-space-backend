package com.datn.coworkingspace.dto;

import com.datn.coworkingspace.entity.Space;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ServiceSpaceDTO {

    @NotNull(message = "Please input title")
    private String title;

    private String note;

    private Space space;

    private List<PackageDTO> packageDTOs;

    public ServiceSpaceDTO() {
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

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public List<PackageDTO> getPackageDTOs() { return packageDTOs; }

    public void setPackageDTOs(List<PackageDTO> packageDTOs) { this.packageDTOs = packageDTOs; }
}
