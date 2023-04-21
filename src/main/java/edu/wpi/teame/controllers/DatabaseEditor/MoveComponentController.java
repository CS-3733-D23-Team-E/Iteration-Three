package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.orm.HospitalNode;
import edu.wpi.teame.entities.orm.LocationName;
import edu.wpi.teame.entities.orm.MoveAttribute;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

  @FXML MFXButton tableEditorSwapButton;
  @FXML Label moveCountText;
  @FXML ListView<String> currentMoveList;
  @FXML TableView<MoveAttribute> futureMoveTable;
  @FXML TableColumn<MoveAttribute, String> nodeIDCol;
  @FXML TableColumn<MoveAttribute, String> nameCol;
  @FXML TableColumn<MoveAttribute, String> dateCol;

  ObservableList<String> floorLocation =
      FXCollections.observableArrayList(
          SQLRepo.INSTANCE.getLongNamesFromLocationName(SQLRepo.INSTANCE.getLocationList()));;

  Date today;
  SimpleDateFormat formatter;

  @FXML
  public void initialize() {
    today = new Date(); // sets today var to be the current date
    formatter = new SimpleDateFormat("yyyy-MM-dd");
    refreshFields();
    initTableAndList();
    initButtons();
    confirmButton.setOnAction(e -> moveToNewNode());
    tableEditorSwapButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.DATABASE_TABLEVIEW);
        });
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
    resetButton.setOnAction(event -> reset());
  }

  private void refreshFields() {
    floorLocation =
        FXCollections.observableArrayList(
            SQLRepo.INSTANCE.getMoveList().stream()
                .filter(
                    (move) -> // Filter out hallways and long names with no corresponding
                        // LocationName
                        LocationName.allLocations.get(move.getLongName()) == null
                            ? false
                            : LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.HALL
                                && LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.STAI
                                && LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.ELEV
                                && LocationName.allLocations.get(move.getLongName()).getNodeType()
                                    != LocationName.NodeType.REST)
                .map((move) -> move.getLongName())
                .sorted() // Sort alphabetically
                .toList());
    List<Integer> nodeIDs = HospitalNode.allNodes.keySet().stream().map(Integer::parseInt).toList();
    newNodeSelector.setItems(FXCollections.observableList(nodeIDs));
    departmentMoveSelector.setItems(floorLocation);
    departmentOneSelector.setItems(floorLocation);
    departmentTwoSelector.setItems(floorLocation);
  }

  private void swapDepartments() {
    if ((departmentOneSelector.getValue() != null)
        && (departmentTwoSelector.getValue() != null)
        && (moveDateSelector.getValue() != null)) {
      MoveAttribute moveOne = findMoveAttribute(departmentOneSelector.getValue());
      MoveAttribute moveTwo = findMoveAttribute(departmentTwoSelector.getValue());

      MoveAttribute swaping1With2 =
          new MoveAttribute(
              moveOne.getNodeID(), moveTwo.getLongName(), moveDateSelector.getValue().toString());
      MoveAttribute swaping2With1 =
          new MoveAttribute(
              moveTwo.getNodeID(), moveOne.getLongName(), moveDateSelector.getValue().toString());

      SQLRepo.INSTANCE.addMove(swaping1With2);
      SQLRepo.INSTANCE.addMove(swaping2With1);

      initTableAndList();
      reset();
    }
  }

  private void moveToNewNode() {
    if ((departmentMoveSelector.getValue() != null)
        && (newNodeSelector.getValue() != null)
        && (moveDateSelector.getValue() != null)) {

      MoveAttribute toBeMoved = findMoveAttribute(departmentMoveSelector.getValue());
      SQLRepo.INSTANCE.addMove(
          new MoveAttribute(
              newNodeSelector.getValue(),
              toBeMoved.getLongName(),
              moveDateSelector.getValue().toString()));

      initTableAndList();
      reset();
    }
  }

  private MoveAttribute findMoveAttribute(String longName) {
    List<MoveAttribute> listOfMoveAtt = SQLRepo.INSTANCE.getMoveList();
    for (MoveAttribute movAt : listOfMoveAtt) {
      if (longName.equals(movAt.getLongName())) {
        return movAt;
      }
    }
    return null;
  }

  private void reset() {
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

    List<MoveAttribute> allMovesTemp = SQLRepo.INSTANCE.getMoveList();

    List<MoveAttribute> futureMoves =
        allMovesTemp.stream().filter(move -> inFuture(move) >= 0).toList();

    futureMoveTable.setItems(FXCollections.observableList(futureMoves));

    List<String> currentMoveDescriptions =
        allMovesTemp.stream()
            .filter(move -> inFuture(move) == 0)
            .map(move -> move.getLongName() + " to Node " + move.getNodeID())
            .toList();

    currentMoveList.setItems(FXCollections.observableList(currentMoveDescriptions));

    moveCountText.setText(currentMoveDescriptions.size() + " Moves Today: ");
  }

  /**
   * returns 0 if the two date of the move is the same as the current day, less than 0 if it is
   * before, more than 0 if afterwards
   *
   * @param move the given MoveAttribute that is being compared to today's date
   * @return
   */
  private int inFuture(MoveAttribute move) {
    Date moveDate;
    try {
      moveDate = formatter.parse(move.getDate());
    } catch (ParseException e) {
      moveDate = new Date();
      System.out.println(e);
    }

    if (formatter.format(today).equals(formatter.format(moveDate))) return 0;

    return moveDate
        .toInstant()
        .truncatedTo(ChronoUnit.DAYS)
        .compareTo(today.toInstant().truncatedTo(ChronoUnit.DAYS));
  }
}
