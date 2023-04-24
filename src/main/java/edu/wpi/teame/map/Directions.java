package edu.wpi.teame.map;

import static java.lang.Math.PI;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.Main;
import java.util.List;
import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.Getter;

public class Directions {
  @Getter VBox parent;
  List<HospitalNode> path;
  @Getter HBox hbox;
  @Getter TurnType turnType;
  HospitalNode currentNode;
  int index;

  public enum TurnType {
    RIGHT("turn right."),
    LEFT("turn left."),
    STRAIGHT("continue straight."),
    START("start"),
    END("end"),
    UP("take "),
    ERROR("ERROR");

    private final String turnString;

    TurnType(String turnString) {
      this.turnString = turnString;
    }

    public String getTurnString() {
      return turnString;
    }
  }

  public Directions(VBox parent, List<HospitalNode> path, int index) {

    // Set values
    this.parent = parent;
    this.path = path;
    this.currentNode = path.get(index);
    this.index = index;

    // Get the text for the label
    Label destinationLabel;
    // Check if the node is first or last
    if (index == 0) { // first
      this.turnType = TurnType.START;
      destinationLabel =
          new Label(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID())));
    } else if (index == path.size() - 1) { // last
      this.turnType = TurnType.END;
      destinationLabel =
          new Label(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID())));
    } else { // all other nodes
      // Destination Label
      destinationLabel = new Label("In " + getDistance() + "ft " + getTurn().turnString);
    }

    // Set the image
    Image icon = setImage();
    ImageView pathIcon = new ImageView();
    pathIcon.setImage(icon);
    pathIcon.setPreserveRatio(true);
    pathIcon.setFitWidth(30);

    // Draw the dividing line
    Line line = new Line();
    line.setStartX(0);
    line.setStartY(0);
    line.setEndX(0);
    line.setEndY(50);
    line.setOpacity(0.25);

    destinationLabel.setFont(Font.font("Roboto", 16));
    destinationLabel.setTextAlignment(TextAlignment.CENTER);
    destinationLabel.setWrapText(true);

    // Configure hbox
    HBox hBox = new HBox();
    configureHBOX(hBox);
    setDropShadow(hBox);
    setInteractions(hBox);

    // Add children to hbox
    hBox.getChildren().addAll(pathIcon, line, destinationLabel);
    this.hbox = hBox;

    // Add the hbox to the vbox
    parent.getChildren().add(hBox);
  }

  public void setDropShadow(HBox hBox) {
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
    // Set the drop shadow
    hBox.setEffect(dropShadow);
  }

  public void setInteractions(HBox hBox) {
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
  }

  public void configureHBOX(HBox hBox) {
    hBox.setBackground(
        new Background(new BackgroundFill(Color.web("#D9DAD7"), CornerRadii.EMPTY, Insets.EMPTY)));
    hBox.setPrefHeight(65);
    hBox.setAlignment(Pos.CENTER_LEFT);
    hBox.setSpacing(10);
    hBox.setPadding(new Insets(0, 10, 0, 10));
  }

  public TurnType getTurn() {
    // Straight
    double angle = getTurnAngle(path, index);
    if ((angle > 315 || angle < 45) || (angle < 225 && angle > 135)) {
      this.turnType = TurnType.STRAIGHT;
      return TurnType.STRAIGHT;
    }
    // Right
    if (angle >= 45 && angle <= 135) {
      this.turnType = TurnType.RIGHT;
      return TurnType.RIGHT;
    }
    // Left
    if (angle >= 225 && angle <= 315) {
      this.turnType = TurnType.LEFT;
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

  public int getDistance() {
    // Get the points
    double x1 = path.get(index - 1).getXCoord();
    double y1 = path.get(index - 1).getYCoord();
    double x2 = path.get(index).getXCoord();
    double y2 = path.get(index).getYCoord();
    // Calculate length
    int length = (int) Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    return length;
  }

  public Image setImage() {
    Image icon = null;
    switch (this.turnType) {
      case START:
        icon = new Image(String.valueOf(Main.class.getResource("images/start.png")));
        break;
      case END:
        icon = new Image(String.valueOf(Main.class.getResource("images/destination.png")));
        break;
      case RIGHT:
        icon = new Image(String.valueOf(Main.class.getResource("images/right_arrow.png")));
        break;
      case LEFT:
        icon = new Image(String.valueOf(Main.class.getResource("images/left_arrow.png")));
        break;
      case STRAIGHT:
        icon = new Image(String.valueOf(Main.class.getResource("images/straight_arrow.png")));
        break;
      case ERROR:
        icon = new Image(String.valueOf(Main.class.getResource("images/interrogation.png")));
        break;
    }
    return icon;
  }
}
