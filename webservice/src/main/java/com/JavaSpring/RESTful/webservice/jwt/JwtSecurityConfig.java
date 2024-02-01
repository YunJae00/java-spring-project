package com.JavaSpring.RESTful.webservice.jwt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.JavaSpring.RESTful.webservice.dataSource.DataSourceConfig;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
@Import(DataSourceConfig.class)
@EnableWebSecurity
@EnableMethodSecurity
public class JwtSecurityConfig {

	private final DataSource dataSource;

	@Autowired
	public JwtSecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, HandlerMappingIntrospector introspector)
			throws Exception {

		return httpSecurity
				.authorizeHttpRequests(auth -> auth.requestMatchers("/api/auth/login").permitAll()
						.requestMatchers("/api/auth/register").permitAll() // 다른 요청은 모두 허용
						.requestMatchers(new AntPathRequestMatcher("/**", "OPTIONS")).permitAll().anyRequest()
						.authenticated())
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
				.httpBasic(Customizer.withDefaults())
				.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())).build();
	}

	@Bean
	public AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		var authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		JWKSet jwkSet = new JWKSet(rsaKey());
		return (((jwkSelector, securityContext) -> jwkSelector.select(jwkSet)));
	}

	@Bean
	JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	JwtDecoder jwtDecoder() throws JOSEException {
		return NimbusJwtDecoder.withPublicKey(rsaKey().toRSAPublicKey()).build();
	}

	@Bean
	public RSAKey rsaKey() {
		KeyPair keyPair = keyPair();
		return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()).privateKey((RSAPrivateKey) keyPair.getPrivate())
				.keyID(UUID.randomUUID().toString()).build();
	}

	@Bean
	public KeyPair keyPair() {
		try {
			var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			throw new IllegalStateException("Unable to generate an RSA Key Pair", e);
		}
	}

	@Bean
	public PasswordEncoder jwtPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
