package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.OfficeSuppliesData;
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
  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;

  @FXML Text recipientNameText;
  @FXML Text roomText;
  @FXML Text officeSupplyTypeText;
  @FXML Text deliveryDateText;
  @FXML Text numberOfSuppliesText;
  @FXML Text deliveryTimeText;
  @FXML Text notesText;
  @FXML Text staffText;

  String language = "english";
  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");
  ObservableList<String> officeSupplies =
      FXCollections.observableArrayList(
          "pencils", "pens", "white-out", "tape", "ruler", "hole puncher", "sharpener", "charger");

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

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
    deliveryTime.setItems(deliveryTimes);
    supplyType.setItems(officeSupplies);

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

    SQLRepo.INSTANCE.addServiceRequest(requestData);
    return requestData;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  public void translateToSpanish() {
    // Input Fields
    recipientNameText.setText("Nombre de Destinatario"); // Recipient Name
    roomText.setText("Cuarto"); // Room
    officeSupplyTypeText.setText("Tipo de Suministros de Oficina"); // Office Supply Type
    deliveryDateText.setText("Fecha de Entrega"); // Delivery Date
    numberOfSuppliesText.setText("N" + aU + "mero de Suministros"); // Number of Supplies
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
    officeSupplyTypeText.setText("Office Supply Type"); // Keep in English
    deliveryDateText.setText("Delivery Date"); // Keep in English
    numberOfSuppliesText.setText("Number of Supplies"); // Keep in English
    deliveryTimeText.setText("Delivery Time"); // Keep in English
    notesText.setText("Notes"); // Keep in English
    staffText.setText("Staff"); // Keep in English

    // Buttons
    cancelButton.setText("Cancel"); // Keep in English
    resetButton.setText("Reset"); // Keep in English
    submitButton.setText("Submit"); // Keep in English
  }
}
