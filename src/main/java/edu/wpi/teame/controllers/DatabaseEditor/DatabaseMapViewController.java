package edu.wpi.teame.controllers.DatabaseEditor;

import static edu.wpi.teame.map.HospitalNode.allNodes;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.*;
import edu.wpi.teame.utilities.MapUtilities;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.controlsfx.control.SearchableComboBox;

public class DatabaseMapViewController {

  @FXML AnchorPane mapPaneLowerTwo;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;

  @FXML Tab floorOneTab;
  @FXML Tab floorTwoTab;
  @FXML Tab floorThreeTab;
  @FXML Tab lowerLevelTwoTab;
  @FXML Tab lowerLevelOneTab;

  @FXML TabPane tabPane;

  // Sidebar Elements
  @FXML AnchorPane sidebar;

  @FXML Text editPageText;

  @FXML TextField xField;
  @FXML TextField yField;
  @FXML SearchableComboBox<String> buildingSelector;
  @FXML MFXButton confirmButton;
  @FXML MFXButton deleteNodeButton;
  @FXML MFXButton cancelButton;

  @FXML MFXButton addEdgeButton;
  @FXML MFXButton removeEdgeButton;
  @FXML TableView<HospitalNode> edgeView;
  @FXML TableColumn<HospitalNode, String> edgeColumn;
  @FXML SearchableComboBox<String> addEdgeField;

  @FXML TextField newLongNameField;
  @FXML TextField newShortNameField;
  @FXML MFXButton addLocationButton;
  @FXML MFXButton removeLocationButton;

  @FXML SearchableComboBox<LocationName.NodeType> nodeTypeChoice;

  @FXML SearchableComboBox<String> longNameSelector;

  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3

  Floor currentFloor;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  private Circle currentCircle;
  private Label currentLabel;

  List<HospitalNode> edges = new LinkedList<>();
  List<HospitalNode> addList = new LinkedList<>();
  List<HospitalNode> deleteList = new LinkedList<>();

  List<HospitalNode> workingList = new LinkedList<>();

  HospitalNode curNode;

  boolean widthLoaded = false;
  boolean heightLoaded = false;

  @FXML
  public void initialize() {
    initializeMapUtilities();
    currentFloor = Floor.LOWER_TWO;

    sidebar.setVisible(true);
    // Sidebar functions
    cancelButton.setOnAction(event -> cancel());
    confirmButton.setOnAction(event -> uploadChangesToDatabase());
    updateCombo(); // TODO: Change
    deleteNodeButton.setOnAction(event -> deleteNode());

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

    edgeColumn.setCellValueFactory(new PropertyValueFactory<HospitalNode, String>("nodeID"));

    displayAddMenu();
    initializeButtons();
  }

  private void cancel() {
    if (currentCircle != null) {
      currentCircle.setRadius(5);
      currentLabel.setVisible(false);
    }
    currentCircle = null;
    currentLabel = null;
    displayAddMenu();
    // workingList.clear();
    // turn circle back to normal

  }

  private void deleteNode() {
    SQLRepo.INSTANCE.deletenode(curNode);
    displayAddMenu();
    refreshMap();
  }

  public void loadFloorNodes() {
    // create nodes
    List<HospitalNode> floorNodes = SQLRepo.INSTANCE.getNodesFromFloor(currentFloor);
    List<HospitalEdge> floorEdges =
        SQLRepo.INSTANCE.getEdgeList().stream()
            .filter(
                edge -> HospitalNode.allNodes.get(edge.getNodeOneID()).getFloor() == currentFloor)
            .toList();

    // create edges
    for (HospitalEdge edge : floorEdges) {

      HospitalNode node1 = HospitalNode.allNodes.get(edge.getNodeOneID());
      HospitalNode node2 = HospitalNode.allNodes.get(edge.getNodeTwoID());

      // only draw edges on the same floor
      if (node1.getFloor() == node2.getFloor()) {
        whichMapUtility(currentFloor).drawEdge(node1, node2);
      }
    }

    for (HospitalNode node : floorNodes) {
      setupNode(node);
    }
  }

  public void initialLoadFloor(Floor floor) {
    currentFloor = floor;
    loadFloorNodes();
  }

  private void setupNode(HospitalNode node) {

    String nodeID = node.getNodeID();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);

    Circle nodeCircle = currentMapUtility.drawHospitalNode(node);
    Label nodeLabel = currentMapUtility.drawHospitalNodeLabel(node);
    nodeLabel.setVisible(false);

    nodeCircle.setOnMouseClicked(
        event -> {
          if (currentCircle != null && currentLabel != null) {
            currentCircle.setRadius(5);
            currentLabel.setVisible(false);
          }
          currentCircle = nodeCircle;
          currentCircle.setRadius(9);
          currentLabel = nodeLabel;
          currentLabel.setVisible(true);
          setEditMenuVisible(true);
          updateEditMenu();
        });
  }

  public void refreshMap() {
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    currentMapUtility.removeAll();
    loadFloorNodes();
  }

  private void setEditMenuVisible(boolean isVisible) {
    if (isVisible) {
      editPageText.setText("Edit Node");
    } else {
      editPageText.setText("Add Node");
    }
  }

  // APPEARS WHEN YOU CLICK ON A NODE
  private void updateEditMenu() {
    String nodeID = currentCircle.getId();
    editPageText.setText("Edit Node: ID = " + nodeID);

    curNode = allNodes.get(nodeID);
    System.out.println(curNode);
    //    edges =
    //        SQLRepo.INSTANCE.getEdgeList().stream()
    //            .filter((edge) -> (edge.getNodeOneID().equals(nodeID)))
    //            .toList();
    edges = curNode.getNeighbors();
    System.out.println(curNode.getNeighbors());

    workingList = new LinkedList<>();

    // System.out.println("item added to working list!");
    workingList.addAll(edges);
    //    workingList = FXCollections.observableList(edges);

    addList = new LinkedList<>();
    deleteList = new LinkedList<>();

    String x = Integer.toString(curNode.getXCoord());
    String y = Integer.toString(curNode.getYCoord());
    longNameSelector.setValue(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)));

    buildingSelector.setValue(curNode.getBuilding());

    xField.setText(x);
    yField.setText(y);

    confirmButton.setOnAction(
        (event) -> {
          uploadChangesToDatabase();
        });

    edgeView.setItems(FXCollections.observableList(workingList));
    System.out.println(workingList);

    deleteNodeButton.setVisible(true);
  }

  // APPEARS WHEN YOU CLICK OFF A NODE/CANCEL (DEFAULT)
  private void displayAddMenu() {
    setEditMenuVisible(false);

    int nodeID = makeNewNodeID();
    currentCircle = new Circle();
    currentCircle.setId(nodeID + "");
    // System.out.println(nodeID);
    editPageText.setText("Add Node: ID = " + nodeID);

    // System.out.println("making sure we are here");

    // clear all items
    xField.setText("");
    yField.setText("");
    edges = new LinkedList<>();
    addList = new LinkedList<>();
    workingList = new LinkedList<>();
    deleteList = new LinkedList<>();
    longNameSelector.setValue(null);
    buildingSelector.setValue(null);
    // edgeView.getItems().clear();
    deleteNodeButton.setVisible(false);

    edgeView.setItems(FXCollections.observableList(workingList));

    confirmButton.setOnAction(
        (event -> {
          addNodeToDatabase();
        }));
  }

  private void addLocationName() {
    if (newLongNameField.getText() != "" && newShortNameField.getText() != "") {
      LocationName addedLN =
          new LocationName(
              newLongNameField.getText(), newShortNameField.getText(), nodeTypeChoice.getValue());
      longNameSelector.getItems().add(addedLN.getLongName());
      SQLRepo.INSTANCE.addLocation(addedLN);
      newShortNameField.setText("");
      newLongNameField.setText("");
      nodeTypeChoice.setValue(null);
    } else {
      // nothing
    }
  }

  private void removeLocation() {
    LocationName toBeDeleted =
        new LocationName(longNameSelector.getValue(), "", LocationName.NodeType.INFO);
    SQLRepo.INSTANCE.deleteLocation(toBeDeleted);
    longNameSelector.getItems().remove(longNameSelector.getValue());
  }

  private void uploadChangesToDatabase() {
    String nodeID = currentCircle.getId();
    HospitalNode hospitalNode = allNodes.get(nodeID);
    LocationName.NodeType nodeType =
        LocationName.NodeType.stringToNodeType(
            SQLRepo.INSTANCE.getNodeTypeFromNodeID(Integer.parseInt(nodeID)));

    List<HospitalNode> nodesToBeUpdated = new ArrayList<>();
    nodesToBeUpdated.add(hospitalNode);

    if (nodeType == LocationName.NodeType.ELEV) {
      // Getting Elevator (elevator letter) which is at the 10th index TODO parse/link elevator
      // better
      String elevatorName =
          SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)).substring(0, 10);

      List<LocationName> locationNames = SQLRepo.INSTANCE.getLocationList();
      locationNames =
          locationNames.stream()
              .filter(locationName -> locationName.getLongName().contains(elevatorName))
              .toList();
      nodesToBeUpdated =
          locationNames.stream()
              .map(
                  locationName ->
                      HospitalNode.allNodes.get(
                          Integer.toString(
                              SQLRepo.INSTANCE.getNodeIDFromName(locationName.getLongName()))))
              .toList();

      for (HospitalNode node : nodesToBeUpdated) {

        String currentNodeID = node.getNodeID();

        String newX = xField.getText();
        String newY = yField.getText();

        // update the database
        SQLRepo.INSTANCE.updateNode(node, "xcoord", newX);
        SQLRepo.INSTANCE.updateNode(node, "ycoord", newY);
        edgeUpdateDatabase();

        if (currentNodeID.equals(nodeID)) {
          SQLRepo.INSTANCE.updateUsingNodeID(
              nodeID,
              SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)),
              "longName",
              longNameSelector.getValue());
          continue;
        }
      }

    } else {
      String newX = xField.getText();
      String newY = yField.getText();
      nodeID = hospitalNode.getNodeID();

      // update the database
      SQLRepo.INSTANCE.updateNode(hospitalNode, "xcoord", newX);
      SQLRepo.INSTANCE.updateNode(hospitalNode, "ycoord", newY);
      edgeUpdateDatabase();

      // EDIT THE MOVE
      SQLRepo.INSTANCE.updateUsingNodeID(
          nodeID,
          SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)),
          "longName",
          longNameSelector.getValue());
    }

    // RELOAD THE DATABASE
    refreshMap();

    // CLOSE THE MENU
    displayAddMenu();
  }

  private void addNodeToDatabase() {
    int id = makeNewNodeID();
    // add respective node
    HospitalNode node =
        new HospitalNode(
            id + "",
            Integer.parseInt(xField.getText()),
            Integer.parseInt(yField.getText()),
            currentFloor,
            buildingSelector.getValue());
    SQLRepo.INSTANCE.addNode(node);
    // add respective move
    MoveAttribute move =
        new MoveAttribute(id, longNameSelector.getValue(), LocalDate.now().toString());
    SQLRepo.INSTANCE.addMove(move);
    // add respective edges
    edgeUpdateDatabase();
    refreshMap();
  }

  private void updateCombo() {
    // POPULATE COMBOBOXES

    List<LocationName> locationNames = SQLRepo.INSTANCE.getLocationList();
    List<String> longNames = SQLRepo.INSTANCE.getLongNamesFromLocationName(locationNames);
    longNameSelector.setItems(FXCollections.observableList(longNames));

    buildingSelector.setItems(FXCollections.observableArrayList(HospitalNode.allBuildings()));

    nodeTypeChoice.setItems(
        FXCollections.observableArrayList(LocationName.NodeType.allNodeTypes()));

    addEdgeField.setItems(FXCollections.observableList(allNodes.keySet().stream().toList()));
  }

  public AnchorPane whichPane(Floor curFloor) {
    switch (curFloor) {
      case ONE:
        return mapPaneOne;
      case TWO:
        return mapPaneTwo;
      case THREE:
        return mapPaneThree;
      case LOWER_ONE:
        return mapPaneLowerOne;
      case LOWER_TWO:
        return mapPaneLowerTwo;
    }
    return mapPaneOne;
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

  private void initializeButtons() {
    addEdgeButton.setOnAction(
        (event -> {
          // if item is in edge list, remove from delete list
          if (edges.contains(addEdgeField.getValue())) {
            deleteList.remove(addEdgeField.getValue());
          } else { // if item is not in edge list, add to add list
            addList.add(allNodes.get(addEdgeField.getValue()));
          }
          workingList.add(allNodes.get(addEdgeField.getValue()));
          // System.out.println("item added to working list!");
          // refresh the table
          refreshEdgeTable();
        }));

    removeEdgeButton.setOnAction(
        (event -> {
          // if item is in edge list, add to delete list
          if (edges.contains(edgeView.getSelectionModel().getSelectedItem())) {
            deleteList.add(edgeView.getSelectionModel().getSelectedItem());
            // System.out.println("added to delete list!");
          } else { // if item is not in the edge list, remove from add list
            addList.remove(edgeView.getSelectionModel().getSelectedItem());
          }
          workingList.remove(edgeView.getSelectionModel().getSelectedItem());
          refreshEdgeTable();
        }));
    removeLocationButton.setOnAction(
        event -> {
          removeLocation();
        });
    addLocationButton.setOnAction(event -> addLocationName());
  }

  private void refreshEdgeTable() {
    edgeView.setItems(FXCollections.observableList(workingList));
  }

  private void edgeUpdateDatabase() {
    for (HospitalNode edgeAddition : addList) {
      HospitalEdge addEdge = new HospitalEdge(curNode.getNodeID(), edgeAddition.getNodeID());
      curNode.getNeighbors().add(edgeAddition);
      SQLRepo.INSTANCE.addEdge(addEdge);
    }
    for (HospitalNode edgeDeletion : deleteList) {
      HospitalEdge delEdge1 = new HospitalEdge(curNode.getNodeID(), edgeDeletion.getNodeID());
      HospitalEdge delEdge2 = new HospitalEdge(edgeDeletion.getNodeID(), curNode.getNodeID());
      curNode.getNeighbors().remove(edgeDeletion);
      // whichever gets deleted, gets deleted
      SQLRepo.INSTANCE.deleteEdge(delEdge1);
      SQLRepo.INSTANCE.deleteEdge(delEdge2);
    }
  }

  private int makeNewNodeID() {
    return (Integer.parseInt(
            allNodes.keySet().stream()
                .sorted(
                    new Comparator<>() {
                      @Override
                      public int compare(String str1, String str2) {
                        return Integer.parseInt((String) str1) - Integer.parseInt((String) str2);
                      }
                    })
                .toList()
                .get(allNodes.size() - 1))
        + 5);
  }
}
