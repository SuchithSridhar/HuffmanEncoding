import java.io.IOException;
import java.io.InputStream;

public class BitInputStream {
    private final static int BYTE_SIZE = 8;
    private InputStream input;
    private int nextByte;
    private int numBitsRemaining;

    public BitInputStream(InputStream in) {
        input = in;
        numBitsRemaining = 0;
        nextByte = 0;
    }

    public int readBit() throws IOException {
        if (numBitsRemaining == 0) {
            nextByte = input.read();
            if (nextByte == -1) {
                return -1;
            }

            numBitsRemaining = BYTE_SIZE;
        }
        numBitsRemaining--;
        return (nextByte >> numBitsRemaining) & 1;
    }

    public void close() throws IOException {
    }
}
