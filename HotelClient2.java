import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HotelClient2 extends Application{
	
	private Button user;
	private Button reserve;
	private Button cancel;
	private Button avail;
	private Button quit;
	private TextField nameValue;
	private TextField firstValue;
	private TextField lastValue;
	
	private static final int PORT = 1181;

	private DataInputStream in;
	private DataOutputStream out;
	private Socket s;
	private String response;
	
	
	
	
	
	@Override
	public void start(Stage primaryStage) {
		
		Pane root = new Pane();
		
		user = new Button("USER");
		reserve = new Button("RESERVE");
		cancel = new Button("CANCEL");
		avail = new Button("AVAIL");
		quit = new Button("QUIT");
		nameValue = new TextField("Name");
		firstValue = new TextField("First");
		lastValue = new TextField("Last");
		
		
		HBox userName = new HBox(user, nameValue);
		HBox reserveValues = new HBox(reserve, firstValue, lastValue);
		
		VBox allButton = new VBox(userName, reserveValues, cancel, avail, quit);
		root.getChildren().addAll(allButton);		
		
		try {
			s = new Socket("localhost", PORT);
			in = new DataInputStream(s.getInputStream());
		    out = new DataOutputStream(s.getOutputStream());
		    response = in.readUTF();
		    System.out.println("Receiving: " + response);
		    
		    ButtonEventHandler handle = new ButtonEventHandler();
		    user.setOnAction(handle);
		    reserve.setOnAction(handle);
		    cancel.setOnAction(handle);
		    avail.setOnAction(handle);
		    quit.setOnAction(handle);
		    
		    
		}
		catch(IOException e) {
			System.out.println(e.getStackTrace());
		}		
		
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Hotel Client");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	public class ButtonEventHandler implements EventHandler<ActionEvent>{
		
		@Override
		public void handle(ActionEvent e) {
			
			try(Socket s2 = s){		    
			    
				
				if(e.getSource() == user) {
					String name = nameValue.getText();
					out.writeUTF("USER");
					out.writeUTF(name);
					out.flush();
					response = in.readUTF();
				    System.out.println("Receiving: " + response);
				    
			    }
				else if(e.getSource() == reserve) {
					out.writeUTF("RESERVE");
		    		out.writeInt(Integer.parseInt(firstValue.getText()));		    		
		    		out.writeInt(Integer.parseInt(lastValue.getText()));
		    		out.flush();    
			    	System.out.println("\nReceiving: " + in.readUTF());	
			    	
				}
				else if(e.getSource() == cancel) {
					out.writeUTF("CANCEL");
		    		out.flush();	
			    	System.out.println("\nReceiving: " + in.readUTF());	
				}
				else if(e.getSource() == avail) {
					out.writeUTF("AVAIL");
		    		out.flush();		    	  
			    	System.out.println("\nReceiving: " + in.readUTF());	
				}
				else if(e.getSource() == quit) {
					out.writeUTF("QUIT");
		    		out.flush();	
			    	System.out.println("\nReceiving: " + in.readUTF());	
				}
			}
			catch(IOException c) {
				System.out.println(c.getStackTrace());
			}			
		}
		
	}
	
}

























