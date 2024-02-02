package com.example.spacelab.model.student;

import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.course.Course;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="students")
public class Student extends UserEntity implements UserDetails {

    @Embedded
    private StudentDetails details = new StudentDetails();

    private String password;

    private Integer rating;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private int learningDuration;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "student")
    private Set<StudentTask> tasks = new HashSet<>();

    private String taskTrackingProfileId;

    public String getFullName() {
        return String.join(" ",
                this.details.getFirstName(),
                this.details.getFathersName(),
                this.details.getLastName());
    }

    public String getInitials() {
        return this.details.getFirstName().charAt(0) + "." + this.details.getLastName();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRole().getAuthorities().stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public String getUsername() {
        return this.details.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.details.getAccountStatus().equals(StudentAccountStatus.BLOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return (
                this.details.getAccountStatus().equals(StudentAccountStatus.ACTIVE)
                || this.details.getAccountStatus().equals(StudentAccountStatus.HIRED)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(details, student.details) && Objects.equals(password, student.password) && Objects.equals(rating, student.rating) && Objects.equals(taskTrackingProfileId, student.taskTrackingProfileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), details, password, rating, taskTrackingProfileId);
    }
}
