package com.hotelmanagement.controller;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelmanagement.model.Skill;
import com.hotelmanagement.service.SkillService;

@RestController 
@RequestMapping("/api/skills")

public class SkillController {

    @Autowired 
    private SkillService skillService;

    @PostMapping ("/save")
    public ResponseEntity<String> save(@RequestBody Skill skill){

String message = skillService.save(skill);
return new ResponseEntity<>(message,HttpStatus.CREATED);

    }


@GetMapping ("/all")

public List<Skill> getAll(){

    return skillService.getAll();
}


// C3

@GetMapping ("/unused")
public List<Skill> getUnused(){

    return skillService.getUnused();
}
@GetMapping("/{id}")
public ResponseEntity<Object> getById(@PathVariable UUID id){

    Optional<Skill> skill = skillService.getById(id);
    if (skill.isPresent()) {

return new ResponseEntity<>(skill.get(),HttpStatus.OK);}

else{

    return new ResponseEntity<>(SkillService.NOT_FOUND,HttpStatus.NOT_FOUND);}}



     @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Skill skill) {
        String message = skillService.update(id, skill);
        if (message.equals(SkillService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = skillService.delete(id);
        if (message.equals(SkillService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
























