package ui.libraryui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.sql.*;

public class loanBookPaneController {
    @FXML
    private Text authorLoan;

    @FXML
    private Pane coverLoan;

    @FXML
    private Text dueLoan;

    @FXML
    private Button renewBtn;

    @FXML
    private Button returnBtn;

    @FXML
    private Text titleLoan;
    private static User student;
    private Book studentLoanBook;
    private static mainPageController controller;

    public void setUser(User user) {
        student = user;

    }
    public loanBookPaneController(mainPageController mainPageController) {
        controller = mainPageController;

    }

    public void setLoandata(Book book){
        String cover = book.getCover();
        this.studentLoanBook = book;


        if (cover == null || cover.isEmpty()) {
            cover = "http://books.google.com/books/content?id=baEJAAAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        }
        int duration = book.getLoanDuration();
        authorLoan.setText(book.getAuthor());
        titleLoan.setText(book.getTitle());
        dueLoan.setText("Due in " + String.valueOf(duration)+ " days");

        coverLoan.setStyle("-fx-background-image: url('" + cover + "');" +
                "-fx-background-size: cover;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: center center;");



    }
    @FXML
    public void onLoanbook(ActionEvent actionEvent) {
        String ISBN = studentLoanBook.getIsbn();
        String url = "jdbc:sqlite:src/main/resources/ui/libraryui/Database/book.db"; // Replace with your SQLite DB path
        String query = "UPDATE book SET status = 'available' WHERE isbn10 = ?";
        try(Connection connection = DriverManager.getConnection(url)){
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ISBN);
            int rs =ps.executeUpdate();
            if(rs>0){
                student.removeBook(studentLoanBook);
                student.serialize(student);
                setLoandata(studentLoanBook);
                setUser(student);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("You have return the book!");
            alert.setContentText("The book "+ studentLoanBook.getTitle() +" have successfully returned!");
            alert.showAndWait();
            controller.onChekBag(actionEvent);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void onRenew(ActionEvent actionEvent) throws IOException {
        if(studentLoanBook.getLoanDuration() <14){
            studentLoanBook.setLoanDuration();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("You have renew the book!");
            alert.setContentText("The book "+ studentLoanBook.getTitle() +" have renew for another 7 days");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You cannot loan more than 14 days");
            alert.setHeaderText("The book "+ studentLoanBook.getTitle() + " have already been loaned for "+ studentLoanBook.getLoanDuration() + " days");
            alert.showAndWait();
        }

        controller.onChekBag(actionEvent);

    }
}
