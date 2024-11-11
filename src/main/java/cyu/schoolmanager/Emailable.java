package cyu.schoolmanager;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Emailable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
}
