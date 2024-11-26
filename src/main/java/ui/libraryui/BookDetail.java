package ui.libraryui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDetail{
    @FXML
    private Label Rating2;

    @FXML
    private Label Rating3;

    @FXML
    private Label Rating4;

    @FXML
    private Label Rating5;

    @FXML
    private Label Ratting1;

    @FXML
    private Label authorName;

    @FXML
    private Label bookStatus;

    @FXML
    private Label category;

    @FXML
    private ImageView coverPage;

    @FXML
    private Label description;

    @FXML
    private Label isbn;

    @FXML
    private Button loanBtn;

    @FXML
    private Label numberOfRating;

    @FXML
    private Label overAllRating;

    @FXML
    private ProgressBar ratingIndicator1;

    @FXML
    private ProgressBar ratingIndicator2;

    @FXML
    private ProgressBar ratingIndicator3;

    @FXML
    private ProgressBar ratingIndicator4;

    @FXML
    private ProgressBar ratingIndicator5;

    @FXML
    private Label titleText;

    @FXML
    private Label yearofBook;
    @FXML
    private Button closeDetail;
    private static StackPane test;
    private static Book bookToLoan;
    private static User user1;

    public void setUser(User user) {
        user1 = user;
    }

    public void setDetial(Book book, StackPane pane) {
        test = pane;
        bookToLoan = book;
        String ISBN = book.getIsbn();
        String url = "jdbc:sqlite:src/main/resources/ui/libraryui/Database/book.db";
        String query = "SELECT status FROM book WHERE isbn10 = ?";
        String status;
        try (Connection connection = DriverManager.getConnection(url)) {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ISBN);
            ResultSet rs = ps.executeQuery();
            status = rs.getString("status");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        List<Integer> numOfPeople = new ArrayList<>();
        String coverImg = book.getCover();
        if (coverImg == null || coverImg.isEmpty()) {
            coverImg = "http://books.google.com/books/content?id=baEJAAAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        }
        authorName.setText(book.getAuthor());
        bookStatus.setText(status);
        category.setText(book.getCategory());
        isbn.setText(book.getIsbn());
        titleText.setText(book.getTitle());
        coverPage.setImage(new Image(coverImg));
        description.setText(book.getDescription());
        double avgRating = book.getBookRating();
        overAllRating.setText(Double.toString(avgRating));
        numberOfRating.setText(Double.toString(book.getRating_counts()));

        yearofBook.setText(Integer.toString(book.getPublication()));
        if (book.getRating_counts() != 0) {
            numOfPeople = calculateRatings(avgRating, book.getRating_counts());
            Rating5.setText(Integer.toString(numOfPeople.get(0)));
            Rating4.setText(Integer.toString(numOfPeople.get(1)));
            Rating3.setText(Integer.toString(numOfPeople.get(2)));
            Rating2.setText(Integer.toString(numOfPeople.get(3)));
            Ratting1.setText(Integer.toString(numOfPeople.get(4)));
            ratingIndicator1.setProgress((double) (numOfPeople.get(4)) / book.getRating_counts());
            ratingIndicator2.setProgress((double) (numOfPeople.get(3)) / book.getRating_counts());
            ratingIndicator3.setProgress((double) (numOfPeople.get(2)) / book.getRating_counts());
            ratingIndicator4.setProgress((double) (numOfPeople.get(1)) / book.getRating_counts());
            ratingIndicator5.setProgress((double) (numOfPeople.get(0)) / book.getRating_counts());


        }
        if (status.equals("unavailable")) {
            loanBtn.setDisable(true);

        } else {
            loanBtn.setDisable(false);
        }


    }
    public static List<Integer> calculateRatings(double average, int totalRatings) {
        // Initialize proportions (adjustable based on assumption)
        double p5 = 0.6; // 60% rate 5
        double p4 = 0.3; // 30% rate 4
        double p3 = 0.06; // 6% rate 3
        double p2 = 0.03; // 3% rate 2
        double p1 = 0.01; // 1% rate 1

        // Convert proportions into estimated initial counts
        int n5 = (int) (p5 * totalRatings);
        int n4 = (int) (p4 * totalRatings);
        int n3 = (int) (p3 * totalRatings);
        int n2 = (int) (p2 * totalRatings);
        int n1 = (int) (p1 * totalRatings);

        // Adjust counts to satisfy the average rating
        int currentTotalScore = (n5 * 5) + (n4 * 4) + (n3 * 3) + (n2 * 2) + (n1 * 1);
        int expectedTotalScore = (int) (average * totalRatings);

        // Adjust ratings by redistributing scores
        while (currentTotalScore < expectedTotalScore) {
            if (n4 > 0) {
                n4--;
                n5++;
            } else if (n3 > 0) {
                n3--;
                n4++;
            } else if (n2 > 0) {
                n2--;
                n3++;
            } else if (n1 > 0) {
                n1--;
                n2++;
            }
            currentTotalScore++;
        }

        while (currentTotalScore > expectedTotalScore) {
            if (n5 > 0) {
                n5--;
                n4++;
            } else if (n4 > 0) {
                n4--;
                n3++;
            } else if (n3 > 0) {
                n3--;
                n2++;
            } else if (n2 > 0) {
                n2--;
                n1++;
            }
            currentTotalScore--;
        }

        // Return the counts as a list
        List<Integer> ratingCounts = new ArrayList<>();
        ratingCounts.add(n5);
        ratingCounts.add(n4);
        ratingCounts.add(n3);
        ratingCounts.add(n2);
        ratingCounts.add(n1);

        return ratingCounts;
    }
    @FXML
    public void backDetail(){
        ObservableList<Node> children = test.getChildren();
        if (!children.isEmpty()) {
            children.remove(children.size() - 1);
        }
    }
    @FXML
    void loanBook(ActionEvent event)  {
        user1.setBooksToLoan(bookToLoan);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText("You have successfully loan the book!");
        alert.setContentText("The book "+ bookToLoan.getTitle() +" have been added to your inventory!");
        alert.showAndWait();
        setDetial(bookToLoan, test);

    }
}
