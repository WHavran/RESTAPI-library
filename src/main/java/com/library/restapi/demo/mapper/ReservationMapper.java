package com.library.restapi.demo.mapper;

import com.library.restapi.demo.model.dto.reservation.ReservationByUserUpdateDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationForBookDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationForUserDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationViewDTO;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Reservation;
import com.library.restapi.demo.model.entity.User;

import java.util.List;

public interface ReservationMapper {

    ReservationForUserDTO mapEntityToReservationForUserDTO(Reservation entity);

    ReservationForBookDTO mapEntityToReservationForBookDTO(Reservation entity);

    ReservationViewDTO mapEntityToReservationViewDTO(Reservation entity);

    List<ReservationViewDTO> mapListEntityToListViewDTO(List<Reservation> listEntities);

    Reservation mapViewDTOToEntityCreate(ReservationViewDTO inputDTO, User inputUser, Book inputBook);

    Reservation mapByUserUpdateDTOToEntityCreate(ReservationByUserUpdateDTO inputDTO, User inputUser, Book inputBook);

    void mapViewDTOToEntityForUpdate(ReservationViewDTO inputDTO, Reservation entity ,User inputUser, Book inputBook);

}
