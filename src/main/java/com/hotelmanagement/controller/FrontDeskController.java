package com.hotelmanagement.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.logging.log4j.message.StringMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.hotelmanagement.model.FrontDesk;
import com.hotelmanagement.service.FrontDeskService;

@RestController 
@RequestMapping ("/api/front-desk")
public class FrontDeskController {

    @Autowired 
    private FrontDeskService frontDeskService;
    @PostMapping ("/save")

    public ResponseEntity<String> save(@RequestBody FrontDesk frontDesk){

String message = frontDeskService.save(frontDesk);
return new ResponseEntity<>(message,HttpStatus.CREATED);

    }
@GetMapping("/all")
public List<FrontDesk> getAll(){

    return frontDeskService.getAll();
}

//C2
@GetMapping ("/free")
public List<FrontDesk> free() {
        return frontDeskService.getFree();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable UUID id){

Optional<FrontDesk> frontDesk = frontDeskService.getById(id);

if (frontDesk.isPresent()) {
      return new ResponseEntity<>(frontDesk.get(), HttpStatus.OK);
        } else{

return  new ResponseEntity<>(FrontDeskService.NOT_FOUND,HttpStatus.NOT_FOUND);

        }}


        @PutMapping("/update/{id}")
        public ResponseEntity<String> update(@PathVariable UUID id,@RequestBody FrontDesk frontDesk){
            String message = frontDeskService.update(id, frontDesk);
            if(message.equals(FrontDeskService.NOT_FOUND)){
                return new ResponseEntity<>(message,HttpStatus.NOT_FOUND);

            }
else{
    return new ResponseEntity<>(message,HttpStatus.OK);

}
        }

        @DeleteMapping("/delete/{id}")
        public  ResponseEntity<String> delete(@PathVariable UUID id){
            String message = frontDeskService.delete(id);
            if (message.equals(FrontDeskService.NOT_FOUND)){

                return new  ResponseEntity<>(message,HttpStatus.NOT_FOUND);
            }else{

                return new ResponseEntity<>(message,HttpStatus.OK);
                
            }
        }









}