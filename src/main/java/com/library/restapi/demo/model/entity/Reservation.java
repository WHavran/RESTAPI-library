package com.library.restapi.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonBackReference("user-reservation")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    @JsonBackReference("book-reservation")
    private Book book;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "returned_at")
    private LocalDateTime returnedAt;

    public Reservation() {
    }

    public Reservation(int id, User user, Book book, LocalDateTime reservedAt, LocalDateTime dueDate, LocalDateTime returnedAt) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.reservedAt = reservedAt;
        this.dueDate = dueDate;
        this.returnedAt = returnedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(LocalDateTime reservedAt) {
        this.reservedAt = reservedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}

