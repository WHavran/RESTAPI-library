package com.library.restapi.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "genre")
    private String genre;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "book",
            fetch = FetchType.LAZY)
    @JsonManagedReference("book-reservation")
    private List<Reservation> reservations;

    @Column(name = "is_active")
    private boolean isActive;

    public Book() {
    }

    public Book(int id, String title, String isbn, String genre, LocalDate publishedDate, Author author, Location location, boolean isActive) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.genre = genre;
        this.publishedDate = publishedDate;
        this.author = author;
        this.location = location;
        this.isActive = isActive;
        this.reservations = new ArrayList<>();
    }

    public Book(String title, String isbn, String genre, LocalDate publishedDate) {
        this.title = title;
        this.isbn = isbn;
        this.genre = genre;
        this.publishedDate = publishedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation theReservation){
        reservations.add(theReservation);
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
