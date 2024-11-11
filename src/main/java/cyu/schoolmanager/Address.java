package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@Entity
@Table(name = "address")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "number")
	@NotBlank(message = "Le numéro ne peut pas être vide")
	private String number;

	@Column(name = "street")
	@NotBlank(message = "La rue ne peut pas être vide")
	private String street;

	@Column(name = "city")
	@NotBlank(message = "La ville ne peut pas être vide")
	private String city;

	@Column(name = "postal_code")
	@Positive
	@Digits(integer = 10, fraction = 0)
	private int postalCode;

	@Column(name = "country")
	private String country;

	@CreationTimestamp // permet une gestion automatique
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", updatable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;


	public Long getId() { return id; }

	public String getNumber() { return number; }
	public void setNumber(String number) { this.number = number; }

	public String getStreet() { return street; }
	public void setStreet(String street) { this.street = street; }

	public String getCity() { return city; }
	public void setCity(String city) { this.city = city; }

	public int getPostalCode() { return postalCode; }
	public void setPostalCode(int postalCode) { this.postalCode = postalCode; }

	public String getCountry() { return country; }
	public void setCountry(String country) { this.country = country; }
	
	public Date getCreatedAt() { return createdAt; }
	public Date getUpdatedAt() { return updatedAt; }
}