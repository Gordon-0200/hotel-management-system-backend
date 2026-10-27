package com.hotelmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelmanagement.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, UUID> {

    List<Guest> findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName);

    @Query("SELECT DISTINCT r.guest FROM Reservation r WHERE r.receptionist.id = :receptionistId")
    List<Guest> findGuestsOfReceptionist(@Param("receptionistId") UUID receptionistId);

    boolean existsByIdNumber(String idNumber);
}

