package com.example.BlogSpring.Repo;

import com.example.BlogSpring.Models.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Long> {

    List<Post> findByTitleContains(String title);
    List<Post> findByTitle(String title);
}