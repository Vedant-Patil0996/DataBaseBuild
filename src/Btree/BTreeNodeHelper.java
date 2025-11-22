package Btree;

import storage.BufferManager;
import java.io.IOException;

public class BTreeNodeHelper {

    public static void insertIntoLeaf(BtreeNode leafNode, int key, long val, BufferManager bf)
    {
        int i = 0;
        while(i < leafNode.numKeys && leafNode.keys[i] < key)
        {
            i++;
        }
        for(int j = leafNode.numKeys; j>i;j--)
        {
            leafNode.keys[j] = leafNode.keys[j-1];
            leafNode.dataPointer[j] = leafNode.dataPointer[j-1];
        }
        leafNode.keys[i] = key;
        leafNode.dataPointer[i] = val;
        leafNode.numKeys++;

        bf.markDirty(leafNode.pageId);
    }

    public static void insertIntoInternal(BtreeNode internalNode, int key, long childPageId, BufferManager bf)
    {
        int i = 0;
        while(i < internalNode.numKeys && internalNode.keys[i] < key){
            i++;
        }
        for(int j = internalNode.numKeys;j>i;j--){
            internalNode.keys[j] = internalNode.keys[j - 1];
            internalNode.childrenId[j + 1] = internalNode.childrenId[j];
        }
        internalNode.keys[i] = key;
        internalNode.childrenId[i + 1] = childPageId;
        internalNode.numKeys++;


        bf.markDirty(internalNode.pageId);
    }

    public static void split(BtreeNode nodeToSplit, BPlustree tree, BufferManager bf) throws Exception {
        int middleIndex = nodeToSplit.numKeys/2;

        BtreeNode sibling = bf.createPage(nodeToSplit.isLeaf, nodeToSplit.order);

        sibling.parentId = nodeToSplit.parentId;

        if (nodeToSplit.isLeaf) {
            int keyToPromote = nodeToSplit.keys[middleIndex];

            int j = 0;
            for (int i = middleIndex; i < nodeToSplit.numKeys; i++) {
                sibling.keys[j] = nodeToSplit.keys[i];
                sibling.dataPointer[j] = nodeToSplit.dataPointer[i];
                j++;
            }
            sibling.numKeys = j;
            nodeToSplit.numKeys = middleIndex;

            sibling.nextNodeId = nodeToSplit.nextNodeId;
            nodeToSplit.nextNodeId= sibling.pageId;

            bf.markDirty(nodeToSplit.pageId);
            bf.markDirty(sibling.pageId);


            promoteKey(nodeToSplit, keyToPromote, sibling, tree, bf);

        } else {
            int keyToPromote = nodeToSplit.keys[middleIndex];

            int j = 0;
            for (int i = middleIndex + 1; i < nodeToSplit.numKeys; i++) {
                sibling.keys[j] = nodeToSplit.keys[i];
                j++;
            }
            sibling.numKeys = j;

            j = 0;
            for (int i = middleIndex + 1; i < nodeToSplit.numKeys + 1; i++) {
                sibling.childrenId[j] = nodeToSplit.childrenId[i];
                j++;
            }
            nodeToSplit.numKeys = middleIndex;

            for (int i = 0; i < sibling.numKeys + 1; i++) {
                BtreeNode child = bf.fetchPage(sibling.childrenId[i], sibling.order);
                child.parentId = sibling.pageId;
                bf.markDirty(child.pageId);

            }

            bf.markDirty(nodeToSplit.pageId);
            bf.markDirty(sibling.pageId);

            // This is the promotion logic you had at the end
            promoteKey(nodeToSplit, keyToPromote, sibling, tree, bf);
        }
    }

    private static void promoteKey(BtreeNode nodeToSplit, int keyToPromote, BtreeNode sibling, BPlustree tree, BufferManager bf) throws Exception {

        // We check the Page ID, not the object reference, to see if it's the root
        if (nodeToSplit.parentId == -1) {
            // This node IS the root. We must create a NEW root.
            BtreeNode newRoot = bf.createPage(false, nodeToSplit.order);

            newRoot.numKeys = 1;
            newRoot.keys[0] = keyToPromote;
            newRoot.childrenId[0] = nodeToSplit.pageId;
            newRoot.childrenId[1] = sibling.pageId;

            // Update the children's parent pointers
            nodeToSplit.parentId = newRoot.pageId;
            sibling.parentId = newRoot.pageId;

            // Tell the tree about the new root
            tree.setRoot(newRoot);

            // Mark all three modified nodes as dirty
            bf.markDirty(nodeToSplit.pageId);
            bf.markDirty(sibling.pageId);
            bf.markDirty(newRoot.pageId);

        } else {

            BtreeNode parent = bf.fetchPage(nodeToSplit.parentId, nodeToSplit.order);

            insertIntoInternal(parent, keyToPromote, sibling.pageId, bf);

            if (parent.numKeys >= parent.order) {
                split(parent, tree, bf);
            }
        }
    }
}

