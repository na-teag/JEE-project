package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

@Entity
public abstract class Emailable extends Model {

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "L'email doit être valide")
    private String email;

    public String getEmail() {
        return email;
    }
    public void setEmail(String emailAddress) {
        this.email = emailAddress;
    }
}
