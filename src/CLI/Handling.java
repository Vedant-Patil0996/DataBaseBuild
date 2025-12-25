package CLI;

import Btree.BPlustree;
import Btree.BtreeNode;
import storage.Table;
import storage.tables.Row;
import storage.tables.TableSchema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Handling {

    public static void optionInsert(String s1, Table table, BPlustree btree) throws Exception {
        String[] parts = s1.split(" ");
        if(parts.length!=4)
        {
            System.out.println("Incorrect Syntax");
            return;
        }
        int id = Integer.parseInt(parts[1]);
        String name = parts[2];
        String email = parts[3];

        Row r1 = new Row();
        r1.addValue(id);
        r1.addValue(name);
        r1.addValue(email);

        long placer = table.insertRow(r1);

        btree.BPlusTreeInsert(id,placer);

        System.out.println("Inserted successfully: Pointer->" + placer);
    }
    public static void optionSelect(String s1, Table table, BPlustree btree, TableSchema schema) throws Exception
    {
        String[] parts = s1.split(" ");
        if(parts.length!=2)
        {
            System.out.println("Incorrect Syntax");
            return;
        }
        int id = Integer.parseInt(parts[1]);

        BtreeNode b1 = btree.BPlusTreeSearch(id);
        if(b1 == null)
        {
            System.out.println("Entry not found");
        }
        long pointer = -1;
        boolean found = false;

        for (int i = 0; i < b1.numKeys; i++) {
            if (b1.keys[i] == id) {
                pointer = b1.dataPointer[i];
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("ID " + id + " not found.");
            return;
        }

        Row result = table.readRow(pointer);

        System.out.println("Found Row: " + result.toString());

    }
    public static void optionSelectAll(String s1,Table table,BPlustree btree, TableSchema schema) throws Exception
    {
        String[] parts = s1.split(" ");
        if(parts.length!=1)
        {
            System.out.println("Incorrect Syntax");
            return;
        }
        List<Long> list = new ArrayList<>();
        btree.traverse(list);
        List<Row> s2 = new ArrayList<>();
        for(int i=0;i<list.size();i++)
        {
            Row res = table.readRow(list.get(i));
            if(res==null)
            {
                continue;
            }
            s2.add(res);
        }
        Utils.TableFormat.createTable(s2);
    }

    public static void optionDeleteByID(String s1,Table table,BPlustree btree, TableSchema schema) throws Exception
    {
        String[] parts = s1.split(" ");
        if(parts.length!=2)
        {
            System.out.println("Incorrect Syntax");
            return;
        }
        int id = Integer.parseInt(parts[1]);
        BtreeNode b1 = btree.BPlusTreeSearch(id);
        if(b1 == null)
        {
            System.out.println("Entry not found");
        }
        long pointer = -1;
        boolean found = false;

        for (int i = 0; i < b1.numKeys; i++) {
            if (b1.keys[i] == id) {
                pointer = b1.dataPointer[i];
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("ID " + id + " not found.");
            return;
        }

        table.deleteRow(pointer);
    }
}
