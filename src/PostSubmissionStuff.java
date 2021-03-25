import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class PostSubmissionStuff {
	
	private static EncodeDecodeGUI gui = new EncodeDecodeGUI();
	private static EncodeDecode enc_dec = new EncodeDecode(gui);
	private static HuffmanCompressionUtilities huffUtil = new HuffmanCompressionUtilities();
	private static GenWeights gw = new GenWeights(); 
	
	private static String[] fileNames = new String[] {
			"Green Eggs and Ham", 
	        "simple",
			"Harry Potter and the Sorcerer",
			"The Cat in the Hat", 
			"warAndPeace"
	};
	
	
	public static void main(String[] args) {
//		weightsFileEncoding(fileNames[3]); //Cat in the Hat
//		weightsFileEncoding(fileNames[4]); //War and Peace
//		
//		someFileDecoding(fileNames[2]); //Why did you know i had a soft space for guardians of the galaxy
		compressionAnalysis(); 
	}
	
	
	private static void weightsFileEncoding(String wtsFile) {
		String inFileName;
		String outputFileName;
		String freqWtsFileName = wtsFile + ".csv";
		
		new File("output/twice/" + wtsFile).mkdir();
		
		for(String fileName : fileNames) {
			inFileName = fileName + ".txt";
			outputFileName = "twice/" + wtsFile + "/"
				+ fileName + ".bin";
			enc_dec.encode(inFileName, outputFileName, freqWtsFileName, false);
		}
	}
	
	
	private static void someFileDecoding(String fileName) {
		String binFileName = "some_file_mac.bin";
		String outputFileName;
		String freqWtsFileName;
		
		new File("output/someFileDecode/").mkdir();
		
		outputFileName = "someFileDecode/" + "some-file-" + fileName + ".txt";
		freqWtsFileName = fileName + ".csv";
			
		enc_dec.decode(binFileName, outputFileName, freqWtsFileName, false);
	}
	
	
	private static void compressionAnalysis() {
		new File("analysis").mkdir(); 
		
		File analysisFile = new File("analysis/compressionAnalysis.txt");
		
		
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(analysisFile));
			
			String writeText;
			File pointerFile;
			for(String fileName : fileNames) {
				
				writeText = "-------------------------------------------------------------------\n";
				
				pointerFile = new File("data/" + fileName + ".txt");
				writeText += "Original version of " + fileName + ".txt size: " + pointerFile.length() + "\n\n";
				pw.write(writeText); 
				writeText = ""; 
				
				//For original file
				pointerFile = new File("output/" + fileName + ".bin");
				writeText += "Same Weights version of " + fileName + ".txt size: " + pointerFile.length() + "\n\n";
				pw.write(writeText); 
				writeText = ""; 
				
				//The Cat in the Hat version
				pointerFile = new File("output/twice/The Cat in the Hat/" + fileName + ".bin");
				writeText +=  "The Cat in the Hat version of " + fileName + ".txt size: " 
						+ pointerFile.length() + "\n\n";
				pw.write(writeText); 
				writeText = ""; 
				
				//War and Peace
				pointerFile = new File("output/twice/warAndPeace/" + fileName + ".bin");
				writeText +=  "War and Peace version of " + fileName + ".txt size: " 
						+ pointerFile.length() + "\n";
				pw.write(writeText); 
				writeText = ""; 
				
			}
			
			pw.write("-------------------------------------------------------------------\n");

			pw.write("Analysis of Huffman Compression: \n");
			pw.write("	The Huffman compression is a great file compression algorithm. It compresses the file by almost\n"
					+"	half of the original size. When using the Cat in the Hat and the War and Peace weights files\n"
					+"	the pattern you observe is that for larger files the war and peace seems to be close to the\n"
					+"	original weights compression but a tad bit larger, however, for smaller files, the Cat in the\n"
					+"	Hat seems to be more efficient, but not by that much. I would suppose this exists because War \n"
					+"	and Peace is a large book so it comes closer to having the natural occurence of characters as \n"
					+"	any other large book. I would choose War and Peace because it proves to be more efficient for \n"
					+"	larger files. \n");
			
			pw.write("-------------------------------------------------------------------\n");
			
			pw.write("Oh and, classy choice, Hitch hiker's Guide to the Galaxy, knew INSTANTlY when I read the pity of the whale sperm\n");
			
			pw.write("-------------------------------------------------------------------\n");
			
			pw.write("PS I'm glad you appreciated me showing my understanding of file manipulation\n"
					+"Best, \n"
					+"VishDaGod");
			
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

//				
//		for(String fileName : fileNames) {
//			
//		}
	}
	
	
	
}
