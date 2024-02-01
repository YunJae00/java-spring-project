package com.JavaSpring.RESTful.webservice.letter;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class LetterService {

	private LetterRepository letterRepository;

	public LetterService(LetterRepository letterRepository) {
		this.letterRepository = letterRepository;
	}

	public List<Letter> findByUsername(String username) {
		return letterRepository.findByUsername(username);
	}

	public void deleteById(int id) {
		letterRepository.deleteById(id);
	}

	public Letter findById(int id) {
		return letterRepository.findById(id).get();
	}

	public void updateLetter(Letter letter) {
		letterRepository.save(letter);
	}

	public Letter addLetter(String username, Letter letter) {
		letter.setUsername(username);
		letter.setId(null);
		return letterRepository.save(letter);
	}
}
