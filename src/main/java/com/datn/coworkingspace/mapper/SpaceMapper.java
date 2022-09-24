package com.datn.coworkingspace.mapper;

import com.datn.coworkingspace.dto.SpaceAddressDTO;
import com.datn.coworkingspace.dto.SpaceDTO;
import com.datn.coworkingspace.entity.Space;
import com.datn.coworkingspace.entity.SpaceAddress;

public class SpaceMapper {

    public static SpaceDTO mapToSpaceDTO(Space space) {
        SpaceDTO spaceDTO = new SpaceDTO();

        return spaceDTO;
    }

    public static SpaceAddress getSpaceAddress(SpaceAddressDTO spaceAddressDTO) {
        SpaceAddress spaceAddress = new SpaceAddress();
        spaceAddress.setLocationName(spaceAddressDTO.getLocationName());
        spaceAddress.setAddressLine1(spaceAddressDTO.getAddressLine1());
        spaceAddress.setAddressLine2(spaceAddressDTO.getAddressLine2());
        spaceAddress.setFloorNumber(spaceAddressDTO.getFloorNumber());
        spaceAddress.setCountry(spaceAddressDTO.getCountry());
        spaceAddress.setProvince(spaceAddressDTO.getProvince());
        spaceAddress.setDistrict(spaceAddressDTO.getDistrict());
        spaceAddress.setSubDistrict(spaceAddressDTO.getSubDistrict());
        spaceAddress.setZipCode(spaceAddressDTO.getZipCode());
        return spaceAddress;
    }
}
