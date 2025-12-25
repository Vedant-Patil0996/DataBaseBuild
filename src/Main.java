import Btree.BPlustree;
import CLI.Handling;
import Utils.CLIStyle;
import Utils.Sleep;
import storage.Table;
import storage.tables.TableSchema;

import java.io.File;
import java.util.Scanner;

import static Utils.CLIStyle.typeWriter;

public class Main {
    public static void main(String[] args)
    {
        typeWriter(CLIStyle.CYAN + "Welcome to VedantDB v1.1..." + CLIStyle.RESET, 50);
        Sleep.Sleep(3000);
        System.out.println("Initializing db...");

        String userHome = System.getProperty("user.home");
        String dbFile = userHome + File.separator + "vedant_db.db";
        try
        {
            TableSchema schema = new TableSchema();
            schema.addCols("id" , "INT",4);
            schema.addCols("name" , "VARCHAR",32);
            schema.addCols("email" , "VARCHAR",256);

            //System.out.println("Schema Defined: "+ schema.rows + " bytes.");

            BPlustree bPlustree = new BPlustree(4,dbFile);

            Table table = new Table(bPlustree.getPager(),schema);
            Scanner sc = new Scanner(System.in);
            System.out.println("Database Ready. Type 'exit' to quit.");
            //Sleep.Sleep(2000);
            System.out.println("Commands:");

            System.out.println("  > insert <id> <name> <email>");
            System.out.println("  > select <id>");
            System.out.println("  > selectall");
            System.out.println("  > delete <id>");
            System.out.println("  > update <id>");
            while(true)
            {
                System.out.print("TARSdb >");
                String s1 = sc.nextLine().trim();
                if(s1.equals("exit"))
                {
                    bPlustree.close();
                    System.out.println(CLIStyle.CYAN+"Saving...");
                     Sleep.Sleep(2000);
                    System.out.println("Database closed successfully"+CLIStyle.RESET);
                    break;
                }

                try
                {
                    if(s1.startsWith("insert"))
                    {
                        Handling.optionInsert(s1,table,bPlustree);
                    }
                    else if(s1.startsWith("selectall")){
                        Handling.optionSelectAll(s1,table,bPlustree,schema);
                    }
                    else if(s1.startsWith("select"))
                    {
                        Handling.optionSelect(s1,table,bPlustree,schema);
                    }
                    else if(s1.startsWith("delete"))
                    {
                        Handling.optionDeleteByID(s1,table,bPlustree,schema);
                    }
                    else if(s1.startsWith("update"))
                    {
                        System.out.println("Operation under development...sorry cutie");
                    }
                    else
                    {
                        System.out.println("Unknown Command");
                    }
                }catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    e.printStackTrace();
                    continue;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
