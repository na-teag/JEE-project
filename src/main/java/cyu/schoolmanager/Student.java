package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

@Entity
@Table(name = "student")
public class Student extends Person {

    @Column(name = "student_number")
    @NotBlank(message = "Le numéro étudiant ne peut pas être vide")
    private int studentNumber;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la classe
    @JoinColumn(name = "classe_id")
    @NotBlank(message = "La classe ne peut pas être vide")
    private Classe classe;


    @ManyToMany
    @JoinTable(
            name = "student_course",  // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "student_id"),  // Clé étrangère vers Student
            inverseJoinColumns = @JoinColumn(name = "course_id")  // Clé étrangère vers Course
    )
    private ArrayList<Course> courses;

    public int getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(int studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
