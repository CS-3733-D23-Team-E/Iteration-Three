package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.MedicalSuppliesData;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class MedicalSupplyRequestController {

  @FXML MFXButton returnButtonMedicalSuppliesRequest;
  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML TextField recipientName;
  @FXML SearchableComboBox<String> roomName;
  @FXML TextField notes;
  @FXML SearchableComboBox<String> deliveryTime;

  @FXML DatePicker deliveryDate;
  @FXML SearchableComboBox<String> supplyType;
  @FXML TextField numberOfSupplies;
  @FXML SearchableComboBox<String> assignedStaff;

  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;
  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");
  ObservableList<String> MedicalSupplies =
      FXCollections.observableArrayList(
          "Stethoscope",
          "Band-aid",
          "Covid Test",
          "Syringe",
          "Surgical Gloves",
          "Thermometer",
          "Scalpel",
          "Epi-Pen",
          "First Aid Kit");

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  @FXML
  public void initialize() {
    Stream<LocationName> locationStream = LocationName.allLocations.values().stream();
    ObservableList<String> names =
        FXCollections.observableArrayList(
            locationStream
                .filter(
                    (locationName) -> {
                      return locationName.getNodeType() != LocationName.NodeType.HALL
                          && locationName.getNodeType() != LocationName.NodeType.STAI
                          && locationName.getNodeType() != LocationName.NodeType.REST
                          && locationName.getNodeType() != LocationName.NodeType.ELEV;
                    })
                .map(
                    (locationName) -> {
                      return locationName.getLongName();
                    })
                .sorted()
                .toList());

    assignedStaff.setItems(
        FXCollections.observableList(
            SQLRepo.INSTANCE.getEmployeeList().stream()
                .filter(employee -> employee.getPermission().equals("STAFF"))
                .map(employee -> employee.getUsername())
                .toList()));

    roomName.setItems(names);
    deliveryTime.setItems(deliveryTimes);
    supplyType.setItems(MedicalSupplies);

    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());

    submitButton.setOnMouseClicked(
        event -> {
          sendRequest();
          requestSubmittedBox.setVisible(true);
          clearForm();
        });
    closeButton.setOnMouseClicked(event -> requestSubmittedBox.setVisible(false));
  }

  private void clearForm() {
    recipientName.clear();
    roomName.setValue(null);
    notes.clear();
    deliveryTime.setValue(null);
    supplyType.setValue(null);
    numberOfSupplies.clear();
    assignedStaff.setValue(null);
  }

  public MedicalSuppliesData sendRequest() {
    MedicalSuppliesData requestData =
        new MedicalSuppliesData(
            0,
            recipientName.getText(),
            roomName.getValue(),
            deliveryDate.getValue().toString(),
            deliveryTime.getValue(),
            assignedStaff.getValue(),
            supplyType.getValue(),
            numberOfSupplies.getText(),
            notes.getText(),
            MedicalSuppliesData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(requestData);
    return requestData;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }
}
