package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "professor_status")
public class ProfessorStatus extends Model {

    @Column(name = "status", nullable = false)
    @NotBlank(message = "le status ne peut pas être vide")
    private String status;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
