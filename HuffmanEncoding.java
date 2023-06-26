import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * A binary tree to represent a node in
 * the Huffman Encoding method.
 */
class HuffmanTree implements Comparable<HuffmanTree> {
    HuffmanTree left;
    HuffmanTree right;
    Character character;
    int frequency;

    @Override
    public int compareTo(HuffmanTree b) {
        return Integer.compare(frequency, b.frequency);
    }

    public String serialize() {
        String s;
        if (this.character == null) {
            s = "null";
        } else {
            s = "" + ((int) this.character);
        }

        return (s + "|" + this.frequency
                + "," + (this.left == null ? "#" : this.left.serialize())
                + "," + (this.right == null ? "#" : this.right.serialize()));
    }

    public static HuffmanTree deserialize(String data) {
        data = data.replace("\n", "");
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return buildTree(queue);
    }

    private static HuffmanTree buildTree(Queue<String> queue) {
        String s = queue.poll();
        if (s.equals("#")) {
            return null;
        }
        HuffmanTree root = new HuffmanTree();
        String nodeString[] = s.split("\\|");
        if (nodeString[0].equals("null")) {
            root.character = null;
        } else {
            root.character = (char) Integer.parseInt(nodeString[0]);
        }
        root.frequency = Integer.parseInt(nodeString[1]);
        root.left = buildTree(queue);
        root.right = buildTree(queue);
        return root;
    }
}

class HuffManEncoding {

    public static HashMap<Character, Integer> calculateFrequencyTable(String inputText) {
        HashMap<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputText.toCharArray()) {
            frequencyTable.put(c, frequencyTable.getOrDefault(c, 0) + 1);
        }
        return frequencyTable;
    }

    public static HuffmanTree huffmanTreeFromFrequencyTable(HashMap<Character, Integer> frequencyTable) {
        PriorityQueue<HuffmanTree> priorityQueue = new PriorityQueue<>(frequencyTable.size());
        for (Map.Entry<Character, Integer> entry : frequencyTable.entrySet()) {
            HuffmanTree node = new HuffmanTree();
            node.character = entry.getKey();
            node.frequency = entry.getValue();
            priorityQueue.add(node);
        }

        HuffmanTree first, second;

        while (priorityQueue.size() > 1) {
            first = priorityQueue.poll();
            second = priorityQueue.poll();
            HuffmanTree node = new HuffmanTree();
            node.character = null; // placeholder value
            node.frequency = first.frequency + second.frequency;
            node.left = first;
            node.right = second;
            priorityQueue.add(node);
        }

        HuffmanTree huffmanTree = priorityQueue.poll();

        return huffmanTree;
    }

    public static HashMap<Character, String> generateStringHuffmanCodes(HuffmanTree ht) {
        HashMap<Character, String> huffmanCodes = new HashMap<>();
        generateStringHuffmanCodes(huffmanCodes, ht, "");
        return huffmanCodes;
    }

    private static void generateStringHuffmanCodes(HashMap<Character, String> huffmanCodes, HuffmanTree node,
            String code) {
        if (node == null)
            return;

        if (node.character != null) {
            huffmanCodes.put(node.character, code);
        }

        generateStringHuffmanCodes(huffmanCodes, node.left, code + "0");
        generateStringHuffmanCodes(huffmanCodes, node.right, code + "1");
    }

    public static String encodeTextToString(String text, HashMap<Character, String> huffmanCodes) {
        StringBuilder encodedText = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedText.append(huffmanCodes.get(c));
        }

        return encodedText.toString();
    }

    public static String decodeStringToText(String huffmanString, HuffmanTree huffmanTree) {
        StringBuilder text = new StringBuilder();
        HuffmanTree current = huffmanTree;

        for (char character : huffmanString.toCharArray()) {
            if (character == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.character != null) {
                text.append(current.character);
                current = huffmanTree;
            }
        }

        return text.toString();
    }

}
