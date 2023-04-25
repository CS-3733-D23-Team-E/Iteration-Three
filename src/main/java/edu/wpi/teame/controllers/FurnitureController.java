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
import javafx.scene.text.Text;
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

  @FXML Text recipientNameText;
  @FXML Text roomText;
  @FXML Text furnitureTypeText;
  @FXML Text deliveryDateText;
  @FXML Text notesText;
  @FXML Text deliveryTimeText;
  @FXML Text staffText;

  String language = "english";
  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

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
    assignedStaff.setItems(
        FXCollections.observableList(
            SQLRepo.INSTANCE.getEmployeeList().stream()
                .filter(employee -> employee.getPermission().equals("STAFF"))
                .map(employee -> employee.getUsername())
                .toList()));

    roomName.setItems(names);
    // Add the items to the combo boxes
    furnitureType.setItems(typeOfFurniture);
    deliveryTime.setItems(deliveryTimes);
    // Initialize the buttons

    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());

    // Page Language Translation Code
    if (language.equals("english")) {
      translateToEnglish();
    } else if (language.equals("spanish")) {
      translateToSpanish();
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
    }

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

  public void translateToSpanish() {
    // Input Fields
    recipientNameText.setText("Nombre de Destinatario"); // Recipient Name
    roomText.setText("Cuarto"); // Room
    furnitureTypeText.setText("Tipo de Mueble"); // Furniture Type
    deliveryDateText.setText("Fecha de Entrega"); // Delivery Date
    deliveryTimeText.setText("Tiempo de Entrega"); // Delivery Time
    notesText.setText("Notas"); // Notes
    staffText.setText("Empleado"); // Staff

    // Buttons
    cancelButton.setText("Cancelar"); // Cancel
    resetButton.setText("Poner a Cero"); // Reset
    submitButton.setText("Presentar"); // Submit
  }

  public void translateToEnglish() {
    recipientNameText.setText("Recipient Name"); // Keep in English
    roomText.setText("Room"); // Keep in English
    furnitureTypeText.setText("Furniture Type"); // Keep in English
    deliveryDateText.setText("Delivery Date"); // Keep in English
    deliveryTimeText.setText("Delivery Time"); // Keep in English
    notesText.setText("Notes"); // Keep in English
    staffText.setText("Staff"); // Keep in English

    // Buttons
    cancelButton.setText("Cancel"); // Keep in English
    resetButton.setText("Reset"); // Keep in English
    submitButton.setText("Submit"); // Keep in English
  }
}
