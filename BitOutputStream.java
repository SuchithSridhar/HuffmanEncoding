import java.io.IOException;
import java.io.OutputStream;

public class BitOutputStream {
    private final static int BYTE_SIZE = 8;
    private final static int PADDING_BIT = 0;
    private OutputStream output;
    private int currentByte;
    private int numBitsInCurrentByte;
    private int numBitsRemaining;

    public BitOutputStream(OutputStream out) {
        output = out;
        currentByte = 0;
        numBitsInCurrentByte = 0;
        numBitsRemaining = 0;
    }

    public void writeBit(int bit) throws IOException {
        if (bit != 0 && bit != 1)
            throw new IllegalArgumentException("Argument must be 0 or 1");

        currentByte = currentByte << 1 | bit;
        numBitsInCurrentByte++;
        if (numBitsInCurrentByte == BYTE_SIZE) {
            // Only write the last 8 bytes of the integer
            output.write(currentByte);
            numBitsInCurrentByte = 0;
        }
    }

    public void close() throws IOException {
        while (numBitsInCurrentByte != 0) {
            writeBit(PADDING_BIT);
        }
    }
}
