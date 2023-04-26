package edu.wpi.teame.controllers;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.AlertData;
import edu.wpi.teame.entities.LoginData;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXListView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HomePageController {
  @FXML MFXButton serviceRequestButton;
  @FXML MFXButton editSignageButton;
  @FXML MFXButton databaseButton;
  @FXML MFXButton pathfindingButton;
  @FXML MFXButton loginButton;
  @FXML TextField username;
  @FXML TextField password;
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;

  @FXML MFXButton menuBarAbout;
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML Text dateText;
  @FXML Text timeText;
  @FXML VBox menuBar;
  @FXML MFXButton alertSubmitButton;
  @FXML Text announcementText;
  @FXML MFXTextField alertTextBox;
  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;
  @FXML MFXButton userButton;
  @FXML ImageView homeI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;

  @FXML ImageView aboutI;

  @FXML ImageView exitI;

  @FXML MFXListView<String> alertList;

  List<AlertData> alerts;

  boolean loggedIn;

  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  public void initialize() {
    LocalTime currentTime = LocalTime.now();
    LocalDate currentDate = LocalDate.now();

    // Format current date as a string
    DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    String currentDateString = currentDate.format(format);
    // Format the current time as a string
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    String currentTimeString = currentTime.format(formatter);

    // Print the current time as a string
    timeText.setText(currentTimeString);
    dateText.setText(currentDateString);

    serviceRequestButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));

    editSignageButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    databaseButton.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_EDITOR));
    pathfindingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));

    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));

    menuBarAbout.setOnMouseClicked(event -> Navigation.navigate((Screen.ABOUT)));

    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_EDITOR)));

    menuBarExit.setOnMouseClicked(event -> Platform.exit());

    loggedIn = false;
    logoutButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SIGNAGE_TEXT);
          SQLRepo.INSTANCE.exitDatabaseProgram();
        });

    alertSubmitButton.setOnMouseClicked(event -> setAlert());

    // Initially set the menu bar to invisible
    menuBarVisible(false);
    logoutPopup(false);

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
        menuBarAbout, "baseline-left", aboutI, "images/abouticon.png", "images/abouticon-blue.png");
    ButtonUtilities.mouseSetupMenuBar(
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

    // makes the buttons highlight when they are hovered over
    ButtonUtilities.mouseSetup(serviceRequestButton);
    ButtonUtilities.mouseSetup(editSignageButton);
    ButtonUtilities.mouseSetup(pathfindingButton);
    ButtonUtilities.mouseSetup(databaseButton);
    ButtonUtilities.mouseSetup(logoutButton);

    fillAlertList();
  }

  public void attemptLogin() {
    // Get the input login info
    LoginData login = new LoginData(username.getText(), password.getText());

    // If the login was successful
    if (login.attemptLogin()) {
      // Hide text fields and button
      password.setVisible(false);
      username.setVisible(false);
      loginButton.setVisible(false);
      // Set loggedIn as true
      loggedIn = true;

    } else {
      // Clear the fields
      password.clear();
      username.clear();
    }
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
    menuBarAbout.setVisible(bool);
    menuBarExit.setVisible(bool);
    menuBarBlank.setVisible(bool);
    menuBar.setVisible(bool);
  }

  public AlertData setAlert() {
    System.out.println("alert sent");

    AlertData alertData = new AlertData(alerts.get(0).getAlertID() + 1, alertTextBox.getText());

    SQLRepo.INSTANCE.addAlert(alertData);

    return alertData;
  }

  private void fillAlertList() {
    alerts =
        new java.util.ArrayList<>(
            SQLRepo.INSTANCE.getAlertList().stream()
                .sorted(
                    new Comparator<AlertData>() {
                      @Override
                      public int compare(AlertData o1, AlertData o2) {
                        return o2.getTime().compareTo(o1.getTime());
                      }
                    })
                .toList());

    List<String> alertTexts =
        alerts.stream()
            .map(alert -> ("\tDate: " + alert.getTimestamp() + "\t\t\t" + alert.getMessage()))
            .toList();
    alertList.setItems(FXCollections.observableList(alertTexts));
  }
}
