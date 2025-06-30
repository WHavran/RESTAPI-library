package com.library.restapi.demo.model.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "birth_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "death_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deathDay;

    @Column(name = "nationality")
    private String nationality;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Book> books;

    @Column(name = "is_active")
    private boolean isActive;

    public Author() {
    }

    public Author(int id, String firstName, String lastName, String bio, LocalDate birthday, LocalDate deathDay, String nationality, boolean isActive) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bio = bio;
        this.birthday = birthday;
        this.deathDay = deathDay;
        this.nationality = nationality;
        this.isActive = isActive;
        this.books = new ArrayList<>();
    }

    public Author(int id, String firstName, String lastName, String bio, LocalDate birthday, String nationality, boolean isActive) {
        this(id,firstName,lastName, bio, birthday, null, nationality, isActive);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book theBook){

        if (books == null) books = new ArrayList<>();

        books.add(theBook);
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public LocalDate getDeathDay() {
        return deathDay;
    }

    public void setDeathDay(LocalDate deathDay) {
        this.deathDay = deathDay;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }
}
