package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "course_promo",  // Table de jointure
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "promo_id")
    )
    private ArrayList<Promo> promo;

    @ManyToMany
    @JoinTable(
            name = "course_pathway",  // Table de jointure
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "pathway_id")
    )
    private ArrayList<Pathway> pathways;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la catégorie du cours
    @JoinColumn(name = "classcategory_id")
    @NotBlank(message = "La catégorie du cours ne peut pas être vide")
    private ClassCategory category;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression du sujet
    @JoinColumn(name = "subject_id")
    @NotNull(message = "Le sujet ne peut pas être vide")
    private Subject subject;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression du professeur
    @JoinColumn(name = "professor_id")
    @NotNull(message = "Le professeur doit exister")
    private Professor professor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList<Promo> getPromo() {
        return promo;
    }

    public void setPromo(ArrayList<Promo> promo) {
        this.promo = promo;
    }

    public ArrayList<Pathway> getPathways() {
        return pathways;
    }

    public void setPathways(ArrayList<Pathway> pathways) {
        this.pathways = pathways;
    }

    public ClassCategory getCategory() {
        return category;
    }

    public void setCategory(ClassCategory category) {
        this.category = category;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }
}
