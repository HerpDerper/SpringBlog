package com.example.BlogSpring.Repo;

import com.example.BlogSpring.Models.Post;
import com.example.BlogSpring.Models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findByDescriptionContains(String description);

    List<Post> findByDescription(String description);

    List<Post> findByUser(User user);
}