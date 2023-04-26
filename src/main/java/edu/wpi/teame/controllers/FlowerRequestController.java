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
import javafx.scene.text.Text;
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

  @FXML Text recipientNameText;
  @FXML Text flowerChoiceText;
  @FXML Text roomText;
  @FXML Text numberOfFlowersText;
  @FXML Text deliveryDateText;
  @FXML Text includeACardText;
  @FXML Text deliveryTimeText;
  @FXML Text cardMessageText;
  @FXML Text staffText;
  @FXML Text notesText;

  String language = "english";
  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark
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

  public void translateToSpanish() {
    // Input Fields
    recipientNameText.setText("Nombre de Destinatario"); // Recipient Name
    flowerChoiceText.setText("Elecci" + aO + "n de Flores"); // Flower Choice
    roomText.setText("Cuarto"); // Room
    numberOfFlowersText.setText("N" + aU + "mero de Flores"); // Number of Flowers
    deliveryDateText.setText("Fecha de Entrega"); // Delivery Date
    includeACardText.setText(aQuestion + "Incluir una Tarjeta?"); // Include a Card?
    deliveryTimeText.setText("Tiempo de Entrega"); // Delivery Time
    cardMessageText.setText("Mensaje de Tarjeta"); // Card Mesage
    staffText.setText("Empleado"); // Staff
    notesText.setText("Notas"); // Notes

    // Buttons
    cancelButton.setText("Cancelar"); // Cancel
    resetButton.setText("Poner a Cero"); // Reset
    submitButton.setText("Presentar"); // Submit
  }

  public void translateToEnglish() {
    recipientNameText.setText("Recipient Name"); // Keep in English
    flowerChoiceText.setText("Flower Choice"); // Keep in English
    roomText.setText("Room"); // Keep in English
    numberOfFlowersText.setText("Number of Flowers"); // Keep in English
    deliveryDateText.setText("Delivery Date"); // Keep in English
    includeACardText.setText("Include a Card?"); // Keep in English
    deliveryTimeText.setText("Delivery Time"); // Keep in English
    cardMessageText.setText("Card Message"); // Keep in English
    staffText.setText("Staff"); // Keep in English
    notesText.setText("Notes"); // Keep in English

    // Buttons
    cancelButton.setText("Cancel"); // Keep in English
    resetButton.setText("Reset"); // Keep in English
    submitButton.setText("Submit"); // Keep in English
  }

  // public List<Employee> getEmployeeList() {
  // return employeeList; }
}
