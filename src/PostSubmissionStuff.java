import java.io.File;

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
		weightsFileEncoding(fileNames[3]); //Cat in the Hat
		weightsFileEncoding(fileNames[4]); //War and Peace
		
		someFileDecoding(fileNames[2]); //Why did you know i had a soft space for guardians of the galaxy
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
	
}
