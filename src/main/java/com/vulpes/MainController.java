package com.vulpes;

import com.vulpes.datamodel.Engine;
import com.vulpes.datamodel.Entry;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML
    private BorderPane mainPanel;
    @FXML
    private ListView<Entry> listView;
    @FXML
    private Label zamiarField;
    @FXML
    private Label nrUmowyField;
    @FXML
    private Label nrRejField;
    @FXML
    private Label startTimeField;
    @FXML
    private Label startGpsField;
    @FXML
    private Label endTimeField;
    @FXML
    private Label distanceField;
    @FXML
    private TextArea gps2Field;

    public void initialize() {
        listView.setItems(Engine.getInstance().getEntries());
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Entry>() {
            @Override
            public void changed(ObservableValue<? extends Entry> observable, Entry oldValue, Entry newValue) {
                if (newValue != null) {
                    Entry entry = listView.getSelectionModel().getSelectedItem();
                    zamiarField.setText(entry.getZamiar());
                    nrUmowyField.setText(entry.getNrUmowy());
                    nrRejField.setText(entry.getNrRej());
                    startTimeField.setText(entry.getStartTime());
                    startGpsField.setText(entry.getStartGps());
                    endTimeField.setText(entry.getEndTime());
                    distanceField.setText(entry.getDistance());
                    gps2Field.setText(entry.generateGps2Preview());
                } else {
                    zamiarField.setText("");
                    nrUmowyField.setText("");
                    nrRejField.setText("");
                    startTimeField.setText("");
                    startGpsField.setText("");
                    endTimeField.setText("");
                    distanceField.setText("");
                    gps2Field.setText("");
                }
            }
        });
        listView.getSelectionModel().selectLast();
    }

    @FXML
    public void addEntry(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Dodaj nowy kurs");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("entrydialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> result = dialog.showAndWait();


        if (result.isPresent() && result.get() == ButtonType.OK) {
            EntryDialogController controller = fxmlLoader.getController();
            Entry newEntry = controller.processResults();
            Engine.getInstance().addEntry(newEntry);
            listView.getSelectionModel().select(newEntry);
        }
    }

    @FXML    public void generateGpsFiles(){
        Engine.getInstance().writeToGps();
        Engine.getInstance().saveGps();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Operacja powiodła się.");
        alert.setContentText("Pomyślnie wygenerowano pliki gps1 i gps2.");
        alert.showAndWait();

    }

    @FXML
    public void removeEntry(){
        Entry selectedEntry = listView.getSelectionModel().getSelectedItem();
        if (selectedEntry == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nie wybrano żadnego kursu");
            alert.setContentText("Najpierw wybierz kurs, który chcesz usunąć.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Usuń kurs");
            alert.setContentText("Na pewno chcesz usnąć zaznaczony kurs?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Engine.getInstance().removeEntry(selectedEntry);
            }
        }
    }

}

