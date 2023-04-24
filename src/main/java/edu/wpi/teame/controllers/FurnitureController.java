
package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.FurnitureRequestData;
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

public class FurnitureController {
  ObservableList<String> typeOfFurniture =
      FXCollections.observableArrayList(
          "cot", "desk chair", "stool", "futon", "filing cabinet", "shelves");

  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  @FXML MFXButton submitButton;
  @FXML TextField recipientName;
  @FXML SearchableComboBox<String> roomName;
  @FXML DatePicker deliveryDate;
  @FXML SearchableComboBox<String> deliveryTime;
  @FXML SearchableComboBox<String> furnitureType;
  @FXML TextField notes;
  @FXML SearchableComboBox<String> assignedStaff;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;

  public void initialize() {
    requestSubmittedBox.setVisible(false);

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

    List<Employee> employeeList = SQLRepo.INSTANCE.getEmployeeList();
    for (Employee emp : employeeList) {
      staffMembers.add(emp.getUsername());
    }

    assignedStaff.setItems(FXCollections.observableArrayList(staffMembers));
    /*assignedStaff.setItems(
    FXCollections.observableList(
        SQLRepo.INSTANCE.getEmployeeList().stream()
            .filter(employee -> employee.getPermission().equals("STAFF"))
            .map(employee -> employee.getFullName())
            .toList()));*/

    roomName.setItems(names);
    // Add the items to the combo boxes
    furnitureType.setItems(typeOfFurniture);
    deliveryTime.setItems(deliveryTimes);
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

  public FurnitureRequestData sendRequest() {

    // Create the service request data
    FurnitureRequestData requestData =
        new FurnitureRequestData(
            0,
            recipientName.getText(),
            roomName.getValue(),
            deliveryDate.getValue().toString(),
            deliveryTime.getValue(),
            assignedStaff.getValue(),
            furnitureType.getValue(),
            notes.getText(),
            FurnitureRequestData.Status.PENDING);
    SQLRepo.INSTANCE.addServiceRequest(requestData);
    System.out.println("furniture request submitted");

    // Return to the home screen

    return requestData;
  }

  // Cancels the current service request
  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  // Clears the current service request fields
  public void clearForm() {
    furnitureType.setValue(null);
    deliveryTime.setValue(null);
    deliveryDate.setValue(null);
    roomName.setValue(null);
    recipientName.clear();
    notes.clear();
    assignedStaff.setValue(null);
  }
}
