package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "address")
public class Address extends Model {

	@Column(name = "number", nullable = false)
	@NotBlank(message = "Le numéro ne peut pas être vide")
	private String number;

	@Column(name = "street", nullable = false)
	@NotBlank(message = "La rue ne peut pas être vide")
	private String street;

	@Column(name = "city", nullable = false)
	@NotBlank(message = "La ville ne peut pas être vide")
	private String city;

	@Column(name = "postal_code", nullable = false)
	@Positive(message = "le code postal doit être positif")
	@Digits(integer = 10, fraction = 0, message = "le code postal doit être un nombre entier à 10 chiffres ou moins")
	private int postalCode;

	@Column(name = "country", nullable = false)
	private String country;



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
}