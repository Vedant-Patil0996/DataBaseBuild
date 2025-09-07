public class BPlustree {
    private BtreeNode root;
    private final int order;

    public BPlustree(int order)
    {
        this.order = order;
        this.root = null;
    }
    public void setRoot(BtreeNode root) {
        this.root = root;
    }

    //Recursive Search
    public BtreeNode BPlusTreeSearchKey(int val,BtreeNode root)
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
        return BPlusTreeSearchKey(val,root.children[index]);
    }
    public BtreeNode BTreeSearch(int val)
    {
        if(this.root==null)
        {
            return null;
        }
        else
        {
            return BPlusTreeSearchKey(val,root);
        }
    }


    public void BPlusTreeInsert(int val)
    {
        if(this.root==null)
        {
            this.root = new BtreeNode(this.order,true);
            BTreeNodeHelper.insertIntoLeaf(this.root,val,val);
            return;
        }
        BtreeNode leaf = findByLeaf(val);

        BTreeNodeHelper.insertIntoLeaf(leaf,val,val);

        if (leaf.numKeys >= this.order)
        {
            BTreeNodeHelper.split(leaf, this);
        }

    }
    private BtreeNode findByLeaf(int val)
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
            curr = curr.children[i];
        }
        return curr;
    }
    public void traverse() {
        if (root == null) return;
        BtreeNode currentNode = root;
        while(!currentNode.isLeaf){
            currentNode = currentNode.children[0];
        }
        while (currentNode != null) {
            for (int i = 0; i < currentNode.numKeys; i++) {
                System.out.print(currentNode.keys[i] + " ");
            }
            currentNode = currentNode.nextNode;
        }
        System.out.println();
    }
}
