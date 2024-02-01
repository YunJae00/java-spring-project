package com.JavaSpring.RESTful.webservice.letter;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Letter {

	public Letter() {

	}

	public Letter(Integer id, String username, String createUsername, String details, LocalDate creationDate) {
		super();
		this.id = id;
		this.username = username;
		this.createUsername = createUsername;
		this.details = details;
		this.creationDate = creationDate;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "letter_seq_generator")
	@SequenceGenerator(name = "letter_seq_generator", sequenceName = "letter_table_seq", allocationSize = 1)
	private Integer id;

	private String username;

	private String createUsername;

	private String details;

	private LocalDate creationDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCreateUsername() {
		return createUsername;
	}

	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDate getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		return "Letter [id=" + id + ", username=" + username + ", createUsername=" + createUsername + ", details="
				+ details + ", creationDate=" + creationDate + "]";
	}

}
