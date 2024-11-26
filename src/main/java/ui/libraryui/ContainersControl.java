package ui.libraryui;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.concurrent.Service;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

public class ContainersControl{
    @FXML
    private ScrollPane allContainer;
    @FXML
    private HBox mostRateContainer;

    @FXML
    private GridPane recentlyAddContainer;
    @FXML
    private StackPane stackPaneContainter;

    @FXML
    private BorderPane borderPane;

    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    private List<Book> highRated;
    private List<Book> recentlyAdded;

    int column = 0;
    int row = 1;
    private final int initialRowCount = 3;
    private final int columnsPerRow = 7;
    private int currentRowCount = 0;

    public void startLibrary(List<Book> highRated, List<Book> recentlyAdded, StackPane mainStackPane) {
        this.highRated = highRated;
        this.recentlyAdded = recentlyAdded;

        // Create a service for loading high-rated books
        Service<Void> highRatedLoader = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        for (int i = 0; i < highRated.size(); i++) {
                            final int index = i;
                            Platform.runLater(() -> {
                                try {
                                    Book book = highRated.get(index);
                                    FXMLLoader fxmlLoader = new FXMLLoader();
                                    fxmlLoader.setLocation(getClass().getResource("bookContainer.fxml"));
                                    AnchorPane vbox = fxmlLoader.load();
                                    BookCoverController bookCoverController = fxmlLoader.getController();
                                    bookCoverController.setMainStackPane(mainStackPane);
                                    bookCoverController.setData(book);
                                    bookCoverController.setUser(user);
                                    mostRateContainer.getChildren().add(vbox);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                            // Add a small delay to prevent UI freezing
                            Thread.sleep(20);
                        }
                        return null;
                    }
                };
            }
        };

        // Create a service for loading initial recently added books
        Service<Void> recentBooksLoader = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        loadMoreBooks(mainStackPane);
                        return null;
                    }
                };
            }
        };

        // Set up completion handling for high-rated books
        highRatedLoader.setOnSucceeded(event -> {
            // Start loading recent books after high-rated books are loaded
            recentBooksLoader.start();
        });

        // Set up scroll listener for infinite scrolling
        allContainer.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        allContainer.vvalueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() == 1.0) {
                // Create a new service for loading more books
                Service<Void> moreBookLoader = new Service<>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<>() {
                            @Override
                            protected Void call() throws Exception {
                                loadMoreBooks(mainStackPane);
                                return null;
                            }
                        };
                    }
                };
                moreBookLoader.start();
            }
        });

        // Start the initial loading process
        highRatedLoader.start();
    }

    private void loadMoreBooks(StackPane mainStackPane) {
        int endIndex = Math.min(recentlyAdded.size(), (currentRowCount + initialRowCount) * columnsPerRow);
        int startIndex = currentRowCount * columnsPerRow;

        for (int i = startIndex; i < endIndex; i++) {
            final int currentIndex = i;
            Platform.runLater(() -> {
                try {
                    Book book = recentlyAdded.get(currentIndex);
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("bookContainer.fxml"));
                    AnchorPane vbox = fxmlLoader.load();
                    BookCoverController bookCoverController = fxmlLoader.getController();
                    bookCoverController.setMainStackPane(mainStackPane);
                    bookCoverController.setData(book);
                    bookCoverController.setUser(user);

                    // Synchronize grid placement
                    synchronized (this) {
                        if (column == columnsPerRow) {
                            column = 0;
                            ++row;
                        }
                        recentlyAddContainer.add(vbox, column++, row);
                        GridPane.setMargin(vbox, new Insets(15, 15, 15, 15));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            try {
                // Add a small delay to prevent UI freezing
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        Platform.runLater(() -> {
            currentRowCount += initialRowCount;
        });
    }



}
