package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.FlowerRequestData;
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

public class FlowerRequestController {

  ObservableList<String> flowerChoices =
      FXCollections.observableArrayList("Tiger Lilies", "Roses", "Tulips", "Daisies");

  ObservableList<String> flowerNum =
      FXCollections.observableArrayList(
          "1", "2", "Small Bouquet (6)", "Medium Bouquet (8)", "Large Bouquet (12)");

  ObservableList<String> yesNo = FXCollections.observableArrayList("yes", "no");

  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  @FXML MFXButton submitButton;
  @FXML TextField recipientName;
  @FXML SearchableComboBox<String> roomName;
  @FXML DatePicker deliveryDate;
  @FXML SearchableComboBox<String> deliveryTime;
  @FXML SearchableComboBox<String> flowerChoice;
  @FXML SearchableComboBox<String> numOfFlowers;
  @FXML SearchableComboBox<String> cardQuestion;
  @FXML TextField cardMessage;
  @FXML TextField notes;
  @FXML SearchableComboBox<String> assignedStaff;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;

  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;

  @FXML
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

    assignedStaff.setItems(
        FXCollections.observableList(
            SQLRepo.INSTANCE.getEmployeeList().stream()
                .filter(employee -> employee.getPermission().equals("STAFF"))
                .map(employee -> employee.getUsername())
                .toList()));

    roomName.setItems(names);
    // Add the items to the combo boxes
    flowerChoice.setItems(flowerChoices);
    numOfFlowers.setItems(flowerNum);
    deliveryTime.setItems(deliveryTimes);
    cardQuestion.setItems(yesNo);
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

  public FlowerRequestData sendRequest() {
    System.out.println("send flower request");

    // Create the service request data
    FlowerRequestData requestData =
        new FlowerRequestData(
            0,
            recipientName.getText(),
            roomName.getValue(),
            deliveryDate.getValue().toString(),
            deliveryTime.getValue(),
            assignedStaff.getValue(),
            flowerChoice.getValue(),
            numOfFlowers.getValue(),
            cardQuestion.getValue(),
            cardMessage.getText(),
            notes.getText(),
            FlowerRequestData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(requestData);

    return requestData;
  }
  // Cancels the current service request
  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  // Clears the current service request fields
  public void clearForm() {
    flowerChoice.setValue(null);
    numOfFlowers.setValue(null);
    deliveryDate.setValue(null);
    deliveryTime.setValue(null);
    roomName.setValue(null);
    cardQuestion.setValue(null);
    cardMessage.clear();
    recipientName.clear();
    notes.clear();
    assignedStaff.setValue(null);
  }

  // public List<Employee> getEmployeeList() {
  // return employeeList; }
}