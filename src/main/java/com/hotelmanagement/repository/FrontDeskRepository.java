package com.hotelmanagement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hotelmanagement.model.FrontDesk;

public interface FrontDeskRepository extends JpaRepository<FrontDesk, UUID> {

    @Query("SELECT f FROM FrontDesk f WHERE NOT EXISTS (SELECT r FROM Receptionist r WHERE r.frontDesk = f) ORDER BY f.deskNumber ASC")
    List<FrontDesk> findFree();

    Optional<FrontDesk> findByDeskNumber(int deskNumber);
}
