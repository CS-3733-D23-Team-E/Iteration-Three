package edu.wpi.teame.controllers.DatabaseEditor;

import edu.wpi.teame.map.Floor;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.utilities.ColorPalette;
import edu.wpi.teame.utilities.MapUtilities;
import java.util.LinkedList;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

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
  @FXML VBox viewMoveBox;

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

  Circle circle1;
  Circle circle2;
  Circle currentCircle;
  HBox previousLabel;

  public MovePreviewController(
      HospitalNode node1, HospitalNode node2, String name1, String name2, boolean bidirectional) {
    this.node1 = node1;
    this.node2 = node2;
    this.name1 = name1;
    this.name2 = name2;
    this.bidirectional = bidirectional;
  }

  @FXML
  public void initialize() {
    System.out.println("Initializing move preview!!");
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
                tabPane.getSelectionModel().select(floorToTab(node1.getFloor()));
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
                tabPane.getSelectionModel().select(floorToTab(node1.getFloor()));
              }
            });

    // set the move description text

    StringBuilder moveDescText = new StringBuilder();
    moveDescText.append(name1).append(" to node ").append(node2.getNodeID()).append("\n");
    if (bidirectional) {
      moveDescText.append(name2).append(" to node ").append(node1.getNodeID()).append("\n");
    }
    moveDescription.setText(moveDescText.toString());


//    List<HospitalNode> nodes = new LinkedList<>();
//    nodes.add(node1);
//    nodes.add(node2);
//    createMoveLabels(viewMoveBox, nodes);
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

    mapUtilityLowerTwo.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F1;");
    mapUtilityLowerOne.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F1;");
    mapUtilityOne.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F1;");
    mapUtilityTwo.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F1;");
    mapUtilityThree.setLabelStyle("-fx-font-size: 10pt; -fx-background-color: #F1F1F1;");
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
    if (node1.getFloor().equals(currentFloor) || node2.getFloor().equals(currentFloor)) {
      whichMapUtility(currentFloor).drawMove(node1, node2);
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

  public void createMoveLabels(VBox vbox, List<HospitalNode> path) {
    for (int i = 0; i < path.size(); i++) {

      HospitalNode currentNode = path.get(i);
      String destination = i == 0 ? name1 : name2;
      System.out.println(destination);

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
}
