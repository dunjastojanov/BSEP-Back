package com.myhouse.MyHouse.dto;

import com.myhouse.MyHouse.model.RealEstate;

public record RealEstateDto(String id, String name) {

    public RealEstateDto(RealEstate realEstate) {
        this(realEstate.getId(), realEstate.getName());
    }

}
