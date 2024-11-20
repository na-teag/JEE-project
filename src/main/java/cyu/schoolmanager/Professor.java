package cyu.schoolmanager;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "professor")
public class Professor extends Person {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
		name = "professor_subject",  // Nom de la table de jointure
		joinColumns = @JoinColumn(name = "professor_id"),  // Clé étrangère vers Professor
		inverseJoinColumns = @JoinColumn(name = "subject_id")  // Clé étrangère vers Course
	)
	private List<Subject> teachingSubjects;


	public List<Subject> getTeachingSubjects() {return teachingSubjects;}
	public void setTeachingSubjects(List<Subject> teachingSubjects) {this.teachingSubjects = teachingSubjects;}
}
