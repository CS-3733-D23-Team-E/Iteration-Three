package edu.wpi.teame.Database;

import edu.wpi.teame.entities.ServiceRequestData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public abstract class ServiceDAO<E> extends DAO<E> {
  @Getter @Setter List<E> serviceRequestDataList;

  public ServiceDAO(Connection c, String tableName) {
    activeConnection = c;
    table = tableName;
  }

  abstract List<E> get();

  abstract void add(E obj);

  @Override
  void update(E obj, String attribute, String value) {
    ServiceRequestData srd = (ServiceRequestData) obj;
    int requestID = srd.getRequestID();
    String sql = "";

    try {
      Statement stmt = activeConnection.createStatement();
      sql =
          "UPDATE "
              + table
              + " "
              + "SET \""
              + attribute
              + "\" = '"
              + value
              + "' WHERE \"requestID\" = "
              + requestID
              + ";";
      int result = stmt.executeUpdate(sql);
      if (result < 1)
        System.out.println(
            "There was a problem updating the " + attribute + " of that ServiceRequest");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  @Override
  void delete(E obj) {
    ServiceRequestData srd = (ServiceRequestData) obj;
    try {
      Statement stmt = activeConnection.createStatement();
      int deleteID = srd.getRequestID();

      String sql =
          "DELETE FROM " + table + " WHERE " + table + ".\"requestID\" = " + deleteID + ";";

      int result = stmt.executeUpdate(sql);

      if (result < 1) System.out.println("There was a problem deleting the ServiceRequest");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  int returnNewestID() {
    try {
      Statement stmt = activeConnection.createStatement();

      String sql = "SELECT currval('serial') AS val;";
      ResultSet rs = stmt.executeQuery(sql);

      int currentID = rs.getInt("val");

      return currentID;
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
