package edu.wpi.teame.Database;

import edu.wpi.teame.entities.SignageComponentData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SignageComponentDAO<E> extends DAO<SignageComponentData> {

  List<SignageComponentData> signageComponent;

  public SignageComponentDAO(Connection c) {
    activeConnection = c;
    table = "\"SignageForm\"";
  }

  @Override
  List<SignageComponentData> get() {
    signageComponent = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        signageComponent.add(
            new SignageComponentData(
                rs.getString("date"),
                rs.getString("kiosk_location"),
                rs.getString("location"),
                SignageComponentData.ArrowDirections.stringToDirection(
                    rs.getString("arrowDirection"))));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return signageComponent;
  }

  @Override
  void update(SignageComponentData obj, String attribute, String value) {
    String date = obj.getDate();
    String kloc = obj.getKiosk_location();
    String loc = obj.getLocationNames();

    String sqlUpdate =
        "UPDATE "
            + table
            + " SET \""
            + attribute
            + "\" = '"
            + value
            + "' WHERE date = '"
            + date
            + "' AND kiosk_location = '"
            + kloc
            + "' AND location = '"
            + loc
            + "';";

    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlUpdate);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  SignageComponentData.ArrowDirections getDirectionFromPKey(
      String date, String klocation, String location) throws SQLException {
    String sql =
        "SELECT * FROM "
            + table
            + " WHERE location = '"
            + location
            + "' AND kiosk_location = '"
            + klocation
            + "' AND date = '"
            + date
            + "';";
    Statement stmt = null;
    ResultSet rs = null;
    try {
      stmt = activeConnection.createStatement();
      rs = stmt.executeQuery(sql);
      if (rs.next()) {
        return SignageComponentData.ArrowDirections.stringToDirection(
            rs.getString("arrowDirection"));
      } else {
        throw new SQLException("No matching record found");
      }
    } finally {
      if (rs != null) {
        rs.close();
      }
      if (stmt != null) {
        stmt.close();
      }
    }
  }

  @Override
  void delete(SignageComponentData obj) {
    String date = obj.getDate();
    String loc = obj.getKiosk_location();
    try {
      Statement stmt = activeConnection.createStatement();

      String sql =
          "DELETE FROM "
              + table
              + " WHERE \"date\" = '"
              + date
              + "' AND \"kiosk_location\" = '"
              + loc
              + "';";

      int result = stmt.executeUpdate(sql);

      if (result < 1) System.out.println("There was a problem deleting the Signage Component");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void add(SignageComponentData obj) {
    String locationNames = obj.getLocationNames();
    String date = obj.getDate();
    String kioskLocation = obj.getKiosk_location();
    SignageComponentData.ArrowDirections arrowDirections = obj.getArrowDirections();

    String sqlAdd =
        "INSERT INTO "
            + table
            + " VALUES('"
            + date
            + "','"
            + kioskLocation
            + "','"
            + locationNames
            + "','"
            + arrowDirections
            + "');";

    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlAdd);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void importFromCSV(String filePath, String tableName) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line;
      List<String> rows = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        rows.add(line);
      }
      rows.remove(0);
      reader.close();
      Statement stmt = activeConnection.createStatement();

      String sqlDelete = "DELETE FROM " + table + ";";
      stmt.execute(sqlDelete);

      for (String l1 : rows) {
        String[] splitL1 = l1.split(",");
        String sql =
            "INSERT INTO "
                + table
                + " VALUES ('"
                + splitL1[0]
                + "','"
                + splitL1[1]
                + "','"
                + splitL1[2]
                + "','"
                + splitL1[3]
                + "'); ";
        stmt.execute(sql);
      }

      System.out.println(
          "Imported " + (rows.size()) + " rows from " + filePath + " to " + tableName);

    } catch (IOException | SQLException e) {
      System.err.println("Error importing from " + filePath + " to " + tableName);
      e.printStackTrace();
    }
  }
}
