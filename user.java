import java.net.*; 
import java.io.*; 
import java.lang.*;

public class User { 
	public static void main (String args[]) 
	{
		int serverPort = 58011;
		String ip = "tejo.ist.utl.pt";
		String firstArgument = "";
		String secondArgument = "";
		if (args.length> 0) {
			if (args[0] != null) { ip = args[1]; System.out.println(args[0]);System.out.println(ip);}
			if (args[2] != null) { serverPort = Integer.parseInt(args[3]); System.out.println(serverPort);}
			if (firstArgument.equals("-n")) {
				ip = args[1];
				System.out.println(ip);
			}
			if (firstArgument.equals("-p")) {
				serverPort = Integer.parseInt(args[3]);
				System.out.println(serverPort);
			}
		}
		while (true) {
			Socket s = null; 
			Console console = System.console();
			String data = console.readLine() + "\n";
			//data.concat("\n");
			if (data.equals("exit\n")) {
				break;
			}
			else if (data.equals("list\n")) {
				data = "LST\n";
			}
			if (data.contains("request")) {
				String [] dataSplit = data.split(" ");
				dataSplit[0] = "REQ";
				
			}
			try{ 
				//String data = "LST\n"; 
		  		s = new Socket(ip, serverPort); 
		  		DataOutputStream output = new DataOutputStream(s.getOutputStream());
		  		byte[] arrayOutput = data.getBytes();
		  		output.write(arrayOutput);
		  		DataInputStream input = new DataInputStream( s.getInputStream()); 
		  		BufferedReader buffInput = new BufferedReader(new InputStreamReader(input));
		  		StringBuffer sb = new StringBuffer();
				String st = "";
				while((st = buffInput.readLine()) != null) {
    				sb.append(st);
				}
				String dt = sb.toString();
				System.out.println(dt);
			}
			catch (UnknownHostException e){ 
				System.out.println("Sock:"+e.getMessage());}
			catch (EOFException e){
				System.out.println("EOF:"+e.getMessage()); }
			catch (IOException e){
				System.out.println("IO:"+e.getMessage());} 
			finally {
			  	if(s!=null) 
				  	try {s.close();
				  	} 
				  	catch (IOException e) {/*close failed*/}
				}
  		  }
	  }
  }
