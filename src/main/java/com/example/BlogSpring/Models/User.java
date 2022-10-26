package com.example.BlogSpring.Models;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = "[a-zA-Zа-яА-Я]{1,30}", message = "Имя должно быть от 1 до 30 символов и состоять только из букв")
    private String name;

    @Pattern(regexp = "[a-zA-Zа-яА-Я]{1,30}", message = "Фамилия должна быть от 1 до 30 символов и состоять только из букв")
    private String surname;

    @Pattern(regexp = "[a-zA-Zа-яА-Я]{1,30}", message = "Отчество должно быть от 1 до 30 символов и состоять только из букв")
    private String lastName;

    @NotBlank(message = "Логин не должен быть пустым или состоять из одних лишь пробелов")
    @Column(unique = true)
    @Size(min = 1, max = 16, message = "Логин должен должен быть от 1 до 16 символов")
    private String username;

    @NotBlank
    private String password;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения не должна быть будущей или текущей")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    private boolean active;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "userRole", joinColumns = @JoinColumn(name = "userId"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @OneToOne
    @JoinColumn(name = "contactDataId", referencedColumnName = "id")
    private ContactData contactData;

    @ManyToMany
    @JoinTable(name = "likedUsers", joinColumns = @JoinColumn(name = "userId"), inverseJoinColumns = @JoinColumn(name = "postId"))
    public List<Post> likedPosts;

    public User() {
    }

    public User(String name, String surname, String lastName, String username, String password, Date dateBirth) {
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.dateBirth = dateBirth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDateBirth() {
        return dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    public ContactData getContactData() {
        return contactData;
    }

    public void setContactData(ContactData contactData) {
        this.contactData = contactData;
    }

    public List<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(List<Post> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}