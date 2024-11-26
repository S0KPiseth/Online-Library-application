package ui.libraryui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class mainPageController implements Initializable {


    @FXML
    private Button cancelSearchbutton;

    @FXML
    private TextField searchbar;
    @FXML
    private StackPane centerPane;

    @FXML
    private Label userName2Digit;
    @FXML
    private Button checkBagBtn;

    @FXML
    private ImageView lbBtnImg;
    @FXML
    private ImageView cBBtnImg;
    @FXML
    private Button libraryBtn;

    @FXML
    private Button closeAllbutton;
    @FXML
    private BorderPane mainPane;

    private boolean isLoaded = false;


    private final User user;
    private LoginController logincontroller;


    public mainPageController(User user, LoginController loginController) {
        this.logincontroller = loginController;
        this.user = user;
    }

    private List<Book> highRated;
    private List<Book> recentlyAdded;

    private Scene scene;
    private BorderPane pane;
    private Parent root;

    public void logOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        stage = new Stage();
        scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }


    public void libraryButton(ActionEvent event) throws IOException {
        libraryBtn.setStyle("-fx-background-color: black;");
        checkBagBtn.setStyle("-fx-background-color: transparent;");
        lbBtnImg.setImage(new Image(getClass().getResourceAsStream("Assets/icons8-books-500-white.png")));
        cBBtnImg.setImage(new Image(getClass().getResourceAsStream("Assets/icons8-packaging-500.png")));
        if(!isLoaded){
            String highrateQuery= "SELECT * FROM book WHERE average_rating = 5";
            String recenltyQuery = "SELECT * FROM book ORDER BY published_year DESC";
            isLoaded = true;
            highRated = new ArrayList<>(getBookData(highrateQuery, false));
            recentlyAdded = new ArrayList<>(getBookData(recenltyQuery,false));

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("bookFrame.fxml"));
            StackPane root = loader.load();
            ContainersControl control = loader.getController();
            control.setUser(user);
            control.startLibrary(highRated, recentlyAdded,centerPane);

            centerPane.getChildren().clear();
            centerPane.getChildren().addAll(root);

        }else {
            exitSearch(event);
        }


    }
    public void onChekBag(ActionEvent event) throws IOException {
        lbBtnImg.setImage(new Image(getClass().getResourceAsStream("Assets/icons8-books-500.png")));
        cBBtnImg.setImage(new Image(getClass().getResourceAsStream("Assets/icons8-packaging-500-white.png")));
        checkBagBtn.setStyle("-fx-background-color: black;");
        libraryBtn.setStyle("-fx-background-color: transparent;");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("listOfLoan.fxml"));
        listLoanBookControl llbc = new listLoanBookControl(user, this);
        loader.setController(llbc);
        VBox root = loader.load();
        centerPane.getChildren().add(root);
    }
    @FXML
    public void onSearchEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            cancelSearchbutton.setVisible(true);
            String searchText = searchbar.getText();
            String querySearch = "SELECT * FROM book WHERE LOWER(title) LIKE ?";

            // Collect relevant books
            List<Book> relevantBooks = new ArrayList<>(getBookData(querySearch,true));

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("searchResult.fxml"));
                ScrollPane vbox = fxmlLoader.load();
                searchResultcontrol searchrsc = fxmlLoader.getController();

                // Pass the user object to the search result controller
                searchrsc.setCurrentUser(user);
                searchrsc.setBookdata(relevantBooks, searchText);

                vbox.prefWidthProperty().bind(centerPane.widthProperty());
                vbox.prefHeightProperty().bind(centerPane.heightProperty());

                // Clear previous search results
                ObservableList<Node> children = centerPane.getChildren();
                if (children.size() > 1) { // Ensure there's more than one child
                    children.remove(1, children.size()); // Remove all children except the first one
                }
                centerPane.getChildren().add(vbox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void exitSearch(ActionEvent event){
        cancelSearchbutton.setVisible(false);


        ObservableList<Node> children = centerPane.getChildren();
        if (children.size() > 1) { // Ensure there's more than one child
            children.remove(1, children.size()); // Remove all children except the first one
        }


    }
    private List<Book> getBookData(String query, boolean isRelevant) {
        List<Book> bookFect = new ArrayList<>();
        String url = "jdbc:sqlite:src/main/resources/ui/libraryui/Database/book.db";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set parameter only if searching for relevant books
            if (isRelevant) {
                String searchText = searchbar.getText();
                statement.setString(1,  searchText.toLowerCase());
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String isbn = resultSet.getString("isbn10");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("authors");
                    int publication = resultSet.getInt("published_year");
                    int loanDuration = 7;
                    String cover = resultSet.getString("thumbnail");
                    double bookRating = resultSet.getDouble("average_rating");
                    String status = resultSet.getString("status");
                    String description = resultSet.getString("description");
                    String categories = resultSet.getString("categories");
                    int ratingCounts = resultSet.getInt("rating_counts");

                    Book book = new Book(isbn, title, author, publication, loanDuration,
                            cover, bookRating, status, description,
                            categories, ratingCounts);
                    bookFect.add(book);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookFect;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String twoDigitName;
        if (user.name.contains(" ")) {
            String[] parts = user.name.split(" ");

            char firstLetter = Character.toUpperCase(parts[0].charAt(0));
            char secondLetter = Character.toLowerCase(parts[1].charAt(0));
            twoDigitName = String.valueOf(firstLetter) + secondLetter;
        } else {
            twoDigitName = user.name.substring(0, 2).toUpperCase();
        }

        userName2Digit.setText(twoDigitName);

        libraryBtn.fire();

    }
    @FXML
    public void onClosing(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("You're about the close the library");
        alert.setHeaderText("Do you want to close the application?");
        if(alert.showAndWait().get() == ButtonType.OK){
            logincontroller.stage.close();


        }

    }
}
