package com.JavaSpring.RESTful.webservice.letter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LetterRepository extends JpaRepository<Letter, Integer> {

	List<Letter> findByUsername(String username);
}
