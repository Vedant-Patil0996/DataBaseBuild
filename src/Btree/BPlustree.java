package Btree;
import storage.*;

import java.io.IOException;

public class BPlustree {
    private BtreeNode root;
    private final int order;
    private BufferManager bf;
    private Pager pg;
    public long rootPageId;
    public BPlustree(int order, String dbFilePath) throws Exception
    {
        this.order = order;
        this.pg = new Pager(dbFilePath);
        this.bf = new BufferManager(pg);
        if(pg.getPageCount()==0)
        {
            System.out.println("Creating new database file");
            this.root=bf.createPage(true,order);
            this.rootPageId = this.root.pageId;
        }
        else
        {
            this.rootPageId = 2;
            this.root = bf.fetchPage(this.rootPageId,this.order);
        }
    }
    public void setRoot(BtreeNode root) {
        this.root = root;
        this.rootPageId = root.pageId;
    }

    //Recursive Search
    public BtreeNode BPlusTreeSearch(int val) throws IOException {
        BtreeNode node = findByLeaf(val);
        if (node == null) {
            return null;
        }
        for (int i = 0; i < node.numKeys; i++) {
            if (node.keys[i] == val) {
                return node; // Found
            }
        }
        return null;
    }
    public void BPlusTreeInsert(int val) throws Exception
    {
        BtreeNode leaf = findByLeaf(val);

        BTreeNodeHelper.insertIntoLeaf(leaf,val,val,bf);//inserting at the location

        if (leaf.numKeys >= this.order)
        {
            BTreeNodeHelper.split(leaf, this,bf);//if order size is exceeded split it
        }

    }
    private BtreeNode findByLeaf(int val) throws IOException
    {
        if(this.root==null)return null;
        BtreeNode curr = this.root;

        while(!curr.isLeaf)
        {
            int i = 0;
            while(i<curr.numKeys && val>= curr.keys[i])
            {
                i++;
            }
            long childPageId = curr.childrenId[i];
            curr = bf.fetchPage(childPageId, this.order);
        }
        return curr;
    }
    public void traverse() throws IOException {
        if (root == null) return;
        BtreeNode currentNode = root;
        while(!currentNode.isLeaf){
            long childPageId = currentNode.childrenId[0];
            currentNode = bf.fetchPage(childPageId,this.order);
        }
        while (currentNode != null) {
            for (int i = 0; i < currentNode.numKeys; i++) {
                System.out.print(currentNode.keys[i] + " ");
            }
            if(currentNode.nextNodeId!=-1)
            {
                currentNode = bf.fetchPage(currentNode.nextNodeId, this.order);
            }
            else
            {
                currentNode = null;
            }

        }
        System.out.println();
    }
    public void close() throws IOException {
        bf.saveAll();
        pg.close();
    }
}
