package com.myhouse.MyHouse.service;

import com.myhouse.MyHouse.exceptions.NotFoundException;
import com.myhouse.MyHouse.model.RealEstate;
import com.myhouse.MyHouse.repository.RealEstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RealEstateService {

    @Autowired
    private RealEstateRepository realEstateRepository;

    public RealEstate createRealEstate(String name) {
        RealEstate realEstate = new RealEstate();
        realEstate.setOwnerUserIds(new ArrayList<>());
        realEstate.setResidentUserIds(new ArrayList<>());
        realEstate.setName(name);
        return realEstateRepository.save(realEstate);
    }

    public List<RealEstate> getAll() {
        return realEstateRepository.findAll();
    }

    public Page<RealEstate> getAll(int page,int size) {
        return getPageFromList(page, size, realEstateRepository.findAll());
    }

    public RealEstate getById(String id) {
        Optional<RealEstate> optional = realEstateRepository.findById(id);
        if (optional.isEmpty()) {
            throw new NotFoundException("Real Estate with given id doesn't exist.");
        }
        return optional.get();
    }

    private static PageImpl<RealEstate> getPageFromList(int page, int size, List<RealEstate> entities) {
        List<RealEstate> dtos = new ArrayList<>();
        int total = entities.size();
        int start = page * size;
        int end = Math.min(start + size, total);
        if (start <= end) {
            dtos = entities.subList(start, end);
        }
        return new PageImpl<>(dtos, PageRequest.of(page, size), total);
    }

    public void deleteById(String id) {
        realEstateRepository.deleteById(id);
    }


}
