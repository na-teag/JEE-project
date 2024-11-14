package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "professor")
public class Professor extends Person {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "professor_status_id", referencedColumnName = "id", nullable = false) // Utilisation de la clé étrangère
	@NotBlank(message = "le status ne peut pas être vide")
	private ProfessorStatus status;

	@ManyToMany
	@JoinTable(
		name = "professor_subject",  // Nom de la table de jointure
		joinColumns = @JoinColumn(name = "professor_id"),  // Clé étrangère vers Professor
		inverseJoinColumns = @JoinColumn(name = "subject_id")  // Clé étrangère vers Course
	)
	private List<Subject> teachingSubjects;


	public List<Subject> getTeachingSubjects() {return teachingSubjects;}
	public void setTeachingSubjects(List<Subject> teachingSubjects) {this.teachingSubjects = teachingSubjects;}

	public ProfessorStatus getStatus() {return status;}
	public void setStatus(ProfessorStatus status) {this.status = status;}
}
