package com.example.BlogSpring.Repo;

import com.example.BlogSpring.Models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    Iterable<User> findByUsernameContains(String login);

    Iterable<User> findByUsername(String login);

    User findUserByUsername(String login);
}