package com.hotelmanagement.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotelmanagement.model.Guest;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.repository.GuestRepository;
import com.hotelmanagement.repository.ReceptionistRepository;
import com.hotelmanagement.repository.ReservationRepository;
import com.hotelmanagement.repository.RoomOrderRepository;


@Service
public class ReservationService {

    public static final String NOT_FOUND = "The reservation with that id does not exist";
    public static final String PAST_DATE = "Reservation date cannot be in the past";
    public static final String RECEPTIONIST_BUSY = "Receptionist already has a reservation on that date";
    public static final String HAS_ROOM_ORDER = "That reservation has room orders,delete them first ";
    public static final String CANNOT_CHANGE = "Cannot change the status from";

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private RoomOrderRepository roomOrderRepository;

    @Transactional
    public String save(Reservation reservation) {
        if (reservation.getCheckInDate() == null || reservation.getCheckInDate().isBefore(LocalDate.now())) {
            return PAST_DATE;
        }

        if (reservation.getGuest() != null) {
            Optional<Guest> guest = guestRepository.findById(reservation.getGuest().getId());
            if (guest.isEmpty()) {
                return GuestService.NOT_FOUND;
            }
            reservation.setGuest(guest.get());
        }

        if (reservation.getReceptionist() != null) {
            Optional<Receptionist> receptionist = receptionistRepository.findById(reservation.getReceptionist().getId());
            if (receptionist.isEmpty()) {
                return ReceptionistService.NOT_FOUND;
            }
            reservation.setReceptionist(receptionist.get());
            if (reservationRepository.existsByReceptionistIdAndCheckInDateAndStatusNot(
                    receptionist.get().getId(), reservation.getCheckInDate(), ReservationStatus.CANCELLED)) {
                return RECEPTIONIST_BUSY;
            }
        }

        reservation.setId(null);
        reservation.setStatus(ReservationStatus.RESERVED);
        reservationRepository.save(reservation);
        return "Reservation saved successfully";
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getById(UUID id) {
        return reservationRepository.findById(id);
    }

    // A2: the general update no longer touches the status (only D2 changes it)
    @Transactional
    public String update(UUID id, Reservation input) {
        Optional<Reservation> found = reservationRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        Reservation reservation = found.get();
        reservation.setCheckInDate(input.getCheckInDate());
        reservation.setRoomNumber(input.getRoomNumber());
        reservation.setNights(input.getNights());
        if (input.getGuest() != null) {
            Optional<Guest> guest = guestRepository.findById(input.getGuest().getId());
            if (guest.isEmpty()) {
                return GuestService.NOT_FOUND;
            }
            reservation.setGuest(guest.get());
        }
        if (input.getReceptionist() != null) {
            Optional<Receptionist> receptionist = receptionistRepository.findById(input.getReceptionist().getId());
            if (receptionist.isEmpty()) {
                return ReceptionistService.NOT_FOUND;
            }
            reservation.setReceptionist(receptionist.get());
        }
        reservationRepository.save(reservation);
        return "Reservation updated successfully";
    }

    // A2 + E3: a reservation that has room orders can no longer be deleted
    public String delete(UUID id) {
        Optional<Reservation> found = reservationRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        if (roomOrderRepository.existsByReservation(found.get())) {
            return HAS_ROOM_ORDER;
        }
        reservationRepository.delete(found.get());
        return "Reservation deleted successfully";
    }

    // B2
    public List<Reservation> getByStatus(ReservationStatus status) {
        return reservationRepository.findByStatusOrderByCheckInDateAsc(status);
    }

    // B3
    public List<Reservation> getBetween(LocalDate start, LocalDate end) {
        return reservationRepository.findByCheckInDateBetweenOrderByCheckInDateAsc(start, end);
    }

    // D2: RESERVED -> PAID or CANCELLED, PAID -> CHECKED_IN or CANCELLED, the rest is final
    @Transactional
    public String updateStatus(UUID id, ReservationStatus newStatus) {
        Optional<Reservation> found = reservationRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }
        Reservation reservation = found.get();
        ReservationStatus current = reservation.getStatus();
        boolean allowed = false;
        if (current == ReservationStatus.RESERVED) {
            allowed = newStatus == ReservationStatus.PAID || newStatus == ReservationStatus.CANCELLED;
        } else if (current == ReservationStatus.PAID) {
            allowed = newStatus == ReservationStatus.CHECKED_IN || newStatus == ReservationStatus.CANCELLED;
        }
        if (!allowed) {
            return CANNOT_CHANGE + current + " to " + newStatus;
        }
        reservation.setStatus(newStatus);
        reservationRepository.save(reservation);
        return "Reservation status updated successfully";
    }

    // F1
    public List<Object[]> getStatsByStatus() {
        return reservationRepository.countGroupedByStatus();
    }

    // F2
    @Transactional
    public String cancelDay(UUID receptionistId, LocalDate date) {
        int count = reservationRepository.cancelDay(receptionistId, date, ReservationStatus.CANCELLED,
                ReservationStatus.CHECKED_IN);
        return count + " reservations cancelled";
    }

    // Bonus: page of reservations
    public Page<Reservation> getPage(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
}

