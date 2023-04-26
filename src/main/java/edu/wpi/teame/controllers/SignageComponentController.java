package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.SignageComponentData;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.sql.SQLException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class SignageComponentController {

  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;
  @FXML MFXButton userButton;
  @FXML ImageView homeI;
  @FXML VBox menuBar;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView exitI;
  @FXML MFXButton submitButton;
  @FXML MFXButton cancelButton;
  @FXML MFXButton resetButton;
  @FXML MFXDatePicker date;
  @FXML SearchableComboBox<String> kioskLocations;
  @FXML SearchableComboBox<String> locations;
  @FXML SearchableComboBox<String> directions;
  @FXML MFXButton close;
  @FXML VBox formSubmitted;

  ObservableList<String> kioskDiffLocations =
      FXCollections.observableArrayList(
          "Screen 1, By the info desk", "Screen 2, By the Q Elevator");

  ObservableList<String> allLocationsScreen1 =
      FXCollections.observableArrayList(
          "Information",
          "Shapiro Procedural Check-in",
          "Shapiro Admitting",
          "Watkins Clinic C (up to 3rd floor)",
          "Rehabilitation Services (down to 1st floor)",
          "Watkins Clinics A & B (this floor)");

  ObservableList<String> allLocationsScreen2 =
      FXCollections.observableArrayList(
          "Watkins Clinics A & B (this floor)",
          "L2PRU (down to Lower Level L2)",
          "Brigham Circle Medical Associates (up to 3rd floor)",
          "Watkins Clinic C (EP & Echo) (up to 3rd fl)");

  ObservableList<String> signageDirections =
      FXCollections.observableArrayList(
          SignageComponentData.ArrowDirections.getAllDirections().stream()
              .map(dir -> SignageComponentData.ArrowDirections.directionToString(dir))
              .toList());

  Boolean loggedIn;

  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  @FXML
  public void initialize() {
    menuBar.setVisible(false);
    logoutPopup(false);
    formSubmitted.setVisible(false);
    cancelButton.setOnMouseClicked(event -> cancelRequest());
    resetButton.setOnMouseClicked(event -> clearForm());
    close.setOnMouseClicked(
        event -> {
          clearForm();
          Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE);
          formSubmitted.setVisible(false);
        });

    kioskLocations.setItems(kioskDiffLocations);

    kioskLocations.setOnAction(
        event -> {
          if (kioskLocations.getValue() != null) {
            if (kioskLocations.getValue().equals("Screen 1, By the info desk")) {
              locations.setItems(allLocationsScreen1);
            } else {
              locations.setItems(allLocationsScreen2);
            }
          }
        });

    directions.setItems(signageDirections);

    submitButton.setOnMouseClicked(
        event -> {
          try {
            submitForm();
          } catch (SQLException e) {
            throw new RuntimeException(e);
          }
          clearForm();
          formSubmitted.setVisible(true);
        });

    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_EDITOR)));
    menuBarExit.setOnMouseClicked(event -> Platform.exit());

    loggedIn = false;
    logoutButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SIGNAGE_TEXT);
          SQLRepo.INSTANCE.exitDatabaseProgram();
        });

    // When the menu button is clicked, invert the value of menuVisibility and set the menu bar to
    // that value
    // (so each time the menu button is clicked it changes the visibility of menu bar back and
    // forth)
    menuButton.setOnMouseClicked(
        event -> {
          menuVisibilty = !menuVisibilty;
          menuBarVisible(menuVisibilty);
        });

    // Navigation controls for the button in the menu bar
    menuBarHome.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.HOME);
          menuVisibilty = !menuVisibilty;
        });

    userButton.setOnMouseClicked(
        event -> {
          logoutVisible = !logoutVisible;
          logoutPopup(logoutVisible);
        });

    // makes the menu bar buttons get highlighted when the mouse hovers over them
    ButtonUtilities.mouseSetupMenuBar(
        menuBarHome,
        "baseline-left",
        homeI,
        "images/house-blank.png",
        "images/house-blank-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarServices,
        "baseline-left",
        servicesI,
        "images/hand-holding-medical.png",
        "images/hand-holding-medical-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarSignage,
        "baseline-left",
        signageI,
        "images/diamond-turn-right.png",
        "images/diamond-turn-right-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarMaps, "baseline-left", pathfindingI, "images/marker.png", "images/marker-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarDatabase,
        "baseline-left",
        databaseI,
        "images/folder-tree.png",
        "images/folder-tree-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    ButtonUtilities.mouseSetup(logoutButton);
  }

  public SignageComponentData submitForm() throws SQLException {
    System.out.println("send signage component change");

    SignageComponentData.ArrowDirections get =
        SQLRepo.INSTANCE.getDirectionFromPKeyL(
            date.getValue().toString(), kioskLocations.getValue(), locations.getValue());

    // Create the service request data
    SignageComponentData requestData =
        new SignageComponentData(
            date.getValue().toString(), kioskLocations.getValue(), locations.getValue(), get);

    SQLRepo.INSTANCE.updateSignage(requestData, "arrowDirection", directions.getValue());

    return requestData;
  }

  public void clearForm() {
    date.setValue(null);
    kioskLocations.setValue(null);
    locations.setValue(null);
    directions.setValue(null);
  }

  public void cancelRequest() {
    Navigation.navigate(Screen.HOME);
  }

  public void logoutPopup(boolean bool) {
    logoutBox.setVisible(bool);
  }

  public void menuBarVisible(boolean bool) {
    menuBarHome.setVisible(bool);
    menuBarServices.setVisible(bool);
    menuBarSignage.setVisible(bool);
    menuBarMaps.setVisible(bool);
    menuBarDatabase.setVisible(bool);
    menuBarExit.setVisible(bool);
    menuBarBlank.setVisible(bool);
    menuBar.setVisible(bool);
  }
}
