import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class EncodeDecodeGUI. This is the view for the Huffman Project.
 * This should be sufficient for you to implement the required aspects of the project.
 * You can choose to modify the layout/appearance, but you should not change the 
 * textfields, buttons or checkboxes. All error checking should be done in the controller 
 * (EncodeDecode class), but any alerts should be handled here.
 */
public class EncodeDecodeGUI extends Application {
	
	/** The pane. */
	private BorderPane pane = new BorderPane();
    
    /** The gp. */
    private GridPane gp = new GridPane();
    
    /** The btn box. */
    private HBox btnBox = new HBox(15);
    
    /** The outf. */
    private File inf,outf;
    
    /** The weights. */
    private int[] weights = new int[128]; 
    
    /** The ck optimize. */
    private CheckBox ckOptimize;
    
    /** The hf. */
    private EncodeDecode hf;
    
    /** The optimize. */
    private boolean optimize;
    
    /**
     * Instantiates a new encode decode GUI.
     */
    public EncodeDecodeGUI() {
    	hf = new EncodeDecode(this);
    	optimize = true;
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
		Label hdr = new Label("Huffman File Encode/Decode");
		hdr.setPrefHeight(40);
		Label lblInput = new Label("Text File: ");
		Label lblOutput = new Label("Compressed File: ");
		Label lblWeights = new Label("Weights File");
		TextField txtFile = new TextField();
		txtFile.setPrefWidth(300);
		TextField binFile = new TextField();
		binFile.setPrefWidth(300);
		TextField weightsFile = new TextField();
		weightsFile.setPrefWidth(300);
		gp.setVgap(15);
		gp.setHgap(2);

		gp.add(lblInput, 0, 0);
		gp.add(txtFile, 1, 0);
		gp.add(lblOutput, 0, 1);
		gp.add(binFile, 1, 1);
		gp.add(lblWeights, 0, 2);
		gp.add(weightsFile, 1, 2);
		pane.setTop(hdr);
		pane.setCenter(gp);
		Button encode = new Button("Encode");
		Button decode = new Button("Decode");
		ckOptimize = new CheckBox("Optimize Weights");
		ckOptimize.setSelected(true);
		
		encode.setOnAction(e -> hf.encode(txtFile.getText(),
				                           binFile.getText(),
				                           weightsFile.getText(),
				                           optimize));
		decode.setOnAction(e -> hf.decode(binFile.getText(),
										  txtFile.getText(),
										  weightsFile.getText(),
										  optimize));
		ckOptimize.setOnAction(e -> optimize = ckOptimize.isSelected());
		
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(encode,decode,ckOptimize);
		pane.setBottom(btnBox);
		
		Scene scene = new Scene(pane, 400,200);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public void errorAlert(String errorHeader, String errorContent) {
		Alert alert = new Alert(AlertType.ERROR);
		throwAlert(alert, errorHeader, errorContent);
	}
	
	public void informationAlert(String infoHeader, String infoContent) {
		Alert alert = new Alert(AlertType.INFORMATION);
		throwAlert(alert, infoHeader, infoContent); 
	}	
	
	private void throwAlert(Alert alert, String headerText, String contentText) {
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}
	
	
	public boolean confirmationAlert(String questionHeader, String questionContent) {
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
	
	//TODO: provide any necessary alerts to the user here...

}
