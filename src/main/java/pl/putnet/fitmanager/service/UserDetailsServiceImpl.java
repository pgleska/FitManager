package pl.putnet.fitmanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import pl.putnet.fitmanager.model.ApplicationUser;
import pl.putnet.fitmanager.repository.ApplicationUserRepository;
import pl.putnet.fitmanager.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final ApplicationUserRepository applicationUserRepository;
	private final RoleRepository roleRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		ApplicationUser user = applicationUserRepository.findByEmail(email);
		if(user == null) {
			throw new UsernameNotFoundException(email);
		}
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(int i = 1, v = user.getRole().getValue(); i <= v; i++) {
			authorities.add(new SimpleGrantedAuthority(roleRepository.findByValue(v).getName()));
		}
		return new User(user.getEmail(), user.getPassword(), authorities);
	}

}
