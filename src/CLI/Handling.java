package CLI;

import Btree.BPlustree;
import Btree.BtreeNode;
import Utils.CLIStyle;
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
        int id;
        try{
             id = Integer.parseInt(parts[1]);
        }catch(NumberFormatException e)
        {
            System.out.println("Incorrect Syntax");
            return;
        }


        BtreeNode b1 = btree.BPlusTreeSearch(id);
        if(b1 == null)
        {
            System.out.println("Entry not found");
            return;
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
        if(result==null)
        {
            System.out.println("Row deleted...");
            return;
        }
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
            System.out.println(CLIStyle.RED+"Incorrect Syntax"+CLIStyle.RESET);
            return;
        }
        int id;
        try{
            id = Integer.parseInt(parts[1]);
        }catch(NumberFormatException e)
        {
            System.out.println("Incorrect Syntax");
            return;
        }
        BtreeNode b1 = btree.BPlusTreeSearch(id);
        if(b1 == null)
        {
            System.out.println("Entry not found");
            return;
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

    public static void optionTreeVisual(BPlustree btree) throws IOException {
        Btree.BPlustree.btreeVisualizer(btree);
    }

    public static void optionStressTest(String s1,Table table,BPlustree btree, TableSchema schema) throws Exception
    {
        String[] parts = s1.split(" ");
        if(parts.length!=3 && parts.length!=2)
        {
            System.out.println("Invalid syntax");
            return;
        }

        int value = Integer.parseInt(parts[1]);
        int index;
        if(parts.length==3)
            index = Integer.parseInt(parts[2]);
        else
            index = 50;
        String str1 = "stress";
        String str2 = "@example.com";

        while(value--!=0)
        {
            Row r1 = new Row();
            r1.addValue(index);
            r1.addValue(str1+index);
            r1.addValue(str1+index+str2);

            long placer = table.insertRow(r1);

            btree.BPlusTreeInsert(index,placer);
            index++;
        }
        if(value==0)
        {
            System.out.println("StressTest successful");
        }
    }

    public static void optionByteView(String s1,Table table,BPlustree btree, TableSchema schema) throws Exception
    {
        String[] parts = s1.split(" ");
        if(parts.length==2)
        {

        }
        else if(parts.length==3)
        {
            Utils.ByteVisual.viewPage(btree.getPager(),Integer.parseInt(parts[2]));
        }
        else
        {
            System.out.println("Invalid Syntax");
            return;
        }
    }

    private void byteView(int index,Table table,BPlustree btree, TableSchema schema) throws Exception
    {

    }

}
