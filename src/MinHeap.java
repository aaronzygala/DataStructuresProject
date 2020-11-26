import java.util.Collection;
import java.util.HashMap;

public class MinHeap extends dWayHeap{

    public MinHeap(){
        super(2);
    }
    public MinHeap(HashMap<Integer, Integer> inputArray){
        super(inputArray, 2);
    }
    @Override
    public int Parent(int i){
        return (i - 1)/2;
    }
    public int Left(int i){
        return (2 * i) + 1;
    }
    public int Right(int i){
        return (2 * i) + 2;
    }
    @Override
    public void minHeapify(int i){
        int left = Left(i);
        int right = Right(i);
        int smallest;
        if(left <= heapSize - 1 && dWayHeap.get(left).value < dWayHeap.get(i).value) {
            smallest = left;
        }
        else{
            smallest = i;
        }
        if(right <= heapSize - 1 && dWayHeap.get(right).value < dWayHeap.get(smallest).value){
            smallest = right;
        }
        if(smallest != i){
            //swap minHeap[i] with minHeap[smallest]
            swap(i, smallest);
            minHeapify(smallest);
        }

    }
}
