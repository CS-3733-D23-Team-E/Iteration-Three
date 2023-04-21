package edu.wpi.teame.Database;

import edu.wpi.teame.Main;
import edu.wpi.teame.entities.*;
import edu.wpi.teame.map.*;
import java.sql.*;
import java.util.List;
import java.util.NoSuchElementException;

public enum SQLRepo {
  INSTANCE;

  public enum Table {
    LOCATION_NAME,
    MOVE,
    NODE,
    EDGE,
    SERVICE_REQUESTS,
    EMPLOYEE,
    OFFICE_SUPPLY,
    MEAL_REQUESTS,
    FLOWER_REQUESTS,
    FURNITURE_REQUESTS,
    CONFERENCE_ROOM;
    ;

    public static String tableToString(Table tb) {
      switch (tb) {
        case LOCATION_NAME:
          return "LocationName";
        case MOVE:
          return "Move";
        case NODE:
          return "Node";
        case EDGE:
          return "Edge";
        case SERVICE_REQUESTS:
          return "ServiceRequests";
        case EMPLOYEE:
          return "Employee";
        case OFFICE_SUPPLY:
          return "OfficeSupplies";
        case MEAL_REQUESTS:
          return "MealService";
        case FLOWER_REQUESTS:
          return "FlowerService";
        case CONFERENCE_ROOM:
          return "ConfRoomService";
        case FURNITURE_REQUESTS:
          return "FurnitureService";
        default:
          throw new NoSuchElementException("No such Table found");
      }
    }
  }

  Connection activeConnection;
  DAO<HospitalNode> nodeDAO;
  DAO<HospitalEdge> edgeDAO;
  DAO<MoveAttribute> moveDAO;
  DAO<LocationName> locationDAO;
  EmployeeDAO employeeDAO;
  DatabaseUtility dbUtility;
  ServiceDAO<FurnitureRequestData> furnitureDAO;
  ServiceDAO<OfficeSuppliesData> officesupplyDAO;
  ServiceDAO<MealRequestData> mealDAO;
  ServiceDAO<FlowerRequestData> flowerDAO;
  ServiceDAO<ConferenceRequestData> conferenceDAO;

  public Employee connectToDatabase(String username, String password) {
    try {
      Class.forName("org.postgresql.Driver");
      activeConnection =
          DriverManager.getConnection(
              "jdbc:postgresql://database.cs.wpi.edu:5432/teamedb", "teame", "teame50");
      employeeDAO = new EmployeeDAO(activeConnection);
      Employee loggedIn = employeeDAO.verifyLogIn(username, password);
      if (loggedIn == null) {
        return null;
      } else {
        nodeDAO = new NodeDAO(activeConnection);
        edgeDAO = new EdgeDAO(activeConnection);
        moveDAO = new MoveDAO(activeConnection);
        locationDAO = new LocationDAO(activeConnection);
        dbUtility = new DatabaseUtility(activeConnection);
        officesupplyDAO = new OfficeSuppliesDAO(activeConnection);
        mealDAO = new MealDAO(activeConnection);
        flowerDAO = new FlowerDAO(activeConnection);
        conferenceDAO = new ConferenceRoomDAO(activeConnection);
        furnitureDAO = new FurnitureDAO(activeConnection);

        Employee.setActiveEmployee(loggedIn);

        return loggedIn;
      }
    } catch (SQLException e) {
      exitDatabaseProgram();
      throw new RuntimeException("Your username or password is incorrect");
    } catch (ClassNotFoundException e) {
      exitDatabaseProgram();
      throw new RuntimeException("Sorry something went wrong please try again");
    }
  }

  public void exitDatabaseProgram() {
    try {
      activeConnection.close();
      System.out.println("Database Connection Closed");
    } catch (Exception e) {
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  // DatabaseReset
  public void resetDatabase() {

    String node = Main.class.getResource("Data/NewData/Node.csv").getFile().replaceAll("%20", " ");
    String edge = Main.class.getResource("Data/NewData/Edge.csv").getFile().replaceAll("%20", " ");
    String move = Main.class.getResource("Data/NewData/Move.csv").getFile().replaceAll("%20", " ");
    String location =
        Main.class.getResource("Data/NewData/LocationName.csv").getFile().replaceAll("%20", " ");
    this.importFromCSV(Table.NODE, node);
    this.importFromCSV(Table.EDGE, edge);
    this.importFromCSV(Table.LOCATION_NAME, location);
    this.importFromCSV(Table.MOVE, move);
  }

  // ALL DATABASE UTILITY
  public void handleDiffs(List<Diff<?>> changes){
    this.dbUtility.handleDiffs(changes);
  }

  public int getNodeIDFromName(String longName) {
    return this.dbUtility.getNodeIDFromName(longName);
  }

  public String getNamefromNodeID(int nodeID) {
    return this.dbUtility.getNameFromNodeID(nodeID);
  }

  public void updateUsingNodeID(
      String nodeID, String oldLongName, String columnName, String value) {
    this.dbUtility.updateMoveWithoutObject(nodeID, oldLongName, columnName, value);
  }

  public String getShortNameFromNodeID(String nodeID) throws SQLException {
    return this.dbUtility.getShortNameFromNodeID(nodeID);
  }

  public String getNodeTypeFromNodeID(int nodeID) {
    return this.dbUtility.getNodeTypeFromNodeID(nodeID);
  }

  public List<HospitalNode> getNodesFromFloor(Floor fl) {
    return this.dbUtility.getNodesFromFloor(fl);
  }

  public List<MoveAttribute> getMoveAttributeFromFloor(Floor fl) {
    return this.dbUtility.getMoveAttributeFromFloor(fl);
  }

  public List<String> getLongNamesFromMove(List<MoveAttribute> mv) {
    return this.dbUtility.getLongNamesFromMove(mv);
  }

  public List<String> getLongNamesFromLocationName(List<LocationName> ln) {
    return this.dbUtility.getLongNamesFromLocationName(ln);
  }

  // ALL IMPORTS FOR DAOS
  public void importFromCSV(Table table, String filepath) {
    try {
      switch (table) {
        case MOVE:
          this.moveDAO.importFromCSV(filepath, "Move");
          break;
        case EDGE:
          this.edgeDAO.importFromCSV(filepath, "Edge");
          break;
        case NODE:
          this.nodeDAO.importFromCSV(filepath, "Node");
          break;
        case LOCATION_NAME:
          this.locationDAO.importFromCSV(filepath, "LocationName");
          break;
        case EMPLOYEE:
          this.employeeDAO.importFromCSV(filepath, "Employee");
          break;
        case OFFICE_SUPPLY:
          this.officesupplyDAO.importFromCSV(filepath, "OfficeSupplies");
          break;
        case MEAL_REQUESTS:
          this.mealDAO.importFromCSV(filepath, "MealService");
          break;
        case CONFERENCE_ROOM:
          this.conferenceDAO.importFromCSV(filepath, "ConfRoomService");
          break;
        case FLOWER_REQUESTS:
          this.flowerDAO.importFromCSV(filepath, "FlowerService");
          break;
        case FURNITURE_REQUESTS:
          this.furnitureDAO.importFromCSV(filepath, "FurnitureService");
          break;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // ALL EXPORTS FOR DAOS
  public void exportToCSV(Table table, String filepath, String tableName) {
    try {
      switch (table) {
        case MOVE:
          this.moveDAO.exportToCSV(filepath, tableName);
          break;
        case EDGE:
          this.edgeDAO.exportToCSV(filepath, tableName);
          break;
        case NODE:
          this.nodeDAO.exportToCSV(filepath, tableName);
          break;
        case LOCATION_NAME:
          this.locationDAO.exportToCSV(filepath, tableName);
          break;
        case EMPLOYEE:
          this.employeeDAO.exportToCSV(filepath, tableName);
          break;
        case OFFICE_SUPPLY:
          this.officesupplyDAO.exportToCSV(filepath, tableName);
          break;
        case MEAL_REQUESTS:
          this.mealDAO.exportToCSV(filepath, tableName);
          break;
        case CONFERENCE_ROOM:
          this.conferenceDAO.exportToCSV(filepath, tableName);
          break;
        case FLOWER_REQUESTS:
          this.flowerDAO.exportToCSV(filepath, tableName);
          break;
        case FURNITURE_REQUESTS:
          this.furnitureDAO.exportToCSV(filepath, tableName);
          break;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  // ALL GETS FOR DAOS

  public List<HospitalNode> getNodeList() {
    return this.nodeDAO.get();
  }

  public List<HospitalEdge> getEdgeList() {
    return this.edgeDAO.get();
  }

  public List<LocationName> getLocationList() {
    return this.locationDAO.get();
  }

  public List<MoveAttribute> getMoveList() {
    return this.moveDAO.get();
  }

  public List<Employee> getEmployeeList() {
    return this.employeeDAO.get();
  }

  public List<OfficeSuppliesData> getOfficeSupplyList() {
    return this.officesupplyDAO.get();
  }

  public List<MealRequestData> getMealRequestsList() {
    return this.mealDAO.get();
  }

  public List<ConferenceRequestData> getConfList() {
    return this.conferenceDAO.get();
  }

  public List<FlowerRequestData> getFlowerRequestsList() {
    return this.flowerDAO.get();
  }

  public List<FurnitureRequestData> getFurnitureRequestsList() {
    return this.furnitureDAO.get();
  }

  // ALL UPDATES FOR DAOS

  public void updateNode(HospitalNode obj, String attribute, String value) {
    this.nodeDAO.update(obj, attribute, value);
  }

  public void updateEdge(HospitalEdge obj, String attribute, String value) {
    this.edgeDAO.update(obj, attribute, value);
  }

  public void updateMove(MoveAttribute obj, String attribute, String value) {
    this.moveDAO.update(obj, attribute, value);
  }

  public void updateLocation(LocationName obj, String attribute, String value) {
    this.locationDAO.update(obj, attribute, value);
  }

  public void updateServiceRequest(ServiceRequestData obj, String attribute, String value) {
    if (obj instanceof OfficeSuppliesData) {
      OfficeSuppliesData updateSupplies = (OfficeSuppliesData) obj;
      this.officesupplyDAO.update(updateSupplies, attribute, value);
    } else if (obj instanceof MealRequestData) {
      MealRequestData updateMeal = (MealRequestData) obj;
      this.mealDAO.update(updateMeal, attribute, value);
    } else if (obj instanceof FlowerRequestData) {
      FlowerRequestData updateFlower = (FlowerRequestData) obj;
      this.flowerDAO.update(updateFlower, attribute, value);
    } else if (obj instanceof FurnitureRequestData) {
      FurnitureRequestData updateFurniture = (FurnitureRequestData) obj;
      this.furnitureDAO.update(updateFurniture, attribute, value);
    } else if (obj instanceof ConferenceRequestData) {
      ConferenceRequestData updateConf = (ConferenceRequestData) obj;
      this.conferenceDAO.update(updateConf, attribute, value);
    } else {
      throw new NoSuchElementException("No Service Request of this type");
    }
  }

  public void updateOfficeSupply(OfficeSuppliesData obj, String attribute, String value) {
    this.officesupplyDAO.update(obj, attribute, value);
  }

  public void updateMealRequest(MealRequestData obj, String attribute, String value) {
    this.mealDAO.update(obj, attribute, value);
  }

  public void updateConfRoomRequest(ConferenceRequestData obj, String attribute, String value) {
    this.conferenceDAO.update(obj, attribute, value);
  }

  public void updateFurnitureRequest(FurnitureRequestData obj, String attribute, String value) {
    this.furnitureDAO.update(obj, attribute, value);
  }

  public void updateFlowerRequest(FlowerRequestData obj, String attribute, String value) {
    this.flowerDAO.update(obj, attribute, value);
  }

  public void updateEmployee(Employee obj, String attribute, String value) {
    this.employeeDAO.update(obj, attribute, value);
  }

  // ALL DELETES FOR DAOS
  public void deleteServiceRequest(ServiceRequestData obj) {
    if (obj instanceof OfficeSuppliesData) {
      OfficeSuppliesData deleteSupplies = (OfficeSuppliesData) obj;
      this.officesupplyDAO.delete(deleteSupplies);
    } else if (obj instanceof MealRequestData) {
      MealRequestData deleteMeal = (MealRequestData) obj;
      this.mealDAO.delete(deleteMeal);
    } else if (obj instanceof FlowerRequestData) {
      FlowerRequestData deleteFlower = (FlowerRequestData) obj;
      this.flowerDAO.delete(deleteFlower);
    } else if (obj instanceof FurnitureRequestData) {
      FurnitureRequestData deleteFurniture = (FurnitureRequestData) obj;
      this.furnitureDAO.delete(deleteFurniture);
    } else if (obj instanceof ConferenceRequestData) {
      ConferenceRequestData deleteConf = (ConferenceRequestData) obj;
      this.conferenceDAO.delete(deleteConf);
    } else {
      throw new NoSuchElementException("No Service Request of this type");
    }
  }

  public void deleteOfficeSupplyRequest(OfficeSuppliesData obj) {
    this.officesupplyDAO.delete(obj);
  }

  public void deleteMealRequest(MealRequestData obj) {
    this.mealDAO.delete(obj);
  }

  public void deleteConfRoomRequest(ConferenceRequestData obj) {
    this.conferenceDAO.delete(obj);
  }

  public void deleteFurnitureRequest(FurnitureRequestData obj) {
    this.furnitureDAO.delete(obj);
  }

  public void deleteFlowerRequest(FlowerRequestData obj) {
    this.flowerDAO.delete(obj);
  }

  public void deletenode(HospitalNode obj) {
    this.nodeDAO.delete(obj);
  }

  public void deleteEdge(HospitalEdge obj) {
    this.edgeDAO.delete(obj);
  }

  public void deleteLocation(LocationName obj) {
    this.locationDAO.delete(obj);
  }

  public void deleteMove(MoveAttribute obj) {
    this.moveDAO.delete(obj);
  }

  public void deleteEmployee(Employee obj) {
    this.employeeDAO.delete(obj);
  }

  // ALL ADDITIONS TO DAOS
  public void addServiceRequest(ServiceRequestData obj) {
    if (obj instanceof OfficeSuppliesData) {
      OfficeSuppliesData addSupplies = (OfficeSuppliesData) obj;
      this.officesupplyDAO.add(addSupplies);
    } else if (obj instanceof MealRequestData) {
      MealRequestData addMeal = (MealRequestData) obj;
      this.mealDAO.add(addMeal);
    } else if (obj instanceof FlowerRequestData) {
      FlowerRequestData addFlower = (FlowerRequestData) obj;
      this.flowerDAO.add(addFlower);
    } else if (obj instanceof FurnitureRequestData) {
      FurnitureRequestData addFurniture = (FurnitureRequestData) obj;
      this.furnitureDAO.add(addFurniture);
    } else if (obj instanceof ConferenceRequestData) {
      ConferenceRequestData addConf = (ConferenceRequestData) obj;
      this.conferenceDAO.add(addConf);
    } else {
      throw new NoSuchElementException("No Service Request of this type");
    }
  }

  public void addOfficeSupplyRequest(OfficeSuppliesData obj) {
    this.officesupplyDAO.add(obj);
  }

  public void addMealRequest(MealRequestData obj) {
    this.mealDAO.add(obj);
  }

  public void addConfRoomRequest(ConferenceRequestData obj) {
    this.conferenceDAO.add(obj);
  }

  public void addFurnitureRequest(FurnitureRequestData obj) {
    this.furnitureDAO.add(obj);
  }

  public void addFlowerRequest(FlowerRequestData obj) {
    this.flowerDAO.add(obj);
  }

  public void addNode(HospitalNode obj) {
    this.nodeDAO.add(obj);
  }

  public void addEdge(HospitalEdge obj) {
    this.edgeDAO.add(obj);
  }

  public void addLocation(LocationName obj) {
    this.locationDAO.add(obj);
  }

  public void addMove(MoveAttribute obj) {
    this.moveDAO.add(obj);
  }

  public void addEmployee(Employee obj) {
    this.employeeDAO.add(obj);
  }
}
