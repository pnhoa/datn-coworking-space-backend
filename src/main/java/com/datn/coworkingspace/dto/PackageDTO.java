package com.datn.coworkingspace.dto;

import java.util.List;

public class PackageDTO {

    private String type;

    private String note;

    private ServiceSpaceDTO serviceSpaceDTO;

    private List<SubSpaceDTO> subSpaceDTOs;

    public PackageDTO() {
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

    public ServiceSpaceDTO getServiceSpaceDTO() { return serviceSpaceDTO; }

    public void setServiceSpaceDTO(ServiceSpaceDTO serviceSpaceDTO) { this.serviceSpaceDTO = serviceSpaceDTO; }

    public List<SubSpaceDTO> getSubSpaceDTOs() {
        return subSpaceDTOs;
    }

    public void setSubSpaceDTOs(List<SubSpaceDTO> subSpaceDTOs) {
        this.subSpaceDTOs = subSpaceDTOs;
    }
}
