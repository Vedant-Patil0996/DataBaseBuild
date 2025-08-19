public class Btree {
    private BtreeNode root;
    private int maxDegree;

    public Btree(int maxDegree)
    {
        this.maxDegree = maxDegree;
        this.root = null;
    }
    public BtreeNode BtreeSearchKey(int val,BtreeNode root)
    {
        int index=0;
        while(index<root.numKeys && val>root.keys[index])
        {
            index++;
        }

        if(index<root.numKeys && val==root.keys[index])
        {
            return root;
        }
        if(root.isLeaf)
        {
            return null;
        }

        return BtreeSearchKey(val,root.children[index]);
    }
    public BtreeNode BTreeSearch(int val)
    {
        if(this.root==null)
        {
            return null;
        }
        else
        {
            return BtreeSearchKey(val,root);
        }
    }

}
