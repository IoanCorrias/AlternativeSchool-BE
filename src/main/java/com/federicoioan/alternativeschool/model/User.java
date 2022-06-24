package com.federicoioan.alternativeschool.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private String surname;

	private String username;

	private String email;

	@JsonIgnore
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(	name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="name")
	@JsonIdentityReference(alwaysAsId=true)
	private Set<Role> roles = new HashSet<>();

	@OneToMany(mappedBy = "student")
	@JsonIgnore
	Set<CourseEnrollment> enrolledCourses;

	@OneToMany(mappedBy = "sender")
	@JsonIgnore
	Set<ChatMessage> sendedMessages;

	@OneToMany(mappedBy = "receiver")
	@JsonIgnore
	Set<ChatMessage> receivedMessages;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dateInsert;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime dateUpdate;

	/**
	 * Constructor used for registration
	 * @param name
	 * @param surname
	 * @param username
	 * @param email
	 * @param encodedPassword
	 */
	public User(String name, String surname, String username, String email, String encodedPassword) {
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = encodedPassword;
	}
}
