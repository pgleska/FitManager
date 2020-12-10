package pl.putnet.fitmanager.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import pl.putnet.fitmanager.model.ApplicationUser;
import pl.putnet.fitmanager.model.Clazz;
import pl.putnet.fitmanager.repository.ApplicationUserRepository;
import pl.putnet.fitmanager.repository.ClazzRepository;

@RestController
@RequestMapping("/api/class")
@RequiredArgsConstructor()
public class ClazzController {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private final ApplicationUserRepository userRepository;
	private final ClazzRepository clazzRepository;
	private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH:mm");
	
	@PreAuthorize("hasAuthority('admin')")
	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Map<String, String>> createClass(@RequestBody Map<String, Object> data) {
		Map<String, String> body = new HashMap<>();
		String title = (String) data.get("title");
		Integer limit = (Integer) data.get("limit");
		String dateString = (String) data.get("date");
		String timeString = (String) data.get("time");
		LocalDate date = LocalDate.parse(dateString, df);
		LocalTime time = LocalTime.parse(timeString, dt);		
		
		Clazz clazz = new Clazz(title, limit, date, time);
		clazzRepository.save(clazz);
		body.put("status", "success.clazz.created");
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('user')")
	@PostMapping(value = "/{id}/sign-up", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, String>> signUpForClass(@PathVariable int id, Principal principal) {
		Map<String, String> body = new HashMap<>();
		ApplicationUser requester = userRepository.findByEmail(principal.getName());
		Clazz clazz = clazzRepository.findById(id).get();
		
		for(Clazz c : requester.getClasses()) {
			if(c.getDate().equals(clazz.getDate())) {
				body.put("error", "rejected.only.one.class.allowed.per.day");
				return new ResponseEntity<>(body, HttpStatus.CONFLICT);
			}
		}
		
		if(clazz.getCurrentNumberOfParticipants() < clazz.getLimit()) {
			clazz.addParticipant(requester);
			requester.addClass(clazz);		
			clazzRepository.save(clazz);
			userRepository.save(requester);
			
			body.put("status", "success.signed.up");
			return new ResponseEntity<>(body, HttpStatus.OK);
		} else {
			body.put("error", "all.spots.taken");
			return new ResponseEntity<>(body, HttpStatus.CONFLICT);
		}
	}
	
	@PreAuthorize("hasAuthority('user')")
	@DeleteMapping(value = "/{id}/sign-out", produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, String>> signOutFromClass(@PathVariable int id, Principal principal) {
		Map<String, String> body = new HashMap<>();
		ApplicationUser requester = userRepository.findByEmail(principal.getName());
		Clazz clazz = clazzRepository.findById(id).get();
		
		if(clazz.getUsers().contains(requester)) {
			clazz.getUsers().remove(requester);
			requester.getClasses().remove(clazz);
			clazzRepository.save(clazz);
			userRepository.save(requester);
			body.put("status", "success.signed.out");
			return new ResponseEntity<>(body, HttpStatus.OK);
		} else {
			body.put("error", "not.signed.for.this.class");
			return new ResponseEntity<>(body, HttpStatus.CONFLICT);
		}	
	}
	
	@PreAuthorize("hasAuthority('user')")
	@GetMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<Map<String, Object>> findClasses(@RequestBody Map<String, String> data) {
		Map<String, Object> body = new HashMap<>();		
		String dateString = data.get("date");
		if(dateString == null) {
			body.put("error", "class.missing_date");
			return new ResponseEntity<>(body, HttpStatus.CONFLICT);
		}
		LocalDate date = LocalDate.parse(dateString, df);
		List<Clazz> clazzes = clazzRepository.findByDate(date);
		Collections.sort(clazzes);		
		
		body.put("classes", clazzes);
		return new ResponseEntity<>(body, HttpStatus.OK);
	}
}
