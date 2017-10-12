import java.net.*;
import java.io.*;


/**
 * This class handles the UDP requests/responses.
 */

public class UDProtocol {
	
	// Object state
	private static boolean is_FLW = false;
	private static boolean is_WCT = false;
	private static boolean is_UPP = false;
	private static boolean is_LOW = false;
	
	static int WS_port = 59000;
	static int CS_port = 58011; 
	static String CS_name = "minho.ist.utl.pt";
	
	// Constants
	private static final int REGISTERING = 0;
    private static final int REGISTERED = 1;
    private static final int UNREGISTERED = 2;
    private static final int STOPPED = 3;

	
	public UDProtocol(int _WS_port, int _CS_port, String _CS_name,
			 boolean _is_FLW, boolean _is_WCT, boolean _is_UPP, boolean _is_LOW) {
		
		WS_port = _WS_port;
		CS_port = _CS_port;
		CS_name = _CS_name;
		
		is_FLW = _is_FLW;
		is_WCT = _is_WCT;
		is_UPP = _is_UPP;
		is_LOW = _is_LOW;
		
	}
	
    /**
	 * 
	 * @param udpSocket
	 * @param requestType
	 * @return
	 * @throws IOException 
	 */
	// https://systembash.com/a-simple-java-udp-server-and-udp-client/
	public static String sendUDPRequest(DatagramSocket udpSocket, int requestType) throws IOException {
		
		
		BufferedReader inFromUser =
			     new BufferedReader(new InputStreamReader(System.in));
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress CS_ip = InetAddress.getByName(CS_name);
		  
		//Prepare
		byte[] sendData = new byte[1024];
		byte[] receiveData = new byte[1024];
		
		//Create request text
		String requestText = getUDPRequestText(udpSocket, requestType);
		sendData = requestText.getBytes();
		  
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, CS_ip, CS_port);
		clientSocket.send(sendPacket);
		  
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		  
		//Print and return CS response
		String response = new String(receivePacket.getData());
		System.out.println("" + response);
		
		return response;
	}

	
	
	/**
	 * Creates the Request String 
	 * 	REG PTC1 … PTCn IPWS portWS
	 *  UNR IPWS portWS
	 * @param udpSocket
	 * @param requestType
	 * @return
	 * @throws UnknownHostException
	 */
	public static String getUDPRequestText(DatagramSocket udpSocket, int requestType) throws UnknownHostException {
		String request = "";
		// Register WS in CS
		if(requestType == REGISTERING){
			request += "REG";
			
			if(is_FLW) {
				request += " FLW";
			}else if(is_WCT) {
				request += " WCT";
			}else if(is_UPP) {
				request += " UPP";
			}else if(is_LOW) {
				request += " LOW";
			}
			
			// Get IP of current computer (TODO check with ifconfig / ipconfig)
			InetAddress WS_ip = InetAddress.getByName("localhost");
			request = " " + WS_ip;
			
			String portStr = Integer.toString(WS_port);
			request = " " + portStr;
		
		// Request CS to remove WS from working servers list
		}else if (requestType == STOPPED) {
			request += "UNR";
			
			// Get IP of current computer (TODO check with ifconfig / ipconfig)
			InetAddress WS_ip = InetAddress.getByName("localhost");
			request = " " + WS_ip;
			
			String portStr = Integer.toString(WS_port);
			request = " " + portStr;
		}else {
			System.out.println("Unkown UDP request type.");
		}
		
		return request;
	}

	
	
	/*
	 * Print CS result from trying to unregister WS service
	 * */
	public static void printUDPCSResult(String response) throws Exception {
		
		//handle UDP CS response  "UAK (OK| NOK | ERR)"
		//handle UDP CS response  "RAK (OK| NOK | ERR)"
		String[] splited = response.split("\\s+"); // remove whitespaces
		splited[1].replace("\n","");
		String action = "";
		
		// First argument
		if(splited[0] == "UAK") { // Unregistering/Stopped WS messages
			action = "unregistered";
		}else if(splited[0] == "RAK") { // Registering WS messages
			action = "registered";
		}else {
			throw new Exception("CS unkown syntax response.");
		}
		
		// Second argument
		if(splited[1] != "OK") {
			System.out.println("CS successfully " + action + " WS.");
		}else if(splited[1] != "NOK") {
			throw new Exception("CS didn't " + action + " WS due to unknown error.");
		}else if(splited[1] != "ERR") {
			throw new Exception("CS didn't " + action + " WS due to syntax error.");	
		}else {
			throw new Exception("CS corrupt response.");
		}
	}    

}
