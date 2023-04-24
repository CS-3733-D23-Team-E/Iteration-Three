package edu.wpi.teame.controllers.DatabaseEditor;

import static edu.wpi.teame.map.HospitalNode.allNodes;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.*;
import edu.wpi.teame.utilities.MapUtilities;
import edu.wpi.teame.utilities.Navigation;
import edu.wpi.teame.utilities.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.time.LocalDate;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

public class DatabaseMapViewController {

  @FXML AnchorPane mapPaneLowerTwo;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;

  @FXML GesturePane gesturePaneLowerTwo;
  @FXML GesturePane gesturePaneLowerOne;
  @FXML GesturePane gesturePaneOne;
  @FXML GesturePane gesturePaneTwo;
  @FXML GesturePane gesturePaneThree;

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
  @FXML TableView<HospitalEdge> edgeView;
  @FXML TableColumn<HospitalEdge, String> edgeColumn;
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
  @FXML MFXButton tableEditorSwapButton;

  Floor currentFloor;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  private Circle currentCircle;
  private Label currentLabel;

  List<HospitalEdge> edges = new LinkedList<>();
  List<HospitalEdge> addList = new LinkedList<>();
  List<HospitalEdge> deleteList = new LinkedList<>();

  List<HospitalEdge> workingList = new LinkedList<>();

  HospitalNode currNode;

  boolean widthLoaded = false;
  boolean heightLoaded = false;

  @FXML
  public void initialize() {
    initializeMapUtilities();
    initializeMapPanes();
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

    tableEditorSwapButton.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.DATABASE_TABLEVIEW);
        });

    edgeColumn.setCellValueFactory(new PropertyValueFactory<HospitalEdge, String>("nodeTwoID"));

    displayAddMenu();
    initializeButtons();
  }

  private void cancel() {
    if (currentCircle != null) {
      currentCircle.setRadius(5);
      currentLabel.setVisible(false);
    }
    refreshMap();
    currentCircle = null;
    currentLabel = null;
    displayAddMenu();
    // workingList.clear();
    // turn circle back to normal

  }

  private void deleteNode() {
    SQLRepo.INSTANCE.deletenode(currNode);
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

  private void setupNode(HospitalNode node) {

    String nodeID = node.getNodeID();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);

    Circle nodeCircle = currentMapUtility.drawHospitalNode(node);
    Label nodeLabel = currentMapUtility.drawHospitalNodeLabel(node);
    nodeLabel.setVisible(false);

    nodeCircle.setOnMouseClicked(
        event -> {
          GesturePane currentGesturePane = whichGesturePane(currentFloor);
          currentGesturePane.setGestureEnabled(true);
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
    nodeCircle.setOnMouseDragged(
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

          GesturePane currentGesturePane = whichGesturePane(currentFloor);
          currentGesturePane.setGestureEnabled(false);
          dragUpdate(event);
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

    currNode = allNodes.get(nodeID);
    edges =
        SQLRepo.INSTANCE.getEdgeList().stream()
            .filter((edge) -> (edge.getNodeOneID().equals(nodeID)))
            .toList();

    workingList = new LinkedList<>();

    workingList.addAll(edges);

    addList = new LinkedList<>();
    deleteList = new LinkedList<>();

    String x = Integer.toString(currNode.getXCoord());
    String y = Integer.toString(currNode.getYCoord());
    longNameSelector.setValue(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID)));

    buildingSelector.setValue(currNode.getBuilding());

    xField.setText(x);
    yField.setText(y);

    confirmButton.setOnAction(
        (event) -> {
          uploadChangesToDatabase();
        });

    edgeView.setItems(FXCollections.observableList(workingList));

    deleteNodeButton.setVisible(true);
  }

  // called when node is dragged
  private void dragUpdate(MouseEvent mouseEvent) {
    String nodeID = currentCircle.getId();
    editPageText.setText("Edit Node: ID = " + nodeID);
    currNode = allNodes.get(nodeID);

    // get x and y from drag and set new x and y for circle and label
    ((Circle) mouseEvent.getSource()).setCenterX(mouseEvent.getX());
    ((Circle) mouseEvent.getSource()).setCenterY(mouseEvent.getY());

    double newX = currentCircle.getCenterX();
    double newY = currentCircle.getCenterY();
    currentLabel.setLayoutX(newX);
    currentLabel.setLayoutY(newY);

    // get image coordinates and update on edit menu
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    int imageX = currentMapUtility.PaneXToImageX(currentCircle.getCenterX());
    int imageY = currentMapUtility.PaneYToImageY(currentCircle.getCenterY());
    xField.setText(Integer.toString(imageX));
    yField.setText(Integer.toString(imageY));

    // update edges based off drag
    List<Node> startEdgesToUpdate =
        currentMapUtility.getCurrentNodes().stream()
            .filter(node -> node.getId().contains("startNode:" + currNode))
            .toList();

    List<Node> endEdgesToUpdate =
        currentMapUtility.getCurrentNodes().stream()
            .filter(node -> node.getId().contains("endNode:" + currNode))
            .toList();

    for (Node node : startEdgesToUpdate) {
      Line line = (Line) node;
      line.setStartX(newX);
      line.setStartY(newY);
    }

    for (Node node : endEdgesToUpdate) {
      Line line = (Line) node;
      line.setEndX(newX);
      line.setEndY(newY);
    }
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

  public GesturePane whichGesturePane(Floor curFloor) {
    switch (curFloor) {
      case ONE:
        return gesturePaneOne;
      case TWO:
        return gesturePaneTwo;
      case THREE:
        return gesturePaneThree;
      case LOWER_ONE:
        return gesturePaneLowerOne;
      case LOWER_TWO:
        return gesturePaneLowerTwo;
    }
    return null;
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

  private void initializeMapPanes() {
    mapPaneLowerTwo.setOnMouseClicked(
        event -> {
          gesturePaneLowerTwo.setGestureEnabled(true);
        });
    mapPaneLowerOne.setOnMouseClicked(
        event -> {
          gesturePaneLowerOne.setGestureEnabled(true);
        });
    mapPaneOne.setOnMouseClicked(
        event -> {
          gesturePaneOne.setGestureEnabled(true);
        });
    mapPaneTwo.setOnMouseClicked(
        event -> {
          gesturePaneTwo.setGestureEnabled(true);
        });
    mapPaneThree.setOnMouseClicked(
        event -> {
          gesturePaneThree.setGestureEnabled(true);
        });
  }

  private void initializeButtons() {
    addEdgeButton.setOnAction(
        (event -> {
          // if item is in edge list, remove from delete list
          if (edges.contains(addEdgeField.getValue())) {
            deleteList.remove(addEdgeField.getValue());
          } else { // if item is not in edge list, add to add list
            addList.add(new HospitalEdge(currentCircle.getId(), addEdgeField.getValue()));
          }
          workingList.add(new HospitalEdge(currentCircle.getId(), addEdgeField.getValue()));
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
    for (HospitalEdge edgeAddition : addList) {
      SQLRepo.INSTANCE.addEdge(edgeAddition);
    }
    for (HospitalEdge edgeDeletion : deleteList) {
      SQLRepo.INSTANCE.deleteEdge(edgeDeletion);
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
