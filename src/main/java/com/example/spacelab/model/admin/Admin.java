package com.example.spacelab.model.admin;

import com.example.spacelab.model.contact.ContactInfo;
import com.example.spacelab.model.course.Course;
import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import com.example.spacelab.model.lesson.Lesson;
import com.example.spacelab.model.UserEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="admins")
public class Admin extends UserEntity implements UserDetails {

    private String firstName;
    private String lastName;

    private String phone;
    private String email;

    private String password;
    @Transient private String confirmPassword;

    @ManyToMany
    private List<Course> courses = new ArrayList<>();

    @OneToMany
    private List<Lesson> lessons = new ArrayList<>();

    @OneToMany(mappedBy="admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactInfo> contacts = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRole().getAuthorities().stream().map(SimpleGrantedAuthority::new).toList();
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
}
