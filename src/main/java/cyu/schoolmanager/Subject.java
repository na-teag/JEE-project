package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "subject")
public class Subject extends Model {

    @Column(name = "name", nullable = false)
    @NotBlank(message = "le nom ne peut pas être vide")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
