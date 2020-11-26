import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class HuffmanTree {
    private final HashMap<Integer, Integer> freq;
    private Node root;
    private Node decodeRoot;

    public HuffmanTree(){
        freq = new HashMap<>();
    }
    public void findFrequencyTable(String fileName) throws IOException {
        Reader file = new FileReader(fileName);
        BufferedReader br = new BufferedReader(file);
        String line = br.readLine();

        while (line != null) {
            Integer key = Integer.parseInt(line);
            freq.merge(key, 1, Integer::sum);
            line = br.readLine();
        }
    }
    public void buildTreeBinaryHeap(HashMap<Integer, Integer> freqTable){
        MinHeap minHeap = new MinHeap(freqTable);

        while(minHeap.heapSize != 1){
            Node left, right, newRoot;

            left = minHeap.extractMin();
            right = minHeap.extractMin();

            newRoot = new Node(-1, left.value + right.value, left, right);

            minHeap.insert(newRoot);
        }
        this.root = minHeap.extractMin();
    }
    public void buildTree4WayHeap(HashMap<Integer, Integer> freqTable){
        dWayHeap fourWayHeap = new dWayHeap(freqTable, 4);

        while(fourWayHeap.heapSize != 1){
            Node left, right, newRoot;
            left = fourWayHeap.extractMin();
            right = fourWayHeap.extractMin();

            newRoot = new Node(-1, left.value + right.value, left, right);

            fourWayHeap.insert(newRoot);
        }
        this.root = fourWayHeap.extractMin();
    }
    public void buildTreePairingHeap(HashMap<Integer, Integer> freqTable){
        PairingHeap pairingHeap = new PairingHeap(freqTable);

        while(pairingHeap.heapSize != 1){
            Node left, right, newRoot;
            left = pairingHeap.extractMin();
            right = pairingHeap.extractMin();

            newRoot = new Node(-1, left.value + right.value, left, right);

            pairingHeap.insert(newRoot);
        }
        this.root = pairingHeap.extractMin();
    }
    public void ComparePriorityQueueTimes(HuffmanTree huffmanTree) throws IOException {
        long startTime, endTime, timeElapsed;

        startTime = System.nanoTime();
        // binary heap
        for(int i = 0; i < 10; i++){ //run 10 times on given data set
            huffmanTree.buildTreeBinaryHeap(huffmanTree.freq);
        }
        endTime = System.nanoTime();
        timeElapsed = (endTime - startTime)/1000;
        System.out.println("Time using binary heap (microsecond): " + timeElapsed);

        //4 way Heap
        startTime = System.nanoTime();
        for(int i = 0; i < 10; i++){ //run 10 times on given data set
            huffmanTree.buildTree4WayHeap(huffmanTree.freq);
        }
        endTime = System.nanoTime();
        timeElapsed = (endTime - startTime)/1000;
        System.out.println("Time using 4-way heap (microsecond): " + timeElapsed);

        //pairing heap
        startTime = System.nanoTime();
        for(int i = 0; i < 10; i++){ //run 10 times on given data set
            huffmanTree.buildTreePairingHeap(huffmanTree.freq);
        }
        endTime = System.nanoTime();
        timeElapsed = (endTime - startTime)/1000;
        System.out.println("Time using pairing heap (microsecond): " + timeElapsed);
    }
    public void buildCodeTable(Node r, String path, HashMap<Integer, String> codeTable){
        if (r.left != null)
        {
            buildCodeTable(r.left, path+"0", codeTable);
        }

        if (r.right != null)
        {
            buildCodeTable(r.right, path+"1", codeTable);
        }

        if (r.isLeaf())
        {
            codeTable.put(r.key, path);
        }
    }
    public void writeCodeTable(HashMap<Integer, String> codeTable) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("code_table.txt"));
        codeTable.forEach((k, v) -> {
            try {
                writer.write(k + " " + v + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        writer.close();

    }
    public void writeEncodedData(String inputFile, HashMap<Integer, String> codeTable) throws IOException {
        Reader file = new FileReader(inputFile);
        BufferedReader br = new BufferedReader(file);
        OutputStream writer = new FileOutputStream("encoded.bin");

        String line = br.readLine();

        while (line != null) {
            String binary = codeTable.get(Integer.parseInt(line));
            int binaryInt = Integer.parseInt(binary, 2);
            writer.write(binaryInt);
            line = br.readLine();
        }
        writer.close();
    }
    public void buildDecodeTree(HashMap<Integer, String> codeTable){
        Node newRoot = new Node(0, 0);
        codeTable.forEach((k, v) -> {
            Node current = newRoot;
            for(int i = 0; i < v.length(); i++){
                if(v.charAt(i) == '0'){
                    if(current.left == null){
                        current.left = new Node(-1, -1);;
                    }
                    current = current.left;
                }
                else if(v.charAt(i) == '1'){
                    if(current.right == null){
                        current.right = new Node(-1, -1);;
                    }
                    current = current.right;
                }
                if(i == v.length() - 1){
                    current.key = k;
                }
            }
        });
        this.decodeRoot = newRoot;
    }
    public void writeDecodedData(String inputFile) throws IOException {
        InputStream reader = new FileInputStream(inputFile);
        byte[] array = reader.readAllBytes();
        String binaryString = new String(array);
        System.out.println(binaryString);
        reader.close();
    }
    public static void main(String[] args) throws IOException {
        HuffmanTree tg = new HuffmanTree();
        tg.findFrequencyTable("src/sample_input_small.txt");
        tg.buildTreePairingHeap(tg.freq);
        HashMap<Integer, String> codeTable = new HashMap<>();
        tg.buildCodeTable(tg.root, "", codeTable);
        //tg.writeCodeTable(codeTable);
        //tg.writeEncodedData("src/sample_input_small.txt", codeTable);
        tg.buildDecodeTree(codeTable);
        tg.writeDecodedData("encoded.bin");
    }
}
