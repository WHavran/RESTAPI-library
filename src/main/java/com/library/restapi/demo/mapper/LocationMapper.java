package com.library.restapi.demo.mapper;

import com.library.restapi.demo.model.dto.location.LocationDetailDTO;
import com.library.restapi.demo.model.dto.location.LocationListDTO;
import com.library.restapi.demo.model.entity.Location;

import java.util.List;

public interface LocationMapper {

    LocationDetailDTO mapEntityToDetailDTO(Location entity);

    LocationListDTO mapEntityToListDTO(Location entity);

    List<LocationListDTO> mapListEntitiesToListDTOs(List<Location> listEntities);

    Location mapDetailDTOToEntityCreate(LocationDetailDTO inputDTO);

    void mapDetailDTOToEntityUpdate(LocationDetailDTO inputDTO, Location entity);

}
