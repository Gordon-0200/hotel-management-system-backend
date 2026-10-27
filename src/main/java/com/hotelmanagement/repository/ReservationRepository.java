package com.hotelmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelmanagement.model.Reservation;
import com.hotelmanagement.model.ReservationStatus;
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByStatusOrderByCheckInDateAsc(ReservationStatus status);

    List<Reservation> findByCheckInDateBetweenOrderByCheckInDateAsc(LocalDate start, LocalDate end);

    boolean existsByReceptionistIdAndCheckInDateAndStatusNot(UUID receptionistId, LocalDate checkInDate, ReservationStatus status);

    long countByCheckInDate(LocalDate checkInDate);

    long countByStatus(ReservationStatus status);

    @Query("SELECT r.status, COUNT(r) FROM Reservation r GROUP BY r.status")
    List<Object[]> countGroupedByStatus();

    @Modifying
    @Query("UPDATE Reservation r SET r.status = :newStatus WHERE r.receptionist.id = :receptionistId AND r.checkInDate = :date AND r.status <> :except")
    int cancelDay(@Param("receptionistId") UUID receptionistId,
                  @Param("date") LocalDate date,
                  @Param("newStatus") ReservationStatus newStatus,
                  @Param("except") ReservationStatus except);



}

