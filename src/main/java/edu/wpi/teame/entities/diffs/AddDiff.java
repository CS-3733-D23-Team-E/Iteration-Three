package edu.wpi.teame.entities.diffs;

import edu.wpi.teame.Database.SQLRepo;
import edu.wpi.teame.entities.orm.ORM;

import java.util.List;

public class AddDiff<E extends ORM> extends Diff<E> {

  public AddDiff(E added) {
    super(added);
    getChanges().putAll(added.getFields());
  }

  public String getQuery() {
    StringBuilder query = new StringBuilder();
    query.append("INSERT INTO \"");
    query.append(getObj().getTable());
    query.append("\" (");
    List<String> columnNames = SQLRepo.INSTANCE.getColumnNames(getObj());
    for(String columnName : columnNames){
        query.append("\"");
        query.append(columnName);
        if(columnName != columnNames.get(columnNames.size() - 1))
            query.append("\",");
        else
            query.append("\"");
    }
    query.append(") VALUES (");
    for(String columnName : columnNames){
      try{
        Integer.parseInt(getChanges().get(columnName));
        query.append(getChanges().get(columnName));
      } catch (NumberFormatException e) {
        query.append("'");
        query.append(getChanges().get(columnName));
        query.append("'");
      }
      if(columnName != columnNames.get(columnNames.size() - 1))
          query.append(",");
      else
          query.append("'");
    }
    query.append(");");
    return query.toString();
  }
}
