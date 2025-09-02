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
}//implement split
