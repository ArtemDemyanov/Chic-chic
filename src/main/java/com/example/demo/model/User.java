package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	@Column(unique = true)
	private String email;

	@NotBlank
	@Column(unique = true)
	private String telephoneNumber;

	@NotBlank
	private String password;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePicture;

	private String role;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Portfolio portfolio;

	@ManyToMany
	@JoinTable(
			name = "user_favorite_uslugas",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "usluga_id")
	)
	private List<Usluga> favoriteUslugas = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Usluga> uslugas = new ArrayList<>();

	@OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Review> writtenReviews = new ArrayList<>();

	@OneToMany(mappedBy = "reviewedUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Review> receivedReviews = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = false) // Disable orphan removal
	private List<Application> applications = new ArrayList<>();

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

	public User(String name, String email, String telephoneNumber, String password, String role, byte[] profilePicture) {
		this.name = name;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.password = password;
		this.role = role;
		this.profilePicture = profilePicture;
	}

	// Helper method to maintain bidirectional relationship
	public void addApplication(Application application) {
		applications.add(application);
		application.setUser(this);
	}

	public void removeApplication(Application application) {
		applications.remove(application);
		application.setUser(null);
	}
}