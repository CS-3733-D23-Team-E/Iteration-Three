package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.utilities.ButtonUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class DatabaseEditorController {

  @FXML DatabaseMapViewController mapViewController;
  @FXML DatabaseTableViewController tableViewController;
  @FXML DatabaseServiceRequestViewController serviceRequestViewController;
  @FXML MoveComponentController moveComponentController;

  @FXML AnchorPane tableView;
  @FXML AnchorPane moveView;
  @FXML AnchorPane serviceView;
  @FXML AnchorPane mapView;
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarMaps;

  @FXML MFXButton menuBarAbout;
  @FXML MFXButton menuBarDatabase;

  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML VBox menuBar;
  @FXML AnchorPane employeeView;

  @FXML MFXButton tableEditorSwapButton;
  @FXML MFXButton mapEditorSwapButton;
  @FXML MFXButton moveEditorSwapButton;
  @FXML MFXButton requestsEditorSwapButton;
  @FXML MFXButton employeeEditorSwapButton;
  @FXML VBox importExportZone;

  @FXML MFXButton importButton;
  @FXML MFXButton exportButton;

  @FXML MFXButton backButton;
  @FXML Label editorTitle;
  @FXML ImageView aboutI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView settingsI;
  @FXML ImageView exitI;
  @FXML ImageView homeI;

  Boolean menuVisibilty = false;

  @FXML
  public void initialize() {
    onlyVisible(tableView);
    importExportZone.setVisible(true);
    onlyDisable(tableEditorSwapButton);
    editorTitle.setText("Table editor");
    // Initially set the menu bar to invisible
    menuBarVisible(false);

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

    initButtons();
  }

  private void initButtons() {
    tableEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(tableView);
          importExportZone.setVisible(true);
          onlyDisable(tableEditorSwapButton);
          editorTitle.setText("Table editor");
        });
    mapEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(mapView);
          importExportZone.setVisible(false);
          onlyDisable(mapEditorSwapButton);
          editorTitle.setText("Map editor");
        });
    moveEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(moveView);
          importExportZone.setVisible(false);
          onlyDisable(moveEditorSwapButton);
          editorTitle.setText("Move editor");
        });
    requestsEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(serviceView);
          importExportZone.setVisible(false);
          onlyDisable(requestsEditorSwapButton);
          editorTitle.setText("Request editor");
        });
    employeeEditorSwapButton.setOnAction(
        event -> {
          onlyVisible(employeeView);
          importExportZone.setVisible(false);
          onlyDisable(employeeEditorSwapButton);
          editorTitle.setText("Employee editor");
        });

    importButton.setOnAction(event -> tableViewController.importTable());
    exportButton.setOnAction(event -> tableViewController.exportTable());

    backButton.setOnAction(event -> Navigation.navigate(Screen.HOME));
  }

  private void onlyDisable(MFXButton btn) {
    tableEditorSwapButton.setDisable(false);
    mapEditorSwapButton.setDisable(false);
    moveEditorSwapButton.setDisable(false);
    requestsEditorSwapButton.setDisable(false);
    employeeEditorSwapButton.setDisable(false);
    btn.setDisable(true);
  }

  private void onlyVisible(AnchorPane pane) {
    tableView.setVisible(false);
    moveView.setVisible(false);
    serviceView.setVisible(false);
    mapView.setVisible(false);
    employeeView.setVisible(false);
    pane.setVisible(true);
  }

  public void menuBarVisible(boolean bool) {
    menuBar.setVisible(bool);
  }
}
