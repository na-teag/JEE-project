package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.awt.*;

@Entity
@Table(name = "class_category")
public class ClassCategory extends Model {

	@Column(name = "nom", nullable = false)
	@NotBlank(message = "le nom ne peut pas être vide")
	private String name;

	@Column(name = "color", nullable = false)
	@NotBlank(message = "la couleur ne peut pas être vide")
	@Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "La couleur doit être un code hexadécimal valide de 6 caractères.")
	private String color;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }
}
