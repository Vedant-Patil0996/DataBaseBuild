package Btree;
public class BtreeInsertion {
    public static void main(String[] args) {
        // Create a B+ Tree with order 4.
        // This means each node can have at most 3 keys and 4 children.
        // A node splits when it has to hold a 4th key.
        BPlustree tree = new BPlustree(4);

        System.out.println("--- B+ Tree Insertion Tests ---");
        System.out.println("Order of the tree is 4.\n");

        // Test 1: Simple insertions without any splits
        System.out.println("Test 1: Inserting 10, 20, 5 (no splits)...");
        tree.BPlusTreeInsert(10);
        tree.BPlusTreeInsert(20);
        tree.BPlusTreeInsert(5);
        System.out.print("Current Tree: ");
        tree.traverse();
        System.out.println("Expected: 5 10 20 \n");

        // Test 2: Insertion that causes a leaf split
        // The node [5, 10, 20] is full. Inserting 15 will cause it to split.
        // The node becomes [5, 10] and [15, 20].
        // The key 15 is copied up to a new root.
        System.out.println("Test 2: Inserting 15 (causes a leaf split and new root)...");
        tree.BPlusTreeInsert(15);
        System.out.print("Current Tree: ");
        tree.traverse();
        System.out.println("Expected: 5 10 15 20 \n");

        // Test 3: Insertions that fill up leaves
        System.out.println("Test 3: Inserting 25, 30, 12 (filling up leaves)...");
        tree.BPlusTreeInsert(25);
        tree.BPlusTreeInsert(30);
        tree.BPlusTreeInsert(12);
        System.out.print("Current Tree: ");
        tree.traverse();
        System.out.println("Expected: 5 10 12 15 20 25 30 \n");

        // Test 4: Insertion that causes an internal node split (cascading split)
        // The tree structure should be:
        //      Root: [15]
        // Children: [5, 10, 12] and [15, 20, 25, 30]
        // The right leaf is full. Inserting 18 will split it into [15, 18] and [20, 25, 30].
        // The key 20 is promoted, making the root [15, 20]. No root split yet.
        System.out.println("Test 4: Inserting 18 (causes another leaf split)...");
        tree.BPlusTreeInsert(18);
        System.out.print("Current Tree: ");
        tree.traverse();
        System.out.println("Expected: 5 10 12 15 18 20 25 30 \n");

        // Test 5: Final test causing a root split
        System.out.println("Test 5: Inserting 1, 8, 35, 40 to trigger more splits...");
        tree.BPlusTreeInsert(1);
        tree.BPlusTreeInsert(8);
        tree.BPlusTreeInsert(35);
        tree.BPlusTreeInsert(40);
        System.out.print("Current Tree: ");
        tree.traverse();
        System.out.println("Expected: 1 5 8 10 12 15 18 20 25 30 35 40 \n");

        System.out.println("--- B+ Tree Search Tests ---");
        int keyToFind = 18;
        System.out.println("Searching for key: " + keyToFind);
        if (tree.BTreeSearch(keyToFind) != null) {
            System.out.println("Key " + keyToFind + " found.");
        } else {
            System.out.println("Key " + keyToFind + " not found.");
        }

        keyToFind = 99;
        System.out.println("\nSearching for key: " + keyToFind);
        if (tree.BTreeSearch(keyToFind) != null) {
            System.out.println("Key " + keyToFind + " found.");
        } else {
            System.out.println("Key " + keyToFind + " not found.");
        }
    }
}
