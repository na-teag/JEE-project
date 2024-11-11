package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

@Entity
@Table(name = "professor")
public class Professor extends Person{

    @Column(name = "status")
    @NotBlank(message = "le status ne peut pas être vide")
    private ProfessorStatus status;

    @Column(name = "teachingsubjects")
    @NotBlank(message = "teachingSubjects ne peut pas être vide")
    private ArrayList<Subject> teachingSubjects;
}
