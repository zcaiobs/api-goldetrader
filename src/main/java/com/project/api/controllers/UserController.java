package com.project.api.controllers;

import java.util.List;
import java.util.Optional;

import com.project.api.models.User;
import com.project.api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

    @Autowired
    UserRepository ur;

    @GetMapping("/{id}")
    public Optional<User> find(@PathVariable (value = "id") String id) {
        return ur.findById(id);
    }

    @GetMapping
    public List<User> findAll() {
        return ur.findAll();
    }

    @PostMapping
    public String save(@RequestBody User u) {
        ur.save(u);
        return "User saved.";
    }

    @PutMapping
    public String update(@RequestBody User u) {
        ur.save(u);
        return "User updated.";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable (value = "id") String id) {
        ur.deleteById(id);
        return "User removed.";
    }
}
