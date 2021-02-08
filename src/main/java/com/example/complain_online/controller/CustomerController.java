package com.example.complain_online.controller;

import com.example.complain_online.model.CustomerCard;
import com.example.complain_online.model.Token;
import com.example.complain_online.model.User;
import com.example.complain_online.service.CustomerService;
import com.example.complain_online.service.TwitterService;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Juang Nasution
 */
@RestController
public class CustomerController {

    private final CustomerService customerService;
    private final TwitterService twitterService;

    public CustomerController(CustomerService customerService, TwitterService twitterService) {
        this.customerService = customerService;
        this.twitterService = twitterService;
    }

    @CrossOrigin
    @PostMapping("/register")
    public boolean resgistCustomer(@Valid @RequestBody CustomerCard customerCard) {
        return customerService.registCustomer(customerCard);
    }

    @CrossOrigin
    @PostMapping("/login")
    public String loginCustomer(@Valid @RequestBody User user) {
        return customerService.loginCustomer(user.getEmail(), user.getPassword());
    }

    @CrossOrigin
    @PostMapping("/activation")
    public String sendmail(@Valid @RequestBody Token token) {
        return customerService.updateActive(token);
    }

}
