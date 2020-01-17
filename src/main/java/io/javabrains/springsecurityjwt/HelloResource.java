package io.javabrains.springsecurityjwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javabrains.springsecurityjwt.models.AuthenticationRequest;
import io.javabrains.springsecurityjwt.models.AuthenticationResponse;
import io.javabrains.springsecurityjwt.util.Jwtutil;

@RestController
public class HelloResource {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	Jwtutil jwtUtil;

	@GetMapping("/hello")
	public String hello() {
		return "hello world";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationrequest)
			throws Exception {

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationrequest.getUsername(), authenticationrequest.getPassword()));

		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username/password");
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationrequest.getUsername());
		
		final String jwt = jwtUtil.generateToken(userDetails);
		
		return new ResponseEntity(new AuthenticationResponse(jwt), HttpStatus.OK);

	}
}
