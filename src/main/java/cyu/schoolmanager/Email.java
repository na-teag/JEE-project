package cyu.schoolmanager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "email")
public class Email extends Model{
    @Column(name = "object", nullable = false)
    @NotNull(message = "L'objet ne peut pas être null")
    private String object;

    @Column(name = "body", nullable = false)
    @NotNull(message = "Le corps du mail ne peut pas être null")
    private String body;

    public String getObject(){ return this.object;}

    public void setObject(String object){this.object = object;}
    public String getBody(){ return this.body;}
    public void setBody(String body){this.body = body;}
}
