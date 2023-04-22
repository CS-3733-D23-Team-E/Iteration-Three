package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.LoginData;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML Text dateText;
  @FXML Text timeText;
  @FXML VBox menuBar;
  @FXML MFXButton announcementButton;
  @FXML Text announcementText;
  @FXML MFXTextField announcementTextBox;
  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;
  @FXML MFXButton userButton;

  Boolean loggedIn;

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
    databaseButton.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_TABLEVIEW));
    pathfindingButton.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));

    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate((Screen.DATABASE_TABLEVIEW)));
    menuBarExit.setOnMouseClicked(event -> Platform.exit());

    loggedIn = false;
    logoutButton.setOnMouseClicked(event -> attemptLogin());

    announcementButton.setOnMouseClicked(
        event -> {
          String announcement = announcementTextBox.getText();
          announcementText.setText(announcement);
        });

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

    logoutButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarServices.setOnMouseClicked(event -> Navigation.navigate(Screen.SERVICE_REQUESTS));
    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_TABLEVIEW));
    menuBarExit.setOnMouseClicked((event -> Platform.exit()));

    // makes the menu bar buttons get highlighted when the mouse hovers over them
    ButtonUtilities.mouseSetupMenuBar(menuBarHome, "baseline-left");
    ButtonUtilities.mouseSetupMenuBar(menuBarServices, "baseline-left");
    ButtonUtilities.mouseSetupMenuBar(menuBarSignage, "baseline-left");
    ButtonUtilities.mouseSetupMenuBar(menuBarMaps, "baseline-left");
    ButtonUtilities.mouseSetupMenuBar(menuBarDatabase, "baseline-left");
    ButtonUtilities.mouseSetupMenuBar(menuBarExit, "baseline-center");

    // makes the buttons highlight when they are hovered over
    mouseSetup(serviceRequestButton);
    mouseSetup(editSignageButton);
    mouseSetup(pathfindingButton);
    mouseSetup(databaseButton);
    mouseSetup(logoutButton);
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

  private void mouseSetup(MFXButton btn) {
    btn.setOnMouseEntered(
        event -> {
          btn.setStyle(
              "-fx-background-color: #f1f1f1; -fx-alignment: top-left; -fx-border-color:  #001A3C; -fx-border-width: 3;");
          btn.setTextFill(Color.web("#192d5aff", 1.0));
        });
    btn.setOnMouseExited(
        event -> {
          btn.setStyle("-fx-background-color:#001A3C; -fx-alignment: top-left;");
          btn.setTextFill(Color.web("#f1f1f1", 1.0));
        });
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

  public static void mouseSetupMenuBar(
          MFXButton btn,
          String alignment,
          ImageView pic,
          String unhighlightedPic,
          String highlightedPic) {
    Image uPic = new Image(Main.class.getResource(unhighlightedPic).toString());
    Image hPic = new Image(Main.class.getResource(highlightedPic).toString());
    btn.setOnMouseEntered(
            event -> {
              btn.setStyle(
                      "-fx-background-color: #f1f1f1; -fx-alignment: "
                              + alignment
                              + "; -fx-border-color: #001A3C; -fx-border-width: 0; -fx-font-size: 18;");
              btn.setTextFill(Color.web("#192d5aff", 1.0));
              pic.setImage(hPic);
            });
    btn.setOnMouseExited(
            event -> {
              btn.setStyle(
                      "-fx-background-color: #001A3C; -fx-alignment: " + alignment + ";-fx-font-size: 18;");
              btn.setTextFill(WHITE);
              pic.setImage(uPic);
            });
  }

}
