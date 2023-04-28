package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.Main;
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
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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
  @FXML private ImageView item1Img;
  @FXML private ImageView item2Img;
  @FXML private ImageView item3Img;
  @FXML private ImageView item4Img;
  @FXML private ImageView item5Img;
  @FXML private ImageView item6Img;

  ObservableList<String> staffMembers = FXCollections.observableArrayList();

  ObservableList<String> hoursList =
      FXCollections.observableArrayList(
          "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12");
  ObservableList<String> minutesList = FXCollections.observableArrayList("00", "15", "30", "45");
  ObservableList<String> ampmList = FXCollections.observableArrayList("A.M.", "P.M.");
  int item1 = 0;
  int item2 = 0;

  int item3 = 0;

  int item4 = 0;
  int item5 = 0;

  int item6 = 0;

  public void initializeButtons() {

    // Item 1
    this.item1Quantity.setText(Integer.toString(item1));
    item1Quantity.setFloatingText("Quantity");
    item1Quantity.setAlignment(Pos.CENTER);
    item1Quantity.setStyle(
        "-fx-border-radius: 0;-fx-background-radius: 0;-fx-border-color: BLACK; -fx-border-width: 3 0 3 0");
    item1Quantity.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
    item1Plus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item1Plus.setStyle(
        "-fx-background-radius:  0 20 20 0; -fx-background-color: #012D5A; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 0 20 20 0");
    item1Minus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item1Minus.setStyle(
        "-fx-background-radius: 20 0 0 20; -fx-background-color: #AAAAAA; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 20 0 0 20");

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

    // Item 2
    this.item2Quantity.setText(Integer.toString(item2));
    item2Quantity.setFloatingText("Quantity");
    item2Quantity.setAlignment(Pos.CENTER);
    item2Quantity.setStyle(
        "-fx-border-radius: 0;-fx-background-radius: 0;-fx-border-color: BLACK; -fx-border-width: 3 0 3 0");
    item2Quantity.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
    item2Plus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item2Plus.setStyle(
        "-fx-background-radius:  0 20 20 0; -fx-background-color: #012D5A; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 0 20 20 0");
    item2Minus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item2Minus.setStyle(
        "-fx-background-radius: 20 0 0 20; -fx-background-color: #AAAAAA; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 20 0 0 20");

    this.item2Minus.setOnAction(
        event -> {
          item2--;
          if (item2 < 0) {
            item2 = 0;
          }

          this.item2Quantity.setText(Integer.toString(item2));
        });
    this.item2Plus.setOnAction(
        event -> {
          item2++;
          this.item2Quantity.setText(Integer.toString(item2));
        });

    // Item 3
    this.item3Quantity.setText(Integer.toString(item3));

    item3Quantity.setFloatingText("Quantity");
    item3Quantity.setAlignment(Pos.CENTER);
    item3Quantity.setStyle(
        "-fx-border-radius: 0;-fx-background-radius: 0;-fx-border-color: BLACK; -fx-border-width: 3 0 3 0");
    item3Quantity.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
    item3Plus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item3Plus.setStyle(
        "-fx-background-radius:  0 20 20 0; -fx-background-color: #012D5A; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 0 20 20 0");
    item3Minus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item3Minus.setStyle(
        "-fx-background-radius: 20 0 0 20; -fx-background-color: #AAAAAA; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 20 0 0 20");

    this.item3Minus.setOnAction(
        event -> {
          item3--;
          if (item3 < 0) {
            item3 = 0;
          }

          this.item3Quantity.setText(Integer.toString(item3));
        });
    this.item3Plus.setOnAction(
        event -> {
          item3++;
          this.item3Quantity.setText(Integer.toString(item3));
        });
    // Item 4
    this.item4Quantity.setText(Integer.toString(item4));

    item4Quantity.setFloatingText("Quantity");
    item4Quantity.setAlignment(Pos.CENTER);
    item4Quantity.setStyle(
        "-fx-border-radius: 0;-fx-background-radius: 0;-fx-border-color: BLACK; -fx-border-width: 3 0 3 0");
    item4Quantity.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
    item4Plus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item4Plus.setStyle(
        "-fx-background-radius:  0 20 20 0; -fx-background-color: #012D5A; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 0 20 20 0");
    item4Minus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item4Minus.setStyle(
        "-fx-background-radius: 20 0 0 20; -fx-background-color: #AAAAAA; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 20 0 0 20");

    this.item4Minus.setOnAction(
        event -> {
          item4--;
          if (item4 < 0) {
            item4 = 0;
          }

          this.item4Quantity.setText(Integer.toString(item4));
        });
    this.item4Plus.setOnAction(
        event -> {
          item4++;
          this.item4Quantity.setText(Integer.toString(item4));
        });
    // Item 5
    this.item5Quantity.setText(Integer.toString(item5));

    item5Quantity.setFloatingText("Quantity");
    item5Quantity.setAlignment(Pos.CENTER);
    item5Quantity.setStyle(
        "-fx-border-radius: 0;-fx-background-radius: 0;-fx-border-color: BLACK; -fx-border-width: 3 0 3 0");
    item5Quantity.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
    item5Plus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item5Plus.setStyle(
        "-fx-background-radius:  0 20 20 0; -fx-background-color: #012D5A; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 0 20 20 0");
    item5Minus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item5Minus.setStyle(
        "-fx-background-radius: 20 0 0 20; -fx-background-color: #AAAAAA; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 20 0 0 20");

    this.item5Minus.setOnAction(
        event -> {
          item5--;
          if (item5 < 0) {
            item5 = 0;
          }

          this.item5Quantity.setText(Integer.toString(item5));
        });
    this.item5Plus.setOnAction(
        event -> {
          item5++;
          this.item5Quantity.setText(Integer.toString(item5));
        });
    // Item 6
    this.item6Quantity.setText(Integer.toString(item6));

    item6Quantity.setFloatingText("Quantity");
    item6Quantity.setAlignment(Pos.CENTER);
    item6Quantity.setStyle(
        "-fx-border-radius: 0;-fx-background-radius: 0;-fx-border-color: BLACK; -fx-border-width: 3 0 3 0");
    item6Quantity.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
    item6Plus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item6Plus.setStyle(
        "-fx-background-radius:  0 20 20 0; -fx-background-color: #012D5A; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 0 20 20 0");
    item6Minus.setFont(Font.font("Ariel", FontWeight.BOLD, 28));
    item6Minus.setStyle(
        "-fx-background-radius: 20 0 0 20; -fx-background-color: #AAAAAA; -fx-text-fill: WHITE; -fx-border-color:BLACK; -fx-border-width: 3; -fx-border-radius: 20 0 0 20");

    this.item6Minus.setOnAction(
        event -> {
          item6--;
          if (item6 < 0) {
            item6 = 0;
          }

          this.item6Quantity.setText(Integer.toString(item6));
        });
    this.item6Plus.setOnAction(
        event -> {
          item6++;
          this.item6Quantity.setText(Integer.toString(item6));
        });
  }

  public void initializeImages() {
  //  Image image1 =
   //     new Image(String.valueOf(Main.class.getResource("images/medicalSupply/bandaid.png")));
   // item1Img.setImage(image1);

    //    File file2 = new
    // File("src/main/resources/edu/wpi/teame/images/medicalSupply/covidTest.png");
    //    Image image2 = new Image(file2.toURI().toString());
    //    item2Img.setImage(image2);
    //
    //    File file3 = new File("src/main/resources/edu/wpi/teame/images/medicalSupply/epipen.png");
    //    Image image3 = new Image(file3.toURI().toString());
    //    item3Img.setImage(image3);
    //
    //    File file4 = new File("src/main/resources/edu/wpi/teame/images/medicalSupply/gloves.png");
    //    Image image4 = new Image(file4.toURI().toString());
    //    item4Img.setImage(image4);
    //
    //    File file5 = new
    // File("src/main/resources/edu/wpi/teame/images/medicalSupply/sthetiscope.png");
    //    Image image5 = new Image(file5.toURI().toString());
    //    item5Img.setImage(image5);
    //
    //    File file6 = new
    // File("src/main/resources/edu/wpi/teame/images/medicalSupply/syringe.png");
    //    Image image6 = new Image(file6.toURI().toString());
    //    item6Img.setImage(image6);
  }

  public void initialize() {
    initializeButtons();
    initializeImages();

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
    submit.setOnMouseClicked(event -> sendRequest());
    clear.setOnMouseClicked(event -> clearForm());

    hours.setItems(hoursList);
    minutes.setItems(minutesList);
    ampm.setItems(ampmList);
  }

  public void sendRequest() {
    System.out.println(recipientsName.getText());
    System.out.println(locationName.getText());
    System.out.println(date.getUserData());
    System.out.println(hours.getText() + ":" + minutes.getText() + " " + ampm.getText());
    System.out.println(staffAssigned.getText());
    System.out.println(notes.getText());
    System.out.println("Item 1: " + item1Quantity.getText());
    System.out.println("Item 2: " + item2Quantity.getText());
    System.out.println("Item 3: " + item3Quantity.getText());
    System.out.println("Item 4: " + item4Quantity.getText());
    System.out.println("Item 5: " + item5Quantity.getText());
    System.out.println("Item 6: " + item6Quantity.getText());
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
    hours.setValue(null);
    minutes.setValue(null);
    ampm.setValue(null);
    item1Quantity.setText("0");
    item2Quantity.setText("0");
    item3Quantity.setText("0");
    item4Quantity.setText("0");
    item5Quantity.setText("0");
    item6Quantity.setText("0");
  }
}
