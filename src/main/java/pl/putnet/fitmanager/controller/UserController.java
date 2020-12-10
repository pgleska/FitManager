package pl.putnet.fitmanager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.putnet.fitmanager.model.ApplicationUser;
import pl.putnet.fitmanager.repository.ApplicationUserRepository;
import pl.putnet.fitmanager.repository.RoleRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
	
	private final ApplicationUserRepository userRepository;
	private final RoleRepository roleRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<String, String>> signUp(@RequestBody ApplicationUser user) {
		Map<String, String> body = new HashMap<>();
		if(userRepository.findByEmail(user.getEmail()) == null) {
			user.setRole(roleRepository.findByName("user"));
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));						
	    	userRepository.save(user);
	    	body.put("status", "user.created");
	    	return new ResponseEntity<>(body, HttpStatus.CREATED);
		} else {
			body.put("error", "user.duplicated");
			return new ResponseEntity<>(body, HttpStatus.CONFLICT);
		}
	}
	
	@PreAuthorize("hasAuthority('admin')")
	@GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ApplicationUser> getAll(){	
		return (List<ApplicationUser>) userRepository.findAll();
	}
}
