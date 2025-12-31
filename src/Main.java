import Btree.BPlustree;
import CLI.Handling;
import Utils.Art;
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
        /*typeWriter(CLIStyle.CYAN + """
                Welcome User!!
                You are currently experiencing under development...
                """ + CLIStyle.RESET, 100);
        System.out.println(CLIStyle.CYAN+Art.logo()+"\n"+Art.art()+CLIStyle.YELLOW);*/
        System.out.println("Initializing db..."+CLIStyle.RESET);
        Sleep.Sleep(3000);
        String userHome = System.getProperty("user.home");

        String dbFile = userHome + File.separator + "vedant_db.db";
        File dbFileObj = new File(dbFile);
        try
        {
            TableSchema schema = new TableSchema();
            schema.addCols("id" , "INT",4);
            schema.addCols("name" , "VARCHAR",32);
            schema.addCols("email" , "VARCHAR",256);

            //System.out.println("Schema Defined: "+ schema.rows + " bytes.");
            //System.out.println("Db path: " + dbFileObj.getAbsolutePath());
            //System.out.println("FILE EXISTS? " + dbFileObj.exists());
            System.out.println("FILE SIZE: " + dbFileObj.length());
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
            System.out.println("  > Treevisual");
            while(true)
            {
                System.out.println("FILE SIZE: " + dbFileObj.length());
                System.out.print("TARSdb >");
                String s1 = sc.nextLine().trim();
                if(s1.equals("exit"))
                {
                    bPlustree.close();
                    System.out.println(CLIStyle.YELLOW+"Saving...");
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
                    else if(s1.startsWith("Tree visual"))
                    {
                        Handling.optionTreeVisual(bPlustree);
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
