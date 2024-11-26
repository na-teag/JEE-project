package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;

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

	@Column(name = "username", nullable = false, unique = true)
	@NotBlank(message = "Le login ne peut pas être vide")
	@Size(min = 4, message = "Le login doit comporter au moins 4 caractères")
	private String username;

	@Column(name = "password", nullable = false)
	@NotBlank(message = "Le mot de passe ne peut pas être vide")
	@Size(min = 6, message = "Le mot de passe doit comporter au moins 6 caractères")
	private String password;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // supprime l'adresse si la personne est supprimée ou modifiée
	@JoinColumn(name = "address_id")
	@NotNull(message = "L'adresse' ne peut pas être vide")
	private Address address;

	@Column(name = "person_number", unique = true, nullable = false)
	private String personNumber;

	@Column(name = "birthday", nullable = false)
	@NotNull(message = "L'anniversaire ne peut pas être null")
	@Past
	private LocalDate birthday;



	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getUsername() { return username; }
	public void setUsername(String username) { this.username = username; }

	public String getPasswordHash() { return password; }
	public void setPassword(String password) { this.password = BCrypt.hashpw(password, BCrypt.gensalt()); }

	public Address getAddress() { return address; }
	public void setAddress(Address address) { this.address = address; }

	public void setPersonNumber() {
		if (personNumber == null){
			Session session = HibernateUtil.getSessionFactory().openSession();
			String requestMax = "SELECT MAX(p.personNumber) FROM Person p";
			Query<String> queryMax = session.createQuery(requestMax, String.class);
			String maxPersonNumber = queryMax.uniqueResult();

			session.close();
			if (maxPersonNumber == null) { // if there is no registered user
				this.personNumber = "00000001";
			} else {
				this.personNumber =  String.format("%08d", Integer.parseInt(maxPersonNumber) + 1);
			}
		}

	}

	public String getPersonNumber(){
		return this.personNumber;
	}

	public LocalDate getBirthday(){return this.birthday;}
	public void setBirthday(LocalDate birthday){this.birthday=birthday;}
}