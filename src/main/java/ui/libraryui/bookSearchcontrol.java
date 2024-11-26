package ui.libraryui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
public class bookSearchcontrol {

    @FXML
    private Label authorBook;

    @FXML
    private Label avialableLabel;

    @FXML
    private ImageView coverBook;

    @FXML
    private Label discriptionBook;

    @FXML
    private Label publishBook;

    @FXML
    private Label titleBook;
    @FXML
    private HBox wholeSearchBookContainer;

    @FXML
    private Button loanBtn;

    private Book userBook;
    private User currentUser; // Add this field

    @FXML
    private void initialize() {
        loanBtn.setOnAction(event -> handleLoanButtonClick());
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setSearchBookData(Book book) {
        this.userBook = book; // Store the reference to the book
        updateUIWithBookData(book);
    }

    private void updateUIWithBookData(Book book) {
        authorBook.setText(book.getAuthor());

        if (book.getStatus().equals("available")) {
            avialableLabel.setText("On shelf at AUPP");
            wholeSearchBookContainer.setDisable(false);
            loanBtn.setDisable(false);
        } else {
            avialableLabel.setText("Unavailable");
            wholeSearchBookContainer.setDisable(true);
            loanBtn.setDisable(true);
        }

        String cover = book.getCover();
        if (cover == null || cover.isEmpty()) {
            cover = "http://books.google.com/books/content?id=baEJAAAACAAJ&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        }
        coverBook.setImage(new Image(cover));
        discriptionBook.setText(book.getDescription());
        publishBook.setText(Integer.toString(book.getPublication()));
        titleBook.setText(book.getTitle());
    }

    private void handleLoanButtonClick() {
        if (userBook != null && currentUser != null) {

            currentUser.setBooksToLoan(userBook);
            userBook.setStatus("unavailable");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("You have successfully loan the book!");
            alert.setContentText("The book "+ userBook.getTitle() +" have been added to your inventory!");
            alert.showAndWait();

            // Update UI
            updateUIWithBookData(userBook);

            // Notify parent controllers of the change
            notifyBookStatusChange();
        }
    }

    private void notifyBookStatusChange() {
        // Get the parent VBox (resultContainer)
        Node parent = wholeSearchBookContainer.getParent();
        if (parent instanceof VBox) {
            VBox resultContainer = (VBox) parent;
            // Update all other book entries in the search results
            for (Node node : resultContainer.getChildren()) {
                if (node instanceof HBox && node != wholeSearchBookContainer) {
                    bookSearchcontrol controller = getControllerFromHBox((HBox) node);
                    if (controller != null) {
                        controller.refreshBookStatus();
                    }
                }
            }
        }
    }

    private void refreshBookStatus() {
        if (userBook != null) {
            updateUIWithBookData(userBook);
        }
    }

    private static bookSearchcontrol getControllerFromHBox(HBox hbox) {
        try {
            return (bookSearchcontrol) hbox.getUserData();
        } catch (Exception e) {
            return null;
        }
    }
}

