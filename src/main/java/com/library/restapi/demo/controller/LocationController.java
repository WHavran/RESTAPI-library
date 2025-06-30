package com.library.restapi.demo.controller;

import com.library.restapi.demo.model.dto.location.LocationDetailDTO;
import com.library.restapi.demo.model.dto.location.LocationListDTO;
import com.library.restapi.demo.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/location")
public class LocationController {

    LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDetailDTO> getOneLocation(@PathVariable int id){
        LocationDetailDTO location = locationService.getOneDetailDTOById(id);
        return ResponseEntity.ok(location);
    }

    @GetMapping("/all")
    public ResponseEntity<List<LocationListDTO>> getAllLocation(){
        List<LocationListDTO> locationList = locationService.findAllLocationDTOs();
        return ResponseEntity.ok(locationList);
    }

    @PostMapping("/create")
    public ResponseEntity<LocationDetailDTO> createNewLocation(@Valid @RequestBody LocationDetailDTO detailDTO){
        LocationDetailDTO newLocation = locationService.createNewLocation(detailDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLocation.id())
                .toUri();

        return ResponseEntity.created(location).body(newLocation);
    }

    @PutMapping("/update")
    public ResponseEntity<LocationDetailDTO> updateLocation(@Valid @RequestBody LocationDetailDTO detailDTO){
        LocationDetailDTO updatedLocation = locationService.updateLocation(detailDTO);
        return ResponseEntity.ok(updatedLocation);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<LocationDetailDTO> patchUpdateLocation(@PathVariable int id,
                                                                 @RequestBody Map<String, Object> patchInput){
        LocationDetailDTO updatedLocation = locationService.patchUpdateLocation(patchInput, id);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> hardDelete(@PathVariable int id){
        locationService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }


}
