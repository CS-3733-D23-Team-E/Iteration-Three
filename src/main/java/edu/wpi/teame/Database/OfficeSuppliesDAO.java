package edu.wpi.teame.Database;

import static java.lang.Integer.parseInt;

import edu.wpi.teame.entities.OfficeSuppliesData;
import edu.wpi.teame.entities.ServiceRequestData;
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

public class OfficeSuppliesDAO<E> extends ServiceDAO<OfficeSuppliesData> {

  public OfficeSuppliesDAO(Connection c) {
    super(c, "\"OfficeSupplies\"");
  }

  @Override
  List<OfficeSuppliesData> get() {
    serviceRequestDataList = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT * FROM \"OfficeSupplies\";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        serviceRequestDataList.add(
            new OfficeSuppliesData(
                rs.getInt("requestID"),
                rs.getString("name"),
                rs.getString("room"),
                rs.getString("deliveryDate"),
                rs.getString("deliverytime"),
                rs.getString("assignedStaff"),
                rs.getString("officesupply"),
                rs.getString("quantity"),
                rs.getString("notes"),
                ServiceRequestData.Status.stringToStatus(rs.getString("status"))));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return serviceRequestDataList;
  }

  void add(OfficeSuppliesData obj) {
    // RequestID auto Generated
    String name = obj.getName();
    String room = obj.getRoom();
    String deliveryDate = obj.getDeliveryDate();
    String quantity = obj.getQuantity();
    ServiceRequestData.Status requestStatus = obj.getRequestStatus();
    String deliveryTime = obj.getDeliveryTime();
    String officeSupply = obj.getOfficeSupply();
    String notes = obj.getNotes();
    String staff = obj.getAssignedStaff();

    String sqlAdd =
        "INSERT INTO \"OfficeSupplies\" VALUES(nextval('serial'), '"
            + name
            + "','"
            + room
            + "','"
            + deliveryDate
            + "','"
            + deliveryTime
            + "','"
            + staff
            + "','"
            + officeSupply
            + "',"
            + quantity
            + ",'"
            + notes
            + "','"
            + requestStatus
            + "');";

    Statement stmt;
    try {
      stmt = activeConnection.createStatement();
      stmt.executeUpdate(sqlAdd);
      obj.setRequestID(this.returnNewestRequestID());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void importFromCSV(String filePath, String tableName) {
    try {
      BufferedReader ireader = new BufferedReader(new FileReader(filePath));
      String line;
      List<String> rows = new ArrayList<>();
      while ((line = ireader.readLine()) != null) {
        rows.add(line);
      }
      rows.remove(0);
      ireader.close();
      Statement stmt = activeConnection.createStatement();

      String sqlDelete = "DELETE FROM \"" + tableName + "\";";
      stmt.execute(sqlDelete);

      for (String l1 : rows) {
        String[] splitL1 = l1.split(",");
        String sql =
            "INSERT INTO "
                + "\""
                + tableName
                + "\""
                + " VALUES ("
                + parseInt(splitL1[0])
                + ",'"
                + splitL1[1]
                + "','"
                + splitL1[2]
                + "',"
                + splitL1[3]
                + ",'"
                + splitL1[4]
                + "','"
                + splitL1[5]
                + "','"
                + splitL1[6]
                + "',"
                + parseInt(splitL1[7])
                + ",'"
                + splitL1[8]
                + "','"
                + splitL1[9]
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
