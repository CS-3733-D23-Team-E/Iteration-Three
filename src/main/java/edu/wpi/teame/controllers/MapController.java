package edu.wpi.teame.controllers;

import static javafx.scene.paint.Color.WHITE;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.Main;
import edu.wpi.teame.map.Floor;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.LocationName;
import edu.wpi.teame.map.pathfinding.AbstractPathfinder;
import edu.wpi.teame.utilities.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
  @FXML MFXButton menuButton;
  @FXML MFXButton menuBarHome;
  @FXML MFXButton menuBarServices;
  @FXML MFXButton menuBarSignage;
  @FXML MFXButton menuBarMaps;
  @FXML MFXButton menuBarDatabase;
  @FXML MFXButton menuBarBlank;
  @FXML MFXButton menuBarExit;
  @FXML VBox menuBar;
  @FXML MFXButton startButton;
  @FXML ImageView mapImageLowerTwo; // Floor L2
  @FXML ImageView mapImageLowerOne; // Floor L1
  @FXML ImageView mapImageOne; // Floor 1
  @FXML ImageView mapImageTwo; // Floor 2
  @FXML ImageView mapImageThree; // Floor 3
  @FXML RadioButton aStarButton;
  @FXML RadioButton dfsButton;
  @FXML RadioButton bfsButton;
  @FXML RadioButton dijkstraButton;
  @FXML GesturePane gesturePaneL2;
  @FXML GesturePane gesturePaneL1;
  @FXML GesturePane gesturePane1;
  @FXML GesturePane gesturePane2;
  @FXML GesturePane gesturePane3;
  @FXML ImageView homeI;
  @FXML ImageView servicesI;
  @FXML ImageView signageI;
  @FXML ImageView pathfindingI;
  @FXML ImageView databaseI;
  @FXML ImageView exitI;
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

    // Initially set the menu bar to invisible
    menuBar.setVisible(false);

    // When the menu button is clicked, invert the value of menuVisibility and set the menu bar to
    // that value
    // (so each time the menu button is clicked it changes the visibility of menu bar back and
    // forth)
    menuButton.setOnMouseClicked(
        event -> {
          menuBar.setVisible(!menuBar.isVisible());
        });

    // Navigation controls for the button in the menu bar
    menuBarHome.setOnMouseClicked(event -> Navigation.navigate(Screen.HOME));
    menuBarServices.setOnMouseClicked(
        event -> {
          Navigation.navigate(Screen.SERVICE_REQUESTS);
          menuBar.setVisible(!menuBar.isVisible());
        });
    menuBarSignage.setOnMouseClicked(event -> Navigation.navigate(Screen.SIGNAGE_TEXT));
    menuBarMaps.setOnMouseClicked(event -> Navigation.navigate(Screen.MAP));
    menuBarDatabase.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE_EDITOR));
    menuBarExit.setOnMouseClicked((event -> Platform.exit()));

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
        menuBarExit,
        "baseline-center",
        exitI,
        "images/sign-out-alt.png",
        "images/sign-out-alt-blue.png");

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

    System.out.println(HospitalNode.allNodes.get(fromNodeID));
    System.out.println(HospitalNode.allNodes.get(toNodeID));

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
    createPathLabels(pathBox, path);
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

  private void mouseSetup(MFXButton btn) {
    btn.setOnMouseEntered(
        event -> {
          btn.setStyle(
              "-fx-background-color: #f1f1f1; -fx-alignment: center; -fx-border-color: #192d5a; -fx-border-width: 2;");
          btn.setTextFill(Color.web("#192d5aff", 1.0));
        });
    btn.setOnMouseExited(
        event -> {
          btn.setStyle("-fx-background-color: #192d5aff; -fx-alignment: center;");
          btn.setTextFill(WHITE);
        });
  }

  public void adjustGesture(GesturePane oldGesture, GesturePane newGesture) {
    newGesture.centreOn(oldGesture.targetPointAtViewportCentre());
    newGesture.zoomTo(oldGesture.getCurrentScale(), newGesture.targetPointAtViewportCentre());
  }

  public void createPathLabels(VBox vbox, List<HospitalNode> path) {
    for (int i = 0; i < path.size(); i++) {

      HospitalNode currentNode = path.get(i);
      String destination =
          SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID()));

      // Image
      Image icon;
      ImageView pathIcon = new ImageView();
      if (i == 0) {
        icon = new Image(String.valueOf(Main.class.getResource("images/start.png")));
      } else if (i == path.size() - 1) {
        icon = new Image(String.valueOf(Main.class.getResource("images/destination.png")));
      } else {
        icon = new Image(String.valueOf(Main.class.getResource("images/right-arrow.png")));
      }
      pathIcon.setImage(icon);
      pathIcon.setPreserveRatio(true);
      pathIcon.setFitWidth(30);

      // Line
      Line line = new Line();
      line.setStartX(0);
      line.setStartY(0);
      line.setEndX(0);
      line.setEndY(50);
      line.setOpacity(0.25);

      // Destination Label
      Label destinationLabel = new Label(destination);
      destinationLabel.setFont(Font.font("Roboto", 16));
      destinationLabel.setTextAlignment(TextAlignment.CENTER);
      destinationLabel.setWrapText(true);

      // Drop Shadow
      DropShadow dropShadow = new DropShadow();
      dropShadow.setBlurType(BlurType.THREE_PASS_BOX);
      dropShadow.setWidth(21);
      dropShadow.setHeight(21);
      dropShadow.setRadius(4);
      dropShadow.setOffsetX(-4);
      dropShadow.setOffsetY(4);
      dropShadow.setSpread(0);
      dropShadow.setColor(new Color(0, 0, 0, 0.25));

      // HBox
      HBox hBox = new HBox();
      hBox.setBackground(
          new Background(
              new BackgroundFill(Color.web("#D9DAD7"), CornerRadii.EMPTY, Insets.EMPTY)));
      hBox.setPrefHeight(65);
      hBox.setEffect(dropShadow);
      hBox.setAlignment(Pos.CENTER_LEFT);
      hBox.setSpacing(10);
      hBox.setPadding(new Insets(0, 10, 0, 10));
      hBox.getChildren().addAll(pathIcon, line, destinationLabel);

      // Add the event listener
      hBox.setOnMouseClicked(
          event -> {
            Floor nodeFloor = currentNode.getFloor();

            // reset highlighted node
            currentCircle.setRadius(4);
            currentCircle.setViewOrder(-1);
            System.out.println("oldcircle: " + currentCircle.getId());

            tabPane.getSelectionModel().select(floorToTab(nodeFloor));
            MapUtilities currentMapUtility = whichMapUtility(nodeFloor);
            GesturePane startingPane = ((GesturePane) currentMapUtility.getPane().getParent());

            // Outline the hbox
            hBox.setBorder(
                new Border(
                    new BorderStroke(
                        Color.web(ColorPalette.LIGHT_BLUE.getHexCode()),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(2))));

            // Remove the previous outline unless previous is null or the same box is clicked again
            if (previousLabel != null && previousLabel != hBox) {
              previousLabel.setBorder(Border.EMPTY);
            }

            // Zoom in on the starting node
            startingPane.zoomTo(2, startingPane.targetPointAtViewportCentre());

            // Pan so starting node is centered
            startingPane.centreOn(
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
            System.out.println("Newcircle: " + currentCircle.getId());
            currentCircle.setRadius(9);
            currentCircle.setViewOrder(-5);
            System.out.println("currentCircle: " + currentCircle);
            System.out.println("Node List: " + nodeList);

            // Set the current label as the previous
            previousLabel = hBox;
          });

      // Make the box bigger when hovering
      hBox.setOnMouseEntered(
          event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200));
            scaleTransition.setNode(hBox);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
          });
      // Smaller on exit
      hBox.setOnMouseExited(
          event -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200));
            scaleTransition.setNode(hBox);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.play();
          });

      // Add path label to VBox
      vbox.getChildren().add(hBox);
    }
  }
}
