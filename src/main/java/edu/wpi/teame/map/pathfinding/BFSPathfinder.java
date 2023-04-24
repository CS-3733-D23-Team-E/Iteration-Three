package edu.wpi.teame.map.pathfinding;

import edu.wpi.teame.map.HospitalNode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class BFSPathfinder extends AbstractPathfinder {

  /**
   * * Finds the shortest path from the starting HospitalNode to the target HospitalNode using
   * Breadth First Search
   *
   * @param from the starting HospitalNode
   * @param to the target HospitalNode
   * @return a list of coordinates representing the shortest path from the starting HospitalNode to
   *     the target HospitalNode
   */
  @Override
  public List<HospitalNode> findPath(HospitalNode from, HospitalNode to) {
    LinkedList<HospitalNode> queue = new LinkedList<HospitalNode>();
    HashMap<HospitalNode, HospitalNode> parentMap = new HashMap<HospitalNode, HospitalNode>();
    queue.add(from);
    parentMap.put(from, null);

    while (!queue.isEmpty()) {
      HospitalNode current = queue.remove();
      if (current.equals(to)) {
        return reconstructPath(parentMap, current);
      }
      for (HospitalNode neighbor : current.getNeighbors()) {
        if (!parentMap.containsKey(neighbor)) {
          // Parent map doubles as a visited set
          queue.add(neighbor);
          parentMap.put(neighbor, current);
        }
      }
    }

    return null;
  }
}
