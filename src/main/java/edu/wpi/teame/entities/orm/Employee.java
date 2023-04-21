package edu.wpi.teame.entities.orm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.NoSuchElementException;

import lombok.Getter;
import lombok.Setter;

public class Employee implements ORM {

  public enum Permission {
    STAFF,
    ADMIN;

    public static String permissionToString(Permission p) {
      switch (p) {
        case STAFF:
          return "STAFF";
        case ADMIN:
          return "ADMIN";
        default:
          throw new NoSuchElementException("No such permission");
      }
    }

    public static Permission stringToPermission(String str) {
      switch (str) {
        case "STAFF":
          return STAFF;
        case "ADMIN":
          return ADMIN;
        default:
          throw new NoSuchElementException("No such permission");
      }
    }
  }

  @Getter @Setter private String fullName;
  @Getter @Setter private String username;
  @Getter @Setter private String password;
  @Getter @Setter private String permission;

  @Setter public static Employee activeEmployee;

  public Employee(String fullName, String username, String password, String permission) {
    this.fullName = fullName;
    this.username = username;
    this.password = hashPassword(password);
    this.permission = permission.toUpperCase();
  }

  public Employee(String fullName, String username, String permission) {
    this.fullName = fullName;
    this.permission = permission;
    this.username = null;
    this.password = null;
  }

  public static String hashPassword(String password) {
    try {

      // Static getInstance method is called with hashing MD5
      MessageDigest md = MessageDigest.getInstance("MD5");

      // digest() method is called to calculate message digest
      // of an input digest() return array of byte
      byte[] messageDigest = md.digest(password.getBytes());

      // Convert byte array into signum representation
      BigInteger no = new BigInteger(1, messageDigest);

      // Convert message digest into hex value
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public String getTable() {
    return "Employee";
  }

  public void applyChanges(HashMap<String,String> changes) {
    if (changes.containsKey("fullName")) {
      this.fullName = changes.get("fullName");
    }
    if (changes.containsKey("username")) {
      this.username = changes.get("username");
    }
    if (changes.containsKey("password")) {
      this.password = changes.get("password");
    }
    if (changes.containsKey("permission")) {
      this.permission = changes.get("permission");
    }
  }

  public String getPrimaryKey() {
    return "\"username\" = '"
            + username;
  }
}
