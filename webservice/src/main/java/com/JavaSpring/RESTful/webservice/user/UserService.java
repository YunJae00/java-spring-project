package com.JavaSpring.RESTful.webservice.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

	private UserRepository userRepository;
	private PasswordEncoder jwtPasswordEncoder;

	public UserService(UserRepository userRepository, PasswordEncoder jwtPasswordEncoder) {
		this.userRepository = userRepository;
		this.jwtPasswordEncoder = jwtPasswordEncoder;
	}

	@Transactional
	public User registerUser(User user) {
		user.setPassword(jwtPasswordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
