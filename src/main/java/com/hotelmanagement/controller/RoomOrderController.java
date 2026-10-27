package com.hotelmanagement.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelmanagement.model.RoomOrder;
import com.hotelmanagement.service.ReservationService;
import com.hotelmanagement.service.RoomOrderService;

@RestController 
@RequestMapping ("/api/room-orders")
public class RoomOrderController{

    @Autowired 
    private RoomOrderService roomOrderService;

// E1
@PostMapping ("/save")
public ResponseEntity<String> save(@RequestParam UUID reservationId,@RequestBody RoomOrder roomOrder){

    String message = roomOrderService.save(reservationId,roomOrder);
    if (message.equals(ReservationService.NOT_FOUND)){

return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);
    }
    else if (message.equals(RoomOrderService.NOT_CHECKED_IN)) {
      return new ResponseEntity<>(message,HttpStatus.BAD_REQUEST);
    }else{


        return new ResponseEntity<>(message,HttpStatus.CREATED);
    }  
    }

    @GetMapping ("/all")
    public List<RoomOrder> getAll(){

        return roomOrderService.getAll();
    }
    //E2
    @GetMapping("/guest/{idNumber}")
    public List<RoomOrder> getByGuest(@PathVariable String idNumber){

        return roomOrderService.getByGuest(idNumber);
    }

    // E3

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){

String message = roomOrderService.delete(id);
if (message.equals(RoomOrderService.NOT_FOUND)){

    return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);}
    else{
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}


    }








