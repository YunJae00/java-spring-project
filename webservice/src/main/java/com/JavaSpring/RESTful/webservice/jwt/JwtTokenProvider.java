package com.JavaSpring.RESTful.webservice.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
	private final JwtEncoder jwtEncoder;

	public JwtTokenProvider(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	public String generateToken(Authentication authentication) {

		var scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(" "));

		var claims = JwtClaimsSet.builder().issuer("self").issuedAt(Instant.now())
				.expiresAt(Instant.now().plus(90, ChronoUnit.MINUTES)).subject(authentication.getName())
				.claim("scope", scope).build();

		return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
	}

//	@Value("${jwt.expiration}")
//	private long expiration;
//
//	@Value("${jwt.private-key}") // RSA 개인 키를 Base64로 인코딩한 값
//	private String privateKeyString;
//
//	private PrivateKey getPrivateKey() {
//		byte[] privateKeyBytes = java.util.Base64.getDecoder().decode(privateKeyString);
//		KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
//		keyPair.getPrivate().getEncoded();
//		return keyPair.getPrivate();
//	}
//
//	public String generateToken(UserDetails userDetails) {
//		Map<String, Object> claims = new HashMap<>();
//		return createToken(claims, userDetails.getUsername());
//	}
//
//	private String createToken(Map<String, Object> claims, String subject) {
//		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + expiration))
//				.signWith(SignatureAlgorithm.RS256, getPrivateKey()) // RS256 알고리즘을 사용하여 RSA 개인 키로 서명
//				.compact();
//	}

//	public boolean validateToken(String token, UserDetails userDetails) {
//		final String username = getUsernameFromToken(token);
//		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//	}
//
//	private Boolean isTokenExpired(String token) {
//		final Date expirationDate = getExpirationDateFromToken(token);
//		return expirationDate.before(new Date());
//	}
//
//	public String getUsernameFromToken(String token) {
//		return getClaimFromToken(token, Claims::getSubject);
//	}
//
//	public Date getExpirationDateFromToken(String token) {
//		return getClaimFromToken(token, Claims::getExpiration);
//	}
//
//	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
//		final Claims claims = getAllClaimsFromToken(token);
//		return claimsResolver.apply(claims);
//	}
//
//	private Claims getAllClaimsFromToken(String token) {
//		return Jwts.parser().setSigningKey(getPrivateKey()).parseClaimsJws(token).getBody();
//	}
}
