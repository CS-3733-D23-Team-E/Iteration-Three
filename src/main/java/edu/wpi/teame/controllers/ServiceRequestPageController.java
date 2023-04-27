package edu.wpi.teame.controllers;

import static edu.wpi.teame.entities.ServiceRequestData.Status.*;
import static edu.wpi.teame.entities.ServiceRequestData.Status.DONE;
import static edu.wpi.teame.entities.ServiceRequestData.Status.IN_PROGRESS;
import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.Employee;
import edu.wpi.teame.entities.ServiceRequestData;
import edu.wpi.teame.entities.Settings;
import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

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
  @FXML MFXButton spanishButton;
  @FXML MFXButton englishButton;

  @FXML Text inProgressRequestTitleText;
  @FXML Text pendingRequestsTitleText;
  @FXML Text completedRequestTitleText;
  @FXML Label nonCompletedTitleText;
  @FXML Tab flowerRequestTab;
  @FXML Tab mealRequestTab;
  @FXML Tab officeSuppliesTab;
  @FXML Tab conferenceRoomTab;
  @FXML Tab furnitureDeliveryTab;

  @FXML ListView<String> outgoingRequestsList;

  @FXML VBox logoutBox;
  @FXML MFXButton logoutButton;

  boolean menuVisibilty = false;
  boolean logoutVisible = false;

  String language = "english";
  String nyay = "\u00F1"; // ñ
  String aA = "\u0301"; // á
  String aE = "\u00E9"; // é
  String aI = "\u00ED"; // í
  String aO = "\u00F3"; // ó
  String aU = "\u00FA"; // ù
  String aQuestion = "\u00BF"; // Upside down question mark

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
    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_EDITOR_PAGE));
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

    Timeline timeline =
        new Timeline(
            new KeyFrame(
                Duration.seconds(1),
                event -> {
                  fillServiceRequestsFields();
                  if (Settings.INSTANCE.getLanguage() == Settings.Language.ENGLISH) {
                    translateToEnglish();
                  } else if (Settings.INSTANCE.getLanguage() == Settings.Language.SPANISH) {
                    translateToSpanish();
                  }
                }));

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();

    fillServiceRequestsFields();

    if (language.equals("english")) {
      translateToEnglish();
    } else if (language.equals("spanish")) {
      translateToSpanish();
    } else // throw error for language not being a valid language
    {
      // throw some sort of error here at some point
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
      nonCompletedTitleText.setText("Your Non-completed requests:");
    } else {
      nonCompletedTitleText.setText("All Non-completed requests:");
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

  public void translateToSpanish() {
    // Change language variable
    language = "spanish";

    // Menu Bar
    menuBarHome.setText("Principal"); // Home
    menuBarServices.setText("Servicios"); // Services
    menuBarSignage.setText("Se" + nyay + "alizaci" + aO + "n"); // Signage
    menuBarMaps.setText("Navegaci" + aO + "n"); // Pathfinding
    menuBarDatabase.setText("Base de Datos"); // Database
    menuBarExit.setText(("Salida")); // Exit

    // Logout Button
    logoutButton.setText("Cerrar Sesi" + aO + "n"); // Logout
    Font spanishLogout = new Font("Roboto", 13);
    logoutButton.setFont(spanishLogout);

    // Request Status Bar
    pendingRequestsTitleText.setText("Pendiente"); // Pending:
    inProgressRequestTitleText.setText("En Curso"); // In Progress:
    completedRequestTitleText.setText("Completo"); // Completed:
    nonCompletedTitleText.setText("Solicitudes No Completadas"); // Non-Completed Requests

    // Service Request Tabs
    flowerRequestTab.setText("Solicitud de Flores"); // Flower Request
    mealRequestTab.setText("Solicitud de Comida"); // Meal Request
    officeSuppliesTab.setText("Suministros de Oficina"); // Office Supplies
    conferenceRoomTab.setText("Sala de Conferencias"); // Conference Room
    furnitureDeliveryTab.setText("Entrega de Muebles"); // Furniture Delivery
  }

  public void translateToEnglish() {
    // Change language variable
    language = "english";

    // Menu Bar
    menuBarHome.setText("Home"); // Keep in English
    menuBarServices.setText("Services"); // Keep in English
    menuBarSignage.setText("Signage"); // Keep in English
    menuBarMaps.setText("Pathfinding"); // Keep in English
    menuBarDatabase.setText("Database"); // Keep in English
    menuBarExit.setText(("Exit")); // Keep in English

    // Logout Button
    logoutButton.setText("Logout"); // Keep in English
    Font englishLogout = new Font("Roboto", 18);
    logoutButton.setFont(englishLogout);

    // Request Status Bar
    inProgressRequestTitleText.setText("In Progress"); // Keep in English
    pendingRequestsTitleText.setText("Pending:"); // Keep in English
    completedRequestTitleText.setText("Completed:"); // Keep in English
    nonCompletedTitleText.setText("Non-completed Requests"); // Keep in English

    // Service Request Tabs
    flowerRequestTab.setText("Flower Request"); // Flower Request
    mealRequestTab.setText("Meal Request"); // Meal Request
    officeSuppliesTab.setText("Office Supplies"); // Office Supplies
    conferenceRoomTab.setText("Conference Room"); // Conference Room
    furnitureDeliveryTab.setText("Furniture Delivery"); // Furniture Delivery
  }
}
