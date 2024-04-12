/**
 * represents node in quadtree structure. this class encapsulates properties of a quadrant,
 * such as coordinates, size, color, and relationship with parent and children nodes.
 * @author Benson Ye
 * @version 1.0
 */
public class QTreeNode {
    private int x, y, size, color; // coordinates, size, and color of node
    private QTreeNode parent; // parent node in quadtree
    private QTreeNode[] children = new QTreeNode[4]; // children nodes in quadtree

    /**
     * default constructor initializing QTreeNode with default properties.
     */
    public QTreeNode() {
        this.parent = null;
        this.children = new QTreeNode[4];
        this.x = 0;
        this.y = 0;
        this.size = 0;
        this.color = 0;
    }

    /**
     * constructor with parameters to set initial properties of QTreeNode.
     *
     * @param theChildren array of child nodes
     * @param xcoord      X coordinate of node
     * @param ycoord      Y coordinate of node
     * @param theSize     size of node (assumed square)
     * @param theColor    color value of node
     */
    public QTreeNode(QTreeNode[] theChildren, int xcoord, int ycoord, int theSize, int theColor) {
        this.children = theChildren != null ? theChildren : new QTreeNode[4];
        this.x = xcoord;
        this.y = ycoord;
        this.size = theSize;
        this.color = theColor;
        this.parent = null; 
    }

    /**
     * checks if node contains point within its boundaries.
     *
     * @param xcoord X coordinate of point
     * @param ycoord Y coordinate of point
     * @return true if point is within node's boundaries, false otherwise
     */
    public boolean contains(int xcoord, int ycoord) {
        return xcoord >= x && xcoord < x + size && ycoord >= y && ycoord < y + size;
    }

    // getter methods
    public int getx() { return x; }
    public int gety() { return y; }
    public int getSize() { return size; }
    public int getColor() { return color; }
    public QTreeNode getParent() { return parent; }

    // setter methods
    public void setx(int newx) { x = newx; }
    public void sety(int newy) { y = newy; }
    public void setSize(int newSize) { size = newSize; }
    public void setColor(int newColor) { color = newColor; }
    public void setParent(QTreeNode newParent) { parent = newParent; }

    /**
     * retrieves specific child node by index.
     *
     * @param index index of child to retrieve
     * @return child node at specified index
     * @throws QTreeException if index is out of bounds
     */
    public QTreeNode getChild(int index) throws QTreeException {
        if (index < 0 || index >= children.length) {
            throw new QTreeException("Child index out of bounds.");
        }
        return children[index];
    }

    /**
     * sets child node at specified index.
     *
     * @param child child node to set
     * @param index index where child is to be placed
     * @throws QTreeException if index is out of bounds
     */
    public void setChild(QTreeNode child, int index) throws QTreeException {
        if (index < 0 || index >= this.children.length) {
            throw new QTreeException("Child index out of bounds.");
        }
        this.children[index] = child;
    }

    /**
     * determines if node is leaf, i.e., it has no children.
     *
     * @return true if node is leaf, false otherwise
     */
    public boolean isLeaf() {
        for (QTreeNode child : children) {
            if (child != null) return false;
        }
        return true;
    }
}
