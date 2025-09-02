public class BPlustree {
    private BtreeNode root;
    private int order;

    public BPlustree(int order)
    {
        this.order = order;
        this.root = null;
    }
    public BtreeNode BPlustreeSearchKey(int val,BtreeNode root)
    {
        int index=0;
        while(index<root.numKeys && val>root.keys[index])
        {
            index++;
        }

        if(root.isLeaf)
        {
            if(index< root.numKeys && val==root.keys[index])
                return root;

            return null;
        }
        return BPlustreeSearchKey(val,root.children[index]);
    }
    public BtreeNode BTreeSearch(int val)
    {
        if(this.root==null)
        {
            return null;
        }
        else
        {
            return BPlustreeSearchKey(val,root);
        }
    }

    //changes to be made below....
    public void BPlustreeInsert(int val)
    {
        if (this.root == null)
        {
            this.root = new BtreeNode(this.order, true);
            this.root.keys[0] = val;
            this.root.numKeys = 1;
        }

    }
    private void splitChild(BtreeNode parent,int index)
    {
        BtreeNode NodeToSplit=parent.children[index];
        BtreeNode newNode=new BtreeNode(NodeToSplit.order,NodeToSplit.isLeaf);
        newNode.numKeys=this.order/2 -1;

        for(int j=0;j<this.order/2 -1;j++)
        {
            newNode.keys[j]=NodeToSplit.keys[j+this.order/2];
        }
        if(!NodeToSplit.isLeaf)
        {
            for(int j=0;j<this.order/2;j++)
            {
                newNode.children[j]=NodeToSplit.children[j+this.order/2];
            }
        }
        NodeToSplit.numKeys=this.order/2 -1;
        for(int j=parent.numKeys;j>=index+1;j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[index+1]=newNode;
        for(int j=parent.numKeys-1;j>=index;j--)
        {
            parent.keys[j+1]=parent.keys[j];
        }
        parent.keys[index]=NodeToSplit.keys[this.order/2 -1];
        parent.numKeys++;
        // System.out.println("Spliting node "+NodeToSplit.keys[this.maxDegree/2 -1]);

    }
}
