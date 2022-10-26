package com.example.BlogSpring.Repo;

import com.example.BlogSpring.Models.ContactData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactDataRepository extends CrudRepository<ContactData, Long> {

    List<ContactData> findByEmail(String email);

}