package com.JavaSpring.RESTful.webservice.letter;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LetterJpaResource {

	private LetterService letterService;

	public LetterJpaResource(LetterService letterService) {
		this.letterService = letterService;
	}

	@GetMapping("/users/{username}/letters")
	public List<Letter> retrieveLetters(@PathVariable String username) {
		return letterService.findByUsername(username);
	}

	@GetMapping("/users/{username}/letters/{id}")
	public Letter retrieveLetter(@PathVariable String username, @PathVariable int id) {
		return letterService.findById(id);
	}

	@DeleteMapping("/users/{username}/letters/{id}")
	public ResponseEntity<Void> deleteLetter(@PathVariable String username, @PathVariable int id) {
		letterService.deleteById(id);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/users/{username}/letters/{id}")
	public Letter updateLetter(@PathVariable String username, @PathVariable int id, @RequestBody Letter letter) {
		letterService.updateLetter(letter);
		return letter;
	}

	@PostMapping("/users/{username}/letters")
	public Letter createTodo(@PathVariable String username, @RequestBody Letter letter) {
		return letterService.addLetter(username, letter);
	}
}
