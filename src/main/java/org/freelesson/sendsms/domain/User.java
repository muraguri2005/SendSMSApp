package org.freelesson.sendsms.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	public Long id;
	@Column(unique=true, nullable=false)
	public String username;
	
	@JsonIgnore
	@Column(nullable=false)
	public String password;
	
	public boolean isEnabled=false;
	
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	@JsonIgnore
	public Set<Role> roles = new HashSet<>();
	public User(String username, String password, boolean isEnabled) {
		this.username = username;
		this.password = password;
		this.isEnabled = isEnabled;
	}
	public User() {
		
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", roles=" + roles + ", username=" + username + ", password=" + password+ "]";
	}
	
	
}
