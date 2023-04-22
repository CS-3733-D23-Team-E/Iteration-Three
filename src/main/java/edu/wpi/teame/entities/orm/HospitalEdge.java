package edu.wpi.teame.entities.orm;

import java.util.HashMap;

public class HospitalEdge implements ORM {
  String nodeOneID;
  String nodeTwoID;
  int edgeWeight;

  public HospitalEdge(String nodeOneID, String nodeTwoID) {
    this.nodeOneID = nodeOneID;
    this.nodeTwoID = nodeTwoID;
    this.edgeWeight = 1; // Default edge weight of 1
  }

  public HospitalEdge(String nodeOneID, String nodeTwoID, int weight) {
    this.nodeOneID = nodeOneID;
    this.nodeTwoID = nodeTwoID;
    this.edgeWeight = weight;
  }

  public String getNodeOneID() {
    return this.nodeOneID;
  }

  public String getNodeTwoID() {
    return this.nodeTwoID;
  }

  public int getEdgeWeight() {
    return this.edgeWeight;
  }

  public String getTable() {
    return "Edge";
  }

  public void applyChanges(HashMap<String, String> changes) {
    HospitalNode.removeEdge(nodeOneID, nodeTwoID);
    if (changes.containsKey("startNode")) {
      this.nodeOneID = changes.get("nodeOneID");
    }
    if (changes.containsKey("endNode")) {
      this.nodeTwoID = changes.get("nodeTwoID");
    }
    HospitalNode.addEdge(nodeOneID, nodeTwoID);
  }

  public String getPrimaryKey() {
    return "\"endNode\" = " + nodeOneID + " AND \"startNode\" = " + nodeTwoID;
  }
}
