public class Node{
    protected int key;
    protected int value;
    protected Node pairingRightSibling, pairingLeftSibling, pairingChild; //pointers for pairing heaps
    protected Node left, right; //pointers for HuffmanTrees

    public Node(int k, int v){
        key = k;
        value = v;
        pairingRightSibling = null;
        pairingLeftSibling = null;
        pairingChild = null;
        left = null;
        right = null;
    }
    public Node(int k, int v, Node l, Node r){
        key = k;
        value = v;
        pairingRightSibling = null;
        pairingLeftSibling = null;
        pairingChild = null;
        left = l;
        right = r;
    }

    public boolean isLeaf(){
        return (left == null) && (right == null);
    }
}
