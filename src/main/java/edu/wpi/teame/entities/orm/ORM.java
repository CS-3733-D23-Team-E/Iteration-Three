package edu.wpi.teame.entities.orm;

import java.util.HashMap;
import java.util.Map;

public interface ORM {
  public String getTable();

  public String getPrimaryKey();

  public void applyChanges(HashMap<String, String> changes);

  public Map<String, String> getFields();
}
