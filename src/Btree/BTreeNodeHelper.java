package Btree;
public class BTreeNodeHelper {
    public static void insertIntoLeaf(BtreeNode root,int key,long val)
    {
        int i=0;
        while(i<root.numKeys && root.keys[i]<key)
        {
            i++;
        }
        for(int j=root.numKeys;j>i;j--)
        {
            root.keys[j]=root.keys[j-1];
            root.dataPointer[j]=root.dataPointer[j-1];
        }
        root.keys[i]=key;
        root.dataPointer[i]=val;
        root.numKeys++;
    }

    public static void insertIntoInternal(BtreeNode root,int key,BtreeNode child)
    {
        int i=0;
        while(i<root.numKeys && root.keys[i]<key)
        {
            i++;
        }
        for(int j=root.numKeys;j>i;j--)
        {
            root.keys[j]=root.keys[j-1];
            root.children[j+1]=root.children[j];
        }
        root.keys[i]=key;
        root.children[i+1]=child;
        child.parent=root;
        root.numKeys++;
    }
    public static void split(BtreeNode nodeToSplit,BPlustree root)
    {
        int middleIndex = nodeToSplit.numKeys/2;
        int keyToPromote = nodeToSplit.keys[middleIndex];

        BtreeNode sibling = new BtreeNode(nodeToSplit.order,nodeToSplit.isLeaf);
        sibling.parent = nodeToSplit.parent;

        //splitting leaf nodes
        if(nodeToSplit.isLeaf)
        {
            int j=0;
            for(int i=middleIndex;i< nodeToSplit.numKeys;i++)
            {
                sibling.keys[j]= nodeToSplit.keys[i];
                sibling.dataPointer[j]=nodeToSplit.dataPointer[i];
                j++;
            }
            sibling.numKeys=j;
            nodeToSplit.numKeys=middleIndex;
            sibling.nextNode = nodeToSplit.nextNode;
            nodeToSplit.nextNode=sibling;
            //promoteKey(nodeToSplit, keyToPromote, sibling, root);
        }
        //splitting internal nodes
        else
        {
            int j=0;
            for(int i=middleIndex+1;i< nodeToSplit.numKeys;i++)
            {
                sibling.keys[j]=nodeToSplit.keys[i];
                j++;
            }
            sibling.numKeys=j;
            j=0;
            for(int i=middleIndex+1;i<nodeToSplit.numKeys+1;i++)
            {
                sibling.children[j] = nodeToSplit.children[i];
                sibling.children[j].parent = sibling;
                j++;
            }
            nodeToSplit.numKeys = middleIndex;
        }

        //promoting nodeToPromote to parent
        if(nodeToSplit.parent==null)
        {
            BtreeNode newParent = new BtreeNode(nodeToSplit.order,false);
            newParent.numKeys=1;
            newParent.keys[0]=keyToPromote;
            newParent.children[0]=nodeToSplit;
            newParent.children[1]=sibling;
            nodeToSplit.parent=newParent;
            sibling.parent= newParent;
            root.setRoot(newParent);
        }
        else
        {
            insertIntoInternal(nodeToSplit.parent,keyToPromote,sibling);

            if(nodeToSplit.parent.numKeys>=nodeToSplit.order)
            {
                split(nodeToSplit.parent,root);
            }
        }
    }
}
