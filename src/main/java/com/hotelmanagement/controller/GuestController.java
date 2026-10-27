package com.hotelmanagement.controller;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.hotelmanagement.model.Guest;
import com.hotelmanagement.repository.ReceptionistRepository;
import com.hotelmanagement.service.GuestService;
import com.hotelmanagement.service.ReceptionistService;

@RestController 
@RequestMapping("/api/guests")
public class GuestController{

@Autowired 
private GuestService guestService;
@Autowired private ReceptionistService receptionistService;

@PostMapping ("/save")
public ResponseEntity<String> save(@RequestBody Guest guest) {
String message = guestService.save(guest);
if (message.equals(GuestService.DUPLICATE_ID_NUMBER)){

return new ResponseEntity<>(message,HttpStatus.CONFLICT);
}
else{
    return new ResponseEntity<>(message,HttpStatus.CREATED);
}

}

@GetMapping ("/all")
public List<Guest> getAll(){
    return guestService.getAll();
}

//B1
@GetMapping ("by-last-name")
public List<Guest> getByLastName(@RequestParam String lastName){
    return guestService.getByLastName(lastName);
}
// C4
@GetMapping ("/of-receptionist/{receptionistId}")
public ResponseEntity<Object> getOfReceptionist(@PathVariable UUID receptionistId) {

   if (receptionistService.getById(receptionistId).isEmpty()){
    return new ResponseEntity<>(ReceptionistService.NOT_FOUND,HttpStatus.NOT_FOUND);}

    else{
         return new ResponseEntity<>(guestService.getOfReceptionist(receptionistId),HttpStatus.OK);}
    }
@GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id) {
        Optional<Guest> guest = guestService.getById(id);
        if (guest.isPresent()) {
            return new ResponseEntity<>(guest.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(GuestService.NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable UUID id, @RequestBody Guest guest) {
        String message = guestService.update(id, guest);
        if (message.equals(GuestService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = guestService.delete(id);
        if (message.equals(GuestService.NOT_FOUND)) {
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
    }
}
    




    

   





















