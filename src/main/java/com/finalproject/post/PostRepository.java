package com.finalproject.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.finalproject.models.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	 
	}
