package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "course_occurrence")
public class CourseOccurrence extends Model{
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de cours
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Le cours ne peut pas être null")
    private Course course;

    @Column(name = "classroom", nullable = true)
    private String classroom;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression du professeur
    @JoinColumn(name = "professor_id", nullable = true)
    private Professor professor;

    @Column(name = "day", nullable = false)
    @NotNull(message = "Le jour ne peut pas être null")
    private LocalDate day;

    @Column(name = "beginning", nullable = false)
    @NotNull(message = "Le début du cours ne peut pas être null")
    private LocalTime beginning;

    @Column(name = "end", nullable = false)
    @NotNull(message = "La fin du cours ne peut pas être null")
    private LocalTime end;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la catégorie du cours
    @JoinColumn(name = "course_category_id", nullable = false)
    @NotNull(message = "La catégorie du cours ne peut pas être vide")
    private ClassCategory category;

    public Course getCourse(){return this.course;}
    public void setCourse(Course course){this.course=course;}
    public String getClassroom(){
        if (this.classroom == null){return this.course.getClassroom();}
        return this.classroom;
    }
    public void setClassroom(String classroom){this.classroom=classroom;}
    public Professor getProfessor() {
        if (this.professor == null) {return this.course.getProfessor();}
        return this.professor;
    }
    public void setProfessor(Professor professor){this.professor=professor;}

    public LocalDate getDay(){return this.day;}
    public void setDay(LocalDate day){this.day=day;}

	public LocalTime getBeginning(){return this.beginning;}
    public void setBeginning(LocalTime beginning){this.beginning=beginning;}

	public LocalTime getEnd(){return this.end;}
    public void setEnd(LocalTime end){this.end=end;}

	public ClassCategory getCategory() { return category; }
	public void setCategory(ClassCategory category) { this.category = category; }
}
