package org.openjfx.projet_jee.schoolmanager;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "student_group")
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_group_id")
    private Long studentGroupId;

    @Column(name = "student_group_name", nullable = false)
    private String studentGroupName;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    // Getters et setters

    public Long getStudentGroupId() {
        return studentGroupId;
    }

    public void setStudentGroupId(Long studentGroupId) {
        this.studentGroupId = studentGroupId;
    }

    public String getStudentGroupName() {
        return studentGroupName;
    }

    public void setStudentGroupName(String studentGroupName) {
        this.studentGroupName = studentGroupName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
