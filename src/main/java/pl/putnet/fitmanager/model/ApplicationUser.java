package pl.putnet.fitmanager.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "application_user")
public class ApplicationUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -934780151850516483L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "email", nullable = false, unique = true)
	private String email;
		
	@Column(name = "password", nullable = false)
	private String password;
		
	@Column(name = "token")
	private String token;
	
	@Column(name = "firstName")
	private String firstName;
	
	@Column(name = "lastName")
	private String lastName;
	
	@JsonIgnore
	@ManyToMany(targetEntity = Clazz.class, fetch = FetchType.LAZY)
	@JoinTable(name = "user_class", 
			  joinColumns = @JoinColumn(name = "userId", referencedColumnName = "id"), 
			  inverseJoinColumns = @JoinColumn(name = "classId", referencedColumnName = "id"))
	private Set<Clazz> classes;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "roleId", nullable = false)
	private Role role;
	
	public ApplicationUser() {
		
	}
	
	public ApplicationUser(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public ApplicationUser(String email, String password, String firstName) {
		this(email, password);
		this.firstName = firstName;
	}
	
	public ApplicationUser(String email, String password, String token, String firstName, String lastName) {
		this(email, password);
		this.token = token;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Clazz> getClasses() {
		return classes;
	}

	public void setClasses(Set<Clazz> classes) {
		this.classes = classes;
	}
	
	public void addClass(Clazz clazz) {
		classes.add(clazz);
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
}
