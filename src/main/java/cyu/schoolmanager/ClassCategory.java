package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "class_category")
public class ClassCategory extends Model {

	@Column(name = "nom")
	@NotBlank(message = "le nom ne peut pas être vide")
	private String name;

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
}
