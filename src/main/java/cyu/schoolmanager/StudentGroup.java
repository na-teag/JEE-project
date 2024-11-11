package cyu.schoolmanager;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class StudentGroup extends Emailable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
}
