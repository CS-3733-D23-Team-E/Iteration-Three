package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.map.Floor;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.utilities.MapUtilities;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class MovePreviewController {
  @FXML AnchorPane mapPaneLowerTwo;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;

  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3

  @FXML Tab floorOneTab;
  @FXML Tab floorTwoTab;
  @FXML Tab floorThreeTab;
  @FXML Tab lowerLevelTwoTab;
  @FXML Tab lowerLevelOneTab;

  @FXML TabPane tabPane;

  @FXML Label moveDescription;

  Floor currentFloor;

  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  boolean widthLoaded = false;
  boolean heightLoaded = false;

  HospitalNode node1;
  HospitalNode node2;
  String name1;
  String name2;
  boolean bidirectional;

  public MovePreviewController(
      HospitalNode node1, HospitalNode node2, String name1, String name2, boolean bidirectional) {
    this.node1 = node1;
    this.node2 = node2;
    this.name1 = name1;
    this.name2 = name2;
    this.bidirectional = bidirectional;
  }

  @FXML
  void initialize() {
    System.out.println("Initializing move preview!!");
    initializeMapUtilities();
    currentFloor = Floor.LOWER_TWO;

    tabPane
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              currentFloor = tabToFloor(newValue);
              refreshMap();
            });

    // TODO do this better
    mapPaneLowerTwo
        .widthProperty()
        .addListener(
            (observable, oldWidth, newWidth) -> {
              if (newWidth.doubleValue() > 0) {
                widthLoaded = true;
              }
              if (widthLoaded && heightLoaded) {
                currentFloor = Floor.LOWER_TWO;
                loadFloorNodes();
              }
            });
    mapPaneLowerTwo
        .heightProperty()
        .addListener(
            (observable, oldHeight, newHeight) -> {
              if (newHeight.doubleValue() > 0) {
                heightLoaded = true;
              }
              if (widthLoaded && heightLoaded) {
                currentFloor = Floor.LOWER_TWO;
                loadFloorNodes();
              }
            });

    // set the move description text
    StringBuilder moveDescText = new StringBuilder();
    moveDescText.append(name1).append(" to node ").append(node2.getNodeID()).append("\n");
    if (bidirectional) {
      moveDescText.append(name2).append(" to node ").append(node1.getNodeID()).append("\n");
    }
    moveDescription.setText(moveDescText.toString());
  }

  public Floor tabToFloor(Tab tab) {
    if (tab == lowerLevelTwoTab) {
      return Floor.LOWER_TWO;
    }
    if (tab == lowerLevelOneTab) {
      return Floor.LOWER_ONE;
    }
    if (tab == floorOneTab) {
      return Floor.ONE;
    }
    if (tab == floorTwoTab) {
      return Floor.TWO;
    }
    if (tab == floorThreeTab) {
      return Floor.THREE;
    }
    return Floor.ONE;
  }

  private void initializeMapUtilities() {
    mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
    mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
    mapUtilityOne = new MapUtilities(mapPaneOne);
    mapUtilityTwo = new MapUtilities(mapPaneTwo);
    mapUtilityThree = new MapUtilities(mapPaneThree);

    mapUtilityLowerTwo.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityLowerOne.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityOne.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityTwo.setLabelStyle("-fx-font-size: 10pt");
    mapUtilityThree.setLabelStyle("-fx-font-size: 10pt");
  }

  public void refreshMap() {
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    currentMapUtility.removeAll();
    loadFloorNodes();
  }

  public MapUtilities whichMapUtility(Floor currFloor) {
    switch (currFloor) {
      case LOWER_TWO:
        return mapUtilityLowerTwo;
      case LOWER_ONE:
        return mapUtilityLowerOne;
      case ONE:
        return mapUtilityOne;
      case TWO:
        return mapUtilityTwo;
      case THREE:
        return mapUtilityThree;
    }
    return mapUtilityLowerOne;
  }

  public void loadFloorNodes() {
    // create edges
    // edges
    if (node1.getFloor() == node2.getFloor() && node1.getFloor().equals(currentFloor)) {
      whichMapUtility(currentFloor).drawEdge(node1, node2);
    }

    // TODO: draw label on node dependent on the name parameter passed in, NOT te database
    if (node1.getFloor().equals(currentFloor) || node2.getFloor().equals(currentFloor)) {
      whichMapUtility(currentFloor).drawEdge(node1, node2);
      if (node1.getFloor().equals(currentFloor)) {
        // draw phantom label for node 2
        setupNode(node1, name1);
        if (node2.getFloor().equals(currentFloor)) {
          setupNode(node2, name2);
        } else {
          whichMapUtility(currentFloor)
              .drawHospitalNodeLabel(node2, "Moved to floor: " + node2.getFloor());
        }
      } else {
        // draw phantom label for node 1
        setupNode(node2, name2);
        whichMapUtility(currentFloor)
            .drawHospitalNodeLabel(node1, "Moved to floor: " + node1.getFloor());
      }
    }
  }

  private void setupNode(HospitalNode node, String name) {

    String nodeID = node.getNodeID();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);

    Circle nodeCircle = currentMapUtility.drawHospitalNode(node);
    Label nodeLabel = currentMapUtility.drawHospitalNodeLabel(node, name);
    nodeLabel.setVisible(true);
  }
}
