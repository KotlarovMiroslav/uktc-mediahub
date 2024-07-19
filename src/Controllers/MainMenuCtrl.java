package Controllers;

import Classes.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainMenuCtrl {
    @FXML void rent(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/Rent.fxml");
    }

    @FXML void manageUser(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/ManageUser.fxml");
    }

    @FXML void manageMedia(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/ManageMedia.fxml");
    }

    @FXML void aboutTheCreators(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/AboutTheCreators.fxml");
    }
}
