package edu.wpi.teame.entities.orm;

import java.util.HashMap;

public interface ORM {
    public String getTable();
    public String getPrimaryKey();
    public void applyChanges(HashMap<String, String> changes);
}
