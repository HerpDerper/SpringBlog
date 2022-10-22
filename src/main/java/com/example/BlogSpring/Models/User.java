package com.example.BlogSpring.Models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

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

    @Column(unique = true)
    @Email(regexp = "[a-zA-Z0-9]{3,20}@[a-zA-Z0-9]{3,15}[.][a-zA-Z]{2,5}", message = "Некорретный ввод электронной почты")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()â€\"[{}]:;'.,?/*~$^+=<>]).{8,16}$", message = "Пароль должен быть от 8 до 16 символов и содержать спицсимволы, цифры строчные и прописные латинские буквы")
    private String password;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения не должна быть будущей или текущей")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dateBirth;

    public User() {
    }

    public User(String name, String surname, String lastName, String email, String password, Date dateBirth) {
        this.name = name;
        this.surname = surname;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}