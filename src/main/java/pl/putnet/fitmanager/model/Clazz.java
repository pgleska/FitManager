package pl.putnet.fitmanager.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "class")
public class Clazz implements Comparable<Clazz>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 740386673915998765L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private Integer id;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "limit_p", nullable = false)
	private Integer limit;
		
	@Column(name = "date_w", nullable = false)
	private LocalDate date;
	
	@Column(name = "time_w", nullable = false)
	private LocalTime time;
	
	@JsonIgnore
	@ManyToMany(targetEntity = ApplicationUser.class, mappedBy = "classes", fetch = FetchType.LAZY)
	private Set<ApplicationUser> users;
	
	@Transient
	private List<String> participants;
	
	public void addParticipant(ApplicationUser user) {
		users.add(user);
	}
	
	public Clazz() {
		
	}
	
	public Clazz(String title, Integer limit, LocalDate date, LocalTime time ) {
		this.title = title;
		this.limit = limit;
		this.date = date;
		this.time = time;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getCurrentNumberOfParticipants() {
		return getParticipants().size();
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public List<String> getParticipants() {
		participants = new ArrayList<>();		
		for(ApplicationUser u : users) {
			StringBuilder sb = new StringBuilder();
			String fn = u.getFirstName();
			if(fn != null) {
				sb.append(fn);				
			} else {
				sb.append("NN");
			}
			participants.add(sb.toString());
		}
		return participants;
	}
	
	public Set<ApplicationUser> getUsers() {
		return users;
	}

	@Override
	public int compareTo(Clazz that) {
		if(this.getDate().isBefore(that.getDate())) {
			return -1;
		} else if (this.getDate().isAfter(that.getDate())) {
			return 1;
		} else {
			if(this.getTime().isBefore(that.getTime())) {
				return -1;
			} else if (this.getTime().isAfter(that.getTime())) {
				return 1;
			} else {
				return this.title.compareTo(that.getTitle());
			}
		}
	}
}
