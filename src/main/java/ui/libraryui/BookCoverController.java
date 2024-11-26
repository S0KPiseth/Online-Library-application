package ui.libraryui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import java.net.URL;
import java.util.ResourceBundle;

public class BookCoverController implements Initializable {
    @FXML
    private Label bookAuthor;

    @FXML
    private Label bookTitle;

    @FXML
    private ImageView coverImage;

    private Book bookReserver;

    private StackPane mainStackPane;
    private User user;

    public void setUser(User user) {
        this.user = user;
    }
    // Setter to receive StackPane reference from FirstChildController
    public void setMainStackPane(StackPane stackPane) {
        this.mainStackPane = stackPane;
    }


    public void setData(Book book) {
        this.bookReserver = book;
        String cover = book.getCover();
        if (cover == null || cover.isEmpty()) {
            cover = "http://books.google.com/books/content?id=baEJAAAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        }

        Image image = new Image(cover);
        coverImage.setImage(image);
        bookAuthor.setText(book.getAuthor());
        bookTitle.setText(book.getTitle());


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coverImage.setOnMouseClicked(event -> {
            try {
                // Load ThirdChild FXML
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("bookDetail.fxml"));
                Pane pane = loader.load();
                BookDetail bdController = loader.getController();
                bdController.setDetial(bookReserver, mainStackPane);
                bdController.setUser(user);
                mainStackPane.getChildren().add(pane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}

