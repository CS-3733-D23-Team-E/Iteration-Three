package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.MealRequestData;
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
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class MealRequestController {
  @FXML MFXButton returnButtonMealRequest;
  @FXML MFXButton cancelButton;
  @FXML MFXButton submitButton;
  @FXML TextField notes;
  @FXML TextField recipientName;
  @FXML SearchableComboBox<String> roomName;
  @FXML SearchableComboBox<String> deliveryTime;
  @FXML DatePicker deliveryDate;
  @FXML SearchableComboBox<String> mainCourse;
  @FXML SearchableComboBox<String> sideCourse;
  @FXML SearchableComboBox<String> drinkChoice;

  @FXML TextField allergiesBox;
  @FXML SearchableComboBox<String> assignedStaff;
  @FXML MFXButton resetButton;
  @FXML MFXButton closeButton;
  @FXML VBox requestSubmittedBox;

  @FXML Text recipientNameText;
  @FXML Text mainCourseText;
  @FXML Text roomText;
  @FXML Text sideCourseText;
  @FXML Text deliveryDateText;
  @FXML Text drinkChoiceText;
  @FXML Text deliveryTimeText;
  @FXML Text allergiesText;
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

  ObservableList<String> deliveryTimes =
      FXCollections.observableArrayList(
          "10am - 11am", "11am - 12pm", "12pm - 1pm", "1pm - 2pm", "2pm - 3pm", "3pm - 4pm");

  ObservableList<String> mainCourses =
      FXCollections.observableArrayList(
          "Hamburger", "Cheeseburger", "Grilled Cheese", "Chicken Nuggets");
  ObservableList<String> sideCourses =
      FXCollections.observableArrayList("Fries", "Apple Slices", "Tater Tots", "Carrots");

  ObservableList<String> drinks =
      FXCollections.observableArrayList("Water", "Apple Juice", "Orange Juice", "Coffee", "Tea");

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
    mainCourse.setItems(mainCourses);
    sideCourse.setItems(sideCourses);
    drinkChoice.setItems(drinks);
    deliveryTime.setItems(deliveryTimes);
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

  public MealRequestData sendRequest() {

    // Create the service request data
    MealRequestData requestData =
        new MealRequestData(
            0,
            recipientName.getText(),
            roomName.getValue(),
            deliveryDate.getValue().toString(),
            deliveryTime.getValue(),
            assignedStaff.getValue(),
            mainCourse.getValue(),
            sideCourse.getValue(),
            drinkChoice.getValue(),
            allergiesBox.getText(),
            notes.getText(),
            MealRequestData.Status.PENDING);

    SQLRepo.INSTANCE.addServiceRequest(requestData);
    System.out.println("Meal Request Added");

    // Return to the home screen

    return requestData;
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  // Clears the current service request fields
  public void clearForm() {
    recipientName.clear();
    roomName.setValue(null);
    deliveryTime.setValue(null);
    mainCourse.setValue(null);
    sideCourse.setValue(null);
    drinkChoice.setValue(null);
    deliveryDate.setValue(null);
    notes.clear();
    assignedStaff.setValue(null);
  }

  public void translateToSpanish() {
    // Input Fields
    recipientNameText.setText("Nombre de Destinatario"); // Recipient Name
    mainCourseText.setText("Plato Fuerte"); // Main Course
    roomText.setText("Cuarto"); // Room
    sideCourseText.setText("Plato Lateral"); // Side Course
    deliveryDateText.setText("Fecha de Entrega"); // Delivery Date
    drinkChoiceText.setText("Opci" + aO + "n de la Bebida"); // Drink Choice
    deliveryTimeText.setText("Tiempo de Entrega"); // Delivery Time
    allergiesText.setText("Alergias"); // Allergies
    staffText.setText("Empleado"); // Staff
    notesText.setText("Notas"); // Notes

    // Buttons
    cancelButton.setText("Cancelar"); // Cancel
    resetButton.setText("Poner a Cero"); // Reset
    submitButton.setText("Presentar"); // Submit
  }

  public void translateToEnglish() {
    recipientNameText.setText("Recipient Name"); // Keep in English
    mainCourseText.setText("Main Course"); // Keep in English
    roomText.setText("Room"); // Keep in English
    sideCourseText.setText("Side Course"); // Keep in English
    deliveryDateText.setText("Delivery Date"); // Keep in English
    drinkChoiceText.setText("Drink Choice"); // Keep in English
    deliveryTimeText.setText("Delivery Time"); // Keep in English
    allergiesText.setText("Allergies"); // Keep in English
    staffText.setText("Staff"); // Keep in English
    notesText.setText("Notes"); // Keep in English

    // Buttons
    cancelButton.setText("Cancel"); // Keep in English
    resetButton.setText("Reset"); // Keep in English
    submitButton.setText("Submit"); // Keep in English
  }
}
