package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.OfficeSuppliesData;
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
import org.controlsfx.control.SearchableComboBox;

public class OfficeSuppliesController {

  @FXML MFXButton returnButtonOfficeSuppliesRequest;
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

  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");
  ObservableList<String> officeSupplies =
      FXCollections.observableArrayList(
          "pencils", "pens", "white-out", "tape", "ruler", "hole puncher", "sharpener", "charger");

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

    /*assignedStaff.setItems(
    FXCollections.observableList(
        SQLRepo.INSTANCE.getEmployeeList().stream()
            .filter(employee -> employee.getPermission().equals("STAFF"))
            .map(employee -> employee.getFullName())
            .toList()));*/

    List<Employee> employeeList = SQLRepo.INSTANCE.getEmployeeList();
    for (Employee emp : employeeList) {
      staffMembers.add(emp.getUsername());
    }

    assignedStaff.setItems(FXCollections.observableArrayList(staffMembers));

    roomName.setItems(names);
    deliveryTime.setItems(deliveryTimes);
    supplyType.setItems(officeSupplies);
    submitButton.setOnMouseClicked(event -> sendRequest());
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
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

  public OfficeSuppliesData sendRequest() {
    OfficeSuppliesData requestData =
        new OfficeSuppliesData(
            0,
            recipientName.getText(),
            roomName.getValue(),
            deliveryDate.getValue().toString(),
            deliveryTime.getValue(),
            assignedStaff.getValue(),
            supplyType.getValue(),
            numberOfSupplies.getText(),
            notes.getText(),
            OfficeSuppliesData.Status.PENDING);
    Navigation.navigate(Screen.HOME);
    SQLRepo.INSTANCE.addServiceRequest(requestData);
    return requestData;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }
}
