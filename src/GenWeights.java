import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * The Class GenWeights. This class provides a GUI front-end for the user to 
 * specify a file for generating a character-based frequency analysis, and a 
 * file to write the results for later consumption. This is self-contained class
 * in that it does not adhere to the MVC programming model; all work is done inside
 * this class
 */
public class GenWeights extends Application {
	
	/** The pane. */
	private BorderPane pane = new BorderPane();
    
    /** The gp. */
    private GridPane gp = new GridPane();
    
    /** The btn box. */
    private HBox btnBox = new HBox(15);
    
    /** The file handles for input (inf) and output (outf) */
    private File inputFile,outputFile;
    
    /** The weights - the # of occurences of each character with index 0-127. */
    private int[] weights = new int[128]; 
    
    /** The boolean indicating if the weights array contains valid information. */
    private boolean weightsValid = false;
    
	/**
	 * Instantiates a new GenWeights object
	 */
	public GenWeights() {
		initWeights();
	}

	/**
	 * Inits the weights array to 0; should be called in the constructor and
	 * as the first step when the Generate button is pressed.
	 */
	private void initWeights() {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 0;
		}
		weightsValid = false;
	}
	
	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Label hdr = new Label("Generate Frequency Weights");
		hdr.setPrefHeight(40);
		Label lblInput = new Label("Input File: ");
		Label lblOutput = new Label("Output File: ");
		TextField inputFile = new TextField();
		inputFile.setPrefWidth(300);
		TextField outputFile = new TextField();
		outputFile.setPrefWidth(300);
		gp.setVgap(15);
		gp.setHgap(2);

		gp.add(lblInput, 0, 0);
		gp.add(inputFile, 1, 0);
		gp.add(lblOutput, 0, 1);
		gp.add(outputFile, 1, 1);
		pane.setTop(hdr);
		pane.setCenter(gp);
		Button genWt = new Button("Generate");
		Button save = new Button("Save To File");
		
		// TODO #1 create the setOnAction() events to handle button pushes.
		genWt.setOnAction(ae -> this.generateWeights("data/"+inputFile.getText()));
		save.setOnAction(ae -> this.saveWeightsToFile("output/"+outputFile.getText()));
		
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(genWt,save);
		pane.setBottom(btnBox);
		
		Scene scene = new Scene(pane, 400,150);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	/**
	 * Generate character-based frequency weights. You will write this method,
	 * which must address the following cases:
	 * a) if the filename is empty, raise a WARNING alert with an appropriate message and
	 *    return.
	 * b) if the file does not exist or is empty, raise a WARNING alert with an appropriate 
	 *    message and return.
	 * c) if the file exists and is not empty, but is not readable, raise a WARNING alert with 
	 *    an appropriate message and return.
	 * 
	 * Each of those cases should be differentiated to the user
	 * 
	 * Assuming that the requirements of a, b and c have all been met successfully, 
	 * initialize the weights and read the input file character by character to update 
	 * the weights.. review the notes in Student Materials from today's class if you 
	 * have questions. Note that it might make your code cleaner if the actual reading of
	 * the file was done in a helper method. 
	 * 
	 * Once the input file has been fully processed, you should print the weights to the console.
	 *
	 * @param infName - the name of the file to read; should come from the appropriate
	 *                  text field
	 * @throws FileNotFoundException 
	 *              
	 */
	private void generateWeights(String infName){
		
		//File name Empty Check 
		if(infName.isEmpty()) {
			errorAlert("File is Empty", "Type valid file name");
			return;
		}
		
		inputFile = new File(infName);
		
		//File does not exist
		if(!inputFile.exists()) {
			errorAlert("No File Found", "Type an appropriate file name"); 
			return;
		}
		
		//Cannot read file
		if(!inputFile.canRead()) {
			errorAlert("Can't Read File", "Cannot read inputted file");
			return;
		}
		
		
		initWeights(); 
		
		try(BufferedReader bufferRead = new BufferedReader(new FileReader(inputFile))) {
			
			String newLine; 
			while((newLine=bufferRead.readLine()) != null)
				for(char character : newLine.toCharArray()) 
					if ((int)character < 128)
						weights[(int)character]++;
			
		} catch (IOException e) {
			errorAlert("Input File Error", "Error in reading the file");
			return;
		}
		
		informationAlert("File Read Successfully", "The file desired was read accurately");
	}
	
	/**
	 * Prints the weights to the console. Non-printing characters (0-31, 127) 
	 * are indicated with [ ], printing characters are displayed to help with debug
	 * 
	 */
	private void printWeights() {
		for (int i = 0; i < weights.length; i++) {
			if ((i < 32) || (i == 127))  
				System.out.println("i:"+i+" [ ] = "+weights[i]);
			else 
				System.out.println("i:"+i+" ("+(char)i+") = "+weights[i]);
		}
	}
	
	/**
	 * Write the character-based frequency data to the specified file, one index per line.
	 * Use the following format:
	 *   print the index and the frequency count separated by a comma
	 *   
	 * Assuming that there are no errors, raise an INFORMATION alert with an appropriate message
	 * to indicate to the user that the output file was created successfully. Make sure to refer to 
	 * the notes from class today for any more details. Again, the actual writing of the file might be
	 * best done in a separate helper method.
	 *   
	 * Error Handling:
	 *   if outfName is blank, raise a WARNING alert with an appropriate message and return.
	 *   if the output file exists but is not writeable, raise a WARNING alear with an appropriate message
	 *   and return.
	 *   if the output file exists and is writeable, raise a CONFIRMATION alert with an appropriate
	 *   message to the user. if they cancel the operation, return; otherwise, continue.
	 *   
	 * @param outfName the outf name
	 */
	private void saveWeightsToFile(String outfName) {
		outputFile = new File(outfName); 
		
		PrintWriter pw;
		
		//If file exists, check if user wants to overwrite
		if (outputFile.exists())
			if(!confirmationAlert("Save File Exists", "The file chosen to write exists, do you want to overwrite it?"))
				return; 
		System.out.println("Successsss");
		//If cannot write over file
		if(!outputFile.canWrite() && outputFile.exists()) {
			errorAlert("Cannot Write File", "Cannot write on inputted file");
			return;
		}
		
		
		try {
			pw = new PrintWriter(new FileWriter(outputFile));
		}
		catch (IOException e) {
			errorAlert("Output File Error", "Error in using the file");
			return; 
		} 
		
		for(int i = 0; i < weights.length; i++)
			pw.println(i + "," + weights[i]); 
		
		informationAlert("File saved", "The information has been saved"); 
		
		pw.close();

	}
	
	//TODO #4: I strongly recommend writing reuseable alerts for input errors, output
	//         errors, confirmation and information... You can supply the specific error
	//         message as a string that is passed in. Write these methods here....
	
	private void errorAlert(String errorHeader, String errorContent) {
		Alert alert = new Alert(AlertType.ERROR);
		throwAlert(alert, errorHeader, errorContent);
	}
	
	private void informationAlert(String infoHeader, String infoContent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		throwAlert(alert, infoHeader, infoContent); 
	}	
	
	private void throwAlert(Alert alert, String headerText, String contentText) {
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	
	private boolean confirmationAlert(String questionHeader, String questionContent) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		
		alert.setHeaderText(questionHeader);
		alert.setContentText(questionContent);
		
		return alert.showAndWait().get() == ButtonType.OK; 
	}
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

}
