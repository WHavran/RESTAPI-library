package com.library.restapi.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.restapi.demo.exceptions.EmptyViewListException;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.exceptions.ReservationConflictException;
import com.library.restapi.demo.mapper.ReservationMapper;
import com.library.restapi.demo.model.dto.reservation.ReservationByUserUpdateDTO;
import com.library.restapi.demo.model.dto.reservation.ReservationViewDTO;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Reservation;
import com.library.restapi.demo.model.entity.User;
import com.library.restapi.demo.repository.ReservationRepository;
import com.library.restapi.demo.service.ReservationService;
import com.library.restapi.demo.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    ReservationMapper reservationMapper;
    ObjectMapper objectMapper;
    SupportService supportService;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository, ReservationMapper reservationMapper,
                                  ObjectMapper objectMapper, SupportService supportService) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.objectMapper = objectMapper;
        this.supportService = supportService;
    }

    @Override
    public Reservation findEntityById(int theId) {
        return reservationRepository.findById(theId)
                .orElseThrow(EntityNotFound::new);
    }

    @Override
    public ReservationViewDTO getReservationViewDTO(int theId) {
        Reservation entity = findEntityById(theId);
        return reservationMapper.mapEntityToReservationViewDTO(entity);
    }

    @Override
    public List<ReservationViewDTO> getListAll() {
        List<Reservation> dbList = reservationRepository.findAll();
        if (dbList.isEmpty()){
            throw new EmptyViewListException();
        }
        return reservationMapper.mapListEntityToListViewDTO(dbList);
    }

    @Override
    public List<ReservationViewDTO> getListByIsReturned(boolean isReturned) {
        List<Reservation> dbList= reservationRepository.findByReservedAtNullOrNotNull(isReturned);
        if (dbList.isEmpty()){
            throw new EmptyViewListException();
        }
        return reservationMapper.mapListEntityToListViewDTO(dbList);
    }

    @Override
    public List<ReservationViewDTO> getListByUsername(String username) {
        supportService.checkIfAuthorExistByUsername(username);
        List<Reservation> dbList = reservationRepository.findByUsername(username.trim());
        if (dbList.isEmpty()){
            throw new EmptyViewListException();
        }
        return reservationMapper.mapListEntityToListViewDTO(dbList);
    }

    @Override
    public List<ReservationViewDTO> getListByBookTitle(String bookTitle) {
        supportService.checkIfBookExistByBookTitle(bookTitle);
        List<Reservation> dbList = reservationRepository.findByBookTitle(bookTitle.trim());
        if (dbList.isEmpty()){
            throw new EmptyViewListException();
        }
        return reservationMapper.mapListEntityToListViewDTO(dbList);
    }

    @Override
    public ReservationViewDTO createNew(ReservationViewDTO inputDTO) {
        Book dbBook = supportService.findBookByTitle(inputDTO.bookTitle());
        boolean bookIsReserved = supportService.checkIfBookHasActiveReservation(dbBook.getId());
        if (bookIsReserved){
            throw new ReservationConflictException("Reservation failed: book is already reserved");
        }

        User dbUser = supportService.findUserByName(inputDTO.username());
        Reservation newEntity = reservationMapper.mapViewDTOToEntityCreate(inputDTO, dbUser, dbBook);
        reservationRepository.save(newEntity);
        return reservationMapper.mapEntityToReservationViewDTO(newEntity);
    }

    @Override
    public ReservationViewDTO createNewByUser(ReservationByUserUpdateDTO inputDTO, String userName) {
        Book dbBook = supportService.findBookByTitle(inputDTO.bookTitle());
        boolean bookIsReserved = supportService.checkIfBookHasActiveReservation(dbBook.getId());
        if (bookIsReserved){
            throw new ReservationConflictException("Reservation failed: book is already reserved");
        }
        User dbUser = supportService.findUserByName("alice_w");
        Reservation newEntity = reservationMapper.mapByUserUpdateDTOToEntityCreate(inputDTO, dbUser, dbBook);
        reservationRepository.save(newEntity);
        return reservationMapper.mapEntityToReservationViewDTO(newEntity);
    }

    @Override
    public ReservationViewDTO updateExist(ReservationViewDTO inputDTO) {
        Reservation dbReservation = findEntityById(inputDTO.id());
        Book dbBook = supportService.findBookByTitle(inputDTO.bookTitle());
        User dbUser = supportService.findUserByName(inputDTO.username());
        reservationMapper.mapViewDTOToEntityForUpdate(inputDTO, dbReservation, dbUser, dbBook);
        reservationRepository.save(dbReservation);
        return reservationMapper.mapEntityToReservationViewDTO(dbReservation);

    }

    @Override
    public ReservationViewDTO patchUpdate(Map<String, Object> patchInput, int reservationId) {

        if (patchInput.containsKey("id")){
            throw new IllegalArgumentException("Id is not allowed in request body ");
        }

        Reservation dbReservation = findEntityById(reservationId);
        dbReservation = patchInputEntityMerge(patchInput, dbReservation);

        reservationRepository.save(dbReservation);

        return reservationMapper.mapEntityToReservationViewDTO(dbReservation);

    }

    @Override
    public void hardDelete(int theId) {
        Reservation reservation = findEntityById(theId);
        if (reservation.getReturnedAt() == null){
            throw new ReservationConflictException("Reservation has to be closed before delete");
        }
        reservationRepository.delete(reservation);

    }

    private Reservation patchInputEntityMerge(Map<String, Object> patchInput, Reservation inputReservation){

        if (patchInput.containsKey("bookTitle")){
            String bookTitle = (String) patchInput.get("bookTitle");
            Book dbBook = supportService.findBookByTitle(bookTitle);
            inputReservation.setBook(dbBook);
            patchInput.remove("bookTitle");
        }
        if (patchInput.containsKey("username")){
            String username = (String) patchInput.get("username");
            User dbUser = supportService.findUserByName(username);
            inputReservation.setUser(dbUser);
            patchInput.remove("username");
        }

        return supportService.patchMergeEntity(patchInput, inputReservation, Reservation.class);

    }
}
