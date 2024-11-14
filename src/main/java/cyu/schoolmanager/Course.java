package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.security.InvalidParameterException;
import java.util.List;

@Entity
@Table(name = "course")
public class Course extends Model {

	@ManyToMany
	@JoinTable(
		name = "course_student_group",
		joinColumns = @JoinColumn(name = "course_id"),
		inverseJoinColumns = @JoinColumn(name = "student_group_id")
	)
	private List<StudentGroup> studentGroups;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la catégorie du cours
	@JoinColumn(name = "class_category_id", nullable = false)
	@NotNull(message = "La catégorie du cours ne peut pas être vide")
	private ClassCategory category;

	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression du sujet
	@JoinColumn(name = "subject_id", nullable = false)
	@NotNull(message = "Le sujet ne peut pas être vide")
	private Subject subject;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression du professeur
	@JoinColumn(name = "professor_id", nullable = false)
	@NotNull(message = "Le professeur doit exister")
	private Professor professor;

	@Column(name = "classroom", nullable = false)
	@NotBlank(message = "La salle ne peut pas être vide")
	private String classroom;



	public List<StudentGroup> getStudentGroups() {
		return studentGroups;
	}
	public void setStudentGroups(List<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

	public ClassCategory getCategory() { return category; }
	public void setCategory(ClassCategory category) { this.category = category; }

	public Subject getSubject() { return subject; }
	public void setSubject(Subject subject) throws InvalidParameterException {
		if (this.professor != null && !this.professor.getTeachingSubjects().contains(subject)) {
			throw new InvalidParameterException("Le professeur " + this.professor.getFirstName() + " " + this.professor.getLastName() + " n'est pas apte à enseigner le cours de " + this.subject);
		}
		this.subject = subject;
	}

	public Professor getProfessor() { return professor; }
	public void setProfessor(Professor professor) throws InvalidParameterException {
		if (this.subject != null && !professor.getTeachingSubjects().contains(subject)) {
			throw new InvalidParameterException("Le professeur " + this.professor.getFirstName() + " " + this.professor.getLastName() + " n'est pas apte à enseigner le cours de " + this.subject);
		}
		this.professor = professor;
	}

	public String getClassroom(){return this.classroom;}

	public void setClassroom(String classroom){this.classroom=classroom;}
}
