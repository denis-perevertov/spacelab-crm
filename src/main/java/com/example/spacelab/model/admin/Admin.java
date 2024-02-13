package com.example.spacelab.model.admin;

import com.example.spacelab.model.UserEntity;
import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.settings.Settings;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="admins")
public class Admin extends UserEntity implements UserDetails {

    @Column(name = "first_name")
    private String firstName;
    private String lastName;

    private String phone;
    private String email;

    private String password;
    @Transient private String confirmPassword;

    @ToString.Exclude
    @ManyToMany
    private Set<Course> courses = new HashSet<>();

    @ToString.Exclude
    @OneToMany
    private List<Lesson> lessons = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy="admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactInfo> contacts = new ArrayList<>();

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "admin")
    private Settings settings;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRole().getAuthorities().stream().map(SimpleGrantedAuthority::new).toList();
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public String getUserEntityName() {
        return this.getFullName();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(firstName, admin.firstName) && Objects.equals(lastName, admin.lastName) && Objects.equals(phone, admin.phone) && Objects.equals(email, admin.email) && Objects.equals(password, admin.password) && Objects.equals(confirmPassword, admin.confirmPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, phone, email, password, confirmPassword);
    }


}
