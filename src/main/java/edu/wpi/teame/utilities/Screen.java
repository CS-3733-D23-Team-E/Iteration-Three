package edu.wpi.teame.utilities;

public enum Screen {
  ROOT("views/Root.fxml"),
  HOME("views/HomePage.fxml"),

  SERVICE_REQUESTS("views/ServiceRequestPage.fxml"),

  SIGNAGE_TEXT("views/SignagePage.fxml"),
  MEAL_REQUEST("views/MealRequest.fxml"),
  FLOWER_REQUEST("views/FlowerRequest.fxml"),
  OFFICE_SUPPLIES_REQUEST("views/OfficeSuppliesRequest.fxml"),
  MAP("views/Map.fxml"),
  DATABASE_EDITOR("views/DatabaseEditor/DatabaseEditor.fxml"),
  MOVE_COMPONENT("views/DatabaseEditor/MoveComponent.fxml"),
  DATABASE_TABLEVIEW("views/DatabaseEditor/DatabaseTableView.fxml"),
  DATABASE_MAPVIEW("views/DatabaseEditor/DatabaseMapView.fxml"),
  DATABASE_SERVICEVIEW("views/DatabaseEditor/DatabaseServiceRequestView.fxml"),
  DATABASE_EMPLOYEEVIEW("views/DatabaseEditor/DatabaseEmployeeView.fxml");

  private final String filename;

  Screen(String filename) {
    this.filename = filename;
  }

  public String getFilename() {
    return filename;
  }
}
