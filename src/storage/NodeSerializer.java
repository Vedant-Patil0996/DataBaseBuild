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
        buffer.putLong(node.parentId);
        buffer.putLong(node.nextNodeId);
        if(isLeaf==1)
        {
            for(int i=0;i<node.order;i++)
            {
                if (i < node.numKeys)
                {
                    buffer.putLong(node.dataPointer[i]);
                    buffer.putInt(node.keys[i]);
                }
                else
                {
                    buffer.putLong(-1);
                    buffer.putInt(0);
                }
            }
        }
        else
        {

            for(int i =0;i<node.order;i++)
            {
                if (i<node.numKeys)
                {
                    buffer.putInt(node.keys[i]);
                }
                else
                {
                    buffer.putInt(0);
                }
            }
            for(int i=0;i<=node.order+1;i++)
            {
                if (i<=node.numKeys)
                {
                    buffer.putLong(node.childrenId[i]);
                }
                else
                {
                    buffer.putLong(-1);
                }
            }
        }
        return buffer.array();
    }
    public static BtreeNode deserialize(byte[] data,int order)
    {
        ByteBuffer buffer = ByteBuffer.wrap(data);

        boolean isLeaf = buffer.getInt() == 1;
        int numKeys = buffer.getInt();
        long parentNodeId = buffer.getLong();
        long nextNodeId = buffer.getLong();
        BtreeNode node = new BtreeNode(order, isLeaf );
        node.numKeys = numKeys;
        node.nextNodeId = nextNodeId;
        node.parentId = parentNodeId;
        if(isLeaf)
        {
            for(int i=0;i<node.order;i++)
            {
                node.dataPointer[i] = buffer.getLong();
                node.keys[i] = buffer.getInt();
            }

        }
        else
        {
            for (int i = 0; i < node.order; i++)
            {
                node.keys[i] = buffer.getInt();
            }
            for(int i=0;i<=node.order;i++)
            {
                node.childrenId[i]= buffer.getLong();
            }
        }
        return node;
    }

    /*
    public static void main(String[] args) throws Exception {

        BtreeNode b1 = new BtreeNode(4,false);
        b1.keys=new int[]{1,2,3};
        b1.childrenId = new long[]{12,19,21,22};
        b1.parentId = 67;
        byte[] arr1 = serialize(b1);
        System.out.println(Arrays.toString(arr1));
        BtreeNode b2 = deserialize(arr1,4);
        byte[] arr2 = serialize(b2);
        System.out.println(Arrays.toString(arr2));

    }
    */

}
