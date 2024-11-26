package ui.libraryui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class searchResultcontrol {

    @FXML
    private Label searchText;

    @FXML
    private VBox resultContainer;

    private User currentUser; // Add this field

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setBookdata(List<Book> listBook, String searchtext) {
        searchText.textProperty().setValue(searchtext);
        resultContainer.getChildren().clear(); // Clear existing results

        int limit = listBook.size() < 6 ? listBook.size() : 10;

        for (int i = 0; i < limit; i++) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("searchBook.fxml"));
            try {
                HBox root = loader.load();
                bookSearchcontrol searchBookControll = loader.getController();
                searchBookControll.setCurrentUser(currentUser); // Pass the user object
                searchBookControll.setSearchBookData(listBook.get(i));
                root.setUserData(searchBookControll); // Store controller reference
                resultContainer.getChildren().add(root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        resultContainer.layout();
    }

}
