package ui.libraryui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class listLoanBookControl implements Initializable {
    @FXML
    private GridPane gridLoan;
    private User user;
    private mainPageController mainController;

    public listLoanBookControl(User user, mainPageController mainController) {
        this.user = user;
        this.mainController = mainController;
    }
    int column =0;
    int row =1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Book> loanBook = user.getArray();
        loanBookPaneController controller=new loanBookPaneController(mainController);
        try{
            for (Book book : loanBook) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("loanBookPane.fxml"));
                fxmlLoader.setController(controller);
                VBox vbox = fxmlLoader.load();

                controller.setUser(user);
                controller.setLoandata(book);
                if(column==4){
                    column=0;
                    ++row;
                }
                gridLoan.add(vbox,column++,row);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
