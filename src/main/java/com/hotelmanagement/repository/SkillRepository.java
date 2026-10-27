package com.hotelmanagement.repository;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.hotelmanagement.model.Skill;
public interface SkillRepository extends JpaRepository<Skill,UUID>{

// C3 (JPQL,IS EMPTY):skills that no receptionist holds 
@Query("SELECT s FROM Skill s WHERE s.receptionists IS EMPTY")
List<Skill> findUnused();



}