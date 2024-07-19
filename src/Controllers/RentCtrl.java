package Controllers;

import Classes.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class RentCtrl implements Initializable {
    @FXML TextField userSearch;
    @FXML TextField mediaSearch;
    @FXML private TableView<Media> mediaTable;
    @FXML private TableColumn<Media, String> mediaName;
    @FXML private TableColumn<Media, Integer> quantity;
    @FXML private TableColumn<Media, String> checkBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> userName;
    @FXML private TableColumn<User, String> courseNum;
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    ObservableList<User> userList;
    ObservableList<Media> mediaList;
    SortedList<User> sortedUserData;
    SortedList<Media> sortedMediaData;
    private JDBC jdbcQuery;
    Alert alert;

    public ObservableList<Media> mediaListInserter() throws SQLException {
        jdbcQuery = new JDBC("SELECT * FROM media", "SELECT * FROM media");

        ObservableList<Media> mediaToBeAddedList = FXCollections.observableArrayList();

        Media media;
        CheckBox checkBox;
        String name, qrCode;
        int id, quantity;

        while (jdbcQuery.resultSet.next()) {
            id = Integer.parseInt(jdbcQuery.resultSet.getString("id"));
            name = jdbcQuery.resultSet.getString("name");
            quantity = Integer.parseInt(jdbcQuery.resultSet.getString("quantity"));
            qrCode = jdbcQuery.resultSet.getString("qr_code");
            checkBox = new CheckBox();

            if (quantity == 0) {
                continue;
            }

            media = new Media();
            media.setId(id);
            media.setName(name);
            media.setQuantity(quantity);
            media.setQrCode(qrCode);
            media.setCheckBox(checkBox);

            mediaToBeAddedList.add(media);
        }

        jdbcQuery.resultSet.close();
        return mediaToBeAddedList;
    }

    public ObservableList<User> userListInserter() throws SQLException {
        jdbcQuery = new JDBC("SELECT * FROM users", "SELECT * FROM users");

        ObservableList<User> usersToBeAdded = FXCollections.observableArrayList();

        User user;
        String name, courseNum;
        int id;

        while (jdbcQuery.resultSet.next()) {
            id = Integer.parseInt(jdbcQuery.resultSet.getString("id"));
            name = jdbcQuery.resultSet.getString("name");
            courseNum = jdbcQuery.resultSet.getString("course_number");

            user = new User();
            user.setId(id);
            user.setName(name);
            user.setCourseNum(courseNum);

            usersToBeAdded.add(user);
        }

        jdbcQuery.resultSet.close();
        return usersToBeAdded;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //USER TABLE COLUMNS
        userName.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseNum.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        //USER TABLE COLUMNS

        //MEDIA TABLE COLUMNS
        mediaName.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        checkBox.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        //MEDIA TABLE COLUMNS

        try {
            mediaList = mediaListInserter();
            userList = userListInserter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        userTable.setItems(userList);

        //MEDIA SEARCH
        FilteredList<Media> filteredMediaData = new FilteredList<>(mediaList, b -> true);

        mediaSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredMediaData.setPredicate(media -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (media.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(media.getQuantity()).contains(lowerCaseFilter)) {
                    return true;
                } else if (media.getQrCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedMediaData = new SortedList<>(filteredMediaData);

        sortedMediaData.comparatorProperty().bind(mediaTable.comparatorProperty());

        mediaTable.setItems(sortedMediaData);
        //MEDIA SEARCH

        //USER SEARCH

        FilteredList<User> filteredUserData = new FilteredList<>(userList, b -> true);

        userSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUserData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getCourseNum().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedUserData = new SortedList<>(filteredUserData);

        sortedUserData.comparatorProperty().bind(userTable.comparatorProperty());

        userTable.setItems(sortedUserData);
        //USER SEARCH
    }

    @FXML
    void rentMedia(ActionEvent event) throws IOException, SQLException {
        int userID;
        int quantity;
        int mediaID;
        String userName;
        LocalDate myStartDate, myEndDate;
        String formattedStartDate, formattedEndDate;

        jdbcQuery = new JDBC("SELECT * FROM rent", "INSERT INTO rent(id_media, id_user, start_date, end_date) VALUES (?,?,?,?)");
        JDBC quantityEditor = new JDBC("SELECT * FROM media", "UPDATE media SET quantity=? WHERE id=?");

        boolean isSelected = false;
        for (Media media : mediaList) {
            if (media.getCheckBox().isSelected()) {
                isSelected = true;
                break;
            }
        }

        alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Грешка при избиране");
        alert.setHeaderText("");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

        if (userTable.getSelectionModel().getSelectedItem() == null) {
            alert.setContentText(alert.getContentText() + "Моля изберете ученик!\n\n");
        }

        if (startDate.getValue() == null && endDate.getValue() == null) {
            alert.setContentText(alert.getContentText() + "Моля изберете дати!\n\n");
        } else {
            String[] startSplit = startDate.getValue().toString().split("-");
            String startTime = startSplit[0] + startSplit[1] + startSplit[2];
            String[] endSplit = endDate.getValue().toString().split("-");
            String endTime = endSplit[0] + endSplit[1] + endSplit[2];

            if (Integer.parseInt(startTime) > Integer.parseInt(endTime)) {
                alert.setContentText(alert.getContentText() + "Крайната дата е преди началната!\n\n");
            }
        }

        if (!isSelected) {
            alert.setContentText(alert.getContentText() + "Моля изберете медия за вземане!\n\n");
        }

        if (alert.getContentText().equals("")) {
            myStartDate = startDate.getValue();
            myEndDate = endDate.getValue();

            formattedStartDate = myStartDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            formattedEndDate = myEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            userID = userTable.getSelectionModel().getSelectedItem().getId();
            userName = userTable.getSelectionModel().getSelectedItem().getName();

            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Потвърждаване на действие");
            alert.setContentText("Напът сте да заемете на " + userName + " дадената медиа:\n");
            for (Media media : mediaList) {
                if (media.getCheckBox().isSelected()) {
                    alert.setContentText(alert.getContentText() + media.getName() + "\n");
                }
            }
            alert.setContentText(alert.getContentText() + "Натиснете OK за да продължите!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (Media media : mediaList) {
                    mediaID = media.getId();
                    quantity = media.getQuantity();

                    if (media.getCheckBox().isSelected()) {
                        quantityEditor.writeData.setString(1, Integer.toString(quantity - 1));

                        quantityEditor.writeData.setString(2, Integer.toString(mediaID));

                        jdbcQuery.writeData.setInt(1, mediaID);
                        jdbcQuery.writeData.setInt(2, userID);
                        jdbcQuery.writeData.setString(3, formattedStartDate);
                        jdbcQuery.writeData.setString(4, formattedEndDate);

                        quantityEditor.writeData.executeUpdate();
                        jdbcQuery.writeData.executeUpdate();
                    }
                }
                Utility.changeScene(event, "/Scenes/Rent.fxml");
            }
        } else {
            alert.show();
        }
    }

    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/MainMenu.fxml");
    }

    @FXML void rentedMedia(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/RentedMedia.fxml");
    }
}