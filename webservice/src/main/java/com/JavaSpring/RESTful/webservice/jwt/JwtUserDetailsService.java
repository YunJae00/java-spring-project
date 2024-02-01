package com.JavaSpring.RESTful.webservice.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.JavaSpring.RESTful.webservice.user.User;
import com.JavaSpring.RESTful.webservice.user.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordEncoder jwtPasswordEncoder;

	public JwtUserDetailsService(UserRepository userRepository, PasswordEncoder jwtPasswordEncoder) {
		this.userRepository = userRepository;
		this.jwtPasswordEncoder = jwtPasswordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}

		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		return org.springframework.security.core.userdetails.User.builder().username(user.getUsername())
				.password(user.getPassword()) // 이미 해싱된 비밀번호 사용
				.roles("USER").build();

	}

}
