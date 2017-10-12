import java.net.*;
import java.io.*;


/**
 * This class handles the TCP requests/responses.
 */

public class TCProtocol {
	
	static boolean is_FLW = false;
	static boolean is_WCT = false;
	static boolean is_UPP = false;
	static boolean is_LOW = false;

    /**
     * Process CS request. Only handles the 4 arguments, TODO possible bug
     * [data] is left to read in function storeFile(String filename, int size, Socket socket); 
     * format: WRQ PTC filename size [data]
     * @param theInput
     * @return
     * @throws Exception 
     */
    public void processInput(String theInput, Socket cs_socket) throws Exception {
        
    	System.out.println(theInput);
    	
        // TODO is it splitting data too?
        String[] splited = theInput.split("\\s+"); // remove whitespaces
        splited[3].replace("\n","");

        if(splited[0] != "WRQ") {
        	throw new Exception("Syntax error in CS request: " + theInput);
        }else if( splited.length != 5) {
        	throw new Exception("Syntax error in CS request: " + theInput);
        }
        
        String filename = splited[2];
        int size = Integer.parseInt(splited[3]);
        
        switch (splited[1]) {
	        case "FLW":
	            FLW(filename, size, cs_socket);
	            is_FLW = true;
	            break;
	            
	        case "WCT":
	            WCT(filename, size, cs_socket);
	            is_WCT = true;
	            break;
	            
	        case "UPP":
	            UPP(filename, size, cs_socket);
	            is_UPP = true;
	            break;
	            
	        case "LOW":
	            LOW(filename, size, cs_socket);
	            is_LOW = true;
	            break;
	        default:
	            throw new Exception("Syntax error in CS request: " + theInput);
	    }
        return;
    }

	private void storeFile(String filename, int size, Socket cs_socket) throws IOException {
		// read data from cs_socket via DataInputStream
		DataInputStream input = new DataInputStream( cs_socket.getInputStream()); 
		
		// TODO
		// write data to file in folder ./input_files
		
	}


	private String FLW(String filename, int size, Socket cs_socket) throws IOException {
		storeFile(filename, size, cs_socket);
		//TODO
		
		//open file descriptor of file
		//longest_word = ""
		//longest_word_count = 0;
		// while (not reached EOF)
			// get line
			// words = separate line in words (by spaces)
			// for W in words
				// if ( longest_word_count < count(W)){
				//		longest_word_count = count(W)
				//		longest_word = W
		
		// create Report File with only "longest_word" value inside
		
		return null;
	}
	
	private int WCT(String filename, int size, Socket cs_socket) throws IOException {
		storeFile(filename, size, cs_socket);
		
		//TODO
		
		//open file descriptor of file
				//word_count = 0;
				// while (not reached EOF)
					// get line
					// words = separate line in words (by spaces)
					// for W in words
						// word_count++
		
		// create Report File with only "word_count" value inside
		
		return 0;
	}

	private String UPP(String filename, int size, Socket cs_socket) throws IOException {
		storeFile(filename, size, cs_socket);
		//TODO
		
		//open file descriptor of file
				//word_count = 0;
				// while (not reached EOF)
					// get line
					// words = separate line in words (by spaces)
					// for W in words
						// W.toUpperCase();
		
		return null;
	}
	
	private String LOW(String filename, int size, Socket cs_socket) throws IOException {
		storeFile(filename, size, cs_socket);
		// TODO 
		
		//open file descriptor of file
		//word_count = 0;
		// while (not reached EOF)
			// get line
			// words = separate line in words (by spaces)
			// for W in words
				// W.toLowerCase();
		
		return null;
	}
	
	
	public static void sendTCPResponse(Socket cs_socket) throws IOException {
		// setup output streams
		PrintWriter outText =
			new PrintWriter(cs_socket.getOutputStream(), true);
		DataOutputStream outData =
			new DataOutputStream(cs_socket.getOutputStream());
		
		// write message on PrintWriter
		String message = getTCPResponseText();
		outText.println(message);
		
		//TODO
		// read file data (can be reply or processed text file)
		// write it on DataOutputStream
		
		
		return;
	}

	/**
	 * Creates the Response String 
	 * REP RT size
	 */
	public static String getTCPResponseText() throws UnknownHostException {
		String response = "REP";
		//TODO
	
		/**
		 * 
		 * The WS reply to the CS server includes the output of the file processing task
			performed. There are two possible response types (RT) depending on the task. 
			
			If RT=R the data contains the reply report in a file, and 
			if RT=F the reply is a processed text file. 
			
			The transmitted file will have size size in Bytes, 
			and the data bytes are available in data.
			
			If the WRQ request cannot be answered (e.g., invalid PTC) the reply will be
			“WRP EOF”. If the WRQ request is not correctly formulated the reply is “WRP
			ERR”
		 */
	
		
		return response;
	}
    
}
