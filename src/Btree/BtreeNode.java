package Btree;
public class BtreeNode {
    public int[] keys;
    public BtreeNode[] children;
    public int order;
    public boolean isLeaf;
    public int numKeys;
    public BtreeNode nextNode; // Pointer to the next leaf node in the linked list
    public long[] dataPointer;// Array to store pointers to actual data rows (only used in leaf nodes)
    public BtreeNode parent;
    public BtreeNode(int order, boolean isLeaf) {
        this.order = order;//number of children for each node
        this.isLeaf = isLeaf;//to check whether the node is a leaf node or not
        this.keys = new int[order];
        this.children = new BtreeNode[order+1];
        this.numKeys = 0;
        this.nextNode = null;
        this.parent = null;
        if(isLeaf)
        {
            this.dataPointer = new long[order];
        }
        else {
            this.dataPointer = null;
        }
    }
}
/*
   -> int[] keys: An array that holds the sorted key values within this node to guide searches.

   ->  int order: Defines the maximum number of children a node can have, setting the tree's capacity.

   ->  BtreeNode[] children: An array of pointers to the child nodes of this internal node.

   ->  boolean isLeaf: A flag that is `true` if this is a bottom-level node, `false` otherwise.

   ->  int numKeys: Counts the number of keys currently stored in this node.

  ->   BtreeNode nextNode: A pointer that links a leaf node to the next leaf node in sequence.

    -> long[] dataPointers: An array in leaf nodes that stores the disk addresses of the actual data rows.
 */
