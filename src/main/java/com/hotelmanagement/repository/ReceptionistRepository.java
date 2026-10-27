package com.hotelmanagement.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hotelmanagement.model.FrontDesk;
import com.hotelmanagement.model.Receptionist;

public interface ReceptionistRepository extends JpaRepository<Receptionist, UUID> {

    @Query("SELECT DISTINCT r FROM Receptionist r JOIN r.skills s WHERE LOWER(s.name) = LOWER(:name)")
    List<Receptionist> findBySkillName(@Param("name") String name);

    boolean existsByFrontDesk(FrontDesk frontDesk);
}


