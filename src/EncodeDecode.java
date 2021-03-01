import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
		
		File frequencyFile = new File(freqWts);
		
		//File does not exist
		if(!frequencyFile.exists()) {
			gui.errorAlert("File does not exist", "Type valide file name"); 
			return;
		}
			
		//Cannot read file
		if(!frequencyFile.canRead()) {
			gui.errorAlert("Can't read file", "Type valid file name");
		}
		
		try(BufferedReader bufferRead = new BufferedReader(new FileReader(frequencyFile))) {
			
			weights = new int[128];
			String newLine; 
			int freqVal;
			int intVal; 
			
			while((newLine=bufferRead.readLine()) != null) {
				intVal = Integer.parseInt(newLine.split(",")[0]); 
				freqVal = Integer.parseInt(newLine.split(",")[1]);
				weights[intVal] = freqVal; 
			}
			
		} catch (IOException e) {
			gui.errorAlert("Input File Error", "Error in reading the file");
			return;
		}
		
		gui.informationAlert("File Read Successfully", "The file desired was read accurately");
			
		//Queue that is ordered with compareWeightOrd
		PriorityQueue<HuffmanTreeNode> pQueue = new PriorityQueue<HuffmanTreeNode>(128,  HuffmanTreeNode.compareWeightOrd); 
		
		//Adding the vals to queue
		for(int ordVal = 0; ordVal < weights.length; ordVal++)
			pQueue.add(new HuffmanTreeNode(ordVal, weights[ordVal])); 
		
		HuffmanTreeNode leftNode;
		HuffmanTreeNode rightNode;
		HuffmanTreeNode parentNode; 
		
		while(pQueue.size() !=1 ) {
			leftNode = (HuffmanTreeNode) pQueue.poll();
			rightNode = (HuffmanTreeNode) pQueue.poll();
			parentNode = new HuffmanTreeNode(-1, leftNode.getWeight() + rightNode.getWeight());
			pQueue.add(parentNode);
			if (pQueue.size() == 1) {
				root = parentNode;
			}
		}
		
		encodeMap = new String[128];
		
		for(int ordVal = 0; ordVal < weights.length; ordVal++) {
			encodeMap[ordVal] = findPath(ordVal, "", root);
			System.out.println(encodeMap[ordVal]);
		}
		
		
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
	}
	
	
	//This needs some work 
	public String findPath(int ordVal, String s, HuffmanTreeNode node) {
		if (node.getOrdValue() == ordVal) {
			return s;
		}
		findPath(ordVal, s + "0", node.getLeft());
		findPath(ordVal, s + "1", node.getRight());
		return "";
		
	}
	
	public static void main(String[] args) {
try(BufferedReader bufferRead = new BufferedReader(new FileReader("output/war-and-peace.txt"))) {
			
			int[] weights = new int[128];
			String newLine; 
			int freqVal;
			int intVal; 
			
			while((newLine=bufferRead.readLine()) != null) {
				intVal = Integer.parseInt(newLine.split(",")[0]); 
				freqVal = Integer.parseInt(newLine.split(",")[1]);
				weights[intVal] = freqVal; 
				System.out.println(weights[intVal]);
			}
			
		} catch (IOException e) {
//			gui.errorAlert("Input File Error", "Error in reading the file");
			return;
		}
	}
	
	
	
}
