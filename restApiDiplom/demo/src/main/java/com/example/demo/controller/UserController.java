package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.resource.UserResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    UserResource get(@PathVariable Integer id) {
        User entity = userRepository.select(id);
        if (entity == null) return null;
        return new UserResource(entity);
    }

//    @GetMapping("/pw")
//    public ResponseEntity<String> getPassword(@RequestParam("login") String login) {
//        String result = userRepository.selectPasswordByLogin(login);
//        if (result == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(result);
//    }
//
//    @GetMapping("/id")
//    public ResponseEntity<Integer> getId(@RequestParam("login") String login) {
//        Integer result = userRepository.selectIdByLogin(login);
//        if (result == null) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(result);
//    }

    @GetMapping("/id")
    public ResponseEntity<User> getByLogin(@RequestParam("login") String login) {
        User result = userRepository.selectByLogin(login);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    UserResource[] getAll() {
        User[] users;
        users = userRepository.select();
        return Arrays.stream(users)
                .map(entity -> {
                    return new UserResource(entity);
                })
                .toArray(UserResource[]::new);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    UserResource post(@RequestBody UserResource resource) {
        User entity = userRepository.insert(resource.toEntity());
        if (entity == null) return null;
        return new UserResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    UserResource put(@PathVariable Integer id,
                         @RequestBody UserResource resource) {
        User entity = userRepository.update(id, resource.toEntity());
        if (entity == null) return null;
        return new UserResource(entity);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    UserResource delete(@PathVariable Integer id) {
        User entity = userRepository.delete(id);
        if (entity == null) return null;
        return new UserResource(entity);
    }
}
