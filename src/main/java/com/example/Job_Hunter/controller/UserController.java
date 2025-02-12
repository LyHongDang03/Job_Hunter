package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.domain.Entity.User;
import com.example.Job_Hunter.domain.request.UpdateUserReq;
import com.example.Job_Hunter.domain.response.ResCreateUserDTO;
import com.example.Job_Hunter.domain.response.ResUpdateUserDTO;
import com.example.Job_Hunter.domain.response.ResultPaginationDTO;
import com.example.Job_Hunter.service.UserService;
import com.example.Job_Hunter.utill.anotation.ApiMessage;
import com.example.Job_Hunter.utill.exception.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody User user) throws IdInvalidException {
       if (userService.isEmailExist(user.getEmail())) {
           throw new IdInvalidException("Email already exists");
       }
        User convert = userService.createUser(user);
        return ResponseEntity.ok(userService.convertToResCreateUserDTO(convert));
    }
    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(
            @Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.ok(userService.getAllUsers(spec, pageable));
    }
    @GetMapping("/users/{id}")
    public ResponseEntity<ResCreateUserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(userService.convertToResCreateUserDTO(user));
    }
    @PutMapping("/users")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody UpdateUserReq user) throws IdInvalidException {
        User convert = userService.updateUser(user);
        return ResponseEntity.ok(userService.convertToResUpdateUserDTO(convert));
    }
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) throws IdInvalidException {
        userService.deleteUser(id);
        return ResponseEntity.ok("deleted");
    }
}
