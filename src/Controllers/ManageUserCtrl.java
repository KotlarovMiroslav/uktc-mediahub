package Controllers;

import Classes.JDBC;
import Classes.User;
import Classes.Utility;
import Classes.Validation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageUserCtrl implements Initializable {
    @FXML private TextField searchBar;
    @FXML private TableView<User> table;
    @FXML private TableColumn<User, String> name;
    @FXML private TableColumn<User, String> courseNum;
    @FXML private TableColumn<User, String> egn;
    @FXML private TableColumn<User, String> phoneNum;
    @FXML private TableColumn<User, String> checkBox;
    @FXML private TextArea showDesc;
    public int lastID;
    public int currID;
    Alert alert;
    ObservableList<User> userList;
    SortedList<User> sortedData;
    private JDBC jdbcQuery;

    public ObservableList<User> userListInserter() throws SQLException {
        jdbcQuery = new JDBC("SELECT * FROM users" , "SELECT * FROM users");

        ObservableList<User> usersToBeAdded = FXCollections.observableArrayList();

        User user;
        CheckBox checkBox;
        String name, courseNum, egn, phoneNum, description;
        int id;

        while (jdbcQuery.resultSet.next()) {
            id = Integer.parseInt(jdbcQuery.resultSet.getString("id"));
            name = jdbcQuery.resultSet.getString("name");
            courseNum = jdbcQuery.resultSet.getString("course_number");
            egn = jdbcQuery.resultSet.getString("egn");
            phoneNum = jdbcQuery.resultSet.getString("phone_number");
            description = jdbcQuery.resultSet.getString("description");
            checkBox = new CheckBox();

            user = new User(id, name, courseNum, egn, phoneNum, description, checkBox);

            usersToBeAdded.add(user);
        }

        jdbcQuery.resultSet.close();
        return usersToBeAdded;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showDesc.setWrapText(true);
        table.setEditable(true);
        table.setOnMouseClicked((MouseEvent event) -> {

            currID = table.getSelectionModel().getSelectedItem().getId();
            if(event.getButton().equals(MouseButton.PRIMARY)){

                if(currID != lastID) {
                    showDesc.setText("");
                }
            }
            showDesc.setText(table.getSelectionModel().getSelectedItem().getDescription());
            lastID = currID;
        });

        //STUDENT NAME
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        name.setOnEditCommit(event -> {
            User user = event.getRowValue();

            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Грешка в редактирането");
            alert.setContentText("");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

            if (Validation.validUsername(event.getNewValue())) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Редактирана информация");
                alert.setContentText("Информацията е редактирана успешно!");

                user.setName(event.getNewValue());
                try {
                    jdbcQuery = new JDBC("SELECT * FROM users", "UPDATE users SET name=? WHERE id=?");
                    jdbcQuery.writeData.setString(1, event.getNewValue());
                    jdbcQuery.writeData.setString(2, Integer.toString(user.getId()));
                    jdbcQuery.writeData.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                alert.setContentText(alert.getContentText() + "Въведеното име не спазва нужните изисквания! Небходими са име и фамилия!\n\n");
                user.setName(event.getOldValue());
            }

            alert.show();
            table.refresh();
        });
        //END STUDENT NAME

        //COURSE NUMBER
        courseNum.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        courseNum.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        courseNum.setOnEditCommit(event -> {
            User user = event.getRowValue();

            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Грешка в редактирането");
            alert.setContentText("");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

            boolean isRepeated = false;
            try {
                jdbcQuery = new JDBC("SELECT * FROM users", "SELECT * FROM users");
                while (jdbcQuery.resultSet.next()) {
                    String currCourseNum = jdbcQuery.resultSet.getString("course_number");

                    if (currCourseNum.equals(event.getNewValue())) {
                        isRepeated = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!isRepeated) {
                if (Validation.validCourseNum(event.getNewValue())) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Редактирана информация");
                    alert.setContentText("Информацията е редактирана успешно!");

                    user.setCourseNum(event.getNewValue());
                    try {
                        jdbcQuery = new JDBC("SELECT * FROM users", "UPDATE users SET course_number=? WHERE id=?");
                        jdbcQuery.writeData.setString(1, event.getNewValue());
                        jdbcQuery.writeData.setString(2, Integer.toString(user.getId()));
                        jdbcQuery.writeData.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    alert.setContentText(alert.getContentText() + "Въведенят курсов номер не спазва нужните изисквания! Небходими са име и фамилия!\n\n");
                    user.setCourseNum(event.getOldValue());
                }
            } else {
                alert.setContentText(alert.getContentText() + "Вече съществува такъв курсов номер в системата!\n");
            }

            alert.show();
            table.refresh();
        });
        //END COURSE NUMBER

        //EGN
        egn.setCellValueFactory(new PropertyValueFactory<>("egn"));
        egn.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        egn.setOnEditCommit(event -> {
            User user = event.getRowValue();

            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Грешка в редактирането");
            alert.setContentText("");
            alert.setHeaderText("");

            boolean isRepeated = false;
            try {
                jdbcQuery = new JDBC("SELECT * FROM users", "SELECT * FROM users");
                while (jdbcQuery.resultSet.next()) {
                    String currEGN = jdbcQuery.resultSet.getString("egn");

                    if (currEGN.equals(event.getNewValue())) {
                        isRepeated = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!isRepeated) {
                if (Validation.validEGN(event.getNewValue())) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Редактирана информация");
                    alert.setContentText("Информацията е редактирана успешно!");
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

                    user.setEgn(event.getNewValue());
                    try {
                        jdbcQuery = new JDBC("SELECT * FROM users", "UPDATE users SET egn=? WHERE id=?");
                        jdbcQuery.writeData.setString(1, event.getNewValue());
                        jdbcQuery.writeData.setString(2, Integer.toString(user.getId()));
                        jdbcQuery.writeData.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    alert.setContentText(alert.getContentText() + "Моля въведете правилно ЕГН-то на ученика! Необходими са точно 10 цифри!\n\n");
                }
            } else {
                alert.setContentText(alert.getContentText() + "Вече съществува такова ЕГН в системата!\n");
            }

            alert.show();
            table.refresh();
        });
        //END EGN

        //PHONE NUMBER
        phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        phoneNum.setCellFactory(TextFieldTableCell.<User>forTableColumn());
        phoneNum.setOnEditCommit(event -> {
            User user = event.getRowValue();

            alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Грешка в редактирането");
            alert.setContentText("");
            alert.setHeaderText("");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

            boolean isRepeated = false;
            try {
                jdbcQuery = new JDBC("SELECT * FROM users", "SELECT * FROM users");
                while (jdbcQuery.resultSet.next()) {
                    String currPhoneNum = jdbcQuery.resultSet.getString("phone_number");

                    if (currPhoneNum.equals(event.getNewValue())) {
                        isRepeated = true;
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!isRepeated) {
                if (Validation.validPhoneNum(event.getNewValue())) {
                    alert.setAlertType(Alert.AlertType.INFORMATION);
                    alert.setTitle("Редактирана информация");
                    alert.setContentText("Информацията е редактирана успешно!");

                    user.setPhoneNum(event.getNewValue());
                    try {
                        jdbcQuery = new JDBC("SELECT * FROM users", "UPDATE users SET phone_number=? WHERE id=?");
                        jdbcQuery.writeData.setString(1, event.getNewValue());
                        jdbcQuery.writeData.setString(2, Integer.toString(user.getId()));
                        jdbcQuery.writeData.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    alert.setContentText(alert.getContentText() + "Моля въведете правилно телефонния номер на ученика! Необходими са точно 10 цифри!\n\n");
                }
            } else {
                alert.setContentText(alert.getContentText() + "Вече съществува такъв телефонен номер в системата!\n");
            }

            alert.show();
            table.refresh();
        });
        //END PHONE NUMBER

        checkBox.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        try {
            userList = userListInserter();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        FilteredList<User> filteredData = new FilteredList<>(userList, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getCourseNum().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getEgn().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getPhoneNum().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
    }

    @FXML void updateDescription(ActionEvent event) throws SQLException, IOException {
        int id;
        String updatedDesc;
        if (table.getSelectionModel().getSelectedItem() != null) {
            //ALERT are you sure
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Потвърждаване на действие");
            alert.setHeaderText("");
            alert.setContentText("Напът сте да промените описанието на ученик!\nНатистнете OK за да продължите!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

            Optional<ButtonType> result = alert.showAndWait();
            //ALERT are you sure
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //ALERT updated
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Успешно действие");
                alert.setContentText("Описанието е обновено!");
                alert.show();
                //ALERT updated
                id = table.getSelectionModel().getSelectedItem().getId();
                updatedDesc = showDesc.getText();
                showDesc.setText(updatedDesc);
                jdbcQuery = new JDBC("SELECT * FROM users", "UPDATE users SET description=? WHERE id=?");
                jdbcQuery.writeData.setString(1, updatedDesc);
                jdbcQuery.writeData.setString(2, Integer.toString(id));
                jdbcQuery.writeData.executeUpdate();
                Utility.changeScene(event, "/Scenes/ManageUser.fxml");
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Грешка при избиране");
            alert.setContentText("Моля изберете ученик!");
            alert.show();
        }
    }

    @FXML void deleteSelectedUsers(ActionEvent event) throws SQLException {
        ObservableList<User> usersToRemoveList = FXCollections.observableArrayList();

        jdbcQuery = new JDBC("SELECT * FROM users", "DELETE FROM users WHERE id=?");
        JDBC jdbcDeleteFromRent = new JDBC("SELECT * FROM rent", "DELETE FROM rent WHERE id_user=?");
        JDBC jdbcRented = new JDBC("SELECT * FROM rent", "SELECT * FROM rent");

        boolean isSelected = false;
        for (User user : userList) {
            if (user.getCheckBox().isSelected()) {
                isSelected = true;
                break;
            }
        }

        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Потвърждаване на действие");
        alert.setHeaderText("");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

        boolean hasRented = false;
        try {
            for (User user : userList) {
                if (user.getCheckBox().isSelected()) {
                    int userID = user.getId();
                    while (jdbcRented.resultSet.next()) {
                        int currUserId = Integer.parseInt(jdbcRented.resultSet.getString("id_user"));

                        if (currUserId == userID) {
                            hasRented = true;
                            break;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isSelected) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Грешка при избиране");
            alert.setContentText("Моля изберете ученик за изтриване!\n");
        }

        if (hasRented) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setTitle("Грешка при изтриване");
            alert.setHeaderText("");
            alert.setContentText("Ученика има медия за връщане!\nКогато се върне взетата медия тогава този ученик ще може да бъде изтрит!\n");
        }

        if (alert.getContentText().equals("")) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Напът сте да изтриете избраните ученици!\nНатистнете OK за да продължите!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (User user : userList) {
                    if (user.getCheckBox().isSelected()) {
                        usersToRemoveList.add(user);
                        jdbcDeleteFromRent.writeData.setString(1, Integer.toString(user.getId()));
                        jdbcQuery.writeData.setString(1, Integer.toString(user.getId()));

                        jdbcDeleteFromRent.writeData.executeUpdate();
                        jdbcQuery.writeData.executeUpdate();
                    }
                }
                userList.removeAll(usersToRemoveList);
            }
        } else {
            alert.show();
        }
    }

    @FXML void insertUser(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/InsertUser.fxml");
    }

    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/MainMenu.fxml");
    }
}
