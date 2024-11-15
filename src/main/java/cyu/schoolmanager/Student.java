package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "student")
public class Student extends Person {

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la classe
	@JoinColumn(name = "classe_id", nullable = false)
	@NotBlank(message = "La classe ne peut pas être vide")
	private Classe classe;


	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "student_course",  // Nom de la table de jointure
			joinColumns = @JoinColumn(name = "student_id"),  // Clé étrangère vers Student
			inverseJoinColumns = @JoinColumn(name = "course_id")  // Clé étrangère vers Course
	)
	private List<Course> courses;



	public Classe getClasse() {
		return classe;
	}
	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public List<Course> getCourses() {
		return courses;
	}
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
}
