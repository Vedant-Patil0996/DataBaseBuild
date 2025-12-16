package storage;
import java.nio.ByteBuffer;
import java.util.*;
import java.io.*;
public class CheckList {
    public static final int CheckListId = 1;
    private Pager pager;
    private List<Long> freePages;


    public CheckList(Pager pager) throws IOException
    {
        this.pager = pager;
        this.freePages = new ArrayList<>();
        loadChecklist();
    }

    private void loadChecklist() throws IOException
    {
        if(pager.getPageCount()<=CheckListId)
        {
            return;
        }

        byte[] pageData = pager.readPage(CheckListId);
        ByteBuffer buffer = ByteBuffer.wrap(pageData);

        int freePagesNum = buffer.getInt();
        for(int i=0;i<freePagesNum;i++)
        {
            freePages.add(buffer.getLong());
        }
    }

    public void saveList() throws IOException
    {
        ByteBuffer buffer = ByteBuffer.allocate(Pager.SIZE);
        buffer.putInt(freePages.size());
        {
            for(Long pageId:freePages)
            {
                buffer.putLong(pageId);
            }
        }
        pager.writePage(CheckListId, buffer.array());
    }
    public void releasePage(long Id)
    {
        freePages.add(Id);
    }
    public long getNextFreePage() throws IOException {
        if(!freePages.isEmpty())
        {
            return freePages.removeLast();
        }
        else {
            return pager.allocateNewPage();
        }
    }
}
