package cyu.schoolmanager;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
@Entity
@Table(name = "classcategory")
public class ClassCategory {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, updatable = false)
        private Long id;

        @Column(name = "nom")
        @NotBlank(message = "le nom ne peut pas être vide")
        private String name;

        @CreationTimestamp // permet une gestion automatique
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "created_at", updatable = false)
        private Date createdAt;

        @UpdateTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "updated_at")
        private Date updatedAt;

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Long getId() { return id; }
        public Date getCreatedAt() { return createdAt; }
        public Date getUpdatedAt() { return updatedAt; }
}
