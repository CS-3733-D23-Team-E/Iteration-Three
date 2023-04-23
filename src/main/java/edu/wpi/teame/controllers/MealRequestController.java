package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.MealRequestData;
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
    mainCourse.setItems(mainCourses);
    sideCourse.setItems(sideCourses);
    drinkChoice.setItems(drinks);
    deliveryTime.setItems(deliveryTimes);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    submitButton.setOnMouseClicked(event -> sendRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
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
    Navigation.navigate(Screen.HOME);

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
}
