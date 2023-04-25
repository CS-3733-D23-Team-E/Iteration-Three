package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.ServiceRequestData.Status.*;
import static edu.wpi.teame.entities.ServiceRequestData.Status.DONE;
import static edu.wpi.teame.entities.ServiceRequestData.Status.IN_PROGRESS;
import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.ServiceRequestData;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ServiceRequestPageController {

  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;
  @FXML MFXButton menuBarAbout;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;

  @FXML MFXButton menuBarHelp;
  @FXML MFXButton menuBarSettings;
  @FXML MFXButton userButton;
  @FXML VBox menuBar;
  @FXML ImageView homeI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;

  @FXML ImageView aboutI;

  @FXML ImageView exitI;

  @FXML Label pendingRequestText;

  @FXML Label inProgressRequestText;

  @FXML Label completedRequestText;

  @FXML Label nonCompletedText;

  @FXML ListView<String> outgoingRequestsList;

  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;

  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  @FXML
  public void initialize() {

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

    userButton.setOnMouseClicked(
        event -> {
          logoutVisible = !logoutVisible;
          logoutPopup(logoutVisible);
        });

    // Navigation controls for the button in the menu bar
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarServices.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SERVICE_REQUESTS);
          menuVisibilty = !menuVisibilty;
        });
    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_EDITOR));

    menuBarAbout.setOnMouseClicked(event -> Navigation.navigate(Screen.ABOUT));

    menuBarExit.setOnMouseClicked((event -> Platform.exit()));
    logoutButton.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));

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

    mouseSetup(logoutButton);

    fillServiceRequestsFields();
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
    menuBarBlank.setVisible(bool);
    menuBarExit.setVisible(bool);
    menuBar.setVisible(bool);
    menuBarSettings.setVisible(bool);
    menuBarHelp.setVisible(bool);
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

  private void fillServiceRequestsFields() {
    List<ServiceRequestData> requests =
        new java.util.ArrayList<>(
            SQLRepo.INSTANCE.getFlowerRequestsList().stream()
                .map(request -> (ServiceRequestData) request)
                .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getFurnitureRequestsList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getMealRequestsList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getOfficeSupplyList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());
    requests.addAll(
        SQLRepo.INSTANCE.getConfList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());

    requests.addAll(
        SQLRepo.INSTANCE.getMedicalSuppliesList().stream()
            .map(request -> (ServiceRequestData) request)
            .toList());

    List<ServiceRequestData> pendingRequests =
        requests.stream()
            .filter(request -> request.getRequestStatus().equals(ServiceRequestData.Status.PENDING))
            .toList();

    List<ServiceRequestData> inProgressRequests =
        requests.stream()
            .filter(request -> request.getRequestStatus().equals(IN_PROGRESS))
            .toList();
    List<ServiceRequestData> completedRequests =
        requests.stream().filter(request -> request.getRequestStatus().equals(DONE)).toList();

    inProgressRequestText.setText(inProgressRequests.size() + "");
    pendingRequestText.setText(pendingRequests.size() + "");
    completedRequestText.setText(completedRequests.size() + "");

    if (Employee.activeEmployee.getPermission().equals("STAFF")) {
      // filter by employee
      requests =
          requests.stream()
              .filter(
                  request ->
                      request
                          .getAssignedStaff()
                          .equalsIgnoreCase(Employee.activeEmployee.getUsername()))
              .toList();
      nonCompletedText.setText("Your Non-completed requests:");
    } else {
      nonCompletedText.setText("All Non-completed requests:");
    }
    List<ServiceRequestData> nonCompleteRequests =
        requests.stream().filter(request -> !request.getRequestStatus().equals(DONE)).toList();

    List<String> requestTexts =
        nonCompleteRequests.stream()
            .map(
                request ->
                    (request.getRequestType()
                        + " request, ID "
                        + request.getRequestID()
                        + ": "
                        + request.getRequestStatus()))
            .toList();
    outgoingRequestsList.setItems(FXCollections.observableList(requestTexts));
  }
}
