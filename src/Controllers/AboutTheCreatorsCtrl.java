package Controllers;

import Classes.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

public class AboutTheCreatorsCtrl {
    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/MainMenu.fxml");
    }
}
