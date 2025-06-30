package com.library.restapi.demo.controller;

import com.library.restapi.demo.model.dto.user.*;
import com.library.restapi.demo.service.UserService;
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
@RequestMapping("api/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> showOneUser(@PathVariable int id){
        UserDetailDTO user = userService.getOneUserWithDetailsById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/myProfile")
    public ResponseEntity<UserProfileDTO> showOneUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfileDTO userProfile = userService.getUserProfileDTO(username);
        return ResponseEntity.ok(userProfile);
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserListDTO>> showAllUsers(){
        List<UserListDTO> userList = userService.findAllUsersByDTO();
        return ResponseEntity.ok(userList);
    }

    @PostMapping("/create")
    public ResponseEntity<UserDetailDTO> createNewUser(@Valid  @RequestBody UserUpdateDTO updateDTO){
        UserDetailDTO newUser = userService.createNewUser(updateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.id())
                .toUri();

        return ResponseEntity.created(location).body(newUser);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDetailDTO> updateUser(@Valid @RequestBody UserUpdateDTO updateDTO){
        UserDetailDTO user = userService.updateUser(updateDTO);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/myProfile/update")
    public ResponseEntity<UserProfileDTO> updateUserByUser(@Valid @RequestBody UserProfileEditDTO updateDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserProfileDTO user = userService.updateUserProfileByUser(updateDTO, username);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/update/{id}")
    private ResponseEntity<UserDetailDTO> patchUpdateUser(@PathVariable int id,
                                                          @RequestBody Map<String, Object> patchInput){
        UserDetailDTO updatedUser = userService.patchUpdateUser(patchInput, id);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<Void> hardDelete (@PathVariable int id){
        userService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

}
