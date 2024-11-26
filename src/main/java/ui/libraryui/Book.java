package ui.libraryui;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;

import java.io.Serializable;

public class Book implements Serializable {
    private String isbn;
    private String title;
    private String author;
    private int publication;
    private int loanDuration;
    private String cover;
    private double bookRating;
    private String status;
    private String description;
    private String category;
    private int rating_counts;
    Book(String isbn, String title, String author, int publication, int loanDuration, String cover, double bookRating, String status, String description, String category, int rating_counts) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publication = publication;
        this.loanDuration = loanDuration;
        this.cover = cover;
        this.bookRating = bookRating;
        this.status = status;
        this.description = description;
        this.category = category;
        this.rating_counts = rating_counts;
    }
    public String getIsbn() {
        return isbn;
    }
    public String getTitle() {
        return title;

    }
    public String getAuthor() {
        return author;
    }
    public int getPublication() {
        return publication;
    }
    public int getLoanDuration() {
        return loanDuration;
    }
    public String getCover() {return cover;}
    public double getBookRating() {return bookRating;}
    public String getStatus() {return status;}
    public String getDescription() {return description;}
    public String getCategory() {return category;}
    public int getRating_counts() {return rating_counts;}

    public void setStatus(String status) {
        this.status = status;
    }
    public void setLoanDuration() {
        this.loanDuration +=7;

    }
}
