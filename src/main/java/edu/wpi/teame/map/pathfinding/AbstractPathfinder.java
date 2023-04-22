package edu.wpi.teame.map.pathfinding;

import edu.wpi.teame.map.HospitalNode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractPathfinder {

  public static AbstractPathfinder getInstance(String algorithm) {
    switch (algorithm) {
      case "A*":
        return new AStarPathfinder();
      case "BFS":
        return new BFSPathfinder();
      case "DFS":
        return new DFSPathfinder();
      case "Djikstra":
        return new DjikstraPathfinder();
      default:
        return null;
    }
  }

  /**
   * * Finds the path from the starting HospitalNode to the target HospitalNode * What algorithm is
   * used depends on which subclass is instantiated
   *
   * @param fromId the Id of the starting HospitalNode
   * @param toId the Id of the target HospitalNode
   * @return a list of coordinates representing the shortest path from the starting HospitalNode to
   *     the target HospitalNode
   */
  public List<HospitalNode> findPath(String fromId, String toId) {
    HospitalNode from = HospitalNode.allNodes.get(fromId);
    HospitalNode to = HospitalNode.allNodes.get(toId);
    if (from == null || to == null) {
      System.out.println("Pathfinder Error: One or both nodes are null");
      return null;
    }
    return findPath(from, to);
  }

  public abstract List<HospitalNode> findPath(HospitalNode from, HospitalNode to);

  /**
   * * Reconstructs the path from the starting coordinate to the target coordinate
   *
   * @param parentMap a map of coordinates to their parent coordinates
   * @param current the target coordinate
   * @return a list of coordinates representing the shortest path from the starting coordinate to
   *     the target coordinate
   */
  List<HospitalNode> reconstructPath(
      HashMap<HospitalNode, HospitalNode> parentMap, HospitalNode current) {
    LinkedList<HospitalNode> path = new LinkedList<HospitalNode>();
    while (current != null) {
      path.addFirst(current);
      current = parentMap.get(current);
    }
    return path;
  }
}
