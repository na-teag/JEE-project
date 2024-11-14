package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@Entity
@Table(name = "grade")
public class Grade extends Model{
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression etudiant
    @JoinColumn(name = "student", nullable = false)
    @NotNull(message = "étudiant ne peut pas être null")
    private Student student;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de cours
    @JoinColumn(name = "course_id", nullable = false)
    @NotNull(message = "Le cours ne peut pas être null")
    private Course course;

    @Column(name = "day", nullable = false)
    @NotNull(message = "Le jour ne peut pas être null")
    private LocalDate day;

    @Column(name = "context")
    private String context;

    @Column(name = "comment")
    private String comment;

    @Column(name = "result", nullable = false)
    @DecimalMin(value = "-1000", message = "La note ne peut pas être inférieure à -1000")
    @DecimalMax(value = "1000", message = "La note ne peut pas être supérieure à 1000")
    private double result;

    @Column(name = "session", nullable = false)
    @NotNull(message = "La session ne peut pas être null")
    @Digits(integer = 1, fraction = 0, message = "la session doit être un nombre entier compris entre 1 et 9")
    @Positive(message = "la session doit être positive")
    private int session;

    public Student getStudent(){return this.student;}
    public void setStudent(Student student){this.student=student;}

    public Course getCourse(){return this.course;}
    public void setCourse(Course course){this.course=course;}

    public LocalDate getDay(){return this.day;}
    public void setDay(LocalDate day){this.day=day;}

    public String getContext(){return this.context;}
    public void setContext(String context){this.context=context;}

    public String getComment(){return this.comment;}
    public void setComment(String comment){this.comment=comment;}

    public double getResult(){return this.result;}
    public void setResult(double result){this.result=result;}

    public int getSession(){return this.session;}
    public void setSession(int session){this.session=session;}
}