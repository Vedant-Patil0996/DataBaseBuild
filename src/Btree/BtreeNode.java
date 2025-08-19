public class BtreeNode {
    int[] keys;
    BtreeNode[] children;
    int maxDegree;
    boolean isLeaf;
    int numKeys;

    public BtreeNode(int maxDegree, boolean isLeaf) {
        this.maxDegree = maxDegree;//number of children for each node
        this.isLeaf = isLeaf;//to check whether the node is a leaf node or not
        this.keys = new int[maxDegree -1];
        this.children = new BtreeNode[maxDegree];
        this.numKeys = 0;
    }
}

