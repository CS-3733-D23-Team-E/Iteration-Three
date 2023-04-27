package edu.wpi.teame.controllers;

import edu.wpi.teame.entities.Settings;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class SettingsPageController {

  @FXML MFXButton englishButton;
  @FXML MFXButton spanishButton;
  @FXML MFXButton frenchButton;

  @FXML MFXButton hawaiianButton;

  @FXML
  public void initialize() {

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10);
    dropShadow.setSpread(.71);
    dropShadow.setWidth(21);
    dropShadow.setHeight(50);
    Color paint = new Color(0.0, 0.6175, 0.65, 0.5);

    englishButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.ENGLISH);
          englishButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          hawaiianButton.setEffect(null);
          frenchButton.setEffect(null);
        });
    spanishButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.SPANISH);
          spanishButton.setEffect(dropShadow);
          englishButton.setEffect(null);
          hawaiianButton.setEffect(null);
          frenchButton.setEffect(null);
        });
    hawaiianButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.HAWAIIAN);
          hawaiianButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          englishButton.setEffect(null);
          frenchButton.setEffect(null);
        });
    frenchButton.setOnMouseClicked(
        event -> {
          Settings.INSTANCE.setLanguage(Settings.Language.FRENCH);
          frenchButton.setEffect(dropShadow);
          spanishButton.setEffect(null);
          hawaiianButton.setEffect(null);
          englishButton.setEffect(null);
        });
  }
}
