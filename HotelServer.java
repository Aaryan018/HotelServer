import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/*
 * This is a server class that follows certain protocols regarding the booking of a hotel. 
 * It In this protocol, the server always responds with a single String.
 * When a client connects the server should send a welcome message to the client.
 * The client's first request should always be to set the user, so that the server knows who is making
 * reservation. A client can reserve, cancel, check availability, set new user or quit.
 * @author: Aaryan Gupta
 */
public class HotelServer {
	
	public static final int PORT = 1181;
	private ServerSocket server;
	private static Hotel hotel = new Hotel();
	
	public static void main(String[] args) {
		(new HotelServer()).runServer();
	}
	
	/*
	 * This run server method runs whenever a client is connected. New threads are created each time 
	 * a new client is connected to the server.
	 * @param: No parameter
	 * @return: no return value.
	 */
	public void runServer() {
		
		try {
			
			server = new ServerSocket(PORT);
			System.out.println("Waiting for server to connect");
			while(true) {
				try {
					Socket s = server.accept();
					System.out.println("Client Connected");
					
					Thread t = new Thread(new ClientHandler(s));
					t.start();					
				}
				catch(IOException e) {
					e.printStackTrace();
				}				
			}			
		}
		catch(IOException e) {
			e.printStackTrace();
		}		
	}
	
	/*
	 * This inner class handles the client. It implements runnable interface. Each time a new client is connected,
	 * a new client handler is run for that client. It allows client to execute various tasks such set new user,
	 * book hotel, cancel, check availability or quit.  
	 */
	public class ClientHandler implements Runnable{
		
		private final Socket s;
		
		/*
		 * This constructor sets the initial value of the variables of the class.
		 * @param s: This is the socket  with which the clients socket is connected.
		 * @return: No return value.
		 */
		public ClientHandler(Socket s) {
			this.s = s;
		}
		
		/*
		 * This run method method is an abstract method of runnable interface which is implemented 
		 * in this class. It allows client to perform various task in relation to the hotel.
		 * If the first command of the client is not to set the user value, the connection closes and
		 * it returns.
		 * @param: No parameter.
		 * @return: no return value.
		 * 
		 */
		public void run() {
			try(Socket s2 = s){
				
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				
				out.writeUTF("Welcome Client");
				out.flush();
				
				String initialCommand1 = in.readUTF();
				
				if(!initialCommand1.equalsIgnoreCase("USER")) {
					System.out.println("Incorrect first command");
					return;
				}
				
				String name = in.readUTF();
				out.writeUTF("HELLO, " + name);
				
				while(true) {
				
					String command = in.readUTF();
					
					if(command.equals("QUIT")) {
						out.writeUTF("Closing Connection");
						out.flush();
						System.out.println("Client has quit.");
						return;
					}
					
					
					if(command.equalsIgnoreCase("USER")) {						
						name = in.readUTF();
						out.writeUTF("Hello, " + name);
						out.flush();						
					}					
					
					else if(command.equalsIgnoreCase("RESERVE")) {
						int first = in.readInt();
						int last = in.readInt();
						
						if(hotel.requestReservation(name, first, last)) {
							out.writeUTF("Reservation made: " + name + " from " + first + " through " + last);
							out.flush();
						}
						else {
							out.writeUTF("Reservation unsuccessful: " + name + " from " + first + " through " + last);
							out.flush();
						}					
					}
					else if(command.equalsIgnoreCase("CANCEL")) {
						
						if(hotel.cancelReservation(name)) {
							out.writeUTF("Reservations successfully cancelled for " + name);
							out.flush();
						}
						else {
							out.writeUTF("Reservation not cancelled for " + name + ", no current reservations");
							out.flush();
						}					
					}
					else if(command.equalsIgnoreCase("AVAIL")) {
						out.writeUTF("The full availability info");
						out.writeUTF(hotel.reservationInformation());
						out.flush();
					}
					else {
						out.writeUTF("Invalid Connection: Closing Connection");
						System.out.println("Client has given invalid command");
						return;
					}					
				}				
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}	
	}	
}






















