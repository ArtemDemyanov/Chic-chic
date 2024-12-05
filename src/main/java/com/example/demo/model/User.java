package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Пользователи")
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@lombok.Setter
	@lombok.Getter
	@NotBlank
	private String name;

	@lombok.Setter
	@lombok.Getter
	@NotBlank
	@Column(unique=true)
	private String email;

	@lombok.Setter
	@lombok.Getter
	@NotBlank
	private String telephone_number;

	@lombok.Setter
	@lombok.Getter
	@NotBlank
	private String password;

	@lombok.Setter
	@lombok.Getter
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;

	@lombok.Setter
	@lombok.Getter
	private String role;

	@lombok.Setter
	@lombok.Getter
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private Portfolio portfolio;

	@lombok.Getter
	@lombok.Setter
	@ManyToMany
	@JoinTable(
			name = "Избранное",
			joinColumns = @JoinColumn(name = "пользватель_id"),
			inverseJoinColumns = @JoinColumn(name = "услуга_id")
	)
	private List<Usluga> favoriteUslugas = new ArrayList<>();

	@lombok.Setter
	@lombok.Getter
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<Usluga> uslugas = new ArrayList<>();

	@lombok.Setter
	@lombok.Getter
	@OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Review> writtenReviews = new ArrayList<>();

	@lombok.Setter
	@lombok.Getter
	@OneToMany(mappedBy = "reviewedUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Review> receivedReviews = new ArrayList<>();

	@Column(nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	private Date createdAt;

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	private Date updatedAt;

	public User() {
		super();
	}

	public User(String name, String email, String telephone_number, String password, String role, byte[] profilePicture, List<Usluga> uslugas, List<Usluga> favoriteUslugas) {
		super();
		this.name = name;
		this.email = email;
		this.telephone_number = telephone_number;
		this.password = password;
		this.role = role;
		if (role.equals("мастер")){
			this.portfolio = new Portfolio();
		}
		this.profilePicture = profilePicture;
		this.uslugas = uslugas;
		this.favoriteUslugas = favoriteUslugas;
	}

}
