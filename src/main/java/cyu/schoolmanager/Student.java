package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

@Entity
@Table(name = "student")
public class Student extends Person {

	@Column(name = "student_number", unique = true, nullable = false)
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
	public void setStudentNumber() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		String request = "SELECT COUNT(s) FROM Student s";
		Query<Long> query = session.createQuery(request, Long.class);
		int count = Math.toIntExact(query.uniqueResult());
		session.close();
		try{
			this.studentNumber = String.format("%08d", count+1); // set the number based on the student population, (int)42 = (String)"00000042"
		}catch (Exception e){
			this.studentNumber = String.format("%08d", count+2); // if two students are saved at the exact same time
		}
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
