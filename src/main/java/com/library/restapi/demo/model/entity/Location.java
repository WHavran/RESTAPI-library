package com.library.restapi.demo.model.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="location")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private int id;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "section")
    private String section;

    @Column(name = "floor")
    private int floor;

    @OneToMany(mappedBy = "location",
            fetch = FetchType.LAZY)
    private List<Book> storedBooks;

    public Location() {
    }

    public Location(String shelf, String section, int floor) {
        this.shelf = shelf;
        this.section = section;
        this.floor = floor;
        this.storedBooks = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public List<Book> getBook() {
        return storedBooks;
    }

    public void setBook(List<Book> book) {
        this.storedBooks = book;
    }

    public void addBook(Book theBook){
        storedBooks.add(theBook);
    }
}
