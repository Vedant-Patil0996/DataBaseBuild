package storage;
import Btree.*;

import java.io.IOException;
import java.util.*;

public class BufferManager {
    private Pager pg;
    private HashMap<Long,BtreeNode>  hp;
    private Set<Long> dirtyPages;
    public BufferManager(Pager pager) {
        this.pg = pager;
        this.hp = new HashMap<>();
        this.dirtyPages = new HashSet<>();
    }
    public BtreeNode createPage(boolean isLeaf,int order) throws IOException
    {
        long id = pg.allocatePage();
        BtreeNode newNode = new BtreeNode(order, isLeaf);
        newNode.pageId = id;

        hp.put(id,newNode);
        dirtyPages.add(id);

        return newNode;

    }
    public BtreeNode fetchPage(long pageId,int order) throws IOException
    {
        if(hp.containsKey(pageId))
        {
            return hp.get(pageId);
        }
        byte[] bytes = pg.readPage(pageId);
        BtreeNode findNode = NodeSerializer.deserialize(bytes,order);
        hp.put(pageId,findNode);
        findNode.pageId = pageId;
        return findNode;
    }

    public void saveAll() throws IOException
    {
        Set<Long> pagesToFlush = new HashSet<>(dirtyPages);

        for (long pageId : pagesToFlush) {
            BtreeNode node = hp.get(pageId);
            if (node != null) {
                // 1. Serialize the modified node from the cache
                byte[] b1 = NodeSerializer.serialize(node);
                // 2. Write it to the Pager
                pg.writePage(pageId, b1);
            }
        }
        // 3. Clear the dirty set once all pages are saved
        dirtyPages.removeAll(pagesToFlush);
    }

    public void markDirty(long pageId) {
        dirtyPages.add(pageId);
    }

    public void deletePage(long pageId) {
        hp.remove(pageId);
        dirtyPages.remove(pageId);
        pg.releasePage(pageId);
    }
}
