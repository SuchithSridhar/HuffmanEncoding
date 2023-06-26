import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;

/**
 * A binary tree to represent a node in
 * the Huffman Encoding method.
 */
public class HuffmanTree implements Comparable<HuffmanTree> {
    private final static String NULL_CHAR = "n";
    private final static String TERM_CHAR = "#";
    private final static String FREQ_SEP = ":";
    private final static String ELEM_SEP = ",";

    // This is a placeholder to signify the end of the text.
    public final static char TEXT_TERM = 255;

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
            s = NULL_CHAR;
        } else {
            s = "" + ((int) this.character);
        }

        return (s + FREQ_SEP + this.frequency
                + ELEM_SEP + (this.left == null ? TERM_CHAR : this.left.serialize())
                + ELEM_SEP + (this.right == null ? TERM_CHAR : this.right.serialize()));
    }

    public static HuffmanTree deserialize(String data) {
        data = data.replace("\n", "");
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(ELEM_SEP)));
        return buildTree(queue);
    }

    private static HuffmanTree buildTree(Queue<String> queue) {
        String s = queue.poll();
        if (s.equals(TERM_CHAR)) {
            return null;
        }
        HuffmanTree root = new HuffmanTree();
        String nodeString[] = s.split(FREQ_SEP);
        if (nodeString[0].equals(NULL_CHAR)) {
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
