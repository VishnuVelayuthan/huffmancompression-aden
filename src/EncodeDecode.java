import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.PriorityQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class EncodeDecode. This is the controller of the Huffman project...
 */
public class EncodeDecode {
	
	/** The encode map. */
	private String[] encodeMap;
	
	/** The huff util. */
	private HuffmanCompressionUtilities huffUtil;
	
	/** The gui. */
	private EncodeDecodeGUI gui;
	
	/** The gw.  This is used to generate the frequency weights if no weights file is specified */
	private GenWeights gw;
	
	/** The bin util. This will be added in part 3 */
	//private BinaryIO binUtil;
	
	/** The input. */
	private BufferedInputStream input;
	
	/** The output. */
	private BufferedOutputStream output;
	
	/** The array for storing the frequency weights*/
	private int[] weights;
	
	private BinaryIO binUtil; 
	
	private HuffmanTreeNode root;
	
	/**
	 * Instantiates a new encode decode.
	 *
	 * @param gui the gui
	 */
	public EncodeDecode (EncodeDecodeGUI gui) {
		this.gui = gui;
		huffUtil = new HuffmanCompressionUtilities();
		binUtil = new BinaryIO();
		gw = new GenWeights();
	}
	
	
	/**
	 * Encode. This function will do the following actions:
	 *         1) Error check the inputs
	 * 	       - Perform error checking on the file to encode
	 *         - Generate the array of frequency weights - either read from a file in the output/ directory
	 *           or regenerate from the file to encode in the data/ directory
	 *         - Error check the output file...
	 *         Any errors will abort the conversion...
	 *         
	 *         2) set the weights in huffUtils
	 *         3) build the Huffman tree using huffUtils;
	 *         4) create the Huffman codes by traversing the trees.
	 *         
	 *         In part 3, you will call executeEncode to perform the conversion.
	 *
	 * @param fName the f name
	 * @param bfName the bf name
	 * @param freqWts the freq wts
	 * @param optimize the optimize
	 */
	void encode(String fName, String bfName, String freqWts, boolean optimize) {
		if(fName.isEmpty() || freqWts.isEmpty()) {
			gui.errorAlert("File is Empty", "Type valid file name");
			return;
		}
		
		File inFile = new File("data/" + fName);
		
		//File does not exist
		if(!inFile.exists()) {
			gui.errorAlert("File does not exist", "Type valide file name"); 
			return;
		}
			
		//Cannot read file
		if(!inFile.canRead()) {
			gui.errorAlert("Can't read file", "Type valid file name");
			return;
		}
		
		File frequencyFile = new File("output/" + freqWts);
		
		
		
		weights = huffUtil.readFreqWeights(frequencyFile); 
		huffUtil.setWeights(weights);
		
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes();
		
		File binFile = new File("output/" + bfName); 
		
		this.executeEncode(inFile, binFile);		
		
	}
	
	/**
	 * Execute encode. This function will write compressed binary file as part of part 3
	 * 
	 * This functions should:
	 * 1) get the encodeMap from HuffUtils 
	 * 2) for each character in the file, use the encodeMap to find the binary string that will
	 *    represent that character. The bits will accumulate and then be written to the compressed file
	 *    at byte granularity as long as the length is > 0)... 
	 * 3) when the input file is exhausted, write the EOF character (this should cause the file to be flushed
	 *    and closed). 
	 *
	 * @param inFile the File object that represents the file to be compressed
	 * @param binFile the File object that represents the compressed output file
	 */
	void executeEncode(File inFile, File binFile) {
		encodeMap = huffUtil.getEncodeMap(); 
		
		try(BufferedReader bufferRead = new BufferedReader(new FileReader(inFile))) {
			
			binUtil.openInputFile(inFile);
			binUtil.openOutputFile(binFile);
			
			String newLine;
			String binStr; 
			int charInt;
			while((newLine=bufferRead.readLine()) != null) {
				for(char character : newLine.toCharArray()) {
					charInt = (int)character;
					if (charInt < 128) {
						binStr = encodeMap[charInt]; 
						binUtil.convStrToBin(binStr);
					}
				}
			}
			binUtil.writeEOF(encodeMap[0]);
			
		} catch (IOException e) {
			gui.errorAlert("Input file error", "Error in reading file");
			return;
		}
	}
	
	/**
	 * Decode. This function will only be addressed in part 3. It will 
	 *         1) Error check the inputs
	 * 	       - Perform error checking on the file to decode
	 *         - Generate the array of frequency weights - this MUST be provided as a file
	 *         - Error check the output file...
	 *         Any errors will abort the conversion...
	 *         
	 *         2) set the weights in huffUtils
	 *         3) build the Huffman tree using huffUtils;
	 *         4) create the Huffman codes by traversing the trees.
	 *         5) executeDecode
	 *
	 * @param bfName the bf name
	 * @param ofName the of name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void decode(String bfName, String ofName, String freqWts,boolean optimize) {
		if(bfName.isEmpty() || freqWts.isEmpty()) {
			gui.errorAlert("File is Empty", "Type valid file name");
			return;
		}
		
		File binInFile = new File(bfName); 
		
		//File does not exist
		if(!binInFile.exists()) {
			gui.errorAlert("File does not exist", "Type valide file name"); 
			return;
		}
			
		//Cannot read file
		if(!binInFile.canRead()) {
			gui.errorAlert("Can't read file", "Type valid file name");
			return;
		}
		
		File frequencyFile = new File(freqWts);
		
		weights = huffUtil.readFreqWeights(frequencyFile); 
		huffUtil.setWeights(weights);
		
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes();
		
		
		try {
			File binOutFile = new File(ofName);
			this.executeDecode(binInFile, binOutFile);
		} 
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Execute decode.  - This is part of PART3...
	 * This function performs the decode of the binary(compressed) file.
	 * It will read each byte from the file and convert it to a string of 1's and 0's
	 * This will be appended to any leftover bits from prior conversions.
	 * Starting from the head of the string, decode occurs by traversing the Huffman Tree from the root
	 * until a Leaf node is reached. If a leaf node is reached, the character is written to the output
	 * file, and the corresponding # of bits is removed from the string. If the end of the bit string is reached
	 * with reaching a leaf node, the next byte is processed, and so on...
	 * After completely decoding the file, the output file is flushed and closed
	 *
	 * @param binFile the bin file
	 * @param outFile the out file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void executeDecode(File binFile, File outFile) throws IOException {
		encodeMap = huffUtil.getEncodeMap(); 
		
		try(BufferedReader bufferRead = new BufferedReader(new FileReader(binFile))) {
			
			binUtil.openInputFile(binFile);
			binUtil.openOutputFile(outFile);
			
			String newLine;
			String binStr;
			
			while((newLine=bufferRead.readLine()) != null) {
				binUtil.convStrToBin(newLine);
				
			}
		}
	}
		
	
	
}
