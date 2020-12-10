package pl.putnet.fitmanager.security;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static pl.putnet.fitmanager.security.SecurityConstants.HEADER_STRING;
import static pl.putnet.fitmanager.security.SecurityConstants.SECRET;
import static pl.putnet.fitmanager.security.SecurityConstants.TOKEN_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import pl.putnet.fitmanager.model.ApplicationUser;
import pl.putnet.fitmanager.repository.ApplicationUserRepository;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;
	private ApplicationUser creds;
	private ApplicationUserRepository applicationUserRepository;
	
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, ApplicationUserRepository employeeRepository) {
		this.authenticationManager = authenticationManager;
		this.applicationUserRepository = employeeRepository;
	}
	
	@Override
    public Authentication attemptAuthentication(HttpServletRequest req,
            									HttpServletResponse res) throws AuthenticationException {
        try {
        	creds = new ObjectMapper()
                    .readValue(req.getInputStream(), ApplicationUser.class);

            return authenticationManager.authenticate(
                    (Authentication) new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
	
	@Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
    	
        String token = JWT.create()
                .withSubject(((User)auth.getPrincipal()).getUsername())
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        
        ApplicationUser applicationUser = applicationUserRepository.findByEmail(creds.getEmail());
        applicationUser.setToken(token);
        applicationUserRepository.save(applicationUser);
        
        String body = "{\"token\" : \""+ token + "\"}";
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(body);
        res.getWriter().flush();
        res.getWriter().close();
    }
}
