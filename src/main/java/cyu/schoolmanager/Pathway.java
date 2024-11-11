package cyu.schoolmanager;

import jakarta.validation.constraints.NotBlank;

public class Pathway extends StudentGroup {
    @NotBlank(message = "Le nom de la filière ne peut pas être vide")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
