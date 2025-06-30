package com.library.restapi.demo.mapper.impl;

import com.library.restapi.demo.mapper.ReservationMapper;
import com.library.restapi.demo.model.dto.reservation.ReservationByUserUpdateDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationForBookDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationForUserDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationViewDTO;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Reservation;
import com.library.restapi.demo.model.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReservationMapImpl implements ReservationMapper {
    @Override
    public ReservationForUserDTO mapEntityToReservationForUserDTO(Reservation entity) {

        return new ReservationForUserDTO(
                entity.getId(),
                entity.getBook().getTitle(),
                entity.getReservedAt(),
                entity.getDueDate(),
                entity.getReturnedAt()
        );
    }

    @Override
    public ReservationForBookDTO mapEntityToReservationForBookDTO(Reservation entity) {
        return new ReservationForBookDTO(
                entity.getId(),
                entity.getUser().getUsername(),
                entity.getReservedAt(),
                entity.getDueDate(),
                entity.getReturnedAt()
        );
    }

    @Override
    public ReservationViewDTO mapEntityToReservationViewDTO(Reservation entity) {
        return new ReservationViewDTO(
                entity.getId(),
                entity.getBook().getTitle(),
                entity.getUser().getUsername(),
                entity.getReservedAt(),
                entity.getDueDate(),
                entity.getReturnedAt()
        );
    }

    @Override
    public List<ReservationViewDTO> mapListEntityToListViewDTO(List<Reservation> listEntities) {

        return listEntities.stream()
                .map(this::mapEntityToReservationViewDTO)
                .toList();
    }

    @Override
    public Reservation mapViewDTOToEntityCreate(ReservationViewDTO inputDTO, User inputUser, Book inputBook) {
        return new Reservation(
                0,
                inputUser,
                inputBook,
                inputDTO.reservedAt(),
                inputDTO.dueDate(),
                inputDTO.returnedAt());
    }

    @Override
    public Reservation mapByUserUpdateDTOToEntityCreate(ReservationByUserUpdateDTO inputDTO, User inputUser, Book inputBook) {
        Reservation newReservation = new Reservation();
        newReservation.setUser(inputUser);
        newReservation.setBook(inputBook);
        newReservation.setDueDate(inputDTO.dueDate());
        newReservation.setReservedAt(LocalDateTime.now());

        return newReservation;
    }

    @Override
    public void mapViewDTOToEntityForUpdate(ReservationViewDTO inputDTO, Reservation entity, User inputUser, Book inputBook) {

        entity.setBook(inputBook);
        entity.setUser(inputUser);
        entity.setReservedAt(inputDTO.reservedAt());
        entity.setReturnedAt(inputDTO.returnedAt());
        entity.setDueDate(inputDTO.dueDate());
    }


}
