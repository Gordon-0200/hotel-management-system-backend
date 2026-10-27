package com.hotelmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.RoomOrder;

public interface RoomOrderRepository extends JpaRepository<RoomOrder, UUID> {

    @Query("SELECT d FROM RoomOrder d WHERE d.reservation.guest.idNumber = :idNumber ORDER BY d.orderDate DESC")
    List<RoomOrder> findByGuestIdNumber(@Param("idNumber") String idNumber);

    boolean existsByReservation(Reservation reservation);

}