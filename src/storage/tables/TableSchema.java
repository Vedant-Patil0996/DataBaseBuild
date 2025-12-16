package storage.tables;

import java.util.ArrayList;
import java.util.List;

public class TableSchema {
    public int rows;
    public List<Columns> cols = new ArrayList<>();

    public void addCols(String name,String type,int size)
    {
        cols.add(new Columns(name,type,size));
        if (type.equals("VARCHAR")) {
            rows += (size*2);
        } else {
            rows += size;
        }
    }

}
