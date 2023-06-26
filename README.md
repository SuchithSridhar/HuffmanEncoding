# Huffman Encoding Compression Algorithm

This is a simple implementation of the Huffman Encoding Algorithm ( [wikipedia](https://en.wikipedia.org/wiki/Huffman_coding) ). Here I use this encoding method to build a program to compress ASCII text.

## Compilation

To compile the java files, just run:

```sh
javac *.java
```

## Usage

Once you have the compiled classes, you can run the program in the following way:

Encode text:

```sh
java HuffmanCompressionDriver encode -i <input file>
```

Decode text:

```sh
java HuffmanCompressionDriver decode -i <input file>
```

To have more control over the program, the following options are available:
- `-o <file>`: Specify the output file.
- `-t <file>`: Specify the tree file.
- `-a`: Flag to enable ASCII mode, writes ASCII 0s and 1s rather than a binary file.

Here are some examples:

```sh
java HuffmanCompressionDriver encode -i text-for-compression.txt -o compressed-text.txt -t tree-file.txt
java HuffmanCompressionDriver decode -i compressed-text.txt -o decoded-text.txt -t tree-file.txt
```


Example of complete run:
```
$ cat text-for-compression.txt
In computer science and information theory, a Huffman code is a particular type of optimal prefix code that is commonly used for lossless data compression. The process of finding or using
 such a code proceeds by means of Huffman coding, an algorithm developed by David A. Huffman while he was a Sc.D. student at MIT, and published in the 1952 paper "A Method for the Constru
ction of Minimum-Redundancy Codes".[1]

The output from Huffman's algorithm can be viewed as a variable-length code table for encoding a source symbol (such as a character in a file). The algorithm derives this table from the e
stimated probability or frequency of occurrence (weight) for each possible value of the source symbol. As in other entropy encoding methods, more common symbols are generally represented 
using fewer bits than less common symbols. Huffman's method can be efficiently implemented, finding a code in time linear to the number of input weights if these weights are sorted.[2] Ho
wever, although optimal among methods encoding symbols separately, Huffman coding is not always optimal among all compression methods - it is replaced with arithmetic coding[3] or asymmet
ric numeral systems[4] if a better compression ratio is required.

$ javac *.java                                                             

$ java HuffmanCompressionDriver encode -i text-for-compression.txt        
Output file not explicity provided, defaulted to: text-for-compression.txt.encoded
Tree file not explicity provided, defaulted to: text-for-compression.txt.tree

$ java HuffmanCompressionDriver decode -i text-for-compression.txt.encoded
Output file not explicity provided, defaulted to: text-for-compression.txt.decoded
Tree file not explicity provided, defaulted to: text-for-compression.txt.tree

$ cat text-for-compression.txt.decoded        
In computer science and information theory, a Huffman code is a particular type of optimal prefix code that is commonly used for lossless data compression. The process of finding or using
 such a code proceeds by means of Huffman coding, an algorithm developed by David A. Huffman while he was a Sc.D. student at MIT, and published in the 1952 paper "A Method for the Constru
ction of Minimum-Redundancy Codes".[1]

The output from Huffman's algorithm can be viewed as a variable-length code table for encoding a source symbol (such as a character in a file). The algorithm derives this table from the e
stimated probability or frequency of occurrence (weight) for each possible value of the source symbol. As in other entropy encoding methods, more common symbols are generally represented 
using fewer bits than less common symbols. Huffman's method can be efficiently implemented, finding a code in time linear to the number of input weights if these weights are sorted.[2] Ho
wever, although optimal among methods encoding symbols separately, Huffman coding is not always optimal among all compression methods - it is replaced with arithmetic coding[3] or asymmet
ric numeral systems[4] if a better compression ratio is required.

$ diff text-for-compression.txt.decoded text-for-compression.txt                                 

$ ls text-for-compression.txt text-for-compression.txt.encoded text-for-compression.txt.decoded text-for-compression.txt.tree
.rw-r--r-- 1.2k suchi 23 Jun 21:48 text-for-compression.txt
.rw-r--r-- 1.2k suchi 26 Jun 20:05 text-for-compression.txt.decoded
.rw-r--r--  692 suchi 26 Jun 20:05 text-for-compression.txt.encoded
.rw-r--r--  736 suchi 26 Jun 20:05 text-for-compression.txt.tree
```

Here we see that the compressed text takes up `692` bytes while the uncompressed takes up `1.2k` bytes. However, we see that the tree-file that's required to decompress the file takes up `736` bytes of space. That said, the tree file is not going to be insignificant to larger files. In this particular case we see that the compression doesn't help the size of the file but if we were to compress a large file, it would.
