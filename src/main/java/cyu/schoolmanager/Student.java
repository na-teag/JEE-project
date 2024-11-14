package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@Entity
@Table(name = "student")
public class Student extends Person {

	@Column(name = "student_number", unique = true, nullable = false)
	@NotBlank
	@Pattern(regexp = "^[0-9]+$")
	private String studentNumber;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la classe
	@JoinColumn(name = "classe_id", nullable = false)
	@NotBlank(message = "La classe ne peut pas être vide")
	private Classe classe;


	@ManyToMany
	@JoinTable(
			name = "student_course",  // Nom de la table de jointure
			joinColumns = @JoinColumn(name = "student_id"),  // Clé étrangère vers Student
			inverseJoinColumns = @JoinColumn(name = "course_id")  // Clé étrangère vers Course
	)
	private List<Course> courses;


	public String getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

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
