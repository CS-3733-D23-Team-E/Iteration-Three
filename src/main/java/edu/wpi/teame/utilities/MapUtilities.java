package edu.wpi.teame.utilities;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.map.HospitalNode;
import edu.wpi.teame.map.LocationName;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javax.swing.*;

public class MapUtilities {
  private final int MAP_X = 5000;
  private final int MAP_Y = 3400;
  private final Pane pane;

  private String lineStyle = "";
  private String circleStyle = "";
  private String labelStyle = "";

  ObservableList<Node> currentNodes = FXCollections.observableArrayList();

  public MapUtilities(Pane pane) {
    this.pane = pane;
  }

  /**
   * draws a circle representing a HospitalNode, and assigns the HospitalNode's nodeID to the ID
   * attribute of the circle
   *
   * @param hospitalNode
   * @return
   */
  public Circle drawHospitalNode(HospitalNode hospitalNode) {

    int x = hospitalNode.getXCoord();
    int y = hospitalNode.getYCoord();
    String nodeID = hospitalNode.getNodeID();

    //    System.out.println("nodetype: " + SQLRepo.INSTANCE.getNodeTypeFromNodeID(105));
    LocationName.NodeType nodeType =
        LocationName.NodeType.stringToNodeType(
            SQLRepo.INSTANCE.getNodeTypeFromNodeID(Integer.parseInt(nodeID)));

    Circle circle = drawCircle(x, y, 5);
    setHospitalNodeColor(circle, nodeType);

    circle.setId(nodeID);
    return circle;
  }

  private void setHospitalNodeColor(Circle circle, LocationName.NodeType nodeType) {
    switch (nodeType) {
      case HALL:
        circle.setFill(Color.BLACK);
        break;
      case CONF:
        circle.setFill(Color.GRAY);
        break;
      case DEPT:
        circle.setFill(Color.WHITE);
        break;
      case INFO:
        circle.setFill(Color.VIOLET);
        break;
      case SERV:
        circle.setFill(Color.PURPLE);
        break;
      case LABS:
        circle.setFill(Color.GREEN);
        break;
      case RETL:
        circle.setFill(Color.YELLOW);
        break;
      case STAI:
        circle.setFill(Color.ORANGE);
        break;
      case ELEV:
        circle.setFill(Color.STEELBLUE);
        break;
      case REST:
        circle.setFill(Color.BLUE);
        break;
      case BATH:
        circle.setFill(Color.AQUA);
        return;
      case EXIT:
        circle.setFill(Color.RED);
        break;
    }
  }

  /**
   * draws a circle representing a HospitalNode, and assigns the HospitalNode's nodeID to the ID
   * attribute of the circle
   *
   * @param hospitalNode
   * @return
   */
  public Label drawHospitalNodeLabel(HospitalNode hospitalNode) {

    int x = hospitalNode.getXCoord();
    int y = hospitalNode.getYCoord();
    String nodeID = hospitalNode.getNodeID();
    String longName = SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(nodeID));

    Label label = createStyledLabel(x, y, 10, 10, longName);
    label.setId("label" + hospitalNode.getNodeID());
    return label;
  }

  /**
   * OVERLOADED: draws a given message associated with a hospitalNode's location (ie "went to floor
   * 4")
   *
   * @param hospitalNode
   * @param message
   * @return
   */
  public Label drawHospitalNodeLabel(HospitalNode hospitalNode, String message) {
    int x = hospitalNode.getXCoord();
    int y = hospitalNode.getYCoord();
    String nodeID = hospitalNode.getNodeID();

    Label label = createStyledLabel(x, y, 10, 10, message);
    label.setId("label" + hospitalNode.getNodeID());
    return label;
  }

  /**
   * Draws the edge between two given nodes
   *
   * @param node
   * @param neighbor
   */
  public Line drawEdge(HospitalNode node, HospitalNode neighbor) {
    int x1 = node.getXCoord();
    int y1 = node.getYCoord();
    int x2 = neighbor.getXCoord();
    int y2 = neighbor.getYCoord();

    return drawLine(x1, y1, x2, y2);
  }

  public Line drawMove(HospitalNode from, HospitalNode to) {
    int x1 = from.getXCoord();
    int y1 = from.getYCoord();
    int x2 = to.getXCoord();
    int y2 = to.getYCoord();

    String message = from.getNodeID() + " to " + to.getNodeID();

    return drawLineWithLabel(x1, y1, x2, y2, message);
  }

  /**
   * draws a stylized version of the line determined by the lineStyle attribute
   *
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @return
   */
  public Line drawStyledLine(int x1, int y1, int x2, int y2) {
    Line line = this.drawLine(x1, y1, x2, y2);
    line.setStyle(lineStyle);
    return line;
  }

  /**
   * draws a line given the starting and ending (x,y) coordinates and saves the line to a global
   * list of lines
   */
  public Line drawLine(int x1, int y1, int x2, int y2) {
    x1 = (int) convertX(x1);
    y1 = (int) convertY(y1);
    x2 = (int) convertX(x2);
    y2 = (int) convertY(y2);

    Line line = new Line(x1, y1, x2, y2);
    addShape(line);
    return line;
  }

  /**
   * draws a line with a given label over it
   *
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @param message
   * @return
   */
  public Line drawLineWithLabel(int x1, int y1, int x2, int y2, String message) {
    int ax = (x1 + x2) / 2;
    int ay = (y1 + y2) / 2;
    Label lineMessage = this.createStyledLabel(ax, ay, message);
    return drawLine(x1, y1, x2, y2);
  }

  /**
   * draws a ring by creating two circles, returns the outer circle, has parameters for colors
   *
   * @param x
   * @param y
   * @param radius
   * @param thickness
   * @param innerColor
   * @param outerColor
   * @return
   */
  public Circle drawRing(
      int x, int y, int radius, int thickness, Color innerColor, Color outerColor) {
    x = (int) convertX(x);
    y = (int) convertY(y);

    Circle innerCircle = new Circle(x, y, radius - thickness, innerColor);
    Circle outerCircle = new Circle(x, y, radius, outerColor);
    addShape(outerCircle);
    addShape(innerCircle);

    return outerCircle;
  }

  /**
   * draws a ring by creating two circles, colored white with a black edge
   *
   * @param x
   * @param y
   * @param radius
   * @param thickness
   */
  public void drawRing(int x, int y, int radius, int thickness) {
    x = (int) convertX(x);
    y = (int) convertY(y);

    Circle innerCircle = new Circle(x, y, radius - thickness, Color.WHITE);
    Circle outerCircle = new Circle(x, y, radius, Color.BLACK);
    addShape(innerCircle);
    addShape(outerCircle);
  }

  /**
   * draws a circle stylized by the circleStyle attribute
   *
   * @param x
   * @param y
   * @param radius
   * @return
   */
  public Circle drawStyledCircle(int x, int y, int radius) {
    Circle circle = this.drawCircle(x, y, radius);
    circle.setStyle(circleStyle);
    return circle;
  }

  /**
   * draws a circle given an x, y, and radius
   *
   * @param x
   * @param y
   * @param radius
   * @return
   */
  public Circle drawCircle(int x, int y, int radius) {
    x = (int) convertX(x);
    y = (int) convertY(y);

    Circle circle = new Circle(x, y, radius, Color.BLACK);
    addShape(circle);
    return circle;
  }

  /**
   * creates a circle at x, y, with a specified radius, but has customizable color
   *
   * @param x
   * @param y
   * @param radius
   * @param color
   * @return
   */
  public Circle drawCircle(int x, int y, int radius, Color color) {
    Circle circle = this.drawCircle(x, y, radius);
    circle.setFill(color);
    return circle;
  }

  /**
   * creates a label given x, y, and the label text
   *
   * @param x
   * @param y
   * @param text
   * @return
   */
  public Label createLabel(int x, int y, String text) {
    Label label = new Label(text);
    label.setLayoutX(convertX(x));
    label.setLayoutY(convertY(y));

    addShape(label);

    return label;
  }

  /**
   * creates a label offset from the x by xOffset and offset from the y by yOffset
   *
   * @param x
   * @param y
   * @param xOffset
   * @param yOffset
   * @param text
   * @return
   */
  public Label createLabel(int x, int y, int xOffset, int yOffset, String text) {
    Label label = this.createLabel(x + xOffset, y + yOffset, text);
    return label;
  }

  /**
   * @param x
   * @param y
   * @param text
   * @return
   */
  public Label createStyledLabel(int x, int y, String text) {
    Label label = this.createLabel(x, y, text);
    label.setStyle(labelStyle);
    return label;
  }

  public Label createStyledLabel(int x, int y, int xOffset, int yOffset, String text) {
    Label label = this.createLabel(x, y, xOffset, yOffset, text);
    label.setStyle(labelStyle);
    return label;
  }

  public double convertY(int yCoord) {
    return ImageCoordToPane(yCoord, MAP_Y, pane.getHeight());
  }

  public double convertX(int xCoord) {
    return ImageCoordToPane(xCoord, MAP_X, pane.getWidth());
  }

  /**
   * @param coord
   * @param mapWidth
   * @return
   */
  private double ImageCoordToPane(int coord, int mapWidth, double paneWidth) {
    return coord * (paneWidth / mapWidth);
  }

  public double PaneXToImageX(double coord) {
    double paneWidth = this.pane.getWidth();
    return coord * (MAP_X / paneWidth);
  }

  public double PaneYToImageY(double coord) {
    double paneWidth = this.pane.getHeight();
    return coord * (MAP_Y / paneWidth);
  }

  /**
   * adds the given node to a list of nodes on the pane that were drawn
   *
   * @param node
   */
  private void addShape(Node node) {
    pane.getChildren().add(node);
    currentNodes.add(node);
  }

  /**
   * removes a specified node from the pane
   *
   * @param node
   */
  public void removeNode(Node node) {
    pane.getChildren().remove(node);
  }

  /**
   * TODO removes all nodes of a given Class from the pane
   *
   * @param obj
   */
  public void removeAllByType(Class obj) {
    System.out.println(obj);
    System.out.println("remove1 :" + pane.getChildren());
    this.pane.getChildren().removeAll(filterShapes(obj));
    System.out.println("remove2 :" + pane.getChildren());
  }

  /** removes all nodes drawn by the mapUtility on the pane */
  public void removeAll() {
    this.pane.getChildren().removeAll(currentNodes);
    this.currentNodes.clear();
  }

  /**
   * returns a list of the nodes in currentNodes filtered by class type
   *
   * @param obj
   * @return
   */
  public ObservableList<Node> filterShapes(Class obj) {
    ObservableList<Node> result = currentNodes;
    result.removeIf(s -> (s.getClass() != obj));
    return result;
  }

  public void setLineStyle(String lineStyle) {
    this.lineStyle = lineStyle;
  }

  public void setCircleStyle(String circleStyle) {
    this.circleStyle = circleStyle;
  }

  public void setLabelStyle(String labelStyle) {
    this.labelStyle = labelStyle;
  }

  public ObservableList<Node> getCurrentNodes() {
    return currentNodes;
  }

  public Pane getPane() {
    return pane;
  }
}
