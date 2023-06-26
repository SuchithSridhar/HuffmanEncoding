import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    private static void writeBinaryStringToFile(String binaryString, String file) {

        FileOutputStream fos;
        BitOutputStream bos;

        try {
            fos = new FileOutputStream(file);
            bos = new BitOutputStream(fos);

            for (char bit : binaryString.toCharArray()) {
                if (bit == '0') {
                    bos.writeBit(0);
                } else if (bit == '1') {
                    bos.writeBit(1);
                }
            }

            bos.close();
            fos.close();

        } catch (IOException e) {
            System.out.println("File I/O Exception occured when trying to write to file.");
            System.exit(1);
        }

    }

    private static String readBinaryFileToString(String file) {
        FileInputStream fis;
        BitInputStream bis;
        String stream = "";
        int bit;

        try {
            fis = new FileInputStream(file);
            bis = new BitInputStream(fis);

            while ((bit = bis.readBit()) != -1) {
                if (bit == 0) {
                    stream += "0";
                } else {
                    stream += "1";
                }
            }

            bis.close();
            fis.close();

        } catch (IOException e) {
            System.out.println("File I/O exception occured when trying to read from file.");
            System.exit(1);
        }

        return stream;
    }

    private static void encodeFileBinary(String inputFile, String outputFile, String treeFile) {
        String inputData = readFile(inputFile);
        HashMap<Character, Integer> freqTable = HuffmanEncoder.calculateFrequencyTable(inputData);
        HuffmanTree hft = HuffmanEncoder.huffmanTreeFromFrequencyTable(freqTable);
        HashMap<Character, String> hfTable = HuffmanEncoder.generateStringHuffmanCodes(hft);
        String encodedData = HuffmanEncoder.encodeTextToString(inputData, hfTable);
        String treeString = hft.serialize();

        // Add character to terminate the text
        encodedData += hfTable.get(HuffmanTree.TEXT_TERM);

        writeBinaryStringToFile(encodedData, outputFile);
        writeFile(treeString, treeFile);
    }

    private static void decodeFileBinary(String inputFile, String outputFile, String treeFile) {
        String inputData = readBinaryFileToString(inputFile);
        String treeData = readFile(treeFile);
        HuffmanTree hft = HuffmanTree.deserialize(treeData);
        String decodedData = HuffmanEncoder.decodeStringToText(inputData, hft);

        writeFile(decodedData, outputFile);
    }

    private static void encodeFile(String inputFile, String outputFile, String treeFile) {
        String inputData = readFile(inputFile);
        HashMap<Character, Integer> freqTable = HuffmanEncoder.calculateFrequencyTable(inputData);
        HuffmanTree hft = HuffmanEncoder.huffmanTreeFromFrequencyTable(freqTable);
        HashMap<Character, String> hfTable = HuffmanEncoder.generateStringHuffmanCodes(hft);
        String encodedData = HuffmanEncoder.encodeTextToString(inputData, hfTable);
        String treeString = hft.serialize();

        writeFile(treeString, treeFile);
        writeFile(encodedData, outputFile);
    }

    private static void decodeFile(String inputFile, String outputFile, String treeFile) {
        String inputData = readFile(inputFile);
        String treeData = readFile(treeFile);
        HuffmanTree hft = HuffmanTree.deserialize(treeData);
        String decodedData = HuffmanEncoder.decodeStringToText(inputData, hft);

        writeFile(decodedData, outputFile);
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Please provide an operation (encode / decode) and an input file.");
            System.out.println("    Example: java HuffmanEncoder encode -i hello.txt");
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
            } else if (element.equals("-i") && i + 1 < args.length) {
                inputFile = args[i + 1];
                i++;
            } else if (element.equals("-o") && i + 1 < args.length) {
                outputFile = args[i + 1];
                i++;
            } else if (element.equals("-t") && i + 1 < args.length) {
                treeFile = args[i + 1];
                i++;
            }
        }

        if (command.equals("") || inputFile.equals("")) {
            System.out.println("Please provide an operation (encode / decode) and an input file.");
            System.out.println("    Example: java HuffmanEncoder encode -i hello.txt");
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

            decodeFileBinary(inputFile, outputFile, treeFile);
        } else {
            encodeFileBinary(inputFile, outputFile, treeFile);
        }
    }
}
