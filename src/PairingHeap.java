import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class PairingHeap {

    protected Node root;
    protected int heapSize;

    public PairingHeap(){
        root = null;
        heapSize = 0;
    }
    public PairingHeap(HashMap<Integer, Integer> inputArray){
        Collection<Node> nodeArray = new ArrayList<>();
        inputArray.forEach((k, v) -> nodeArray.add(new Node(k, v)));

        root = null;
        buildPairingHeap(nodeArray);
    }
    public PairingHeap(Node r){
        root = r;
        heapSize = 1;
    }
    public PairingHeap(HashMap<Integer, Integer> inputArray, Node r){
        Collection<Node> nodeArray = new ArrayList<>();
        inputArray.forEach((k, v) -> nodeArray.add(new Node(k, v)));

        root = r;
        buildPairingHeap(nodeArray);
    }
    public boolean isEmpty() {
        return root == null;
    }
    public Node getMin(){
        return root;
    }
    public void insert(Node node){
        if(isEmpty()){
            root = node;
            heapSize = 1;
        }
        else{
            this.root = meld(root, node);
            heapSize += 1;
        }
    }
    public Node meld(Node A, Node B){
        PairingHeap returnHeap = new PairingHeap();
        if(A == null && B == null){
            System.out.println("Attempting to merge two empty heaps!");
        }
        else if(A == null){
            returnHeap.insert(B);
        }
        else if(B == null){
            returnHeap.insert(A);
        }
        else if(A.value >= B.value){
            if(B.pairingChild != null){
                B.pairingChild.pairingLeftSibling = A;
                A.pairingRightSibling = B.pairingChild;
            }
            A.pairingLeftSibling = B;
            B.pairingChild = A;
            returnHeap.root = B;
        }
        else{
            if(A.pairingChild != null){
                A.pairingChild.pairingLeftSibling = B;
                B.pairingRightSibling = A.pairingChild;
            }
            B.pairingLeftSibling = A;
            A.pairingChild = B;
            returnHeap.root = A;
        }
        return returnHeap.root;
    }
    public void decreaseKey(Node node, int value){
        if(node.value < value){
            System.out.println("New key is larger than old key!");
        }
        else{
            if(node.pairingLeftSibling.pairingChild == node){
                //node is first in child list
                node.pairingLeftSibling.pairingChild = node.pairingRightSibling;
            }
            else{
                node.pairingLeftSibling.pairingRightSibling = node.pairingRightSibling;
            }
            node.pairingRightSibling.pairingLeftSibling = node.pairingLeftSibling;
            node.pairingLeftSibling = node.pairingRightSibling = null;
            node.value = value;
            this.root = meld(root, node);
        }
    }
    public Node combineSiblings(Node firstSibling){
        if(firstSibling.pairingRightSibling == null){
            if(firstSibling.pairingLeftSibling != null)
                firstSibling.pairingLeftSibling = null;

            return firstSibling;
        }
        Vector<Node> subtreeList = new Vector<>();
        Vector<Node> meldedList = new Vector<>();

        while(firstSibling != null){
            subtreeList.add(firstSibling);
            if(subtreeList.size() > 1)//if not the first in the sibling list
                firstSibling.pairingLeftSibling.pairingRightSibling = null;
            firstSibling.pairingLeftSibling = null;

            firstSibling = firstSibling.pairingRightSibling;
        }

        //Pass 1: Meld pairs of subtrees left to right
        for(int x = 0; x < subtreeList.size() - 1; x += 2){
            meldedList.add(meld(subtreeList.get(x), subtreeList.get(x + 1)));
        }
        //If there is an odd number of siblings, add last one
        if(subtreeList.size() % 2 != 0){
            meldedList.set(meldedList.size() - 1, (meld(meldedList.get(meldedList.size() - 1), subtreeList.get(subtreeList.size() - 1))));
        }
        //Pass 2: Meld backwards into working tree
        Node workingTree = meldedList.get(meldedList.size() - 1);
        Node finalTree = workingTree;
        for(int y = meldedList.size() - 2; y >= 0; y--){
            finalTree = meld(finalTree, meldedList.get(y));
        }
        return finalTree;
    }
    public void removeMin(){
        if(root.pairingChild == null){ //if only one node
            root = null;
        }
        else{
            //combine siblings of all children
            root = combineSiblings(root.pairingChild);
        }
    }
    public Node extractMin(){
        Node min = null;
        //remove root node, iterate through child sibling list w/ two pass scheme melding each.
        if(isEmpty()){
            System.out.println("Cannot extract min of empty tree!");
        }
        else{
            min = getMin();
            removeMin();
        }
        this.heapSize -= 1;
        return min;
    }
    public void buildPairingHeap(Collection<Node> inputArray){
        //insert values from an array amortized O(n)
        for(Node i: inputArray){
            insert(i);
        }
    }
}
