package Controllers;

import Classes.JDBC;
import Classes.Media;
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
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageMediaCtrl implements Initializable {
    @FXML private TextField searchBar;
    @FXML private TableView<Media> table;
    @FXML private TableColumn<Media, String> name;
    @FXML private TableColumn<Media, String> author;
    @FXML private TableColumn<Media, Integer> year;
    @FXML private TableColumn<Media, String> genre;
    @FXML private TableColumn<Media, Integer> quantity;
    @FXML private TableColumn<Media, String> checkBox;
    @FXML private TextArea showDesc;
    public int currID;
    public int lastID;
    Alert alert;
    ObservableList<Media> mediaList;
    SortedList<Media> sortedData;
    private JDBC jdbcQuery;

    public ObservableList<Media> mediaListInserter() throws SQLException {
        jdbcQuery = new JDBC("SELECT * FROM media", "SELECT * FROM media");

        ObservableList<Media> mediaToBeAddedList = FXCollections.observableArrayList();

        Media media;
        CheckBox checkBox;
        String name, author, genre, description, qrCode;
        int id, year, quantity;

        while (jdbcQuery.resultSet.next()) {
            id = Integer.parseInt(jdbcQuery.resultSet.getString("id"));
            name = jdbcQuery.resultSet.getString("name");
            author = jdbcQuery.resultSet.getString("author");
            year = Integer.parseInt(jdbcQuery.resultSet.getString("year"));
            genre = jdbcQuery.resultSet.getString("genre");
            description = jdbcQuery.resultSet.getString("description");
            quantity = Integer.parseInt(jdbcQuery.resultSet.getString("quantity"));
            qrCode = jdbcQuery.resultSet.getString("qr_code");
            checkBox = new CheckBox();

            media = new Media(id, name, author, year, genre, description, quantity, qrCode, checkBox);

            mediaToBeAddedList.add(media);
        }

        jdbcQuery.resultSet.close();
        return mediaToBeAddedList;
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

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.<Media>forTableColumn());
        name.setOnEditCommit(event -> {
            Media media = event.getRowValue();
            media.setName(event.getNewValue());
            try {
                jdbcQuery = new JDBC("SELECT * FROM media", "UPDATE media SET name=? WHERE id=?");
                jdbcQuery.writeData.setString(1, event.getNewValue());
                jdbcQuery.writeData.setString(2, Integer.toString(media.getId()));
                jdbcQuery.writeData.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        author.setCellFactory(TextFieldTableCell.<Media>forTableColumn());
        author.setOnEditCommit(event -> {
            Media media = event.getRowValue();
            media.setAuthor(event.getNewValue());
            try {
                jdbcQuery = new JDBC("SELECT * FROM media", "UPDATE media SET author=? WHERE id=?");
                jdbcQuery.writeData.setString(1, event.getNewValue());
                jdbcQuery.writeData.setString(2, Integer.toString(media.getId()));
                jdbcQuery.writeData.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        year.setCellValueFactory(new PropertyValueFactory<>("year"));
        year.setCellFactory(TextFieldTableCell.<Media, Integer>forTableColumn(new IntegerStringConverter()));
        year.setOnEditCommit(event -> {
            Media media = event.getRowValue();
            media.setYear(event.getNewValue());
            try {
                jdbcQuery = new JDBC("SELECT * FROM media", "UPDATE media SET year=? WHERE id=?");
                jdbcQuery.writeData.setString(1, Integer.toString(event.getNewValue()));
                jdbcQuery.writeData.setString(2, Integer.toString(media.getId()));
                jdbcQuery.writeData.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genre.setCellFactory(TextFieldTableCell.<Media>forTableColumn());
        genre.setOnEditCommit(event -> {
            Media media = event.getRowValue();
            media.setGenre(event.getNewValue());
            try {
                jdbcQuery = new JDBC("SELECT * FROM media", "UPDATE media SET genre=? WHERE id=?");
                jdbcQuery.writeData.setString(1, event.getNewValue());
                jdbcQuery.writeData.setString(2, Integer.toString(media.getId()));
                jdbcQuery.writeData.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantity.setCellFactory(TextFieldTableCell.<Media, Integer>forTableColumn(new IntegerStringConverter()));
        quantity.setOnEditCommit(event -> {
            Media media = event.getRowValue();
            media.setQuantity(event.getNewValue());
            try {
                jdbcQuery = new JDBC("SELECT * FROM media", "UPDATE media SET quantity=? WHERE id=?");
                jdbcQuery.writeData.setString(1, Integer.toString(event.getNewValue()));
                jdbcQuery.writeData.setString(2, Integer.toString(media.getId()));
                jdbcQuery.writeData.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        checkBox.setCellValueFactory(new PropertyValueFactory<>("checkBox"));

        try {
            mediaList = mediaListInserter();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //SEARCH BAR
        FilteredList<Media> filteredData = new FilteredList<>(mediaList, b -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(media -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (media.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (media.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(media.getYear()).contains(lowerCaseFilter)) {
                    return true;
                } else if (media.getGenre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (media.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(media.getQuantity()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(media.getQrCode()).contains(lowerCaseFilter)) {
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
            alert.setContentText("Напът сте да промените описанието на медия!\nНатистнете OK за да продължите!");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

            Optional<ButtonType> result = alert.showAndWait();
            //ALERT are you sure
            if(result.isPresent() && result.get() == ButtonType.OK) {
                //ALERT updated
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setTitle("Успешно действие");
                alert.setContentText("Описанието е обновено!");
                alert.show();
                //ALERT updated
                id = table.getSelectionModel().getSelectedItem().getId();
                updatedDesc = showDesc.getText();
                showDesc.setText(updatedDesc);
                jdbcQuery = new JDBC("SELECT * FROM media", "UPDATE media SET description=? WHERE id=?");
                jdbcQuery.writeData.setString(1, updatedDesc);
                jdbcQuery.writeData.setString(2, Integer.toString(id));
                jdbcQuery.writeData.executeUpdate();
                Utility.changeScene(event, "/Scenes/ManageMedia.fxml");
            }
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Грешка при избиране");
            alert.setContentText("Моля изберете медия!");
            alert.show();
        }
    }


    @FXML void deleteSelectedMedia(ActionEvent event) throws SQLException {
        ObservableList<Media> mediaToRemoveList = FXCollections.observableArrayList();

        jdbcQuery = new JDBC("SELECT * FROM media", "DELETE FROM media WHERE id=?");
        JDBC jdbcDeleteFromRent = new JDBC("SELECT * FROM rent", "DELETE FROM rent WHERE id_media=?");

        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Потвърждаване на действие");
        alert.setHeaderText("");
        alert.setContentText("Напът сте да изтриете избраната медия!\nНатиснете OK за да продължите!");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/Styles/img/logo.png").toString()));

        boolean isSelected = false;
        for (Media media : mediaList) {
            if (media.getCheckBox().isSelected()) {
                isSelected = true;
                break;
            }
        }

        if (isSelected) {
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                for (Media media : mediaList) {
                    if (media.getCheckBox().isSelected()) {
                        mediaToRemoveList.add(media);
                        jdbcDeleteFromRent.writeData.setString(1, Integer.toString(media.getId()));
                        jdbcQuery.writeData.setString(1, Integer.toString(media.getId()));

                        jdbcDeleteFromRent.writeData.executeUpdate();
                        jdbcQuery.writeData.executeUpdate();
                    }
                }
            }
            mediaList.removeAll(mediaToRemoveList);
        } else {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Грешка при избиране");
            alert.setContentText("Моля изберете медиа за изтриване!");
            alert.show();
        }
    }

    @FXML void insertMedia(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/InsertMedia.fxml");
    }

    @FXML void back(ActionEvent event) throws IOException {
        Utility.changeScene(event, "/Scenes/MainMenu.fxml");
    }
}
