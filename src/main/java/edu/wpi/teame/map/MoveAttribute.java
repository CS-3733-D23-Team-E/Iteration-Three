package edu.wpi.teame.map;

import edu.wpi.teame.entities.ORM;
import lombok.Getter;

import java.util.HashMap;

public class MoveAttribute implements ORM {
  @Getter int nodeID;
  @Getter String longName;
  @Getter String date;

  public MoveAttribute(int nodeID, String longName, String date) {
    this.nodeID = nodeID;
    this.longName = longName;
    this.date = date;
  }


  public String getTable(){
    return "Move";
  }

  public void applyChanges(HashMap<String, String> changes){
    if(changes.containsKey("nodeID")){
      this.nodeID = Integer.parseInt(changes.get("nodeID"));
    }
    if(changes.containsKey("longName")){
      this.longName = changes.get("longName");
    }
    if(changes.containsKey("date")){
      this.date = changes.get("date");
    }
  }

  public String getPrimaryKey(){
    return "\"nodeID\" = "
            + nodeID
            + " AND \"longName\" = '"
            + longName
            + " AND \"date\" = '"
            + date;
  }
}
