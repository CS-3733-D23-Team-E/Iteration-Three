package edu.wpi.teame.controllers;

import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class SignageController {
  @FXML MFXButton userButton;
  @FXML MFXButton loginButton;
  @FXML MFXTextField usernameField;
  @FXML MFXTextField passwordField;
  @FXML StackPane loginStack;
  @FXML VBox loginFailBox;

  @FXML MFXButton closeButton;

  // TODO: Make login work

  boolean loginVisible = false;

  public void initialize() {

    loginPopout(false);
    loginFailBox.setVisible(false);

    userButton.setOnMouseClicked(
        event -> {
          loginVisible = !loginVisible;
          loginPopout(loginVisible);
        });

    loginButton.setOnMouseClicked(
        event -> {
          attemptLogin();
        });
  }

  private void attemptLogin() {
    Employee staffMember =
        SQLRepo.INSTANCE.connectToDatabase(usernameField.getText(), passwordField.getText());
    if (staffMember == null) {
      loginFailBox.setVisible(true);
      closeButton.setOnMouseClicked(
          event -> {
            loginFailBox.setVisible(false);
          }); // popup to
      // display incorrect login message
      return;
    }
    //     Employee.currentEmployee = staffMember;

    // Successful login
    Navigation.navigate(Screen.HOME);
  }

  public void loginPopout(boolean bool) {
    loginStack.setVisible(bool);
  }

  private void mouseSetup(MFXButton btn) {
    btn.setOnMouseEntered(
        event -> {
          btn.setStyle(
              "-fx-background-color: #ffffff; -fx-alignment: center; -fx-border-color: #192d5a; -fx-border-width: 2;");
          btn.setTextFill(Color.web("#192d5aff", 1.0));
        });
    btn.setOnMouseExited(
        event -> {
          btn.setStyle("-fx-background-color: #192d5aff; -fx-alignment: center;");
          btn.setTextFill(WHITE);
        });
  }
}
