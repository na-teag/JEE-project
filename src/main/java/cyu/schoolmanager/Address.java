package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "address")
public class Address extends Model {

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