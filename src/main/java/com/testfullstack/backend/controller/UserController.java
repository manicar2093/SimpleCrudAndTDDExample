package com.testfullstack.backend.controller;

import com.testfullstack.backend.entity.User;
import com.testfullstack.backend.exception.UserAlreadyExists;
import com.testfullstack.backend.exception.UserNotExists;
import com.testfullstack.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("/registry")
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<?> userRegistry(@RequestBody User user) {

        Map<String,Object> response = new HashMap<>();

        try {

            User user_saved = userService.registryUser(user);
            response.put("message",String.format("User %s has been registrated", user.getUsername()));
            response.put("id",user_saved.getId());
            return new ResponseEntity<Map>(response, HttpStatus.CREATED);

        }catch (UserAlreadyExists e){

            response.put("message",String.format("User %s has not been registrated. It already exists.", user.getUsername()));
            return new ResponseEntity<Map>(response, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/details/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        User user_storaged = null;

        try {

            user_storaged = userService.getUserInformation(id);
            response.put("user", user_storaged);
            return new ResponseEntity<Map>(response, HttpStatus.OK);

        }catch(UserNotExists e){
            response.put("message",String.format("User with id %s does not exist.",id));
            return new ResponseEntity<Map>(response, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user_data){

        Map<String, Object> response = new HashMap<>();

        try {

            User user_updated = userService.modifyUser(id, user_data);
            response.put("message",String.format("User with id %s has been modified.", id));
            response.put("user", user_updated);
            return new ResponseEntity<Map>(response, HttpStatus.CREATED);

        }catch (UserNotExists e){

            response.put("message",String.format("User with id %s does not exist.", id));
            return new ResponseEntity<Map>(response, HttpStatus.BAD_REQUEST);

        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){

        Map<String, Object> response = new HashMap<>();

        boolean is_deleted = userService.deleteUser(id);

        if(is_deleted){

            response.put("message", String.format("User with id %s has been deleted.",id));
            return new ResponseEntity<Map>(response, HttpStatus.OK);

        }

        response.put("message", String.format("User with id %s has not been deleted. User does not exist.",id));
        return new ResponseEntity<Map>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        Map<String, Object> response = new HashMap<>();

        List<User> users = userService.getAllUsers();

        if(users == null || users.isEmpty()){
            users = new ArrayList<>();
        }

        response.put("users",users);
        return new ResponseEntity<Map>(response, HttpStatus.OK);
    }


}
