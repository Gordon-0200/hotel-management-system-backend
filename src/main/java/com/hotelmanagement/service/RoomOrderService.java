package com.hotelmanagement.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.model.RoomOrder;
import com.hotelmanagement.repository.ReservationRepository;
import com.hotelmanagement.repository.RoomOrderRepository;

@Service 

public class RoomOrderService {
    public static final String NOT_FOUND = "The room order with that id does not exist";
    public static final String NOT_CHECKED_IN = "Room orders are only for CHECKED_IN reservations";

@Autowired 
private RoomOrderRepository roomOrderRepository;

@Autowired 
private ReservationRepository reservationRepository;

//E1
public String save(UUID 
reservationId, RoomOrder roomOrder) {
    Optional<Reservation> found = reservationRepository.findById(reservationId);
    if (found.isEmpty()) {
        return ReservationService.NOT_FOUND;
    }

    Reservation reservation = found.get();
    if (reservation.getStatus() != ReservationStatus.CHECKED_IN) {
        return NOT_CHECKED_IN;
    }

    roomOrder.setId(null);
    roomOrder.setOrderDate(LocalDate.now());
    roomOrder.setReservation(reservation);
    roomOrderRepository.save(roomOrder);
    return "Room order saved successfully";
}

public List<RoomOrder> getAll(){
    return roomOrderRepository.findAll();
}



//E2
public List<RoomOrder> getByGuest(String idNumber){
    return roomOrderRepository.findByGuestIdNumber(idNumber);
}
//E3
public String delete(UUID id) {
    Optional<RoomOrder> found = roomOrderRepository.findById(id);
    if (found.isEmpty()) {
        return NOT_FOUND;
    }
    roomOrderRepository.delete(found.get());
    return "Room order deleted successfully";
}





}