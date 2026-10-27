package com.hotelmanagement.controller;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.service.GuestService;
import com.hotelmanagement.service.ReceptionistService;
import com.hotelmanagement.service.ReservationService;



@RestController 
@RequestMapping ("/api/reservations")
public class ReservationController {

    @Autowired 
    private ReservationService reservationService;
    //A2 + D1
    @PostMapping ("/save")
    public ResponseEntity<String> save(@RequestBody Reservation reservation){

String message = reservationService.save(reservation);
if (message.equals(ReservationService.PAST_DATE)) {
    return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
} else if (message.equals(ReservationService.RECEPTIONIST_BUSY)) {
    return new ResponseEntity<>(message, HttpStatus.CONFLICT);
} else if (message.equals(GuestService.NOT_FOUND) || message.equals(ReceptionistService.NOT_FOUND)) {
    return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
} else {
    return new ResponseEntity<>(message, HttpStatus.CREATED);
}
    }

    @GetMapping("/all")
    public List<Reservation> getAll() {
        return reservationService.getAll();
    }

    // B2
    @GetMapping("/by-status")
    public List<Reservation> getByStatus(@RequestParam ReservationStatus status) {
        return reservationService.getByStatus(status);
    }

    // B3: the Strings are parsed into LocalDate here in the controller
    @GetMapping("/between")
    public List<Reservation> getBetween(@RequestParam String start, @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return reservationService.getBetween(startDate, endDate);
    }

    // F1
    @GetMapping("/stats/by-status")
    public List<Object[]> getStatsByStatus() {
        return reservationService.getStatsByStatus();
    }

    // Bonus: the Pageable is built here from the three parameters
    @GetMapping("/page")
    public Page<Reservation> getPage(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "checkInDate,desc") String sort) {
        String[] parts = sort.split(",");
        Sort.Direction direction = Sort.Direction.ASC;
        if (parts.length > 1 && parts[1].equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        return reservationService.getPage(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<Reservation> reservation = reservationService.getById(id);
        if (reservation.isPresent()) {
            return new ResponseEntity<>(reservation.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ReservationService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Reservation reservation) {
        String message = reservationService.update(id, reservation);
        if (message.equals(ReservationService.NOT_FOUND) || message.equals(GuestService.NOT_FOUND)
                || message.equals(ReceptionistService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    // A2 + E3
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = reservationService.delete(id);
        if (message.equals(ReservationService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.toLowerCase().contains("room") || message.toLowerCase().contains("order")) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    // D2
    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam ReservationStatus status) {
        String message = reservationService.updateStatus(id, status);
        if (message.equals(ReservationService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.startsWith(ReservationService.CANNOT_CHANGE)) {
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    // F2
    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDay(@RequestParam UUID receptionistId, @RequestParam String date) {
        LocalDate day = LocalDate.parse(date);
        String message = reservationService.cancelDay(receptionistId, day);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}



