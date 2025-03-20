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
@Table(name = "author", schema = "public")
@EntityListeners(AuditingEntityListener.class) //Vai dizer q a classe escuta sempre q altera a entidade pra mexer nas anotations certas
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
    private List<Book> books;

    @CreatedDate //Sempre que for persistir a info ele coloca a data e hora atual
    @Column(name = "date_register")
    private LocalDateTime dateRegister;

    @LastModifiedDate //Sempre q atualizar preenche com data atual
    @Column(name = "date_update")
    private LocalDateTime dateUpdate;

    @Column(name = "user_id")
    private UUID userId;



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

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
