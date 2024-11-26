package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "student")
public class Student extends Person {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la classe
	@JoinColumn(name = "classe_id", nullable = false)
	@NotNull(message = "La classe ne peut pas être vide")
	private Classe classe;



	public Classe getClasse() {
		return classe;
	}
	public void setClasse(Classe classe) {
		this.classe = classe;
	}

}
