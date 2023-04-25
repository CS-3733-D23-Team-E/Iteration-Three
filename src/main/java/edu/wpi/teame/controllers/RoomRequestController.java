package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.ConferenceRequestData;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class RoomRequestController {
  ObservableList<String> times =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");

  ObservableList<String> changes =
      FXCollections.observableArrayList(
          "Add chair",
          "Add 2 chairs",
          "Add 3 chairs",
          "Add a table",
          "Add a fan",
          "Add a whiteboard",
          "Add a projector and screen");

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  @FXML TextField recipientName;
  @FXML SearchableComboBox<String> roomName;
  @FXML SearchableComboBox<String> bookingTime;
  @FXML SearchableComboBox<String> roomChanges;
  @FXML TextField numberOfHours;
  @FXML DatePicker bookingDate;
  @FXML TextField notes;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML SearchableComboBox<String> assignedStaff;
  @FXML MFXButton submitButton;
  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;

  @FXML
  public void initialize() {
    requestSubmittedBox.setVisible(false);

    // Add the items to the combo boxes
    Stream<LocationName> locationStream = LocationName.allLocations.values().stream();
    ObservableList<String> names =
        FXCollections.observableArrayList(
            locationStream
                .filter(
                    (locationName) -> {
                      return locationName.getNodeType() == LocationName.NodeType.CONF;
                    })
                .map(
                    (locationName) -> {
                      return locationName.getLongName();
                    })
                .sorted()
                .toList());

    /*assignedStaff.setItems(
            FXCollections.observableList(
                SQLRepo.INSTANCE.getEmployeeList().stream()
                    .filter(employee -> employee.getPermission().equals("STAFF"))
                    .map(employee -> employee.getFullName())
                    .toList()));
    */

    List<Employee> employeeList = SQLRepo.INSTANCE.getEmployeeList();
    for (Employee emp : employeeList) {
      staffMembers.add(emp.getUsername());
    }

    assignedStaff.setItems(FXCollections.observableArrayList(staffMembers));
    roomName.setItems(names);
    bookingTime.setItems(times);
    roomChanges.setItems(changes);
    // Initialize the buttons

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

  public ConferenceRequestData sendRequest() {

    // Create the service request data
    ConferenceRequestData requestData =
        new ConferenceRequestData(
            0,
            recipientName.getText(),
            roomName.getValue(),
            bookingDate.getValue().toString(),
            bookingTime.getValue(),
            assignedStaff.getValue(),
            roomChanges.getValue(),
            notes.getText(),
            ConferenceRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(requestData);

    // Return to the home screen

    return requestData;
  }

  // Cancels the current service request
  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  // Clears the current service request fields
  public void clearForm() {
    bookingTime.setValue(null);
    recipientName.clear();
    roomName.setValue(null);
    notes.clear();
    assignedStaff.setValue(null);
    numberOfHours.clear();
    roomChanges.setValue(null);
    bookingDate.setValue(null);
  }
}
