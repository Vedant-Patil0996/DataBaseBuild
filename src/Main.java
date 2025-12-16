import Btree.BPlustree;
import CLI.Handling;
import storage.Table;
import storage.tables.TableSchema;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        System.out.println("Initializing db...");
        String dbFile = "vedant_db.db";
        try
        {
            TableSchema schema = new TableSchema();
            schema.addCols("id" , "INT",4);
            schema.addCols("name" , "VARCHAR",32);
            schema.addCols("email" , "VARCHAR",256);

            System.out.println("Schema Defined: "+ schema.rows + " bytes.");

            BPlustree bPlustree = new BPlustree(4,dbFile);

            Table table = new Table(bPlustree.getPager(),schema);
            Scanner sc = new Scanner(System.in);
            System.out.println("Database Ready. Type 'exit' to quit.");
            System.out.println("Commands:");
            System.out.println("  > insert <id> <name> <email>");
            System.out.println("  > select <id>");
            while(true)
            {
                System.out.print("db >");
                String s1 = sc.nextLine().trim();
                if(s1.equals("exit"))
                {
                    bPlustree.close();
                    System.out.println("Database saved...now closing");
                    break;
                }

                try
                {
                    if(s1.startsWith("insert"))
                    {
                        Handling.optionInsert(s1,table,bPlustree);
                    }
                    else if(s1.startsWith("select"))
                    {
                        Handling.optionSelect(s1,table,bPlustree,schema);
                    }
                    else
                    {
                        System.out.println("Unknown Command");
                    }
                }catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
