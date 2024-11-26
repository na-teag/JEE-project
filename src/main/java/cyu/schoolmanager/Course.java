package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course extends Model {

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "course_student_group",
			joinColumns = @JoinColumn(name = "course_id"),
			inverseJoinColumns = @JoinColumn(name = "student_group_id")
	)
	private List<StudentGroup> studentGroups;

	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression du sujet
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

	public Subject getSubject() { return subject; }
	public void setSubject(Subject subject) throws InvalidParameterException {
		if (this.professor != null) {
			List<Long> idList = new ArrayList<>();
			for (Subject profSubject : this.professor.getTeachingSubjects()) {
				idList.add(profSubject.getId());
			}
			if (!idList.contains(subject.getId())) {
				throw new InvalidParameterException("Le professeur " + this.professor.getFirstName() + " " + this.professor.getLastName() + " n'est pas apte à enseigner le cours de " + this.subject);
			}
		}
		this.subject = subject;
	}

	public Professor getProfessor() { return professor; }
	public void setProfessor(Professor professor) throws InvalidParameterException {
		if (this.subject != null) { // faire une boucle pour vérifier les id manuellement
			List<Long> idList = new ArrayList<>();
			String list = "cours possibles : [";
			for (Subject subject : professor.getTeachingSubjects()) {
				list += subject.getName() + " (" + subject.getId() + ")";
				idList.add(subject.getId());
			}
			list += "]";
			if (!idList.contains(this.subject.getId())) {
				throw new InvalidParameterException("Le professeur " + professor.getFirstName() + " " + professor.getLastName() + " n'est pas apte à enseigner le cours de " + this.subject.getId() + "\n" + list);
			}
		}
		this.professor = professor;
	}

	public String getClassroom(){return this.classroom;}
	public void setClassroom(String classroom){this.classroom=classroom;}

	public List<StudentGroup> getStudentGroups() {
		return studentGroups;
	}
	public void setStudentGroups(List<StudentGroup> studentGroups) {
		this.studentGroups = studentGroups;
	}

}
