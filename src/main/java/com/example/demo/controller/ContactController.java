package com.example.demo.controller;

import com.example.demo.entity.Contact;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/contact")
public class ContactController {
    @Autowired
    private ContactRepository repository;

    @PostMapping("/create")
    public Contact createContact(@RequestBody Contact contact) {
        return repository.save(contact);
    }


}
