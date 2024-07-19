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
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class RentedMediaCtrl implements Initializable {
    @FXML private TextField searchBar;
    @FXML private TableView<RentedMedia> table;
    @FXML private TableColumn<RentedMedia, String> courseNum;
    @FXML private TableColumn<RentedMedia, String> userName;
    @FXML private TableColumn<RentedMedia, String> mediaName;
    @FXML private TableColumn<RentedMedia, String> startDate;
    @FXML private TableColumn<RentedMedia, String> endDate;
    @FXML private TableColumn<RentedMedia, String> checkBox;
    ObservableList<RentedMedia> rentedMediaList;
    SortedList<RentedMedia> sortedData;
    private JDBC jdbcQuery;
    Alert alert;

    //INSERTING RENTED MEDIA
    public ObservableList<RentedMedia> rentedMediaListInserter() throws SQLException {
        jdbcQuery = new JDBC("SELECT rent.id_rent,users.name AS user_name, users.course_number, media.name AS media_name, media.id AS media_id, media.quantity AS media_quantity, media.qr_code, rent.start_date, rent.end_date FROM rent INNER JOIN media ON rent.id_media = media.id INNER JOIN users ON rent.id_user = users.id;", "SELECT rent.id_rent, users.name AS user_name, users.course_number, media.name AS media_name, media.id AS media_id, media.quantity AS media_quantity, media.qr_code, rent.start_date , rent.end_date FROM rent INNER JOIN media ON rent.id_media = media.id INNER JOIN users ON rent.id_user = users.id;");
        ObservableList<RentedMedia> rentedMediaToBeAdded = FXCollections.observableArrayList();

        int mediaID, mediaQuantity, rentID;
        RentedMedia rentedMedia;
        CheckBox checkBox;
        String userName, mediaName, courseNum, startDate, endDate, qrCode;

        while (jdbcQuery.resultSet.next()) {
            rentID = jdbcQuery.resultSet.getInt("rent.id_rent");
            mediaID = jdbcQuery.resultSet.getInt("media_id");
            courseNum = jdbcQuery.resultSet.getString("course_number");
            userName = jdbcQuery.resultSet.getString("user_name");
            mediaName = jdbcQuery.resultSet.getString("media_name");
            mediaQuantity = Integer.parseInt(jdbcQuery.resultSet.getString("media_quantity"));
            checkBox = new CheckBox();
            startDate = jdbcQuery.resultSet.getString("start_date");
            endDate = jdbcQuery.resultSet.getString("end_date");
            qrCode = jdbcQuery.resultSet.getString("media.qr_code");

            rentedMedia = new RentedMedia(rentID, mediaID, userName, mediaName, courseNum, mediaQuantity, checkBox, startDate, endDate, qrCode);

            rentedMediaToBeAdded.add(rentedMedia);
        }

        jdbcQuery.resultSet.close();
        return rentedMediaToBeAdded;
    }
    //END INSERTING RENTED MEDIA

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //ASSIGNING COLUMNS TO TABLE
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        mediaName.setCellValueFactory(new PropertyValueFactory<>("mediaName"));
        courseNum.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        checkBox.setCellValueFactory(new PropertyValueFactory<>("checkBox"));
        startDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        //ASSIGNING COLUMNS TO TABLE

        //INSERTING DATA INTO TABLE
        try {
            rentedMediaList = rentedMediaListInserter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //INSERTING DATA INTO TABLE

        //CHANGE ROW COLOR FOR MISSING RENT
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        table.setRowFactory(tv -> new TableRow<RentedMedia>() {
            @Override
            protected void updateItem(RentedMedia item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null) {
                    setStyle("");
                } else if (Integer.parseInt(java.time.LocalDate.now().toString().replace("-", "")) > Integer.parseInt(item.getEndDate().replace("-", ""))) {
                    setStyle("-fx-background-color: red");
                } else {
                    setStyle("");
                }
            }
        });
        //CHANGE ROW COLOR FOR MISSING RENT

        //SEARCH BAR
        FilteredList<RentedMedia> filteredRentedMediaList = new FilteredList<>(rentedMediaList, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredRentedMediaList.setPredicate(rentedMedia -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (rentedMedia.getUserName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rentedMedia.getMediaName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rentedMedia.getCourseNum().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rentedMedia.getQrCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rentedMedia.getStartDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (rentedMedia.getEndDate().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });

        sortedData = new SortedList<>(filteredRentedMediaList);

        sortedData.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedData);
        //SEARCH BAR
    }

    @FXML void returnSelectedMedia(ActionEvent event) throws SQLException, IOException {
        ArrayList<Integer> rentIDs = new ArrayList<>();
        HashMap<Integer, Integer> mediaToBeReturned = new HashMap<>();
        int amount = 1;
        int mediaID;

        jdbcQuery = new JDBC("SELECT * FROM rent", "DELETE FROM rent WHERE id_rent=?");
        JDBC quantityEditor = new JDBC("SELECT * FROM media", "UPDATE media SET quantity=? WHERE id=?");

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Потвърждаване на действие");
        alert.setHeaderText("");
        alert.setContentText("Напът сте да върнете избраната медиа!\nНатиснете OK за да продължите!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

        boolean isSelected = false;
        for (RentedMedia rentedMedia : rentedMediaList) {
            if (rentedMedia.getCheckBox().isSelected()) {
                isSelected = true;
                break;
            }
        }

        if (isSelected) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (RentedMedia rentedMedia : rentedMediaList) {
                    mediaID = rentedMedia.getMediaID();
                    if (rentedMedia.getCheckBox().isSelected()) {
                        if (!mediaToBeReturned.containsKey(mediaID)) {
                            rentIDs.add(rentedMedia.getRentID());
                            mediaToBeReturned.put(mediaID, 1);
                        } else {
                            rentIDs.add(rentedMedia.getRentID());
                            amount = mediaToBeReturned.get(mediaID);
                            mediaToBeReturned.replace(mediaID, amount + 1);
                        }
                    }
                }
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Грешка при избиране на медиа");
            alert.setContentText("Моля изберете медиа за връщане!");
            alert.show();
        }
        for (int id : mediaToBeReturned.keySet()) {

            JDBC getInfo = new JDBC("Select * from media" , "Select * from media");
            while(getInfo.resultSet.next()) {

                if(getInfo.resultSet.getInt("id") == id) {
                    int mediaQuantity = getInfo.resultSet.getInt("quantity");
                    quantityEditor.writeData.setString(1, Integer.toString(mediaQuantity + mediaToBeReturned.get(id)));
                    quantityEditor.writeData.setString(2, Integer.toString(id));

                    quantityEditor.writeData.executeUpdate();

                }
            }
        }
        for (int i = 0; i < rentIDs.size(); i++) {
            jdbcQuery.writeData.setInt(1, rentIDs.get(i));
            jdbcQuery.writeData.executeUpdate();
        }
        Utility.changeScene(event, "/Scenes/RentedMedia.fxml");
    }

    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/Rent.fxml");
    }
}
