package com.hotelmanagement.controller;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelmanagement.model.Receptionist;
import com.hotelmanagement.service.FrontDeskService;
import com.hotelmanagement.service.ReceptionistService;



@RestController 
@RequestMapping("/api/receptionists")
public class ReceptionistController {

    @Autowired 
    private ReceptionistService receptionistService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Receptionist receptionist) {
        String message = receptionistService.save(receptionist);
        if (message.equals(FrontDeskService.NOT_FOUND) || message.equals(ReceptionistService.SKILL_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Receptionist receptionist) {
        String message = receptionistService.update(id, receptionist);
        if (message.equals(ReceptionistService.NOT_FOUND) || message.equals(FrontDeskService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = receptionistService.delete(id);
        if (message.equals(ReceptionistService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @GetMapping("/all")
    public List<Receptionist> getAll() {
        return receptionistService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<Receptionist> receptionist = receptionistService.getById(id);
        if (receptionist.isPresent()) {
            return new ResponseEntity<>(receptionist.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ReceptionistService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/by-skill")
    public List<Receptionist> bySkill(@RequestParam String name) {
        return receptionistService.getBySkill(name);
    }

    // D3
    @PutMapping("/{receptionistId}/skills/{skillId}")
    public ResponseEntity<String> addSkill(@PathVariable UUID receptionistId, @PathVariable UUID skillId) {
        String message = receptionistService.addSkill(receptionistId, skillId);
        if (message.equals(ReceptionistService.NOT_FOUND) || message.equals(ReceptionistService.SKILL_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals(ReceptionistService.ALREADY_HAS_SKILL)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    // D4
    @PatchMapping("/{receptionistId}/front-desk")
    public ResponseEntity<String> changeFrontDesk(@PathVariable UUID receptionistId,
            @RequestParam int deskNumber) {
        String message = receptionistService.changeFrontDesk(receptionistId, deskNumber);
        if (message.equals(ReceptionistService.NOT_FOUND) || message.equals(ReceptionistService.DESK_NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else if (message.equals(ReceptionistService.DESK_TAKEN)) {
            return new ResponseEntity<>(message, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
