package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.App;
import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.MoveAttribute;
import edu.wpi.teame.utilities.MoveUtilities;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class MoveComponentController {
  @FXML SearchableComboBox<String> departmentMoveSelector;
  @FXML SearchableComboBox<Integer> newNodeSelector;
  @FXML SearchableComboBox<String> departmentOneSelector;
  @FXML SearchableComboBox<String> departmentTwoSelector;
  @FXML DatePicker moveDateSelector;
  @FXML Tab moveTab;
  @FXML Tab swapTab;
  @FXML MFXButton confirmButton;
  @FXML MFXButton resetButton;
  @FXML Label todayIsLabel;
  @FXML Label moveCountText;
  @FXML ListView<String> currentMoveList;
  @FXML TableView<MoveAttribute> futureMoveTable;
  @FXML TableColumn<MoveAttribute, String> nodeIDCol;
  @FXML TableColumn<MoveAttribute, String> nameCol;
  @FXML TableColumn<MoveAttribute, String> dateCol;

  MoveUtilities movUtil;

  @FXML MFXButton mapPreviewButton;

  @FXML
  public void initialize() {
    movUtil = new MoveUtilities();
    todayIsLabel.setText(todayIsLabel.getText() + movUtil.formatToday());
    refreshFields();
    initTableAndList();
    initButtons();
  }

  private void initButtons() {
    swapTab.setOnSelectionChanged(
        event -> {
          if (swapTab.isSelected()) {
            confirmButton.setOnAction(e -> swapDepartments());
          }
        });
    moveTab.setOnSelectionChanged(
        event -> {
          if (moveTab.isSelected()) {
            confirmButton.setOnAction(e -> moveToNewNode());
          }
        });
    resetButton.setOnAction(event -> resetFieldSelections());
    confirmButton.setOnAction(e -> moveToNewNode());
    mapPreviewButton.setOnAction(
        event -> {
          if (moveTab.isSelected()) {
            if (departmentMoveSelector.getValue() != null && newNodeSelector.getValue() != null) {
              openStage(
                  movUtil.getNodeFromMove(
                      movUtil
                          .findMostRecentMoveByDate(departmentMoveSelector.getValue())
                          .getNodeID()),
                  movUtil.getNodeFromMove(newNodeSelector.getValue()));
            }
          } else {
            if (departmentOneSelector.getValue() != null
                && departmentTwoSelector.getValue() != null) {
              openStage(
                  movUtil.getNodeFromMove(
                      movUtil
                          .findMostRecentMoveByDate(departmentOneSelector.getValue())
                          .getNodeID()),
                  movUtil.getNodeFromMove(
                      movUtil
                          .findMostRecentMoveByDate(departmentTwoSelector.getValue())
                          .getNodeID()));
            }
          }
        });
  }

  private void refreshFields() {

    ObservableList<String> availableLocations =
        FXCollections.observableList(
            movUtil.getMovesForDepartments().stream()
                .map(move -> move.getLongName())
                .sorted()
                .distinct()
                .toList());

    // List of node IDs that only contains the node IDs of departments
    List<Integer> nodeIDs =
        movUtil.getMovesForDepartments().stream().map(MoveAttribute::getNodeID).distinct().toList();
    newNodeSelector.setItems(FXCollections.observableList(nodeIDs));

    departmentMoveSelector.setItems(availableLocations);
    departmentOneSelector.setItems(availableLocations);
    departmentTwoSelector.setItems(availableLocations);
  }

  private void swapDepartments() {
    if ((departmentOneSelector.getValue() != null)
        && (departmentTwoSelector.getValue() != null)
        && (moveDateSelector.getValue() != null)) {
      // MoveAttribute moveOne = findMoveAttribute(departmentOneSelector.getValue());
      MoveAttribute moveOne = movUtil.findMostRecentMoveByDate(departmentOneSelector.getValue());
      //      MoveAttribute moveTwo = findMoveAttribute(departmentTwoSelector.getValue());
      MoveAttribute moveTwo = movUtil.findMostRecentMoveByDate(departmentTwoSelector.getValue());

      // make sure the current moves aren't on the same day as the suggested move
      if (movUtil.afterDate(moveOne, moveDateSelector.getValue()) != 0
          && movUtil.afterDate(moveTwo, moveDateSelector.getValue()) != 0) {
        MoveAttribute swaping1With2 =
            new MoveAttribute(
                moveOne.getNodeID(), moveTwo.getLongName(), moveDateSelector.getValue().toString());
        MoveAttribute swaping2With1 =
            new MoveAttribute(
                moveTwo.getNodeID(), moveOne.getLongName(), moveDateSelector.getValue().toString());

        SQLRepo.INSTANCE.addMove(swaping1With2);
        SQLRepo.INSTANCE.addMove(swaping2With1);

        initTableAndList();
        resetFieldSelections();
      } else {
        // Throw an error in a popup or around the text box
        System.out.println("The move you tried to add is too close to another move!");
      }
    }
  }

  private void moveToNewNode() {
    if ((departmentMoveSelector.getValue() != null)
        && (newNodeSelector.getValue() != null)
        && (moveDateSelector.getValue() != null)) {

      MoveAttribute toBeMoved = movUtil.findMostRecentMoveByDate(departmentMoveSelector.getValue());
      SQLRepo.INSTANCE.addMove(
          new MoveAttribute(
              newNodeSelector.getValue(),
              toBeMoved.getLongName(),
              moveDateSelector.getValue().toString()));

      initTableAndList();
      resetFieldSelections();
    }
  }

  private void resetFieldSelections() {
    departmentMoveSelector.setValue(null);
    departmentOneSelector.setValue(null);
    departmentTwoSelector.setValue(null);
    moveDateSelector.setValue(null);
    newNodeSelector.setValue(null);
  }

  private void initTableAndList() {
    nodeIDCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("nodeID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("longName"));
    dateCol.setCellValueFactory(new PropertyValueFactory<MoveAttribute, String>("date"));

    futureMoveTable.setItems(FXCollections.observableList(movUtil.getFutureMoves()));

    currentMoveList.setItems(FXCollections.observableList(movUtil.getCurrentMoveMessages()));

    moveCountText.setText(currentMoveList.getItems().size() + " Move(s) Today: ");
  }

  private void openStage(HospitalNode node1, HospitalNode node2) {
    var resource = App.class.getResource("views/DatabaseEditor/MovePreview.fxml");
    MovePreviewController movePreviewController;
    if (swapTab.isSelected()) {
      movePreviewController =
          new MovePreviewController(
              node1,
              node2,
              departmentOneSelector.getValue(),
              departmentTwoSelector.getValue(),
              true);
    } else {
      movePreviewController =
          new MovePreviewController(
              node1, node2, departmentMoveSelector.getValue(), "New Location", false);
    }

    FXMLLoader loader = new FXMLLoader(resource);
    loader.setController(movePreviewController); // NOTE: replaces this line in the FXML:
    // fx:controller="edu.wpi.teame.controllers.DatabaseEditor.MovePreviewController"

    AnchorPane previewLayout;
    try {
      previewLayout = loader.load();
    } catch (IOException e) {
      previewLayout = new AnchorPane();
    }

    Scene newScene = new Scene(previewLayout);

    Stage newStage = new Stage();
    newStage.setTitle("Move Preview");
    newStage.setScene(newScene);
    newStage.show();
  }
}
