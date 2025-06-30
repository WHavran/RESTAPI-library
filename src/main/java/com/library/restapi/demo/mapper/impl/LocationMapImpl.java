package com.library.restapi.demo.mapper.impl;

import com.library.restapi.demo.mapper.LocationMapper;
import com.library.restapi.demo.model.dto.location.LocationDetailDTO;
import com.library.restapi.demo.model.dto.location.LocationListDTO;
import com.library.restapi.demo.model.entity.Book;
import com.library.restapi.demo.model.entity.Location;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LocationMapImpl implements LocationMapper {
    @Override
    public LocationDetailDTO mapEntityToDetailDTO(Location entity) {

        List<String> bookTitles = Optional.ofNullable(entity.getBook())
                .orElse(List.of())
                .stream()
                .map(Book::getTitle)
                .toList();

        return new LocationDetailDTO(
                entity.getId(),
                entity.getFloor(),
                entity.getShelf(),
                entity.getSection(),
                bookTitles
        );
    }

    @Override
    public LocationListDTO mapEntityToListDTO(Location entity) {
        int numberOfStoredBooks = entity.getBook().size();
        return new LocationListDTO(
                entity.getId(),
                entity.getFloor(),
                entity.getShelf(),
                entity.getSection(),
                numberOfStoredBooks
        );

    }

    @Override
    public List<LocationListDTO> mapListEntitiesToListDTOs(List<Location> listEntities) {
        return listEntities.stream()
                .map(this::mapEntityToListDTO)
                .toList();
    }

    @Override
    public Location mapDetailDTOToEntityCreate(LocationDetailDTO inputDTO) {
        return new Location(
                inputDTO.shelf(),
                inputDTO.section(),
                inputDTO.floor()
        );

    }

    @Override
    public void mapDetailDTOToEntityUpdate(LocationDetailDTO inputDTO, Location entity) {
        entity.setFloor(inputDTO.floor());
        entity.setShelf(inputDTO.shelf());
        entity.setSection(inputDTO.section());
    }
}
