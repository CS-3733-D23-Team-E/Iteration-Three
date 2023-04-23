package edu.wpi.teame.Database;

import edu.wpi.teame.entities.AlertData;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;

public class AlertDAO<E> extends DAO<AlertData> {

  @Getter List<AlertData> alertDataList;

  public AlertDAO(Connection c) {
    activeConnection = c;
    table = "\"Alert\"";
  }

  @Override
  List<AlertData> get() {
    alertDataList = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();
      String sql = "SELECT * FROM \"Alert\";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        alertDataList.add(
            new AlertData(
                rs.getInt("alertID"), rs.getString("message"), rs.getString("timestamp")));
      }
      if (alertDataList.isEmpty()) System.out.println("No Alerts return");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return alertDataList;
  }

  @Override
  void update(AlertData obj, String attribute, String value) {
    int alertID = obj.getAlertID();
    String sqlUpdate =
        "UPDATE \"Alert\" "
            + "SET \""
            + attribute
            + "\" = '"
            + value
            + "' WHERE \"alertID\" = "
            + alertID
            + ";";
    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlUpdate);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void delete(AlertData obj) {
    int alertID = obj.getAlertID();
    String sql = "DELETE FROM \"Alert\" WHERE \"alertID\" = " + alertID + ";";
    try {
      Statement stmt = activeConnection.createStatement();
      stmt.executeUpdate(sql);
      stmt.close();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void add(AlertData obj) {
    try {
      String message = obj.getMessage();
      String timestamp = obj.getTimestamp();

      Statement stmt = activeConnection.createStatement();
      String sql =
          "INSERT INTO \"Alert\" VALUES(nextval('serial'), '"
              + message
              + "', '"
              + timestamp
              + "');";
      int result = stmt.executeUpdate(sql);
      if (result < 1) {
        System.out.println("There was a problem inserting the alert");
      }
      obj.setAlertID(returnNewestRequestID());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void importFromCSV(String filePath, String tableName) {
    try {
      // Load CSV file
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line;
      List<String> rows = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        rows.add(line);
      }
      rows.remove(0);
      reader.close();
      Statement stmt = activeConnection.createStatement();

      String sqlDelete = "DELETE FROM \"" + tableName + "\";";
      stmt.execute(sqlDelete);

      for (String l1 : rows) {
        String[] splitL1 = l1.split(",");
        String sql =
            "INSERT INTO \""
                + tableName
                + "\""
                + " VALUES ("
                + splitL1[0]
                + ", '"
                + splitL1[1]
                + "', '"
                + splitL1[2]
                + "'); ";
        try {
          stmt.execute(sql);
        } catch (SQLException e) {
          System.out.println("Could not import nodeID " + splitL1[0]);
        }
      }

      System.out.println(
          "Imported " + (rows.size()) + " rows from " + filePath + " to " + tableName);

    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    get();
  }

  private int returnNewestRequestID() {
    int currentID = -1;
    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT last_value AS val FROM serial;";
      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        currentID = rs.getInt("val");
      } else {
        System.out.println("Something ain't workin right");
      }
      return currentID;
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
