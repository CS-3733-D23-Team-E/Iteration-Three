package edu.wpi.teame.map.pathfinding;

import edu.wpi.teame.map.HospitalNode;
import java.util.*;

class AStarPathfinder extends AbstractPathfinder {
  /**
   * * Finds the least cost path from the starting HospitalNode to the target HospitalNode using the
   * A-star algorithm
   *
   * @param from the starting HospitalNode
   * @param to the target HospitalNode
   * @return a list of coordinates representing the least cost path from the starting HospitalNode
   *     to the target HospitalNode
   */
  @Override
  public List<HospitalNode> findPath(HospitalNode from, HospitalNode to) {
    HashMap<HospitalNode, Integer> costMap = new HashMap<>();
    HashMap<HospitalNode, HospitalNode> parentMap = new HashMap<HospitalNode, HospitalNode>();

    PriorityQueue<HospitalNode> queue =
        new PriorityQueue<HospitalNode>(
            new Comparator<HospitalNode>() {
              @Override
              public int compare(HospitalNode o1, HospitalNode o2) {
                int costBias = 25;
                // Based on heuristic distance to target
                return (costMap.get(o1) + heuristicDistance(o1, to))
                    - (costMap.get(o2) + heuristicDistance(o2, to));
              }
            });

    queue.add(from);
    parentMap.put(from, null);
    costMap.put(from, 0);

    while (!queue.isEmpty()) {
      HospitalNode current = queue.remove();
      if (current.equals(to)) {
        // If this is the end node, reconstruct the path based on the parent map and return it
        return reconstructPath(parentMap, current);
      }
      for (HospitalNode neighbor : current.getNeighbors()) {
        int newCost = neighbor.getEdgeCosts().get(current) + costMap.get(current);
        // If we've already explored the children of this node, don't add it to the queue
        if (!parentMap.containsKey(neighbor) || costMap.get(neighbor) > newCost) {
          // Parent map doubles as a visited set
          // If there's a cheaper path to this node, update the cost and parent
          costMap.put(neighbor, newCost);
          queue.remove(neighbor); // Remove and add to re-sort the priority queue
          queue.add(neighbor);
          parentMap.put(neighbor, current);
        }
      }
    }

    return null;
  }

  int heuristicDistance(HospitalNode from, HospitalNode to) {
    // estimate the distance to the target based on the euclidean distance to the target
    int floorBias = 100000;
    return (int)
            Math.sqrt(
                Math.pow(from.getXCoord() - to.getXCoord(), 2)
                    + Math.pow(from.getYCoord() - to.getYCoord(), 2))
        + (int) floorBias * Math.abs(from.getFloor().ordinal() - to.getFloor().ordinal());
  }
}
