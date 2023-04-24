package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.*;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

public class MedicalSupplyRequestController {
  @FXML private MFXButton clear;
  @FXML private MFXButton cancel;
  @FXML private MFXButton submit;
  @FXML private MFXTextField recipientsName;
  @FXML private MFXFilterComboBox locationName;
  @FXML private MFXDatePicker date;
  @FXML private MFXComboBox hours;
  @FXML private MFXComboBox minutes;
  @FXML private MFXComboBox ampm;
  @FXML private MFXFilterComboBox staffAssigned;
  @FXML private MFXTextField notes;
  @FXML private MFXButton item1Minus;
  @FXML private MFXButton item1Plus;
  @FXML private MFXTextField item1Quantity;
  @FXML private MFXButton item2Minus;
  @FXML private MFXButton item2Plus;
  @FXML private MFXTextField item2Quantity;
  @FXML private MFXButton item3Minus;
  @FXML private MFXButton item3Plus;
  @FXML private MFXTextField item3Quantity;
  @FXML private MFXButton item4Minus;
  @FXML private MFXButton item4Plus;
  @FXML private MFXTextField item4Quantity;
  @FXML private MFXButton item5Minus;
  @FXML private MFXButton item5Plus;
  @FXML private MFXTextField item5Quantity;
  @FXML private MFXButton item6Minus;
  @FXML private MFXButton item6Plus;
  @FXML private MFXTextField item6Quantity;

  public void clearAll() {
    recipientsName.clear();
    locationName.clear();
    date.clear();
    hours.clear();
    minutes.clear();
    ampm.clear();
    staffAssigned.clear();
    notes.clear();
  }

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  ObservableList<String> hoursList =
      FXCollections.observableArrayList(
          "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
  ObservableList<String> minutesList = FXCollections.observableArrayList("00", "15", "30", "45");
  ObservableList<String> ampmList = FXCollections.observableArrayList("A.M.", "P.M.");
  int item1 = 0;

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

    List<Employee> employeeList = SQLRepo.INSTANCE.getEmployeeList();
    for (Employee emp : employeeList) {
      staffMembers.add(emp.getUsername());
    }
    staffAssigned.setItems(FXCollections.observableArrayList(staffMembers));
    locationName.setItems(names);

    cancel.setOnMouseClicked(event -> cancelRequest());
    //        submit.setOnMouseClicked(event -> sendRequest());
    clear.setOnMouseClicked(event -> clearForm());

    hours.setItems(hoursList);
    minutes.setItems(minutesList);
    ampm.setItems(ampmList);

    this.item1Quantity.setText(Integer.toString(item1));
    this.item1Minus.setOnAction(
        event -> {
          item1--;

          if (item1 < 0) {
            item1 = 0;
          }

          this.item1Quantity.setText(Integer.toString(item1));
        });
    this.item1Plus.setOnAction(
        event -> {
          item1++;
          this.item1Quantity.setText(Integer.toString(item1));
        });
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  public void clearForm() {
    recipientsName.clear();
    locationName.setValue(null);
    date.setValue(null);
    notes.clear();
    staffAssigned.setValue(null);
  }
}
