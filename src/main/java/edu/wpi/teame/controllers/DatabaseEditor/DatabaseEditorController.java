package edu.wpi.teame.controllers.DatabaseEditor;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class DatabaseEditorController {
  @FXML TabPane tabPane;
  @FXML Tab editMapTab;

  @FXML Tab editDatabaseTab;

  @FXML DatabaseMapViewController mapViewController;

  @FXML
  public void initialize() {

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
