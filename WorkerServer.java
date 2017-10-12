import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class WorkerServer {
	
	/**
	 * Object State
	 */
	private static boolean is_FLW = false;
	private static boolean is_WCT = false;
	private static boolean is_UPP = false;
	private static boolean is_LOW = false;
	
	private static final int REGISTERING = 0;
    private static final int REGISTERED = 1;
    private static final int UNREGISTERED = 2;
    private static final int STOPPED = 3;

	
	static int WS_port = 59000;
	static int CS_port = 58011; 
	static String CS_name = "minho.ist.utl.pt";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			parseArgs(args);  // alter WS state (port, CS_port, CS_name)
			
		}catch (Exception e ) {
			System.out.println("Wrong Format: ");
			System.out.println("./WS PTC1 … PTCn [-p WSport] [-n CSname] [-e CSport] ");
		}
		
		
		// Open TCP socket
		ServerSocket tcpSocket;
		try {
			tcpSocket = new ServerSocket(WS_port);
		} catch (IOException e) {
			System.out.println("Unnable to bind tcp socket");
			return;
		}
		
		// Open UDP socket
		DatagramSocket udpSocket;
		try {
			udpSocket = new DatagramSocket(WS_port);
		} catch (SocketException e) {
			System.out.println("Unnable to bind udp socket");
			return;
		}
		
		// UDProtocol class helper
		/*
		 * public UDProtocol(int _WS_port, int _CS_port, String _CS_name,
			 boolean _is_FLW, boolean _is_WCT, boolean _is_UPP, boolean _is_LOW)
		 * */
		UDProtocol udp = new UDProtocol(WS_port, CS_port, CS_name, is_FLW, is_WCT, is_UPP, is_LOW);
		
		// Register WS
		try {
			String registering_response = udp.sendUDPRequest(udpSocket, REGISTERING);
			//handle CS registering_response
			UDProtocol.printUDPCSResult(registering_response);
			
		} catch (IOException e1) {
			System.out.println("CS failed to register WS due to IO error.");
			udpSocket.close();
			return;
		} catch (Exception e1) {
			System.out.println("CS failed to unregistered WS due: " + e1.getMessage());
			udpSocket.close();
			return;
		}
		
		
		// Main Cycle
		try {
			handleTCP(tcpSocket);
			
		}catch (Exception e) {
			System.out.println("WS stopped working due: " + e.getMessage());
			
			// STOP WS
			String stopped_response = "unknown CS response"; //default CS stopped response
			try {
				stopped_response = udp.sendUDPRequest(udpSocket, STOPPED);
				UDProtocol.printUDPCSResult(stopped_response);
				
			} catch (IOException e2) {
				System.out.println("CS didn't unregistered WS due to IO error.");
				udpSocket.close();
				return;
			}catch (Exception e2) {
				System.out.println("CS failed to unregistered WS due: " + e2.getMessage());
				udpSocket.close();
				return;
			}
			
			return;
		}
		
	}
	
	
	
	/**
	 * @param args
	 * Parse and save arguments
	 */
	private static void parseArgs(String[] args) {
		
	    for (int i = 0; i < args.length; i++) {
	        switch (args[i].charAt(0)) {
	        
	        	/**
	        	 *  -p  WS_port 
	        	 *  -n  CS_name
	        	 *  -e  CS_port
	        	 */
		        case '-':
		            if (args[i].length() < 2)
		                throw new IllegalArgumentException("Not a valid argument: "+args[i]);
		            
		            if (args[i].charAt(1) == 'p') {  //
		            	i = i + 1;
		             	String WS_port_string = args[i];
		            	WS_port = Integer.parseInt(WS_port_string);
		             
		            } else if (args[i].charAt(1) == 'n') { // CS_name
		            	i = i + 1;
		            	CS_name = args[i];
		            	    
		            } else if (args[i].charAt(1) == 'e') { // CS_port
		            	i = i + 1;
		             	String CS_port_string = args[i];
		            	CS_port = Integer.parseInt(CS_port_string); 

		            } else  {
		                if (args.length-1 == i)
		                    throw new IllegalArgumentException("Expected arg after: "+args[i]);
		                // -opt
		                i++;
		            }
		            break;
		            
		        default:
		            // Arg is not preceded by (-n | -p | -e)
		        	switch (args[i]) {
			            case "FLW":
			                is_FLW = true;
			                break;
			                
			            case "WCT":
			                is_WCT = true;
			                break;
			                
			            case "UPP":
			                is_UPP = true;
			                break;
			                
			            case "LOW":
			                is_LOW = true;
			                break;
			            default:
			                throw new IllegalArgumentException("Invalid argument: " + args[i]);
			        }
		            break;
		        }
		    } // end for
	    
	    
	    //Check arguments syntax
	    if(args.length == 0) {
	    	throw new IllegalArgumentException("No arguments provided. ");
	    	
		} else if(!is_FLW && !is_UPP && !is_LOW && !is_WCT) {
			throw new IllegalArgumentException("No PTC for WS provided. ");
		}
		
	}
	
	
	
	
	/**
	 * Handle CS TCP communication
	 * @throws IOException 
	 */
	
	private static void handleTCP(ServerSocket serverSocketTCP) throws IOException {
		// http://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try(
			    // Accept Incoming connection requests: CS is the client and WS the server 
			    Socket clientSocket = serverSocketTCP.accept(); // Central Server is our only client

			    BufferedReader in = new BufferedReader(
			        new InputStreamReader(clientSocket.getInputStream()));
			){ 			    
			
				String inputLine, outputLine;
		        
				// Initiate conversation with CS
				TCProtocol tcp = new TCProtocol();
				
				// read only the text message
				while ((inputLine = in.readLine()) != null) {
				    tcp.processInput(inputLine, clientSocket); // here we read the [data]
				    tcp.sendTCPResponse(clientSocket);
				}
				
			} catch (IOException e) {
				System.out.println("WS stopped due to: " + e.getMessage());
				
			}catch (Exception e3) {
				System.out.println("WS stopped due to: " + e3.getMessage());
				serverSocketTCP.close();
				return;
			}
	}
	

}
