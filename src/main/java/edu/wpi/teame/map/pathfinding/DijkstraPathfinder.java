package edu.wpi.teame.map.pathfinding;

import edu.wpi.teame.map.HospitalNode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

class DijkstraPathfinder extends AbstractPathfinder {

  @Override
  public List<HospitalNode> findPath(HospitalNode from, HospitalNode to) {
    HashMap<HospitalNode, Integer> costMap = new HashMap<>();
    HashMap<HospitalNode, HospitalNode> parentMap = new HashMap<HospitalNode, HospitalNode>();

    PriorityQueue<HospitalNode> queue =
        new PriorityQueue<HospitalNode>(
            new Comparator<HospitalNode>() {
              @Override
              public int compare(HospitalNode o1, HospitalNode o2) {
                return costMap.get(o1) - costMap.get(o2);
              }
            });

    for (HospitalNode node : HospitalNode.allNodes.values()) {
      costMap.put(node, Integer.MAX_VALUE);
    }

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
        if (costMap.get(neighbor) > newCost) {
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
}
