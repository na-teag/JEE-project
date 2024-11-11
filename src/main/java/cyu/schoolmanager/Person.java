package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.mindrot.jbcrypt.BCrypt;

@Entity
public abstract class Person extends Emailable {

	@Column(name = "last_name", nullable = false)
	@NotBlank(message = "Le nom de famille ne peut pas être vide")
	@Size(min = 2, message = "Le nom de famille doit comporter au moins 2 caractères")
	private String lastName;

	@Column(name = "first_name", nullable = false)
	@NotBlank(message = "Le prénom ne peut pas être vide")
	@Size(min = 2, message = "Le prénom doit comporter au moins 2 caractères")
	private String firstName;

	@Column(name = "login", nullable = false, unique = true)
	@NotBlank(message = "Le login ne peut pas être vide")
	@Size(min = 4, message = "Le login doit comporter au moins 4 caractères")
	private String login;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "Le mot de passe ne peut pas être vide")
	@Size(min = 6, message = "Le mot de passe doit comporter au moins 6 caractères")
	private String password;

	@OneToOne(cascade = CascadeType.ALL) // supprime l'adresse si la personne est supprimée
	@JoinColumn(name = "address_id")
	@NotNull(message = "L'adresse' ne peut pas être vide")
	private Address address;



	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getLogin() { return login; }
	public void setLogin(String login) { this.login = login; }

	public String getPasswordHash() { return password; }
	public void setPassword(String password) { this.password = BCrypt.hashpw(password, BCrypt.gensalt()); }

	public Address getAddress() { return address; }
	public void setAddress(Address address) { this.address = address; }
}