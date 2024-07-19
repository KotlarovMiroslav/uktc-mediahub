package Controllers;

import Classes.JDBC;
import Classes.Utility;
import Classes.Validation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class InsertMediaCtrl {
    @FXML private TextField mediaName;
    @FXML private TextField mediaAuthor;
    @FXML private TextField mediaYear;
    @FXML private TextField mediaGenre;
    @FXML private TextArea mediaDesc;
    @FXML private TextField mediaQuantity;
    @FXML private TextField mediaQrCode;
    String name, author, genre, description, qrCode;
    int year, quantity;
    Alert alert;
    ArrayList<String> textInput;

    public void insert(ActionEvent event) throws SQLException {
        name = mediaName.getText();
        author = mediaAuthor.getText();
        genre = mediaGenre.getText();
        description = mediaDesc.getText();

        textInput = new ArrayList<>(Arrays.asList(name, author, mediaYear.getText(), genre, mediaQuantity.getText()));

        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Грешка при въвеждане");
        alert.setHeaderText("");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

        if (Validation.emptyInput(textInput)) {
            alert.setContentText(alert.getContentText() + "Моля попълнете всяко поле!\n" + "Баркода не е задължителен!");
        }

        if (!Validation.onlyNumberInput(mediaYear.getText())) {
            alert.setContentText(alert.getContentText() + "Моля въведете правилно годината на медията!\n\n");
        }

        if (!Validation.onlyNumberInput(mediaQuantity.getText())) {
            alert.setContentText(alert.getContentText() + "Моля въведете правилно бройката на медията!\n\n");
        }

        if (alert.getContentText().equals("")) {
            year = Integer.parseInt(mediaYear.getText());
            quantity = Integer.parseInt(mediaQuantity.getText());
            if (mediaQrCode.getText().equals("")) {
                qrCode = "Ψ";
            } else {
                qrCode = mediaQrCode.getText();
            }
            mediaName.setText("");
            mediaAuthor.setText("");
            mediaYear.setText("");
            mediaGenre.setText("");
            mediaDesc.setText("");
            mediaQuantity.setText("");
            mediaQrCode.setText("");
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setContentText("Медията е добавена успешно!");
            JDBC jdbcAddNewMedia = new JDBC("SELECT * FROM media", "INSERT INTO media(name, author, year, genre, description, quantity, qr_code) VALUES (?,?,?,?,?,?,?)");
            jdbcAddNewMedia.writeData.setString(1, name);
            jdbcAddNewMedia.writeData.setString(2, author);
            jdbcAddNewMedia.writeData.setString(3, Integer.toString(year));
            jdbcAddNewMedia.writeData.setString(4, genre);
            jdbcAddNewMedia.writeData.setString(5, description);
            jdbcAddNewMedia.writeData.setString(6, Integer.toString(quantity));
            jdbcAddNewMedia.writeData.setString(7, qrCode);

            jdbcAddNewMedia.writeData.executeUpdate();
        }
        alert.show();
    }

    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/ManageMedia.fxml");
    }
}
