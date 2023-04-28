package edu.wpi.teame.Database;

import edu.wpi.teame.entities.MedicalSuppliesData;
import edu.wpi.teame.entities.MedicalSupplyData;
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

public class MedicalSupplyDAO<E> extends ServiceDAO<MedicalSupplyData>{
    public MedicalSupplyDAO(Connection c) {
        super(c, "\"MedicalService\"");
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

            String sqlDelete = "DELETE FROM " + tableName + ";";
            stmt.execute(sqlDelete);

            for (String l1 : rows) {
                String[] splitL1 = l1.split(",");
                String sql =
                        "INSERT INTO "
                                + tableName
                                + " VALUES ('"
                                + splitL1[0]
                                + "','"
                                + splitL1[1]
                                + "','"
                                + splitL1[2]
                                + "','"
                                + splitL1[3]
                                + "','"
                                + splitL1[4]
                                + "','"
                                + splitL1[5]
                                + "','"
                                + splitL1[6]
                                + "','"
                                + splitL1[7]
                                + "','"
                                + splitL1[8]
                                + "','"
                                + splitL1[9]
                                + "','"
                                + splitL1[10]
                                + "','"
                                +splitL1[11]
                                + "',"
                                + splitL1[12]
                                + ",'"
                                + splitL1[13]
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

    @Override
    List<MedicalSupplyData> get() {
        serviceRequestDataList = new LinkedList<>();

        try {
            Statement stmt = activeConnection.createStatement();

            String sql = "SELECT * FROM " + table + ";";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                serviceRequestDataList.add(
                        new MedicalSupplyData(
                                rs.getInt("requestID"),
                                rs.getString("name"),
                                rs.getString("room"),
                                rs.getString("deliverydate"),
                                rs.getString("deliverytime"),
                                rs.getString("assignedstaff"),
                                rs.getString("notes"),
                                rs.getString("bandaid"),
                                rs.getString("covidtest"),
                                rs.getString("epipen"),
                                rs.getString("gloves"),
                                rs.getString("sthetiscope"),
                                rs.getString("syringe"),
                                ServiceRequestData.Status.stringToStatus(rs.getString("status"))));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return serviceRequestDataList;
    }

    @Override
    void add(MedicalSupplyData obj) {
        // requestID is generated
        String name = obj.getName();
        String room = obj.getRoom();
        String deliveryDate = obj.getDeliveryDate();
        String deliveryTime = obj.getDeliveryTime();
        String staff = obj.getAssignedStaff();
        String notes = obj.getNotes();
        String bandaid = obj.getBandaid();
        String covidTest = obj.getCovidtest();
        String epipen = obj.getEpipen();
        String gloves = obj.getGloves();
        String sthetiscope = obj.getSthetiscope();
        String syringe = obj.getSyringe();
        ServiceRequestData.Status requestStatus = obj.getRequestStatus();

        String sqlAdd =
                "INSERT INTO " + table + " VALUES('"
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
                        + notes
                        + "','"
                        + bandaid
                        + "','"
                        + covidTest
                        + "','"
                        + epipen
                        + "','"
                        + gloves
                        + "','"
                        + sthetiscope
                        + "','"
                        + syringe
                        + "',"
                        + "nextval('serial')"
                        + ",'"
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
    }

