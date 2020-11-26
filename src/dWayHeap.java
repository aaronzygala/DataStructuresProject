import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

public class dWayHeap {
    protected final Vector<Node> dWayHeap;
    protected int heapSize;
    private final int d;

    public dWayHeap(int inputD){
        dWayHeap = new Vector<>();
        heapSize = 0;
        d = inputD;
    }
    public dWayHeap(HashMap<Integer, Integer> inputArray, int inputD){
        Collection<Node> nodeArray = new ArrayList<>();
        inputArray.forEach((k, v) -> nodeArray.add(new Node(k, v)));

        dWayHeap = new Vector<>(nodeArray);
        heapSize = inputArray.size();
        d = inputD;
        buildDWayHeap();
    }
    public int Parent(int i){
        return (i - 1)/d;
    }
    public int Child(int i, int j){
        return (i * d) + j;
    }
    public int getMin(){
        return dWayHeap.get(0).value;
    }
    public void swap(int x, int y){
        Node temp = dWayHeap.get(x);
        dWayHeap.set(x, dWayHeap.get(y));
        dWayHeap.set(y, temp);
    }
    public void minHeapify(int i){
        int smallest = i;
        for(int x = 1; x <= d; x++){
            int l = Child(i, x);
            if(l <= heapSize - 1 && dWayHeap.get(l).value < dWayHeap.get(smallest).value){
                smallest = l;
            }
        }
        if(smallest != i){
            swap(i, smallest);
            minHeapify(smallest);
        }

    }
    public void buildDWayHeap(){
        for(int i = (heapSize - 1)/d; i >= 0; i--){
            minHeapify(i);
        }
    }
    public Node extractMin(){
        if(heapSize < 1){
            System.out.println("Trying to extract min from empty heap!");
            return (new Node(0, 0));
        }
        else{
            Node min = dWayHeap.get(0);
            dWayHeap.set(0, dWayHeap.get(heapSize - 1));
            dWayHeap.remove(heapSize - 1);
            heapSize -= 1;
            minHeapify(0);
            return min;
        }
    }
    public void decreaseKey(int i, Node node){
        if(dWayHeap.get(i).value < node.value){
            System.out.println("New key is larger than old key!");
        }
        else{
            dWayHeap.set(i, node);
            while(i > 0 && dWayHeap.get(Parent(i)).value > dWayHeap.get(i).value){
                //swap minHeap[i] and its Parent
                swap(i, Parent(i));
                i = Parent(i);
            }
        }
    }
    public void insert(Node node){
        heapSize += 1;
        dWayHeap.add(new Node(Integer.MAX_VALUE, 0));
        decreaseKey(heapSize - 1, node);
    }
}
