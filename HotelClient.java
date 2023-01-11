import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/*
 * This class simulated the client. When the client starts it will connect to the server and ask
 * the user for their name. Then repeatedly allow the user to use the server's basic commands (reserve, cancel,
 * availability) until the user chooses to quit. The user should be shown a menu that lists their options and allows
 * them to choose any of the five commands, not type the commands in manually. Then disconnect from the
 * server.
 * @author: Aaryan Gupta
 */

public class HotelClient{
	
	public static void main(String[] args) {
		
		final int PORT = 1181;
		Scanner sc = new Scanner(System.in);
		
		try(Socket s = new Socket("localhost", PORT)){
			
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			
			String response;
			response = in.readUTF();
			System.out.println("Receiving: " +response);
			
			System.out.println("Please enter your name: ");
			String name = sc.next();
			
			out.writeUTF("USER");
			out.writeUTF(name);
			out.flush();
			
			response = in.readUTF();
			System.out.println("Receiving: " + response);
			
			int command = 1;
			
			while(command == 1 || command == 2 || command == 3 || command == 4) {
				
				System.out.println("1: Enter new user");
				System.out.println("2: Reserve");
				System.out.println("3: Cancel");
				System.out.println("4: Availability");
				System.out.println("5: Quit");
				
				command = sc.nextInt();
				
				switch(command) {
				
				case 1:
					System.out.println("Please enter your name");
					name = sc.next();
					out.writeUTF("USER");
					out.writeUTF(name);
					System.out.println("Receiving: " +in.readUTF());
					break;				
				
				
				
				case 2:
					System.out.println("Please enter your first day");
					int first = sc.nextInt();
					
					System.out.println("Please enter your last day");
					int last = sc.nextInt();
					
					out.writeUTF("RESERVE");
		    		out.writeInt(first);
		    		out.writeInt(last);
		    		out.flush();
			    	System.out.println("Receiving: " + in.readUTF());
			    	break;
			    	
				case 3:
					out.writeUTF("CANCEL");
					out.flush();
					System.out.println("Receiving: " + in.readUTF());
					break;
					
				case 4:
					out.writeUTF("AVAIL");
					out.flush();
					System.out.println("Receiving: " + in.readUTF());
					System.out.println(in.readUTF());
					
					break;
					
				case 5:
					out.writeUTF("QUIT");
					out.flush();
					System.out.println("Receiving: " + in.readUTF());
					break;			
				
					
				}
				
			}	
					
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
			
}