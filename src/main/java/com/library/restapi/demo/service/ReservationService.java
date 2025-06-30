package com.library.restapi.demo.service;

import com.library.restapi.demo.model.dto.reservation.ReservationByUserUpdateDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationViewDTO;
import com.library.restapi.demo.model.entity.Reservation;

import java.util.List;
import java.util.Map;

public interface ReservationService {

    Reservation findEntityById(int theId);

    ReservationViewDTO getReservationViewDTO(int theId);

    List<ReservationViewDTO> getListAll();

    List<ReservationViewDTO> getListByIsReturned(boolean isReturned);

    List<ReservationViewDTO> getListByUsername(String username);

    List<ReservationViewDTO> getListByBookTitle(String bookTitle);

    ReservationViewDTO createNew(ReservationViewDTO inputDTO);

    ReservationViewDTO createNewByUser(ReservationByUserUpdateDTO inputDTO, String userName);

    ReservationViewDTO updateExist(ReservationViewDTO inputDTO);

    ReservationViewDTO patchUpdate(Map<String, Object> patchInput, int reservationId);

    void hardDelete(int theId);


}
