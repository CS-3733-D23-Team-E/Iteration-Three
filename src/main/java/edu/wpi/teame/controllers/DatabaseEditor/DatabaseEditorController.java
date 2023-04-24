package edu.wpi.teame.controllers.DatabaseEditor;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;

public class DatabaseEditorController {
  @FXML TabPane tabPane;
  @FXML Tab editMapTab;

  @FXML Tab editDatabaseTab;

  @FXML DatabaseMapViewController mapViewController;

  @FXML
  AnchorPane tableView;

  @FXML
  public void initialize() {
    tableView.setVisible(false);
    tableView.setManaged(false);

    //    backButton.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    //    tabPane
    //        .getSelectionModel()
    //        .selectedItemProperty()
    //        .addListener(
    //            (observable, oldTab, newTab) -> {
    //              if (newTab == editMapTab) {
    //                mapViewController.initialLoadFloor(Floor.LOWER_TWO);
    //              }
    //            });


  }
}
