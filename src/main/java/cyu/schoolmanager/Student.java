package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "student")
public class Student extends Person {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la classe
	@JoinColumn(name = "classe_id", nullable = false)
	@NotBlank(message = "La classe ne peut pas être vide")
	private Classe classe;



	public Classe getClasse() {
		return classe;
	}
	public void setClasse(Classe classe) {
		// TODO envoyer un mail pour signaler le changement
		this.classe = classe;
	}

}
