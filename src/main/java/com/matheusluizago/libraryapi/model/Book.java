package com.matheusluizago.libraryapi.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "publish_date")
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING) //Fazendo usar os enums
    @Column(name = "genre", length = 30, nullable = false)
    private GenreBook genre;

    @Column(name = "price", precision = 18, scale = 2)
    private BigDecimal price;

    //Fazendo uma foreign key
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")
    private Author author;

    @Deprecated
    public Book() {
    }

    public Book(UUID id, String isbn, String title, LocalDate publicationDate, GenreBook genre, BigDecimal price, Author author) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.publicationDate = publicationDate;
        this.genre = genre;
        this.price = price;
        this.author = author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public GenreBook getGenre() {
        return genre;
    }

    public void setGenre(GenreBook genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", publicationDate=" + publicationDate +
                ", genre=" + genre +
                ", price=" + price +
                ", author=" + author +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(isbn, book.isbn) && Objects.equals(title, book.title) && Objects.equals(publicationDate, book.publicationDate) && genre == book.genre && Objects.equals(price, book.price) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isbn, title, publicationDate, genre, price, author);
    }
}
