package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

@MappedSuperclass
public abstract class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "personId", nullable = false)
	private Long id;

	@Column(name = "lastName", nullable = false)
	@NotBlank(message = "Le nom de famille ne peut pas être vide")
	@Size(min = 2, message = "Le nom de famille doit comporter au moins 2 caractères")
	private String lastName;

	@Column(name = "firstName", nullable = false)
	@NotBlank(message = "Le prénom ne peut pas être vide")
	@Size(min = 2, message = "Le prénom doit comporter au moins 2 caractères")
	private String firstName;

	@Column(name = "email", nullable = false, unique = true)
	@Email(message = "L'email doit être valide")
	private String emailAddress;

	@Column(name = "login", nullable = false, unique = true)
	@NotBlank(message = "Le login ne peut pas être vide")
	@Size(min = 4, message = "Le login doit comporter au moins 4 caractères")
	private String login;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "Le mot de passe ne peut pas être vide")
	@Size(min = 6, message = "Le mot de passe doit comporter au moins 6 caractères")
	private String password;

	@OneToOne(cascade = CascadeType.ALL) // supprime l'adresse si la personne est supprimée
	@JoinColumn(name = "addressId")
	@NotNull(message = "L'adresse' ne peut pas être vide")
	private Address address;

	@CreationTimestamp // permet une gestion automatique
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;



	public Long getId() { return id; }

	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getEmailAddress() { return emailAddress; }
	public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getPasswordHash() { return password; }
	public void setPassword(String password) { this.password = BCrypt.hashpw(password, BCrypt.gensalt()); }

	public Address getAddress() { return address; }
	public void setAddress(Address address) { this.address = address; }
}