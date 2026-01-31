package storage;

import storage.tables.Row;
import storage.tables.RowSerializer;
import storage.tables.TableSchema;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Table {
    private Pager pager;
    private TableSchema schema;
    private long currentPageID;
    private int currentOffset;

    public Table(Pager pager,TableSchema schema) throws IOException
    {
        this.pager = pager;
        this.schema = schema;

        this.currentPageID = -1;
        this.currentOffset = 0;
    }

    public long insertRow(Row row) throws IOException
    {
        int page_header = 16;
        boolean newPage = false;
        ByteBuffer header = null;
        byte[] data = RowSerializer.serializeRow(row,schema);
        if (currentPageID == -1 || currentOffset + data.length > Pager.SIZE) {
            currentPageID = pager.allocateNewPage();

            header = ByteBuffer.allocate(page_header);
            header.putInt(2);
            header.putInt(0); // Reserved / Row Count (Optional)
            header.putLong(0L); // Reserved
            newPage =true;

            currentOffset = page_header;
        }

        byte[] pageData = pager.readPage(currentPageID);
        if (newPage) {
            byte[] headerBytes = header.array();
            for(int i=0; i<page_header; i++) {
                pageData[i] = headerBytes[i];
            }
        }
        for (int i = 0; i < data.length; i++) {
            pageData[currentOffset + i] = data[i];
        }
        pager.writePage(currentPageID, pageData);

        long pointer = (currentPageID << 16) | currentOffset;

        currentOffset += data.length;
        return pointer;
    }
    public Row readRow(long pointer) throws IOException {
        long pageId = pointer >> 16;
        int offset = (int) (pointer & (1<<16)-1);
        // System.out.println("{DEBUG} -> Reading RowPointer: " + pointer);
        // System.out.println("{DEBUG} -> Unpacked PageID: " + pageId);
        // System.out.println("{DEBUG} -> Unpacked Offset : " + offset);
        byte[] pageData = pager.readPage(pageId);
        if (pageData[offset] == 1) {
            return null;
        }
        byte[] rowData = new byte[schema.rows+1];
        for (int i = 0; i < schema.rows; i++) {
            rowData[i] = pageData[offset + i];
        }

        return RowSerializer.deserializeRow(rowData, schema);
    }
    public void deleteRow(long pointer) throws IOException
    {
        long pageId = pointer >> 16;
        int offset = (int) (pointer & (1<<16)-1);

        byte[] pageData = pager.readPage(pageId);
        pageData[offset] = (byte) 1;

        pager.writePage(pageId, pageData);
    }

}
