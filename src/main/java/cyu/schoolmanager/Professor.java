package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

@Entity
@Table(name = "professor")
public class Professor extends Person{


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_status_id", referencedColumnName = "id") // Utilisation de la clé étrangère
    @NotBlank(message = "le status ne peut pas être vide")
    private ProfessorStatus status;

    @ManyToMany
    @JoinTable(
            name = "professor_subject",  // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "professor_id"),  // Clé étrangère vers Professor
            inverseJoinColumns = @JoinColumn(name = "subject_id")  // Clé étrangère vers Course
    )
    private ArrayList<Subject> teachingSubjects;
}
