package com.library.restapi.demo.service;

import com.library.restapi.demo.model.dto.location.LocationDetailDTO;
import com.library.restapi.demo.model.dto.location.LocationListDTO;
import com.library.restapi.demo.model.entity.Location;

import java.util.List;
import java.util.Map;

public interface LocationService {

    Location findEntityById(int theId);

    Location findEntityWithDetailsById(int theId);

    LocationDetailDTO getOneDetailDTOById(int theId);

    List<LocationListDTO> findAllLocationDTOs();

    LocationDetailDTO createNewLocation(LocationDetailDTO inputDTO);

    LocationDetailDTO updateLocation(LocationDetailDTO inputDTO);

    LocationDetailDTO patchUpdateLocation(Map<String, Object> patchInput, int inUserId);

    void hardDelete(int theId);

}
