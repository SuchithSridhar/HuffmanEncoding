import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanEncoder {

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

        // Term character to signify the end of the encoded text
        HuffmanTree node = new HuffmanTree();
        node.character = HuffmanTree.TEXT_TERM;
        node.frequency = 1;
        priorityQueue.add(node);

        HuffmanTree first, second;

        while (priorityQueue.size() > 1) {
            first = priorityQueue.poll();
            second = priorityQueue.poll();
            node = new HuffmanTree();
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
                if (current.character == HuffmanTree.TEXT_TERM)
                    break;

                text.append(current.character);
                current = huffmanTree;
            }
        }

        return text.toString();
    }

}
