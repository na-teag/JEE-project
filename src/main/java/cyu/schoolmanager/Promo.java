package cyu.schoolmanager;

import jakarta.validation.constraints.NotBlank;

public class Promo extends StudentGroup {

    @NotBlank(message = "Le nom de la promotion ne peut pas être vide")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
