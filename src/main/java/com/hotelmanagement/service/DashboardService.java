package com.hotelmanagement.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotelmanagement.model.ReservationStatus;
import com.hotelmanagement.repository.FrontDeskRepository;
import com.hotelmanagement.repository.GuestRepository;
import com.hotelmanagement.repository.ReceptionistRepository;
import com.hotelmanagement.repository.ReservationRepository;
import com.hotelmanagement.repository.RoomOrderRepository;

@Service
public class DashboardService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private FrontDeskRepository frontDeskRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomOrderRepository roomOrderRepository;

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("guests", guestRepository.count());
        summary.put("receptionist", receptionistRepository.count());
        summary.put("freeFrontDesks", (long) frontDeskRepository.findFree().size());
        summary.put("reservationsToday", reservationRepository.countByCheckInDate(LocalDate.now()));
        summary.put("reserved", reservationRepository.countByStatus(ReservationStatus.RESERVED));
        summary.put("roomOrder", roomOrderRepository.count());
        return summary;
    }
}
