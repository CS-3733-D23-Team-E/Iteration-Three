package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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

  @FXML MFXButton tableEditorSwapButton;
  @FXML MFXButton mapEditorSwapButton;
  @FXML MFXButton moveEditorSwapButton;
  @FXML MFXButton requestsEditorSwapButton;
  @FXML VBox importExportZone;

  @FXML MFXButton importButton;
  @FXML MFXButton exportButton;

  @FXML MFXButton backButton;
  @FXML Label editorTitle;

  @FXML
  public void initialize() {
    tableView.setVisible(true);
    moveView.setVisible(false);
    serviceView.setVisible(false);
    mapView.setVisible(false);
    importExportZone.setVisible(true);
    onlyDisable(tableEditorSwapButton);
    editorTitle.setText("Table editor");

    initButtons();
  }

  private void initButtons() {
    tableEditorSwapButton.setOnAction(
        event -> {
          tableView.setVisible(true);
          moveView.setVisible(false);
          serviceView.setVisible(false);
          mapView.setVisible(false);
          importExportZone.setVisible(true);
          onlyDisable(tableEditorSwapButton);
          editorTitle.setText("Table editor");
        });
    mapEditorSwapButton.setOnAction(
        event -> {
          tableView.setVisible(false);
          moveView.setVisible(false);
          serviceView.setVisible(false);
          mapView.setVisible(true);
          importExportZone.setVisible(false);
          onlyDisable(mapEditorSwapButton);
          editorTitle.setText("Map editor");
        });
    moveEditorSwapButton.setOnAction(
        event -> {
          tableView.setVisible(false);
          moveView.setVisible(true);
          serviceView.setVisible(false);
          mapView.setVisible(false);
          importExportZone.setVisible(false);
          onlyDisable(moveEditorSwapButton);
          editorTitle.setText("Move editor");
        });
    requestsEditorSwapButton.setOnAction(
        event -> {
          tableView.setVisible(false);
          moveView.setVisible(false);
          serviceView.setVisible(true);
          mapView.setVisible(false);
          importExportZone.setVisible(false);
          onlyDisable(requestsEditorSwapButton);
          editorTitle.setText("Request editor");
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
    btn.setDisable(true);
  }
}
