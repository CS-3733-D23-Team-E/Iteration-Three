package edu.wpi.teame.map;

public class HospitalEdge {
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
}
