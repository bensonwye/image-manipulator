/**
 * represents quadrant tree data structure.
 * this class provides functionality to build and manipulate a quadrant tree.
 * tree is built from 2D array of pixels, with each pixel representing a unit square area of space.
 * @author Benson Ye
 * @version 1.0
 */
public class QuadrantTree {
    private QTreeNode root;

    /**
     * constructs quadrant tree from 2D array of pixels.
     *
     * @param thePixels 2D array of integers representing pixel values
     */
    public QuadrantTree(int[][] thePixels) {
        this.root = buildTree(thePixels, 0, 0, thePixels.length, null);
    }

    private QTreeNode buildTree(int[][] pixels, int x, int y, int size, QTreeNode parent) {
        if (size == 1) {
            QTreeNode leaf = new QTreeNode(null, x, y, size, pixels[y][x]);
            leaf.setParent(parent); // set parent
            return leaf;
        }
        int newSize = size / 2;
        QTreeNode node = new QTreeNode(null, x, y, size, 0); // pass null for children initially
        node.setParent(parent); // set parent

        // recursively build subtrees for each quadrant
        node.setChild(buildTree(pixels, x, y, newSize, node), 0); // top-left
        node.setChild(buildTree(pixels, x + newSize, y, newSize, node), 1); // top-right
        node.setChild(buildTree(pixels, x, y + newSize, newSize, node), 2); // bottom-left
        node.setChild(buildTree(pixels, x + newSize, y + newSize, newSize, node), 3); // bottom-right

        // calculate and set average color of current node
        int color = Gui.averageColor(pixels, x, y, size);
        node.setColor(color);

        return node;
    }

    /**
     * gets root node of quadrant tree.
     *
     * @return root node of quadrant tree
     */
    public QTreeNode getRoot() {
        return this.root;
    }

    /**
     * retrieves all nodes at specified level of tree starting from given node.
     *
     * @param r        starting node
     * @param theLevel level of tree to retrieve nodes from
     * @return list of nodes at specified level
     */
    public ListNode<QTreeNode> getPixels(QTreeNode r, int theLevel) {
        // base cases
        if (r == null) return null;
        if (theLevel == 0) {
            // return list containing just this node
            return new ListNode<>(r);
        }

        // recursive case
        if (!r.isLeaf()) {
            ListNode<QTreeNode> list = new ListNode<>(null); // dummy head
            ListNode<QTreeNode> tail = list; // keeps track of last node in list

            for (int i = 0; i < 4; i++) {
                try {
                    ListNode<QTreeNode> childList = getPixels(r.getChild(i), theLevel - 1);
                    // append child list to current list
                    while (childList != null) {
                        tail.setNext(childList); // add elements from childList to result
                        tail = tail.getNext(); // move to last element
                        childList = childList.getNext(); // move to next element in child list
                    }
                } catch (QTreeException e) {
                    e.printStackTrace();
                }
            }

            return list.getNext(); // skip dummy head
        }

        return null;
    }

    /**
     * finds nodes with color similar to specified color at given level starting from given node.
     *
     * @param r        starting node
     * @param theColor color to compare
     * @param theLevel level of tree to search for similar color nodes
     * @return Duple object containing list of nodes with similar color and count of such nodes
     */
    public Duple findMatching(QTreeNode r, int theColor, int theLevel) {
        if (r == null) {
            return new Duple(new ListNode<QTreeNode>(null), 0);
        }
        if (theLevel == 0 || r.isLeaf()) {
            boolean isSimilar = Gui.similarColor(r.getColor(), theColor);
            if (isSimilar) {
                ListNode<QTreeNode> list = new ListNode<QTreeNode>(r);
                return new Duple(list, 1);
            } else {
                return new Duple(new ListNode<QTreeNode>(null), 0);
            }
        }
        Duple result = new Duple(new ListNode<QTreeNode>(null), 0);
        for (int i = 0; i < 4; i++) { 
            Duple childDuple = findMatching(r.getChild(i), theColor, theLevel - 1);
            result.setFront(mergeLists(result.getFront(), childDuple.getFront()));
            result.setCount(result.getCount() + childDuple.getCount());
        }
        return result;
    }

    /**
     * merges two ListNode lists.
     *
     * @param a first list
     * @param b second list
     * @return merged list
     */
    private ListNode<QTreeNode> mergeLists(ListNode<QTreeNode> a, ListNode<QTreeNode> b) {
        if (a == null) return b;
        if (b == null) return a;

        ListNode<QTreeNode> temp = new ListNode<QTreeNode>(null); // dummy head
        ListNode<QTreeNode> current = temp;

        while (a != null && a.getData() != null) {
            current.setNext(new ListNode<QTreeNode>(a.getData()));
            current = current.getNext();
            a = a.getNext();
        }

        while (b != null && b.getData() != null) {
            current.setNext(new ListNode<QTreeNode>(b.getData()));
            current = current.getNext();
            b = b.getNext();
        }

        return temp.getNext(); // skip dummy head
    }

    /**
     * finds node containing point at specified level starting from given node.
     *
     * @param r        starting node
     * @param theLevel level of the tree to search for the node
     * @param x        x-coordinate of the point
     * @param y        y-coordinate of the point
     * @return node containing point if found, otherwise null
     */
    public QTreeNode findNode(QTreeNode r, int theLevel, int x, int y) {
        if (r == null || theLevel < 0) {
            return null;
        }
        if (theLevel == 0 || r.isLeaf()) {
            if (r.contains(x, y)) {
                return r;
            }
            return null;
        }
        for (int i = 0; i < 4; i++) {
            QTreeNode child = r.getChild(i);
            if (child != null && child.contains(x, y)) {
                return findNode(child, theLevel - 1, x, y);
            }
        }
        return null;
    }
}
