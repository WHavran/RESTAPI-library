package com.library.restapi.demo.service.impl;

import com.library.restapi.demo.exceptions.EmptyViewListException;
import com.library.restapi.demo.exceptions.EntityNotFound;
import com.library.restapi.demo.mapper.LocationMapper;
import com.library.restapi.demo.model.dto.location.LocationDetailDTO;
import com.library.restapi.demo.model.dto.location.LocationListDTO;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;
import com.library.restapi.demo.repository.LocationRepository;
import com.library.restapi.demo.service.LocationService;
import com.library.restapi.demo.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LocationServiceImpl implements LocationService {

    LocationRepository locationRepository;
    LocationMapper locationMapper;
    SupportService supportService;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationMapper locationMapper, SupportService supportService) {
        this.locationRepository = locationRepository;
        this.locationMapper = locationMapper;
        this.supportService = supportService;
    }

    @Override
    public Location findEntityById(int theId) {
        return locationRepository.findById(theId)
                .orElseThrow(() -> new EntityNotFound("Location not found"));
    }

    @Override
    public Location findEntityWithDetailsById(int theId) {
        return locationRepository.findOneWithDetailsById(theId)
                .orElseThrow(() -> new EntityNotFound("Location not found"));
    }

    @Override
    public LocationDetailDTO getOneDetailDTOById(int theId) {
        Location dbLocation = findEntityWithDetailsById(theId);
        return locationMapper.mapEntityToDetailDTO(dbLocation);
    }

    @Override
    public List<LocationListDTO> findAllLocationDTOs() {
        List<Location> listLocations = locationRepository.findAllLocationWithDetails();
        if (listLocations.isEmpty()){
            throw new EmptyViewListException();
        }
        return locationMapper.mapListEntitiesToListDTOs(listLocations);
    }

    @Override
    @Transactional
    public LocationDetailDTO createNewLocation(LocationDetailDTO inputDTO) {
        Location newLocation = locationMapper.mapDetailDTOToEntityCreate(inputDTO);
        locationRepository.save(newLocation);

        return locationMapper.mapEntityToDetailDTO(newLocation);
    }

    @Override
    @Transactional
    public LocationDetailDTO updateLocation(LocationDetailDTO inputDTO) {
        Location dbLocation = findEntityWithDetailsById(inputDTO.id());
        locationMapper.mapDetailDTOToEntityUpdate(inputDTO, dbLocation);
        locationRepository.save(dbLocation);

        return locationMapper.mapEntityToDetailDTO(dbLocation);
    }

    @Override
    @Transactional
    public LocationDetailDTO patchUpdateLocation(Map<String, Object> patchInput, int inUserId) {
        if (patchInput.containsKey("id")){
            throw new IllegalArgumentException("Id is not allowed in request body ");
        }
        Location dbLocation = findEntityWithDetailsById(inUserId);
        dbLocation = patchInputEntityMerge(patchInput, dbLocation);
        locationRepository.save(dbLocation);

        return locationMapper.mapEntityToDetailDTO(dbLocation);
    }

    @Override
    @Transactional
    public void hardDelete(int theId) {
        Location dbLocation = findEntityWithDetailsById(theId);
        if (!dbLocation.getBook().isEmpty()){
            throw new IllegalArgumentException("Cannot delete Location with stored books.");
        }
        locationRepository.deleteById(theId);
    }

    private Location patchInputEntityMerge(Map<String, Object> patchInput, Location inputLocation){

        if (patchInput.containsKey("bookTitles")){
            Object patchObject = patchInput.get("bookTitles");
            List<String> bookTitles = new ArrayList<>();
            if (patchObject instanceof List<?>) {
                for (Object o : (List<?>) patchObject) {
                    if (o instanceof String) {
                        bookTitles.add((String) o);
                    }
                }
            }
            List<Book> listBooks = bookTitles.stream()
                    .map(title -> supportService.findBookByTitle(title))
                    .toList();

            inputLocation.getBook().clear();
            inputLocation.setBook(listBooks);
            patchInput.remove("bookTitles");
        }

        return supportService.patchMergeEntity(patchInput, inputLocation, Location.class);

    }
}
