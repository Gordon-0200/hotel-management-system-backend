package com.hotelmanagement.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotelmanagement.model.FrontDesk;
import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.model.Skill;
import com.hotelmanagement.repository.FrontDeskRepository;
import com.hotelmanagement.repository.ReceptionistRepository;
import com.hotelmanagement.repository.SkillRepository;

@Service
public class ReceptionistService {

    public static final String NOT_FOUND = "The receptionist with that id does not exist";
    public static final String SKILL_NOT_FOUND = "That skill does not exist";
    public static final String DESK_NOT_FOUND = "The front desk with that deskNumber does not exist";
    public static final String ALREADY_HAS_SKILL = "The receptionist already has that skill";
    public static final String DESK_TAKEN = "That front desk is taken by another receptionist";

    @Autowired
    private ReceptionistRepository receptionistRepository;

    @Autowired
    private FrontDeskRepository frontDeskRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Transactional
    public String save(Receptionist receptionist) {
        receptionist.setId(null);

        if (receptionist.getFrontDesk() != null) {
            Optional<FrontDesk> desk = frontDeskRepository.findById(receptionist.getFrontDesk().getId());
            if (desk.isEmpty()) {
                return FrontDeskService.NOT_FOUND;
            }
            receptionist.setFrontDesk(desk.get());
        }

        List<Skill> skills = new ArrayList<>();
        if (receptionist.getSkills() != null) {
            for (Skill s : receptionist.getSkills()) {
                Optional<Skill> skill = skillRepository.findById(s.getId());
                if (skill.isEmpty()) {
                    return SKILL_NOT_FOUND;
                }
                skills.add(skill.get());
            }
        }

        receptionist.setSkills(skills);
        receptionistRepository.save(receptionist);
        return "Receptionist saved successfully";
    }

    public List<Receptionist> getAll() {
        return receptionistRepository.findAll();
    }

    public Optional<Receptionist> getById(UUID id) {
        return receptionistRepository.findById(id);
    }

    @Transactional
    public String update(UUID id, Receptionist input) {
        Optional<Receptionist> found = receptionistRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }

        Receptionist receptionist = found.get();
        receptionist.setFirstName(input.getFirstName());
        receptionist.setLastName(input.getLastName());
        receptionist.setEmail(input.getEmail());

        if (input.getFrontDesk() != null) {
            Optional<FrontDesk> desk = frontDeskRepository.findById(input.getFrontDesk().getId());
            if (desk.isEmpty()) {
                return FrontDeskService.NOT_FOUND;
            }
            receptionist.setFrontDesk(desk.get());
        }

        if (input.getSkills() != null) {
            List<Skill> skills = new ArrayList<>();
            for (Skill s : input.getSkills()) {
                Optional<Skill> skill = skillRepository.findById(s.getId());
                if (skill.isEmpty()) {
                    return SKILL_NOT_FOUND;
                }
                skills.add(skill.get());
            }
            receptionist.setSkills(skills);
        }

        receptionistRepository.save(receptionist);
        return "Receptionist updated successfully";
    }

    public String delete(UUID id) {
        Optional<Receptionist> found = receptionistRepository.findById(id);
        if (found.isEmpty()) {
            return NOT_FOUND;
        }

        receptionistRepository.delete(found.get());
        return "Receptionist deleted successfully";
    }

    public List<Receptionist> getBySkill(String name) {
        return receptionistRepository.findBySkillName(name);
    }

    @Transactional
    public String addSkill(UUID receptionistId, UUID skillId) {
        Optional<Receptionist> foundReceptionist = receptionistRepository.findById(receptionistId);
        if (foundReceptionist.isEmpty()) {
            return NOT_FOUND;
        }

        Optional<Skill> foundSkill = skillRepository.findById(skillId);
        if (foundSkill.isEmpty()) {
            return SKILL_NOT_FOUND;
        }

        Receptionist receptionist = foundReceptionist.get();
        Skill skill = foundSkill.get();

        if (receptionist.getSkills() == null) {
            receptionist.setSkills(new ArrayList<>());
        }

        if (receptionist.getSkills().contains(skill)) {
            return ALREADY_HAS_SKILL;
        }

        receptionist.getSkills().add(skill);
        receptionistRepository.save(receptionist);
        return "Skill added successfully";
    }

    @Transactional
    public String changeFrontDesk(UUID receptionistId, int deskNumber) {
        Optional<Receptionist> foundReceptionist = receptionistRepository.findById(receptionistId);
        if (foundReceptionist.isEmpty()) {
            return NOT_FOUND;
        }

        Optional<FrontDesk> foundDesk = frontDeskRepository.findByDeskNumber(deskNumber);
        if (foundDesk.isEmpty()) {
            return DESK_NOT_FOUND;
        }

        Receptionist receptionist = foundReceptionist.get();
        FrontDesk desk = foundDesk.get();

        boolean alreadyMine = receptionist.getFrontDesk() != null && receptionist.getFrontDesk().getId().equals(desk.getId());
        if (!alreadyMine && receptionistRepository.existsByFrontDesk(desk)) {
            return DESK_TAKEN;
        }

        receptionist.setFrontDesk(desk);
        receptionistRepository.save(receptionist);
        return "Front desk changed successfully";
    }
}

