package Btree;
import storage.*;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

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
            // 1.Page 0 (Reserved for Header/Metadata)
            pg.allocateNewPage(); // Page 0 reserved
            pg.allocateNewPage();
            this.root=bf.createPage(true,order);
            this.rootPageId = this.root.pageId;

            saveRootPageId(this.rootPageId);
        }
        else
        {
            this.rootPageId = loadRootPageId();
            this.root = bf.fetchPage(this.rootPageId, this.order);
        }
    }
    public void setRoot(BtreeNode root) throws IOException {
        this.root = root;
        this.rootPageId = root.pageId;
        saveRootPageId(this.rootPageId);
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
    public void BPlusTreeInsert(int key, long recordPointer) throws Exception {

        // 1. Find the correct leaf node
        BtreeNode leaf = findByLeaf(key);

        // 2. Insert the Key AND the Pointer
        BTreeNodeHelper.insertIntoLeaf(leaf, key, recordPointer, bf);

        // 3. Handle splits if necessary
        if (leaf.numKeys >= this.order) {
            BTreeNodeHelper.split(leaf, this, bf);
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
    public void traverse(List<Long> li) throws IOException {
        if (root == null) return;
        BtreeNode currentNode = root;
        while(!currentNode.isLeaf){
            long childPageId = currentNode.childrenId[0];
            currentNode = bf.fetchPage(childPageId,this.order);
        }
        while (currentNode != null) {
            for (int i = 0; i < currentNode.numKeys; i++) {
                //System.out.print(currentNode.keys[i] + " ");
                li.add(currentNode.dataPointer[i]);
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
        //System.out.println();
    }
    public Pager getPager() {
        return this.pg;
    }
    public void close() throws IOException {
        bf.saveAll();
        pg.close();
    }

    public static void btreeVisualizer(BPlustree tree) throws IOException
    {
        if(tree.root==null)
        {
            System.out.println("Tree is empty");
            return;
        }

        BtreeNode start = tree.root;
        System.out.print("(ROOT)");
        System.out.print("-(KEYS)=>");
        for(int i=0;i< start.numKeys;i++)
        {
            System.out.print(start.keys[i]+", ");
        }
        if (start.isLeaf) {
            System.out.println(" [Leaf Node - No Children]");
            return; // or continue if inside a loop
        }
        System.out.print(" -(CHILDREN)=>");
        int len = start.childrenId.length;
        Stack<BtreeNode> st = new Stack<>();
        for(int i = start.numKeys; i >= 0; i--)
        {
            long val = start.childrenId[i];
            System.out.print(val+", ");
            st.push(tree.bf.fetchPage(val,tree.order));
        }
        System.out.println();
        while(!st.isEmpty())
        {
            BtreeNode b1 = st.pop();
            len = b1.keys.length;
            System.out.print("(NODE)");
            System.out.print("-(KEYS)=>");
            for(int i=0;i<b1.numKeys;i++)
            {
                System.out.print(b1.keys[i]+", ");
            }
            if(b1.isLeaf)
            {
                continue;
            }
            System.out.print(" -(CHILDREN)=>");
            len = b1.childrenId.length;
            for(int i = b1.numKeys; i >= 0; i--)
            {
                long val = b1.childrenId[i];
                System.out.print(val+", ");
                st.push(tree.bf.fetchPage(val,tree.order));
            }
            System.out.println();
        }

    }
    private void saveRootPageId(long pageId) throws IOException {
        pg.writeLong(0, pageId);
    }

    private long loadRootPageId() throws IOException {
        return pg.readLong(0);
    }
}
