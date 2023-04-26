package edu.wpi.teame.map;

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
  int index, distance;

  public Directions(List<HospitalNode> path, int index, TurnType turnType, int distance) {

    // Set values
    this.path = path;
    this.index = index;
    this.turnType = turnType;
    this.distance = distance;
    this.currentNode = path.get(index);

    // Get the text for the label
    Label destinationLabel;
    // Check if the node is first or last
    if (index == 0) { // first
      destinationLabel = new Label();
    } else if (index == path.size() - 1) { // last
      destinationLabel =
          new Label(SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID())));
    } else { // all other nodes
      // Destination Label
    }

    // Configure hbox
    HBox hBox = new HBox();
    configureHBOX(hBox);
    setDropShadow(hBox);
    setInteractions(hBox);
    setAttributes(hBox);

    // Add children to hbox
    this.hbox = hBox;
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

  public void setAttributes(HBox hBox) {
    Image icon = null;
    Floor nextFloor;
    String directionsText = "In " + distance + "ft ";
    switch (this.turnType) {
      case START:
        icon = new Image(String.valueOf(Main.class.getResource("images/start.png")));
        directionsText =
            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID()));
        break;
      case END:
        icon = new Image(String.valueOf(Main.class.getResource("images/destination.png")));
        directionsText =
            SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID()))
                + " ("
                + distance
                + "ft)";
        break;
      case RIGHT:
        icon = new Image(String.valueOf(Main.class.getResource("images/right_arrow.png")));
        directionsText += "turn right.";
        break;
      case LEFT:
        icon = new Image(String.valueOf(Main.class.getResource("images/left_arrow.png")));
        directionsText += "turn left.";
        break;
      case STRAIGHT:
        icon = new Image(String.valueOf(Main.class.getResource("images/straight_arrow.png")));
        directionsText += "continue straight.";
        break;
      case ELEVATOR:
        nextFloor = path.get(index + 1).getFloor();
        icon = new Image(String.valueOf(Main.class.getResource("images/elevator.png")));
        if (currentNode.getFloor() != nextFloor) {
          directionsText +=
              "take the elevator to floor "
                  + Floor.floorToString(path.get(index + 1).getFloor())
                  + ".";
        } else {
          directionsText = "Floor " + Floor.floorToString(nextFloor);
        }
        break;
      case STAIRS:
        nextFloor = path.get(index + 1).getFloor();
        icon = new Image(String.valueOf(Main.class.getResource("images/stairs.png")));
        if (currentNode.getFloor() != nextFloor) {
          directionsText +=
              "take the stairs to floor "
                  + Floor.floorToString(path.get(index + 1).getFloor())
                  + ".";
        } else {
          directionsText = "Floor " + Floor.floorToString(nextFloor);
        }
        break;
      case ERROR:
        icon = new Image(String.valueOf(Main.class.getResource("images/interrogation.png")));
        directionsText = "ERROR";
        break;
    }
    // Set the image view icon
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

    // Set the label text
    Label destinationLabel = new Label(directionsText);
    destinationLabel.setFont(Font.font("Roboto", 16));
    destinationLabel.setTextAlignment(TextAlignment.CENTER);
    destinationLabel.setWrapText(true);

    // Add the attributes to the HBox
    hBox.getChildren().addAll(pathIcon, line, destinationLabel);
  }
}
