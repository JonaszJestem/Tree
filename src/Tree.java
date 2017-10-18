/**
 * Created by Jonasz on 6/5/2017.
 */
@SuppressWarnings("unchecked")
class Tree<T extends Comparable<T>>{
    private Node root;

    /**
     * Some functions that invokes other function in tree to trick java that I use proper types of objects
     * @param n
     */

    void insert(Object n) {
        add((T) n);
        printTree(root);
    }
    String doSearch(Object n) {
        if(search((T) n) != null) return "FOUND";
        return "NOT FOUND";
    }

    void doRemove(Object n) {
        remove(search((T)n), root);
    }

    String draw() {
        return toString(root);
    }

    /**
     * Inserts given value into the tree
     * @param n
     */
    private void add(T n) {
        if(root == null) {
            root = new Node(n, null);
        }
        else {
            Node activeNode = root;
            Node parent = null;
            while(true) {
                if(n.compareTo(activeNode.getValue()) < 0) {
                    if(activeNode.left == null) {
                        activeNode.left = new Node(n, parent);
                        break;
                    }
                    else {
                        parent = activeNode;
                        activeNode = activeNode.left;
                    }
                }
                else {
                    if(activeNode.right == null) {
                        activeNode.right = new Node(n, parent);
                        break;
                    }
                    else {
                        parent = activeNode;
                        activeNode = activeNode.right;
                    }
                }
            }
        }
    }

    /**
     * Searches for given value in tree
     * @param n
     * @return returns the Node with value, null otherwise
     */

    private Node search(T n) {
        if(root == null) return null;
        Node activeNode = root;

        while(activeNode.getValue().compareTo(n) != 0) {
            if(n.compareTo(activeNode.getValue())<0) {
                activeNode = activeNode.left;
            }
            else {
                activeNode = activeNode.right;
            }
            if(activeNode == null) break;
        }

        return activeNode;
    }

    /**
     * Prints the tree inorder
     * @param activeNode
     */

    private void printTree(Node activeNode) {
        if(activeNode != null) {
            printTree(activeNode.left);
            String leftChild = activeNode.left!=null ? activeNode.left.getValue().toString() : "NULL";
            String rightChild = activeNode.right!=null ? activeNode.right.getValue().toString() : "NULL";
            System.out.println(activeNode.getValue() + " - Left Child: \t" + leftChild + "\t" + "Right child: \t" + rightChild);
            printTree(activeNode.right);
        }
    }

    /**
     * Removes given element
     * @param z
     * @param r
     * @return
     */

    private Node remove(Node z, Node r) {
        if (r == null) return null;
        if (z.value.compareTo(r.value)<0)
            r.left = remove(z, r.left);
        else if (z.value.compareTo(r.value)>0)
            r.right = remove(z, r.right);
        else
        {
            if (r.left == null)
            {
                return r.right;
            }
            else if (r.right == null)
            {
                return r.left;
            }
            Node temp = min(r.right);
            r.value = temp.value;
            r.right = remove(temp, r.right);
        }
        return r;
    }

    /**
     * Finds the minimal element in tree
     * @param activeNode
     * @return
     */
    private Node min(Node activeNode) {
        while(activeNode.left != null) {
            activeNode = activeNode.left;
        }
        return activeNode;
    }

    /**
     * Overrides methor toString for pretty printing the tree
     * @param r
     * @return
     */
    private String toString(Node r){
        String leftChild, rightChild;


        if(r==null)
            return "";
        else
            leftChild = r.left!=null ? r.left.getValue().toString() : "NULL";
            rightChild = r.right!=null ? r.right.getValue().toString() : "NULL";
            return toString(r.left) + " " +r.value + " - Left child: " + leftChild + " - Right child: " + rightChild+"<br>" +toString(r.right);
    }

    /**
     * Node that represents element in tree, references to left child, right child and parent
     * Holds value of the element
     */
    class Node {
        T value;
        Node left = null;
        Node right = null;
        final Node parent;

        Node(T value, Node parent) {
            this.value = value;
            this.parent = parent;
        }

        T getValue() {
            return value;
        }
    }
}

