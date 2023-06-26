import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class HuffmanCompressionDriver {

    private static boolean writeFile(String text, String filename) {
        try {
            FileWriter fw = new FileWriter(filename);
            fw.write(text);
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static String readFile(String filename) {
        try {
            StringBuilder text = new StringBuilder();
            File f = new File(filename);
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                text.append(sc.nextLine());
                text.append("\n");
            }
            sc.close();
            return text.toString();
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static void encodeFile(String inputFile, String outputFile, String treeFile) {
        String inputData = readFile(inputFile);
        HashMap<Character, Integer> freqTable = HuffManEncoding.calculateFrequencyTable(inputData);
        HuffmanTree hft = HuffManEncoding.huffmanTreeFromFrequencyTable(freqTable);
        HashMap<Character, String> hfTable = HuffManEncoding.generateStringHuffmanCodes(hft);
        String encodedData = HuffManEncoding.encodeTextToString(inputData, hfTable);
        String treeString = hft.serialize();

        writeFile(treeString, treeFile);
        writeFile(encodedData, outputFile);
    }

    private static void decodeFile(String inputFile, String outputFile, String treeFile) {
        String inputData = readFile(inputFile);
        String treeData = readFile(treeFile);
        HuffmanTree hft = HuffmanTree.deserialize(treeData);
        String decodedData = HuffManEncoding.decodeStringToText(inputData, hft);

        writeFile(decodedData, outputFile);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide an operation (encode / decode) and an input file.");
            System.out.println("    Example: java HuffmanEncoding encode -i hello.txt");
            return;
        }

        String command = "";
        String inputFile = "";
        String outputFile = "";
        String treeFile = "";
        String element = "";

        for (int i = 0; i < args.length; i++) {
            element = args[i];
            if (element.equalsIgnoreCase("encode") || element.equalsIgnoreCase("decode")) {
                command = element.toLowerCase();
            } else if (element.equals("-i") && i+1 < args.length) {
                inputFile = args[i+1];
                i++;
            } else if (element.equals("-o") && i+1 < args.length) {
                outputFile = args[i+1];
                i++;
            } else if (element.equals("-t") && i+1 < args.length) {
                treeFile = args[i+1];
                i++;
            }
        }

        if (command.equals("") || inputFile.equals("")) {
            System.out.println("Please provide an operation (encode / decode) and an input file.");
            System.out.println("    Example: java HuffmanEncoding encode -i hello.txt");
            return;
        }

        // Check if file exists
        {
            File f = new File(inputFile);
            if (!f.exists() || f.isDirectory()) {
                System.out.println("The provided input file '" + inputFile + "' is not present.");
                return;
            }
        }

        if (outputFile.equals("")) {
            if (command == "encoded") {
                outputFile = inputFile + ".encoded";
            } else {
                outputFile = inputFile + ".decoded";
            }

            System.out.println("Output file not explicity provided, defaulted to: " + outputFile);
        }

        if (treeFile.equals("")) {
            treeFile = inputFile + ".tree";
            System.out.println("Tree file not explicity provided, defaulted to: " + treeFile);
        }

        // Check if file exists
        if (command.equals("decode")) {
            File f = new File(treeFile);
            if (!f.exists() || f.isDirectory()) {
                System.out.println("The set tree file '" + treeFile + "' is not present.");
                return;
            }

            decodeFile(inputFile, outputFile, treeFile);
        } else {
            encodeFile(inputFile, outputFile, treeFile);
        }
    }
}
