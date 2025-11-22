package storage;
import java.util.*;
import java.io.*;

public class Pager {
    public static final int SIZE=4096;
    private final RandomAccessFile file;
    private long pageCount;
    private CheckList checkList;

    public Pager(String dbFilePath) throws IOException
    {
        File dbFile = new File(dbFilePath);
        this.file = new RandomAccessFile(dbFile,"rw");
        this.pageCount = file.length()/SIZE;
        this.checkList = new CheckList(this);
    }

    public byte[] readPage(long Id) throws IOException
    {
        if(Id>=pageCount)
        {
            throw new IllegalArgumentException("Cannot read page that does not exist: "+ Id);
        }
        byte[] pageData = new byte[SIZE];
        long offset = Id * SIZE;

        file.seek(offset);

        file.readFully(pageData);//synchorinized wont stopped until all data bytes in that place is fully read

        return pageData;
    }

    public void writePage(long Id,byte[] pageData) throws IOException
    {
        if(pageData.length!=SIZE)
        {
            throw new IllegalArgumentException("Page data must be exactly"+SIZE);
        }

        long fileOffset = Id*SIZE;

        file.seek(fileOffset);
        file.write(pageData);
    }


    public long allocatePage() throws IOException
    {
        return checkList.getNextFreePage();
    }

    public long allocateNewPage()
    {
        long newPageId = pageCount;
        pageCount++;
        return newPageId;
    }

    public long getPageCount()
    {
        return pageCount;
    }

    public void releasePage(long pageId)
    {
        checkList.releasePage(pageId);
    }

    public void close() throws IOException {
        checkList.saveList();
        file.close();
    }
}
