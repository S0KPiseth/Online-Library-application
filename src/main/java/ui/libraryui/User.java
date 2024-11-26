package ui.libraryui;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    public String name;
    private ArrayList<Book> books=new ArrayList<>();
    private static String url = "jdbc:sqlite:src/main/resources/ui/libraryui/Database/book.db";
    private static String query = "UPDATE book SET status = 'unavailable' WHERE isbn10 = ?";

    User(String name) {
        this.name =name;
    }
    //check if the book contain in the db it should have two options if the input is isbn search base on isbn and so on.
    // if the book is in the db show the option for user to decide weather to loan it or not
    // if the user loan the book it should change the availability colum of those book to unavailable
    //and should add the book object with the constructor Book( isbn,  title,  author,  publisher,  loanDuration)
    // to the books arrays of the student class

    public List<Book> getArray(){
        return books;
    }

    public void setBooks(Book book) {
        this.books.add(book);
    }
    public void removeBook(Book book) {
        this.books.remove(book);
    }

    public void serialize(User obj) {
        String saveUser = "src/main/resources/ui/libraryui/User data/" + obj.name + ".txt";
        File directory = new File("src/main/resources/ui/libraryui/User data");
        if (!directory.exists()) {
            directory.mkdirs(); // Ensure the directory exists
        }

        try (FileOutputStream fos = new FileOutputStream(saveUser);
             ObjectOutputStream ois = new ObjectOutputStream(fos)) {
            ois.writeObject(obj);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found: " + saveUser, e);
        } catch (IOException e) {
            throw new RuntimeException("IO exception during serialization", e);
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void setBooksToLoan(Book book ) {
        String ISBN = book.getIsbn();
        try(Connection connection = DriverManager.getConnection(url)){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ISBN);
            ps.executeUpdate();
            this.setBooks(book);
            serialize(this);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
