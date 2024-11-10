## Les Validations

site qui explique ça simplement : https://www.baeldung.com/java-validation

| Annotation                          | Utilité                                                                 |
|-------------------------------------|-------------------------------------------------------------------------|
| @NotNull                            | Valide que la valeur n'est pas null.                                   |
| @NotEmpty                           | Valide que la chaîne de caractères n'est pas vide.                     |
| @NotBlank                           | Valide que la chaîne de caractères n'est pas vide et contient des caractères non blancs. |
| @Size(min=, max=)                   | Vérifie que la taille d'un élément est dans une plage donnée.          |
| @Min(value=)                        | Valide que la valeur est supérieure ou égale à un minimum spécifié.    |
| @Max(value=)                        | Valide que la valeur est inférieure ou égale à un maximum spécifié.    |
| @Digits(integer=, fraction=)        | Limite le nombre de chiffres avant et après la virgule pour les nombres décimaux. |
| @Pattern(regexp="")                 | Vérifie que la valeur correspond à une expression régulière donnée.    |
| @Pattern.List                       | Permet d'appliquer plusieurs validations @Pattern à un même champ.     |
| @Email                              | Valide qu'une valeur est une adresse email valide.                     |
| @DecimalMin(value="")               | Vérifie que la valeur décimale est supérieure ou égale à un minimum spécifié. |
| @DecimalMax(value="")               | Vérifie que la valeur décimale est inférieure ou égale à un maximum spécifié. |
| @Positive                           | Valide que la valeur est strictement positive.                         |
| @PositiveOrZero                     | Valide que la valeur est positive ou zéro.                             |
| @Negative                           | Valide que la valeur est strictement négative.                         |
| @NegativeOrZero                     | Valide que la valeur est négative ou zéro.                             |
| @Range(min=, max=)                  | Spécifie un intervalle pour une valeur numérique.                      |
| @Past                               | Valide que la date est dans le passé.                                  |
| @Future                             | Valide que la date est dans le futur.                                  |
| @PastOrPresent                      | Vérifie qu'une date est dans le passé ou le présent.                   |
| @FutureOrPresent                    | Vérifie qu'une date est dans le futur ou le présent.                   |
| @AssertTrue                         | Vérifie que l'expression booléenne est true.                           |
| @AssertFalse                        | Vérifie que l'expression booléenne est false.                          |
| @Null                               | Valide que la valeur est null.                                         |
| @NotNull(groups = Group.class)      | Permet de spécifier un groupe de validation pour un champ non null.    |
| @ScriptAssert(lang="", script="")   | Permet d'exécuter un script Java pour valider un objet.                |


### Exemples

```java
@NotNull
@Size(min = 5, max = 10)
@Pattern(regexp = "^[A-Za-z]+$")
private String myField;
```

exemple avec test des validations :
```java
import jakarta.validation.constraints.*;

public class Professor {

    @NotNull
    @Size(min = 3, message = "Le nom doit avoir au moins 3 caractères.")
    private String name;

    @NotNull
    @Email(message = "L'email doit être valide.")
    private String email;
    public Professor() {}
    public void setName(String name) {this.name = name;}
    public void setEmail(String email) {this.email = email;}
}
```
```java
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;

Professor professor = new Professor();
professor.setName("Jo"); // Trop court pour la validation
professor.setEmail("invalid-email"); // Pas un email valide

// Créer un Validateur
Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

// Valider l'objet Professor
Set<ConstraintViolation<Professor>> violations = validator.validate(professor);

// Si des violations sont trouvées, afficher les messages d'erreurs
for (ConstraintViolation<Professor> violation : violations) {
	System.out.println(violation.getMessage());
}
```

note : peut servir à faire des tests unitaires