package edu.wpi.teame.map;

import edu.wpi.teame.Database.SQLRepo;
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
  @Getter HBox hbox;
  Image icon;
  double rotation;

  public Directions(
      VBox parent, List<HospitalNode> path, HospitalNode currentNode, Image icon, double rotation) {

    // Set values
    this.parent = parent;
    this.icon = icon;
    this.rotation = rotation;

    // Set the image
    ImageView pathIcon = new ImageView();
    pathIcon.setImage(icon);
    pathIcon.setPreserveRatio(true);
    pathIcon.setFitWidth(30);
    pathIcon.setRotate(rotation);

    // Draw the dividing line
    Line line = new Line();
    line.setStartX(0);
    line.setStartY(0);
    line.setEndX(0);
    line.setEndY(50);
    line.setOpacity(0.25);

    // Get the destination
    String destination =
        SQLRepo.INSTANCE.getNamefromNodeID(Integer.parseInt(currentNode.getNodeID()));

    // Destination Label
    Label destinationLabel = new Label(destination);
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
}
