package pl.putnet.fitmanager.security;

import static pl.putnet.fitmanager.security.SecurityConstants.HEADER_STRING;
import static pl.putnet.fitmanager.security.SecurityConstants.SECRET;
import static pl.putnet.fitmanager.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import pl.putnet.fitmanager.model.ApplicationUser;
import pl.putnet.fitmanager.repository.ApplicationUserRepository;
import pl.putnet.fitmanager.repository.RoleRepository;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private final ApplicationUserRepository applicationUserRepository;	
	private final RoleRepository roleRepository;
	
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, ApplicationUserRepository applicationUserRepository, RoleRepository roleRepository) {
		super(authenticationManager);
		this.applicationUserRepository = applicationUserRepository;
		this.roleRepository = roleRepository;
	}
	
	@Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }
	
	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
        	token = token.replace(TOKEN_PREFIX, "");
        	
            // parse the token.
            String user = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
                                    
            if (user != null) {
            	ApplicationUser applicationUser = applicationUserRepository.findByEmail(user);
            	if(applicationUser != null)
            		if(applicationUser.getToken() != null)
		            	if(applicationUser.getToken().equals(token)) {
		            		List<GrantedAuthority> authorities = new ArrayList<>();
		            		for(int i = 1, v = applicationUser.getRole().getValue(); i <= v; i++) {
		            			authorities.add(new SimpleGrantedAuthority(roleRepository.findByValue(v).getName()));
		            		}
		            		return new UsernamePasswordAuthenticationToken(user, null, authorities);
		            	}
            }
            return null;
        }
        return null;
    }
}
