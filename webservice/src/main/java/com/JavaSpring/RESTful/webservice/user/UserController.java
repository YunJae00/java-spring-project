package com.JavaSpring.RESTful.webservice.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.JavaSpring.RESTful.webservice.jwt.JwtTokenProvider;
import com.JavaSpring.RESTful.webservice.jwt.JwtTokenResponse;
import com.JavaSpring.RESTful.webservice.jwt.JwtUserDetailsService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	private final AuthenticationManager authenticationManager;
	private final JwtTokenProvider jwtTokenProvider;
	private final JwtUserDetailsService userDetailsService;
	private final UserService userService;

	public UserController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
			JwtUserDetailsService userDetailsService, UserService userService) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;
		this.userDetailsService = userDetailsService;
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		try {
			// 사용자 중복 체크
			if (userService.findByUsername(user.getUsername()) != null) {
				return ResponseEntity.badRequest().body("Username is already taken");
			}

			// 사용자 등록
			userService.registerUser(user);
			return ResponseEntity.ok("User registered successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Failed to register user");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		try {

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			System.out.println(loginRequest.getUsername());
			System.out.println(loginRequest.getPassword());

			// SecurityContextHolder에 인증 정보 설정
			SecurityContextHolder.getContext().setAuthentication(authentication);

			// UserDetails를 가져옴
			// UserDetails userDetails =
			// userDetailsService.loadUserByUsername(loginRequest.getUsername());
			// System.out.println(userDetails);

			// JWT 토큰 생성
			String token = jwtTokenProvider.generateToken(authentication);

			return ResponseEntity.ok(new JwtTokenResponse(token));
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("Invalid credentials");
		}
	}

}
