package storage;

import java.nio.ByteBuffer;
import Btree.*;
import java.util.*;
public class NodeSerializer {
    public static byte[] serialize(BtreeNode node) {
        int isLeaf = node.isLeaf?1:0;
        int keys = node.numKeys;

        ByteBuffer buffer = ByteBuffer.allocate(Pager.SIZE);
        buffer.putInt(isLeaf);
        buffer.putInt(keys);

        if(isLeaf==1)
        {
            buffer.putLong(-1);
            for (int i = 0; i < node.numKeys; i++) {
                buffer.putLong(node.dataPointer[i]);
                buffer.putInt(node.keys[i]);
            }
        }
        else
        {
            buffer.putLong(-1);

            for (int i = 0; i < node.numKeys; i++) {
                buffer.putInt(node.keys[i]);
                buffer.putLong(-1);
            }
        }
        return buffer.array();
    }
    public static BtreeNode deserialize(byte[] data,int order)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        boolean isLeaf = buffer.getInt() == 1;
        int numKeys = buffer.getInt();

        BtreeNode node = new BtreeNode(order, isLeaf );
        node.numKeys = numKeys;

        if(isLeaf)
        {
            long nextNodeId = buffer.getLong();
            for (int i = 0; i < numKeys; i++) {
                node.dataPointer[i] = buffer.getLong();
                node.keys[i] = buffer.getInt();
            }
        }
        else
        {

            long firstChildId = buffer.getLong();

            for (int i = 0; i < numKeys; i++) {
                node.keys[i] = buffer.getInt();
                long childId = buffer.getLong();
            }
        }
        return node;
    }
}
