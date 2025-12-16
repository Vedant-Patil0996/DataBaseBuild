package storage;

import storage.tables.Row;
import storage.tables.RowSerializer;
import storage.tables.TableSchema;

import java.io.IOException;

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
        byte[] data = RowSerializer.serializeRow(row,schema);
        if (currentPageID == -1 || currentOffset + data.length > Pager.SIZE) {
            currentPageID = pager.allocateNewPage();
            currentOffset = 0;
        }

        byte[] pageData = pager.readPage(currentPageID);
        for (int i = 0; i < data.length; i++) {
            pageData[currentOffset + i] = data[i];
        }
        pager.writePage(currentPageID, pageData);

        // Create Pointer: (PageID << 16) | Offset
        long pointer = (currentPageID << 16) | currentOffset;

        currentOffset += data.length;
        return pointer;
    }
    public Row readRow(long pointer) throws IOException {
        long pageId = pointer >> 16;
        int offset = (int) (pointer & (1<<16)-1);

        byte[] pageData = pager.readPage(pageId);

        byte[] rowData = new byte[schema.rows];
        for (int i = 0; i < schema.rows; i++) {
            rowData[i] = pageData[offset + i];
        }

        return RowSerializer.deserializeRow(rowData, schema);
    }


}
