package com.example.demo.service;

import com.example.demo.entity.Contact;
import com.example.demo.messaging.AppEvent;
import com.example.demo.messaging.DomainEventPublisher;
import com.example.demo.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {
    @Autowired
    public ContactRepository contactRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public Contact createContact(Contact contact) {
        Contact saved = contactRepository.save(contact);
        domainEventPublisher.publishAppEvent(
                AppEvent.of(String.valueOf(saved.getId()), "CONTACT_CREATED", saved));
        return saved;
    }
}

