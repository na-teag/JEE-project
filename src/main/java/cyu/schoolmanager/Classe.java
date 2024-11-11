package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;


@Entity
@Table(name = "classe")
public class Classe extends StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Le nom de la classe ne peut pas être vide")
    private String name;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la filière
    @JoinColumn(name = "pathway_id", nullable = false)
    @NotBlank(message = "La flière ne peut pas être vide")
    private Pathway pathway;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Pas de suppression de la promo
    @JoinColumn(name = "promo_id", nullable = false)
    @NotBlank(message = "La promotion ne peut pas être vide")
    private Promo promo;

    @ManyToMany
    @JoinTable(
            name = "course_classe",  // Nom de la table de jointure
            joinColumns = @JoinColumn(name = "classe_id"),  // Clé étrangère vers Classe
            inverseJoinColumns = @JoinColumn(name = "course_id")  // Clé étrangère vers Course
    )
    private ArrayList<Course> courses;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pathway getPathway() {
        return pathway;
    }

    public void setPathway(Pathway pathway) {
        this.pathway = pathway;
    }

    public Promo getPromo() {
        return promo;
    }

    public void setPromo(Promo promo) {
        this.promo = promo;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
}
