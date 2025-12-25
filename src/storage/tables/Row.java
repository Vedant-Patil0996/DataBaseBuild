package storage.tables;

import java.util.ArrayList;
import java.util.List;

public class Row {
    public List<Object> values = new ArrayList<>();

    public void addValue(Object val) {
        values.add(val);
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public int size()
    {
        return values.size();
    }
}
