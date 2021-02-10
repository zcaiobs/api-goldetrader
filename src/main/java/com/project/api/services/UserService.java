package com.project.api.services;

import java.util.List;
import java.util.Optional;

import com.project.api.models.Bet;
import com.project.api.models.User;
import com.project.api.repositories.UserRepository;
import com.project.api.security.MySecurityConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository ur;

    @Autowired
    MySecurityConfig sc;  

    public User newBet(Bet bet, String email) {
        User user = ur.findByEmail(email).get();
        user.getTrader().add(bet);
        ur.save(user);
        return user;
    }

    public User removeBet(String id, String email) {
        User user = ur.findByEmail(email).get();
        user.getTrader().removeIf( result -> result.getId().equals(id));
        ur.save(user);
        return user;
    }

    public String auth(User acess) {
        try {
            Optional<User> user = ur.findByEmail(acess.getEmail());
            if (user.isPresent()) {
                User u = (User) user.get();
                if (sc.passwordEncoder().matches(acess.getPassword(), u.getPassword())) {
                    return u.getVerification().equals("Yes") ? "valid" : "verify";
                } else {
                    return "false";
                }
            } else {
                return "false";
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return "false";
        }
    }

    public boolean exist(String email) {
        if (ur.existsByEmail(email)) {
            return true;
        } else {
            return false;
        }
    }

    public Optional<User> findOne(String email) {
        return ur.findByEmail(email);
    }

    public List<User> findAll() {
        return ur.findAll();
    }

    public boolean save(User u) {
        try {
            u.setPassword(sc.passwordEncoder().encode(u.getPassword()));
            ur.save(u);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }

    }

    public boolean update(User u) {
        try {
            ur.save(u);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    public String deleteOne(String id) {
        ur.deleteById(id);
        return "User deleted";
    }
}
