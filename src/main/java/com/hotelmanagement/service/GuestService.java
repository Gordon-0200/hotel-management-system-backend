package com.hotelmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.model.Guest;
import com.hotelmanagement.repository.GuestRepository;

@Service
public class GuestService {

    public static final String NOT_FOUND = "The guest with that id does not exist";
    public static final String DUPLICATE_ID_NUMBER = "A guest with that idNumber already exist";

    @Autowired
    private GuestRepository guestRepository;

    public String save(Guest guest) {
        if (guestRepository.existsByIdNumber(guest.getIdNumber())) {
            return DUPLICATE_ID_NUMBER;
        }
        guest.setId(null);
        guestRepository.save(guest);
        return "Guest saved successfully";
    }

    public List<Guest> getAll() {
        return guestRepository.findAll();
    }

    public Optional<Guest> getById(UUID id) {
        return guestRepository.findById(id);
    }

    public String update(UUID id, Guest input) {
        Optional<Guest> found = guestRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }

        Guest guest = found.get();
        guest.setFirstName(input.getFirstName());
        guest.setLastName(input.getLastName());
        guest.setIdNumber(input.getIdNumber());
        guest.setEmail(input.getEmail());
        guest.setPhone(input.getPhone());
        guestRepository.save(guest);
        return "Guest updated successfully";
    }

    public String delete(UUID id) {
        Optional<Guest> found = guestRepository.findById(id);

        if (found.isEmpty()) {
            return NOT_FOUND;
        }

        guestRepository.delete(found.get());
        return "Guest deleted successfully";
    }

    public List<Guest> getByLastName(String lastName) {
        return guestRepository.findByLastNameIgnoreCaseOrderByFirstNameAsc(lastName);
    }

    public List<Guest> getOfReceptionist(UUID receptionistId) {
        return guestRepository.findGuestsOfReceptionist(receptionistId);
    }
}

























