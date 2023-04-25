package edu.wpi.teame.controllers;

import static java.lang.Math.PI;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.*;
import edu.wpi.teame.map.pathfinding.AbstractPathfinder;
import edu.wpi.teame.utilities.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

public class MapController {
  @FXML AnchorPane mapPaneOne;
  @FXML AnchorPane mapPaneTwo;
  @FXML AnchorPane mapPaneThree;
  @FXML AnchorPane mapPaneLowerOne;
  @FXML AnchorPane mapPaneLowerTwo;
  @FXML VBox pathBox;
  @FXML TabPane tabPane;
  @FXML Tab floorOneTab;
  @FXML Tab floorTwoTab;
  @FXML Tab floorThreeTab;
  @FXML Tab lowerLevelTwoTab;
  @FXML Tab lowerLevelOneTab;
  @FXML SearchableComboBox<String> currentLocationList;
  @FXML SearchableComboBox<String> destinationList;
  @FXML MFXButton startButton;
  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3
  @FXML RadioButton aStarButton;
  @FXML RadioButton dfsButton;
  @FXML RadioButton bfsButton;
  @FXML Region zoomInRegion;
  @FXML Region zoomOutRegion;
  @FXML Region labelRegion;
  @FXML RadioButton dijkstraButton;
  @FXML GesturePane gesturePaneL2;
  @FXML GesturePane gesturePaneL1;
  @FXML GesturePane gesturePane1;
  @FXML GesturePane gesturePane2;
  @FXML GesturePane gesturePane3;
  boolean isPathDisplayed = false;
  Floor currentFloor = Floor.LOWER_TWO;
  Circle currentCircle = new Circle();
  HBox previousLabel;
  AbstractPathfinder pf = AbstractPathfinder.getInstance("A*");
  String curLocFromComboBox;
  String destFromComboBox;
  MapUtilities mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
  MapUtilities mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
  MapUtilities mapUtilityOne = new MapUtilities(mapPaneOne);
  MapUtilities mapUtilityTwo = new MapUtilities(mapPaneTwo);
  MapUtilities mapUtilityThree = new MapUtilities(mapPaneThree);

  ObservableList<String> floorLocations =
      FXCollections.observableArrayList(
          SQLRepo.INSTANCE.getLongNamesFromMove(
              SQLRepo.INSTANCE.getMoveAttributeFromFloor(currentFloor)));

  @FXML
  public void initialize() {
    initializeMapUtilities();
    tabPane
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldTab, newTab) -> {
              // Set the zoom and position of the new pane to the old one
              AnchorPane oldPane = (AnchorPane) oldTab.getContent();
              GesturePane oldGesture = (GesturePane) oldPane.getChildren().get(0);
              AnchorPane newPane = (AnchorPane) newTab.getContent();
              GesturePane newGesture = (GesturePane) newPane.getChildren().get(0);
              adjustGesture(oldGesture, newGesture);
            });

    startButton.setOnAction(
        event -> {
          curLocFromComboBox = currentLocationList.getValue();
          destFromComboBox = destinationList.getValue();
          displayPath(curLocFromComboBox, destFromComboBox);
        });

    setSVG();
    // Make sure location list is initialized so that we can filter out the hallways
    SQLRepo.INSTANCE.getLocationList();

    resetComboboxes();
  }

  private void initializeMapUtilities() {
    mapUtilityLowerTwo = new MapUtilities(mapPaneLowerTwo);
    mapUtilityLowerOne = new MapUtilities(mapPaneLowerOne);
    mapUtilityOne = new MapUtilities(mapPaneOne);
    mapUtilityTwo = new MapUtilities(mapPaneTwo);
    mapUtilityThree = new MapUtilities(mapPaneThree);

    mapUtilityLowerTwo.setCircleStyle("-fx-fill: gold; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityLowerOne.setCircleStyle("-fx-fill: cyan; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityOne.setCircleStyle("-fx-fill: lime; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityTwo.setCircleStyle("-fx-fill: hotpink; -fx-stroke: black; -fx-stroke-width: 1");
    mapUtilityThree.setCircleStyle("-fx-fill: orangered; -fx-stroke: black; -fx-stroke-width: 1");

    mapUtilityLowerTwo.setLineStyle("-fx-stroke: gold; -fx-stroke-width: 4");
    mapUtilityLowerOne.setLineStyle("-fx-stroke: cyan; -fx-stroke-width: 4");
    mapUtilityOne.setLineStyle("-fx-stroke: lime; -fx-stroke-width: 4");
    mapUtilityTwo.setLineStyle("-fx-stroke: hotpink; -fx-stroke-width: 4");
    mapUtilityThree.setLineStyle("-fx-stroke: orangered; -fx-stroke-width: 4");
  }

  public void resetComboboxes() {
    floorLocations =
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
    currentLocationList.setItems(floorLocations);
    destinationList.setItems(floorLocations);
    currentLocationList.setValue("");
    destinationList.setValue("");
  }

  @FXML
  public void displayPath(String from, String to) {
    if (from == null || from.equals("") || to == null || to.equals("")) {
      return;
    }
    refreshPath();

    // Choose the pathfinding method based on the selected radio button
    if (aStarButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("A*");
    }
    if (dfsButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("DFS");
    }
    if (bfsButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("BFS");
    }
    if (dijkstraButton.isSelected()) {
      pf = AbstractPathfinder.getInstance("Dijkstra");
    }

    String toNodeID = SQLRepo.INSTANCE.getNodeIDFromName(to) + "";
    String fromNodeID = SQLRepo.INSTANCE.getNodeIDFromName(from) + "";

    System.out.println("From: " + HospitalNode.allNodes.get(fromNodeID));
    System.out.println("To: " + HospitalNode.allNodes.get(toNodeID));

    List<HospitalNode> path =
        pf.findPath(HospitalNode.allNodes.get(fromNodeID), HospitalNode.allNodes.get(toNodeID));
    if (path == null) {
      System.out.println("Path does not exist");
      return;
    }
    ArrayList<String> pathNames = new ArrayList<>();
    for (HospitalNode node : path) {
      pathNames.add(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(node.getNodeID())));
    }
    // Create the labels
    createDirections(pathBox, path);
    drawPath(path);
    isPathDisplayed = true;
  }

  /**
   * draws the path with lines connecting each node on the map
   *
   * @param path
   */
  private void drawPath(List<HospitalNode> path) {

    // Reset the zoom of gesture panes
    gesturePaneL2.reset();
    gesturePaneL1.reset();
    gesturePane1.reset();
    gesturePane2.reset();
    gesturePane3.reset();

    currentFloor = path.get(0).getFloor();
    MapUtilities currentMapUtility = whichMapUtility(currentFloor);
    Floor startingFloor = currentFloor;

    int startX, startY;
    // create circle to symbolize start
    int x1 = path.get(0).getXCoord();
    int y1 = path.get(0).getYCoord();
    startX = x1;
    startY = y1;
    Circle currentLocationCircle = currentMapUtility.drawStyledCircle(x1, y1, 4);
    currentLocationCircle.setId(path.get(0).getNodeID());
    currentMapUtility.createLabel(x1, y1, 5, 5, "Current Location");

    // draw the lines between each node
    int x2, y2;
    for (int i = 1; i < path.size(); i++) {
      HospitalNode node = path.get(i);
      x2 = node.getXCoord();
      y2 = node.getYCoord();

      Floor newFloor = node.getFloor();
      if (newFloor != currentFloor) {
        Floor oldFloor = currentFloor;
        currentMapUtility.createLabel(x2, y2, "Went to Floor: " + newFloor.toString());
        currentFloor = newFloor;
        currentMapUtility = whichMapUtility(currentFloor);
        currentMapUtility.createLabel(x2, y2, "Came from Floor: " + oldFloor.toString());
      }

      Line pathLine = currentMapUtility.drawStyledLine(x1, y1, x2, y2);
      Circle intermediateCircle = currentMapUtility.drawStyledCircle(x2, y2, 4);
      intermediateCircle.setViewOrder(-1);
      intermediateCircle.setId(node.getNodeID());
      intermediateCircle.setVisible(false);
      x1 = x2;
      y1 = y2;
    }

    // create circle to symbolize end
    Circle endingCircle = currentMapUtility.drawStyledCircle(x1, y1, 4);
    endingCircle.setId(path.get(path.size() - 1).getNodeID());
    endingCircle.toFront();
    currentMapUtility.createLabel(x1, y1, 5, 5, "Destination");

    // Switch the current tab to the same floor as the starting point
    currentFloor = startingFloor;
    tabPane.getSelectionModel().select(floorToTab(startingFloor));
    currentMapUtility = whichMapUtility(currentFloor);
    GesturePane startingPane = ((GesturePane) currentMapUtility.getPane().getParent());

    // Zoom in on the starting node
    startingPane.zoomTo(2, startingPane.targetPointAtViewportCentre());

    // Pan so starting node is centered
    startingPane.centreOn(
        new Point2D(currentMapUtility.convertX(startX), currentMapUtility.convertY(startY)));
  }

  /** removes all the lines in the currentLines list */
  public void refreshPath() {
    currentCircle.setRadius(4);
    mapUtilityLowerTwo.removeAll();
    mapUtilityLowerOne.removeAll();
    mapUtilityOne.removeAll();
    mapUtilityTwo.removeAll();
    mapUtilityThree.removeAll();
    pathBox.getChildren().clear();

    isPathDisplayed = false;
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

  public Tab floorToTab(Floor floor) {
    if (floor == Floor.LOWER_TWO) {
      return lowerLevelTwoTab;
    }
    if (floor == Floor.LOWER_ONE) {
      return lowerLevelOneTab;
    }
    if (floor == Floor.ONE) {
      return floorOneTab;
    }
    if (floor == Floor.TWO) {
      return floorTwoTab;
    }
    if (floor == Floor.THREE) {
      return floorThreeTab;
    }
    return floorOneTab;
  }

  public void adjustGesture(GesturePane oldGesture, GesturePane newGesture) {
    newGesture.centreOn(oldGesture.targetPointAtViewportCentre());
    newGesture.zoomTo(oldGesture.getCurrentScale(), newGesture.targetPointAtViewportCentre());
  }

  public void createDirections(VBox vbox, List<HospitalNode> path) {

    // For each node along the path
    int currentDistance = 0;
    for (int i = 0; i < path.size(); i++) {
      // Get the current node
      HospitalNode currentNode = path.get(i);

      // Get the turn type
      TurnType turnType = getTurn(path, i);
      currentDistance += getDistance(path, i);

      // If the turn type is not straight
      if (turnType != TurnType.STRAIGHT) {
        // Create a direction
        Directions direction = new Directions(path, i, turnType, currentDistance);
        currentDistance = 0;
        // Add the event listener
        direction
            .getHbox()
            .setOnMouseClicked(
                event -> {
                  // reset highlighted node
                  currentCircle.setRadius(4);
                  currentCircle.setViewOrder(-1);
                  currentCircle.setVisible(false);

                  // Set the selected tab to the floor of the node
                  Floor nodeFloor = currentNode.getFloor();
                  tabPane.getSelectionModel().select(floorToTab(nodeFloor));
                  MapUtilities currentMapUtility = whichMapUtility(nodeFloor);
                  GesturePane startingPane =
                      ((GesturePane) currentMapUtility.getPane().getParent());

                  // Outline the hbox
                  direction
                      .getHbox()
                      .setBorder(
                          new Border(
                              new BorderStroke(
                                  Color.web(ColorPalette.LIGHT_BLUE.getHexCode()),
                                  BorderStrokeStyle.SOLID,
                                  CornerRadii.EMPTY,
                                  new BorderWidths(2))));

                  // Remove the previous outline unless previous is null or the same box is clicked
                  // again
                  if (previousLabel != null && previousLabel != direction.getHbox()) {
                    previousLabel.setBorder(Border.EMPTY);
                  }

                  // Zoom in on the starting node
                  startingPane.zoomTo(2, startingPane.targetPointAtViewportCentre());

                  // Pan so starting node is centered
                  startingPane
                      .animate(Duration.millis(200))
                      .centreOn(
                          new Point2D(
                              currentMapUtility.convertX(currentNode.getXCoord()),
                              currentMapUtility.convertY(currentNode.getYCoord())));

                  // get Circle that was selected from label
                  List<Node> nodeList =
                      currentMapUtility.getCurrentNodes().stream()
                          .filter(
                              node -> {
                                try {
                                  return node.getId().equals(currentNode.getNodeID());
                                } catch (NullPointerException n) {
                                  return false;
                                }
                              })
                          .toList();
                  currentCircle = (Circle) nodeList.get(0);
                  currentCircle.setRadius(5);
                  currentCircle.setViewOrder(-5);
                  currentCircle.setVisible(true);

                  // Set the current label as the previous
                  previousLabel = direction.getHbox();
                });
        vbox.getChildren().add(direction.getHbox());
      }
    }
  }

  public TurnType getTurn(List<HospitalNode> path, int index) {
    // Check if the node is the start or the end
    // Start
    if (index == 0) {
      return TurnType.START;
    }
    // End
    if (index == path.size() - 1) {
      return TurnType.END;
    }
    // Check if the node is an elevator or stairs
    // Elevator
    if ((LocationName.NodeType.stringToNodeType(
                SQLRepo.INSTANCE.getNodeTypeFromNodeID(
                    Integer.parseInt(path.get(index).getNodeID())))
            == LocationName.NodeType.ELEV)
        && (path.get(index).getFloor()) != path.get(index + 1).getFloor()) {
      return TurnType.ELEVATOR;
    }
    // Stairs
    if ((LocationName.NodeType.stringToNodeType(
                SQLRepo.INSTANCE.getNodeTypeFromNodeID(
                    Integer.parseInt(path.get(index).getNodeID())))
            == LocationName.NodeType.STAI)
        && (path.get(index).getFloor()) != path.get(index + 1).getFloor()) {
      return TurnType.STAIRS;
    }
    // Straight
    double angle = getTurnAngle(path, index);
    if ((angle > 315 || angle < 45) || (angle < 225 && angle > 135)) {
      return TurnType.STRAIGHT;
    }
    // Right
    if (angle >= 45 && angle <= 135) {
      return TurnType.RIGHT;
    }
    // Left
    if (angle >= 225 && angle <= 315) {
      return TurnType.LEFT;
    } else {
      return TurnType.ERROR;
    }
  }

  /**
   * returns the angle between two intersecting lines at a given position along a path
   *
   * @param path
   * @param index
   * @return
   */
  public double getTurnAngle(List<HospitalNode> path, int index) {
    // Get the nodes
    double startX = path.get(index - 1).getXCoord();
    double startY = path.get(index - 1).getYCoord();
    double endX = path.get(index + 1).getXCoord();
    double endY = path.get(index + 1).getYCoord();
    double fixedX = path.get(index).getXCoord();
    double fixedY = path.get(index).getYCoord();

    // Get the angles
    double angle1 = Math.atan2(startY - fixedY, startX - fixedX);
    double angle2 = Math.atan2(endY - fixedY, endX - fixedX);

    double radian = angle1 - angle2;
    double finalAngle = (radian * 180) / PI;
    // Convert negatives to positives: -10 -> 350
    if (finalAngle < 0) {
      finalAngle += 360;
    }
    return finalAngle;
  }

  public int getDistance(List<HospitalNode> path, int index) {
    // If the node is the starting or ending node, return 0
    if (index == 0 || index == path.size() - 1) {
      return 0;
    }
    // Get the points
    double x1 = path.get(index - 1).getXCoord();
    double y1 = path.get(index - 1).getYCoord();
    double x2 = path.get(index).getXCoord();
    double y2 = path.get(index).getYCoord();
    // Calculate length
    int length = (int) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    return length;
  }

  public void setSVG() {
    // Set the svgs for the zoom in and zoom out buttons
    SVGPath outSVG = new SVGPath();
    outSVG.setContent(
        "M5 10C5 7.23858 7.23858 5 10 5C12.7614 5 15 7.23858 15 10C15 11.381 14.4415 12.6296 13.5355 13.5355C12.6296 14.4415 11.381 15 10 15C7.23858 15 5 12.7614 5 10ZM10 3C6.13401 3 3 6.13401 3 10C3 13.866 6.13401 17 10 17C11.5719 17 13.0239 16.481 14.1921 15.6063L19.2929 20.7071C19.6834 21.0976 20.3166 21.0976 20.7071 20.7071C21.0976 20.3166 21.0976 19.6834 20.7071 19.2929L15.6063 14.1921C16.481 13.0239 17 11.5719 17 10C17 6.13401 13.866 3 10 3ZM8 9C7.44772 9 7 9.44772 7 10C7 10.5523 7.44772 11 8 11H12C12.5523 11 13 10.5523 13 10C13 9.44772 12.5523 9 12 9H8Z");
    zoomOutRegion.setShape(outSVG);
    zoomOutRegion.setStyle("-fx-background-color: 'f1f1f1'");
    SVGPath inSVG = new SVGPath();
    inSVG.setContent(
        "M5 10C5 7.23858 7.23858 5 10 5C12.7614 5 15 7.23858 15 10C15 11.381 14.4415 12.6296 13.5355 13.5355C12.6296 14.4415 11.381 15 10 15C7.23858 15 5 12.7614 5 10ZM10 3C6.13401 3 3 6.13401 3 10C3 13.866 6.13401 17 10 17C11.5719 17 13.0239 16.481 14.1921 15.6063L19.2929 20.7071C19.6834 21.0976 20.3166 21.0976 20.7071 20.7071C21.0976 20.3166 21.0976 19.6834 20.7071 19.2929L15.6063 14.1921C16.481 13.0239 17 11.5719 17 10C17 6.13401 13.866 3 10 3ZM11 8C11 7.44772 10.5523 7 10 7C9.44772 7 9 7.44772 9 8V9H8C7.44772 9 7 9.44772 7 10C7 10.5523 7.44772 11 8 11H9V12C9 12.5523 9.44772 13 10 13C10.5523 13 11 12.5523 11 12V11H12C12.5523 11 13 10.5523 13 10C13 9.44772 12.5523 9 12 9H11V8Z");
    zoomInRegion.setShape(inSVG);
    zoomInRegion.setStyle("-fx-background-color: 'f1f1f1'");
    SVGPath labelSVG = new SVGPath();
    labelSVG.setContent(
        "M19.2933 9.95137L16.96 7.15137C16.6073 6.72814 16.43 6.51639 16.2139 6.36426C16.0223 6.22946 15.8084 6.12953 15.5822 6.06868C15.327 6 15.0523 6 14.5014 6H7.2002C6.08009 6 5.51962 6 5.0918 6.21799C4.71547 6.40973 4.40973 6.71547 4.21799 7.0918C4 7.51962 4 8.08009 4 9.2002V14.8002C4 15.9203 4 16.4801 4.21799 16.9079C4.40973 17.2842 4.71547 17.5905 5.0918 17.7822C5.5192 18 6.07899 18 7.19691 18H14.5014C15.0523 18 15.327 17.9998 15.5822 17.9312C15.8084 17.8703 16.0223 17.7702 16.2139 17.6354C16.43 17.4833 16.6073 17.2721 16.96 16.8488L19.2933 14.0488C19.9006 13.32 20.2036 12.9556 20.3197 12.5488C20.422 12.1902 20.422 11.8095 20.3197 11.4509C20.2036 11.0441 19.9006 10.6801 19.2933 9.95137Z");
    labelRegion.setShape(labelSVG);
    labelRegion.setStyle("-fx-background-color: 'f1f1f1'");
  }
}
