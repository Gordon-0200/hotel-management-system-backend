package com.hotelmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.model.FrontDesk;
import com.hotelmanagement.repository.FrontDeskRepository;

@Service
public class FrontDeskService {

    public static final String NOT_FOUND = "The front desk with that id does not exist";

    @Autowired
    private FrontDeskRepository frontDeskRepository;

    public String save(FrontDesk frontDesk) {
        frontDesk.setId(null);
        frontDeskRepository.save(frontDesk);
        return "Front desk saved successfully";
    }

    public List<FrontDesk> getAll() {
        return frontDeskRepository.findAll();
    }

    public Optional<FrontDesk> getById(UUID id) {
        return frontDeskRepository.findById(id);
    }

    public String update(UUID id, FrontDesk input) {
        Optional<FrontDesk> found = frontDeskRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }

        FrontDesk frontDesk = found.get();
        frontDesk.setDeskNumber(input.getDeskNumber());
        frontDesk.setName(input.getName());
        frontDeskRepository.save(frontDesk);
        return "Front desk updated successfully";
    }

    public String delete(UUID id) {
        Optional<FrontDesk> found = frontDeskRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }

        frontDeskRepository.delete(found.get());
        return "Front desk deleted successfully";
    }

    public List<FrontDesk> getFree() {
        return frontDeskRepository.findFree();
    }
}











