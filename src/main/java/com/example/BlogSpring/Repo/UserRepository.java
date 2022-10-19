package com.example.BlogSpring.Repo;

import com.example.BlogSpring.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    Iterable<User> findByEmailContains(String email);
    Iterable<User> findByEmail(String email);
}
