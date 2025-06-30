package com.library.restapi.demo.controller;

import com.library.restapi.demo.model.dto.reservation.ReservationByUserUpdateDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationViewDTO;
import com.library.restapi.demo.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {

    ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationViewDTO> getOneReservation(@PathVariable int id){
        ReservationViewDTO reservation = reservationService.getReservationViewDTO(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReservationViewDTO>> showAllReservation(){
        List<ReservationViewDTO> reservationList = reservationService.getListAll();
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/all/isReturned={isReturned}")
    public ResponseEntity<List<ReservationViewDTO>> showAllByIsReturned(@PathVariable boolean isReturned){
        List<ReservationViewDTO> reservationList = reservationService.getListByIsReturned(isReturned);
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/all/username={username}")
    public ResponseEntity<List<ReservationViewDTO>> showAllByUsername(@PathVariable String username){
        List<ReservationViewDTO> reservationList = reservationService.getListByUsername(username);
        return ResponseEntity.ok(reservationList);
    }

    @GetMapping("/all/bookTitle={bookTitle}")
    public ResponseEntity<List<ReservationViewDTO>> showAllByBook(@PathVariable String bookTitle){
        List<ReservationViewDTO> reservationList = reservationService.getListByBookTitle(bookTitle);
        return ResponseEntity.ok(reservationList);
    }

    @PostMapping("/create")
    public ResponseEntity<ReservationViewDTO> createNewReservation(@Valid @RequestBody ReservationViewDTO viewDTO){
        ReservationViewDTO newReservation = reservationService.createNew(viewDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newReservation.id())
                .toUri();

        return ResponseEntity.created(location).body(newReservation);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReservationViewDTO> createNewReservationByUser(@Valid @RequestBody ReservationByUserUpdateDTO userReservationDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ReservationViewDTO newReservation = reservationService.createNewByUser(userReservationDTO, username);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newReservation.id())
                .toUri();

        return ResponseEntity.created(location).body(newReservation);
    }

    @PutMapping("/update")
    public ResponseEntity<ReservationViewDTO> updateReservation(@Valid @RequestBody ReservationViewDTO viewDTO){
        ReservationViewDTO reservation = reservationService.updateExist(viewDTO);
        return ResponseEntity.ok(reservation);
    }

    @PatchMapping("/update/{id}")
    private ResponseEntity<ReservationViewDTO> patchUpdateReservation(@PathVariable int id,
                                                          @RequestBody Map<String, Object> patchInput){
        ReservationViewDTO updatedReservation = reservationService.patchUpdate(patchInput, id);
        return ResponseEntity.ok(updatedReservation);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> hardDelete (@PathVariable int id){
        reservationService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }
}
