package Controllers;

import Classes.JDBC;
import Classes.User;
import Classes.Utility;
import Classes.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class InsertUserCtrl {
    @FXML private TextField userName;
    @FXML private TextField userCourseNum;
    @FXML private TextField userEgn;
    @FXML private TextField userPhoneNum;
    @FXML private TextArea userDesc;
    JDBC jdbcQuery;
    Alert alert;
    ArrayList<String> textInput;
    String name, courseNum, egn, phoneNum, description, currCourseNum, currEGN, currPhoneNum;

    public void insert(ActionEvent event) throws SQLException {
        name = userName.getText();
        courseNum = userCourseNum.getText();
        egn = userEgn.getText();
        phoneNum = userPhoneNum.getText();
        description = userDesc.getText();
        textInput = new ArrayList<>(Arrays.asList(name, courseNum, egn, phoneNum));

        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Грешка при въвждане");
        alert.setHeaderText("");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));



        if (Validation.emptyInput(textInput)) {
            alert.setContentText(alert.getContentText() + "Моля попълнете всяко поле!\n");
        }

        if (!Validation.validUsername(name)) {
            alert.setContentText(alert.getContentText() + "Моля въведете правилно името на ученика! Необходимо е име и фамилия!\n\n");
        }

        if (!Validation.validCourseNum(courseNum)) {
            alert.setContentText(alert.getContentText() + "Моля въведете правилно курсовия номер на ученика! Необходими са точно 5 цифри!\n\n");
        }

        if (!Validation.validEGN(egn)) {
            alert.setContentText(alert.getContentText() + "Моля въведете правилно ЕГН-то ученика! Необходими са точно 10 цифри!\n\n");
        }

        if (!Validation.validPhoneNum(phoneNum)) {
            alert.setContentText(alert.getContentText() + "Моля въведете правилно телефонния номер на ученика! Необходими са точно 10 цифри!\n\n");
        }

        jdbcQuery = new JDBC("SELECT * FROM users", "SELECT * FROM users");
        while (jdbcQuery.resultSet.next()) {
            currCourseNum = jdbcQuery.resultSet.getString("course_number");
            currEGN = jdbcQuery.resultSet.getString("egn");
            currPhoneNum = jdbcQuery.resultSet.getString("phone_number");

            if (currCourseNum.equals(courseNum)) {
                alert.setContentText(alert.getContentText() + "Вече съществува такъв курсов номер в системата!\n");
                break;
            }

            if (currEGN.equals(egn)) {
                alert.setContentText(alert.getContentText() + "Вече съществува такова ЕГН в системата!\n");
                break;
            }

            if (currPhoneNum.equals(phoneNum)) {
                alert.setContentText(alert.getContentText() + "Вече съществува такъв телефонен номер в системата!\n");
                break;
            }
        }

        if (alert.getContentText().equals("")) {
            userName.setText("");
            userCourseNum.setText("");
            userEgn.setText("");
            userPhoneNum.setText("");
            userDesc.setText("");

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setContentText("Ученикът е добавен успешно!");
            JDBC jdbcAddNewMedia = new JDBC("SELECT * FROM users", "INSERT INTO users(name, course_number, egn, phone_number, description) VALUES (?,?,?,?,?)");
            jdbcAddNewMedia.writeData.setString(1, name);
            jdbcAddNewMedia.writeData.setString(2, courseNum);
            jdbcAddNewMedia.writeData.setString(3, egn);
            jdbcAddNewMedia.writeData.setString(4, phoneNum);
            jdbcAddNewMedia.writeData.setString(5, description);

            jdbcAddNewMedia.writeData.executeUpdate();
        }
        alert.show();
    }

    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/ManageUser.fxml");
    }
}