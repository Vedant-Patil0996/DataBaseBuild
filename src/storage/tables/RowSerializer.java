package storage.tables;

import storage.Pager;

import java.nio.Buffer;
import java.nio.ByteBuffer;

public class RowSerializer {

    public static byte[] serializeRow(Row row, TableSchema schema)
    {

        ByteBuffer buff = ByteBuffer.allocate(schema.rows+1);
        buff.put((byte) 0);
        for(int i=0;i<schema.cols.size();i++) {
            Columns cols = schema.cols.get(i);
            Object val = row.values.get(i);

            if (cols.type.equals("INT")) {
                buff.putInt((Integer) val);
            } else if (cols.type.equals("VARCHAR")) {
                String s1 = val.toString();
                int writeSize = Math.min(s1.length(), cols.size);
                for (int j = 0; j < writeSize; j++) {
                    buff.putChar(s1.charAt(j));
                }
                int padding = cols.size - writeSize;

                for (int k = 0; k < padding; k++) {
                    buff.putChar('\0');
                }
            }
        }
        return buff.array();
    }
    public static Row deserializeRow(byte[] bytes, TableSchema schema)
    {
        ByteBuffer buff = ByteBuffer.wrap(bytes);
        Row rows = new Row();
        if(buff.get()==1)
        {
            return null;
        }
        for(Columns cols: schema.cols)
        {
            if(cols.type.equals("INT"))
            {
                rows.addValue(buff.getInt());
            }
            else if(cols.type.equals("VARCHAR"))
            {
                StringBuilder sb = new StringBuilder();
                for(int j = 0; j < cols.size; j++) {
                    char c = buff.getChar();

                    if(c!='\0') {
                        sb.append(c);
                    }
                }
                rows.addValue(sb.toString());
            }
        }
        return rows;
    }

}
