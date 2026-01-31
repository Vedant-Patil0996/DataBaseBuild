package Utils;

import storage.Pager;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteVisual {

    public static void viewPage(Pager pager, int pageId)
    {
        try{
            byte[] data = pager.readPage(pageId);
            ByteBuffer buff = ByteBuffer.wrap(data);

            if(pageId<3)
            {

            }
            else
            {
                int type = buff.getInt();
                if(type==1)
                {
                    int isLeaf = buff.getInt();
                    int keys = buff.getInt();
                    long parentId = buff.getLong();
                    long nextId = buff.getLong();

                    System.out.println(" > Page Type : B-TREE NODE");
                    System.out.println(" > Kind      : " + (isLeaf == 1 ? "LEAF (1-1)" : "INTERNAL (1-0)"));
                    System.out.println(" > Key Count : " + keys);
                    System.out.println(" > Parent ID : " + parentId);
                    System.out.println(" > Next ID : "+ nextId);
                }
                else if (type == 2)
                {
                    int delete = buff.get(16);

                    System.out.println(" > Page Type : DATA PAGE");
                    if(delete==1)
                    {
                        System.out.println("Row/Data recently deleted");
                    }
                    System.out.println(" > Reserved  : " + buff.getInt(4));
                }
                else {
                    System.out.println(" > Page Type : UNKNOWN (" + type + ")");
                    System.out.println(" > Note      : Might be empty or corrupted.");
                }
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
