package edu.wpi.teame.map;

import static java.util.Objects.hash;

import java.util.*;

import edu.wpi.teame.entities.ORM;
import lombok.Getter;

public class HospitalNode implements ORM {
  public static HashMap<String, HospitalNode> allNodes = new HashMap<>();

  @Getter List<HospitalNode> neighbors;
  @Getter HashMap<HospitalNode, Integer> edgeCosts;

  @Getter String nodeID;

  @Getter int xCoord;
  @Getter int yCoord;
  @Getter Floor floor;
  @Getter String building;

  public HospitalNode(String id, int xCoord, int yCoord, Floor floor, String building) {
    this.neighbors = new LinkedList<HospitalNode>();
    edgeCosts = new HashMap<HospitalNode, Integer>();
    nodeID = id;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
  }

  public HospitalNode() {
    this(allNodes.size() + "", 0, 0, Floor.LOWER_ONE, "Unknown");
  }

  public HospitalNode(String id) {
    this(id, 0, 0, Floor.LOWER_ONE, "Unknown");
  }

  public HospitalNode(String id, int xCoord, int yCoord) {
    this(id, xCoord, yCoord, Floor.LOWER_ONE, "Unknown");
  }

  @Override
  public String toString() {
    return "Node " + nodeID;
  }

  @Override
  public int hashCode() {
    return hash(nodeID);
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof HospitalNode) {
      return nodeID.equals(((HospitalNode) other).nodeID);
    }
    return false;
  }

  public HospitalNode addNeighbor(HospitalNode neighbor) {
    neighbors.add(neighbor);
    edgeCosts.put(neighbor, 1);
    return this;
  }

  public HospitalNode addNeighbor(HospitalNode neighbor, int cost) {
    neighbors.add(neighbor);
    edgeCosts.put(neighbor, cost);
    return this;
  }

  public static void addEdge(HospitalNode node1, HospitalNode node2) {
    node1.neighbors.add(node2);
    node2.neighbors.add(node1);
  }

  public static void addEdge(String nodeId1, String nodeId2) {
    addEdge(allNodes.get(nodeId1), allNodes.get(nodeId2));
  }

  public static void addEdge(HospitalNode node1, HospitalNode node2, int edgeWeight) {
    node1.addNeighbor(node2, edgeWeight);
    node2.addNeighbor(node1, edgeWeight);
  }

  public static void addEdge(String nodeId1, String nodeId2, int edgeWeight) {
    addEdge(allNodes.get(nodeId1), allNodes.get(nodeId2), edgeWeight);
  }

  public static void processEdgeList(List<HospitalEdge> edgeList) {
    for (HospitalEdge edge : edgeList) {
      addEdge(allNodes.get(edge.nodeOneID), allNodes.get(edge.nodeTwoID), edge.edgeWeight);
    }
  }

  public static ArrayList<String> allBuildings() {
    ArrayList<String> buildings = new ArrayList<>();
    buildings.add("45 Francis");
    buildings.add("Tower");
    buildings.add("15 Francis");
    buildings.add("BTM");
    buildings.add("Shapiro");
    return buildings;
  }


  public String getTable(){
    return "Node";
  }

  public void applyChanges(HashMap<String,String> changes) {
    if(changes.containsKey("nodeID")){
      this.nodeID = changes.get("nodeID");
    }
    if(changes.containsKey("xcoord")){
      this.xCoord = Integer.parseInt(changes.get("xcoord"));
    }
    if(changes.containsKey("ycoord")){
      this.yCoord = Integer.parseInt(changes.get("ycoord"));
    }
    if(changes.containsKey("floor")){
      this.floor = Floor.stringToFloor(changes.get("floor"));
    }
    if(changes.containsKey("building")){
      this.building = changes.get("building");
    }
  }

  public String getPrimaryKey(){
    return "\"nodeID\" = " +
            nodeID;
  }
}
