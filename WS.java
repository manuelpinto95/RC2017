import java.net.*; 
import java.io.*; 
import java.lang.*;
import java.net.ServerSocket;
import java.net.Socket;


public class WS {
	public static void main(String[] args) throws Exception{
		String csName = "tejo.ist.utl.pt";
		int ip = 58028;
		int csip = 58011;
		ServerSocket wsServerSocket = new ServerSocket();
		String csRequest = null;
		if (args.length> 0){
			for(int i=0; i<args.length; i++){
				if(args[i].equals("WTC") || args[i].equals("FLW") || args[i].equals("UPP") || args[i].equals("LOW")){
					System.out.println(args[i]);
				}					
				if (args[i].equals("-p")){ 
					ip = Integer.parseInt(args[i+1]);
					System.out.println(ip);
					i++;
				}
				if (args[i].equals("-n")){ 
					csName = args[i+1];
					System.out.println(csName);
					i++;
				}

				if (args[i].equals("-e")){ 
					csip = Integer.parseInt(args[i+1]);
					System.out.println(csip);
					i++;
				}
			}
		}



	/*public static void handleCSRequest(Socket wsSocket) {
    try{
      	BufferedReader wsSocketReader = null;
      	BufferedWriter wsSocketWriter = null;
      	String [] requestSplit = null;
 	 	wsSocketReader = new BufferedReader(new InputStreamReader(wsSocket.getInputStream()))
 		wsSocketWriter = new BufferedWriter(new OutputStreamWriter(wsSocket.getOutputStream()));
      	while ((csRequest = wsSocketReader.readLine()) != null) {
      		requestSplit = csRequest.split(" ");
      		for(int i=0,i<requestSplit.length(),i++){
				if (requestSplit[i] == WCT){

			 	}
				if (requestSplit[i] == FLW){

				}
				if (requestSplit[i] == UPP){

				}
				if (requestSplit[i] == LOW){

				}
      		}
      }*/
  			/*catch (UnknownHostException e){ 
				System.out.println("Sock:"+e.getMessage());}
			catch (EOFException e){
				System.out.println("EOF:"+e.getMessage()); }
			catch (IOException e){
				System.out.println("IO:"+e.getMessage());}*/ 
    }
}