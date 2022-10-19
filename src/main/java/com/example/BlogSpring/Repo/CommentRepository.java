package com.example.BlogSpring.Repo;

import com.example.BlogSpring.Models.Comment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findByTextContains(String text);
    List<Comment> findByText(String text);
}