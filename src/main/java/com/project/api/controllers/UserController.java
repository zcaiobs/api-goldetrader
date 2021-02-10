package com.project.api.controllers;

import java.util.List;
import java.util.Optional;

import com.project.api.models.Bet;
import com.project.api.models.User;
import com.project.api.services.EmailService;
import com.project.api.services.TokenAuthenticationService;
import com.project.api.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {}, exposedHeaders = { "token" })
@RequestMapping("/")
public class UserController {

    @Autowired
    UserService us;

    @Autowired
    TokenAuthenticationService token;

    @Autowired
    EmailService mail;

    @PostMapping("/newbet")
    public User newBet(@RequestBody Bet bet, @RequestHeader HttpHeaders headers) {
        if(!headers.isEmpty()){
            String key = headers.getOrEmpty("key").get(0);
            String email = token.verifyAuthentication(key);
            return us.newBet(bet, email);
        }
        return null;
    }

    @PostMapping("/removebet")
    public User removeBet(@RequestBody Bet bet, @RequestHeader HttpHeaders headers) {
        if(!headers.isEmpty()){
            String key = headers.getOrEmpty("key").get(0);
            String email = token.verifyAuthentication(key);
            return us.removeBet(bet.getId(), email);
        }
        return null;
    }

    @PutMapping("/newpwd")
    public String newpwd(@RequestBody User u, @RequestHeader HttpHeaders headers) {

        if (!headers.getOrEmpty("key").isEmpty()) {
            String key = headers.getOrEmpty("key").get(0);
            String email = token.verifyAuthentication(key);

            if (us.exist(email)) {
                Optional<User> isUser = us.findOne(email);
                isUser.get().setPassword(u.getPassword());
                User user = isUser.get();
                us.save(user);
                System.out.println("Your password has been changed successfully");
                return "Your password has been changed successfully";
            } else {
                return "Error: The Token has expired";
            }
        } else {
            return "Error: User not found";
        }
    }

    @PostMapping("/forgot")
    public String sendMail(@RequestBody User u) {

        System.out.println(u.getEmail());
        if (us.exist(u.getEmail())) {

            String to = u.getEmail();
            String subject = "Reset de senha";
            String msg = "Click here to change your password: http://localhost:3000/newpwd?key="
                    + token.addAuthentication(u.getEmail(), 20L);
            return mail.sendSimpleMessage(to, subject, msg);

        } else {
            return "Email not found.";
        }

    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth(@RequestBody User acess) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if (us.auth(acess) == "valid") {
            responseHeaders.add("token", token.addAuthentication(acess.getEmail(), 60L));
            return new ResponseEntity<>("Content", responseHeaders, HttpStatus.OK);
        }
        if (us.auth(acess) == "verify") {
            responseHeaders.add("token", token.addAuthentication(acess.getEmail(), 60L));
            return new ResponseEntity<>("Content", responseHeaders, HttpStatus.ACCEPTED);
        } else {
            responseHeaders.add("token", null);
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/page")
    public ResponseEntity<Optional<User>> logIn(@RequestHeader HttpHeaders headers) {
        HttpHeaders responseHeaders = new HttpHeaders();

        if (!headers.getOrEmpty("token").isEmpty()) {
            String isToken = headers.getOrEmpty("token").get(0);
            String isUser = token.verifyAuthentication(isToken);
            if (!isUser.isEmpty() && isUser != "Error") {
                if (us.findOne(isUser).get().getVerification() == "No") {
                    responseHeaders.set("token", isToken);
                    return new ResponseEntity<>(null, responseHeaders, HttpStatus.ACCEPTED);
                } else {
                    responseHeaders.set("token", isToken);
                    return new ResponseEntity<>(us.findOne(isUser), responseHeaders, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(null, responseHeaders, HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(null, responseHeaders, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public Optional<User> find(@PathVariable(value = "id") String id) {
        return us.findOne(id);
    }

    @GetMapping
    public List<User> findAll() {
        return us.findAll();
    }

    @PostMapping("/verification")
    public String verify(@RequestHeader HttpHeaders headers) {
        if (!headers.getOrEmpty("key").isEmpty()) {
            String key = headers.getOrEmpty("key").get(0);
            String email = token.verifyAuthentication(key);
            String to = email;
            String subject = "Verificação de Email";
            String msg = "Click here to verify your account: http://localhost:3000/verify?key="
                    + token.addAuthentication(email, 20L);
            mail.sendSimpleMessage(to, subject, msg);
            return "A verification link has been sent to your email account";
        } else {
            return "Error";
        }
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody User u) {
        HttpHeaders responHeaders = new HttpHeaders();
        if (us.save(u)) {
            String to = u.getEmail();
            String subject = "Verificação de Email";
            String msg = "Click here to verify your account: http://localhost:3000/verify?key="
                    + token.addAuthentication(u.getEmail(), 20L);
            mail.sendSimpleMessage(to, subject, msg);
            return new ResponseEntity<>("User registered.", responHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User already exist.", responHeaders, HttpStatus.CONFLICT);
        }
    }

    @PutMapping
    public String verify(@RequestBody User u, @RequestHeader HttpHeaders headers) {
        if (!headers.getOrEmpty("key").isEmpty()) {
            System.out.println(headers.getOrEmpty("key").get(0));
            String isEmail = token.verifyAuthentication(headers.getOrEmpty("key").get(0));
            System.out.println(isEmail);
            Optional<User> isUser = us.findOne(isEmail);
            if (isUser.isPresent()) {
                isUser.get().setVerification(u.getVerification());
                User user = isUser.get();
                return us.update(user) ? "Account verified" : "Verification recused";
            } else {
                return "Verification recused";
            }
        } else {
            return "Request recused";
        }
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable(value = "id") String id) {
        return us.deleteOne(id);
    }
}
