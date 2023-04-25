package edu.wpi.teame.Database;

import edu.wpi.teame.entities.Employee;
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

public class EmployeeDAO extends DAO<Employee> {

  List<Employee> employeeList;

  public EmployeeDAO(Connection c) {
    activeConnection = c;
    table = "\"Employee\"";
  }

  @Override
  List<Employee> get() {
    employeeList = new LinkedList<>();

    try {
      Statement stmt = activeConnection.createStatement();
      String sql = "SELECT * FROM " + table + ";";

      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        employeeList.add(
            new Employee(
                rs.getString("fullName"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("permission")));
      }
      if (employeeList.isEmpty()) System.out.println("There was a problem returning the employees");
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
    return employeeList;
  }

  @Override
  void update(Employee obj, String attribute, String value) {
    String username = obj.getUsername();
    String sql = "";

    try {
      Statement stmt = activeConnection.createStatement();

      if (attribute.equals("password")) value = obj.hashPassword(value);

      sql =
          "UPDATE "
              + table
              + " SET \""
              + attribute
              + "\" = '"
              + value
              + "' WHERE \"username\" = '"
              + username
              + "';";

      int result = stmt.executeUpdate(sql);
      if (result < 1)
        System.out.println(
            "There was a problem updating the "
                + attribute
                + " of the employee: "
                + obj.getFullName());
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
    this.get();
  }

  @Override
  void delete(Employee obj) {
    try {
      Statement stmt = activeConnection.createStatement();
      String deleteEmp = obj.getUsername();

      String sql = "DELETE FROM " + table + " WHERE \"username\" = '" + deleteEmp + "';";

      int result = stmt.executeUpdate(sql);

      if (result < 1) System.out.println("There was a problem deleting the employee");
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
    this.get();
  }

  @Override
  void add(Employee obj) {
    try {
      String fullName = obj.getFullName();
      String username = obj.getUsername();
      String password = obj.getPassword();
      String perm = obj.getPermission();

      Statement stmt = activeConnection.createStatement();
      String sql =
          "INSERT INTO "
              + table
              + " VALUES('"
              + fullName
              + "', '"
              + username
              + "', '"
              + password
              + "', '"
              + perm
              + "');";
      int result = stmt.executeUpdate(sql);
      if (result < 1) {
        System.out.println("There was a problem inserting the employee");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
    this.get();
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
                + " VALUES ('"
                + splitL1[0]
                + "', '"
                + splitL1[1]
                + "', '"
                + splitL1[2]
                + "', '"
                + splitL1[3]
                + "'); ";
        try {
          System.out.println(sql);
          stmt.execute(sql);
        } catch (SQLException e) {
          System.out.println("Could not import employee " + splitL1[0]);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    this.get();
  }

  public Employee verifyLogIn(String username, String password) {
    String sql = "";
    String hashedPassword = Employee.hashPassword(password);

    try {
      Statement stmt = activeConnection.createStatement();

      sql =
          "SELECT * FROM \"Employee\" WHERE \"username\" = '"
              + username
              + "' AND \"password\" = '"
              + hashedPassword
              + "';";

      ResultSet rs = stmt.executeQuery(sql);

      if (rs.next()) {
        System.out.println("You've successfully logged in");
        return new Employee(
            rs.getString("fullName"), rs.getString("username"), rs.getString("permission"));
      } else {
        System.out.println("Your username or password is incorrect");
        return null;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
