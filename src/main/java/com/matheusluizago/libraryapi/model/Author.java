package com.matheusluizago.libraryapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "author", schema = "public")
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

    //Colocando uma lista de todos os lviros do author.
    //Isso é pra mostrar q nn é uma coluna no BD, e que ele é mapeado pela variavel chamada de author na classe livros
    @OneToMany(mappedBy = "author")
    //Da pra botar um cascade all pra deixar os livros fixados no author
    private List<Book> books;

    @Deprecated //Tem que criar o construtor vazio para spring, mas pode deixar Deprecated pra ngm usar
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
