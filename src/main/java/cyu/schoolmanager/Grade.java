package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    @Column(name = "context", nullable = true)
    private String context;

    @Column(name = "comment", nullable = true)
    private String comment;

    @Column(name = "result", nullable = false)
    @Digits(integer = 5, fraction = 3)
    private double result;

    @Column(name = "session", nullable = false)
    @NotNull(message = "La session ne peut pas être null")
    @Digits(integer = 1, fraction = 0)
    @Positive
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