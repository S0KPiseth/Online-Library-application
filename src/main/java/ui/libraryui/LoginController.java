package ui.libraryui;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public Stage stage;
    private Scene scene;

    @FXML
    private TextField studentName;
    @FXML
    private ImageView loginImage;

    private User user;

    @FXML
    private void handleKeyPress(KeyEvent event) throws IOException {
        this.user = null;



        // Check if Enter key is pressed
        if (event.getCode().toString().equals("ENTER")) {
            String name = studentName.getText();
            File userDir = new File("src/main/resources/ui/libraryui/User data");
            for (File file : userDir.listFiles()) {
                String fileName = file.getName();

                fileName = fileName.substring(0, fileName.length() - 4);
                System.out.println(fileName);

                if (fileName.equals(name)) {
                    this.user = deserialzeUser(file);
                    break;
                }
            }
            if(user == null){
                this.user = new User(name);
            }
            System.out.println(user);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("mainPage.fxml"));
            mainPageController controller = new mainPageController(user, this);
            loader.setController(controller);
            Parent root = loader.load();

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            stage = new Stage();
            scene = new Scene(root);
            stage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.show();
        }
    }
    @FXML
    void closeStage(MouseEvent event) {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("You're about the close the library");
        alert.setHeaderText("Do you want to close the application?");
        if(alert.showAndWait().get() == ButtonType.OK){
            stage.close();


        }

    }
    public static User deserialzeUser(File file){
        User user = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ips = new ObjectInputStream(fis);
            user = (User) ips.readObject();
            fis.close();
            ips.close();
            return user;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            studentName.getParent().requestFocus();
        });

    }
}
