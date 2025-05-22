package com.matheusluizago.libraryapi.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "authors", schema = "public")
@EntityListeners(AuditingEntityListener.class) //This will make the class listen every change.
public class Author {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", length = 100, nullable = false)
    private String name;
    @Column(name = "birthdate", nullable = false)
    private LocalDate birthdate;
    @Column(name = "nationality", length = 50, nullable = false)
    private String nationality;

    //It is not a column, but a variable mapping in the book table called author.,
    @OneToMany(mappedBy = "author")
    private List<Book> books;

    @CreatedDate //Every time you persist an info it will use the date and time of the current moment
    @Column(name = "date_register")
    private LocalDateTime dateRegister;

    @LastModifiedDate //Every time it is modified it wirtes the date
    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Deprecated //You need to create an empty constructor for spring, but you can make it deprecated so no one can use it
    public Author() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public LocalDateTime getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(LocalDateTime dateRegister) {
        this.dateRegister = dateRegister;
    }

    public LocalDateTime getDateUpdate() {
        return dateUpdate;
    }

    public void setDateUpdate(LocalDateTime dateUpdate) {
        this.dateUpdate = dateUpdate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", nationality='" + nationality + '\'' +
                ", books=" + books +
                '}';
    }
}
