import java.net.*; 
import java.io.*; 
import java.lang.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;

public class User { 
	public static void main (String args[]) 
	{
		int serverPort = 58011;
		String ip = "tejo.ist.utl.pt";
		/*String firstArgument = "";
		String secondArgument = "";*/
		String [] dataSplit = null;
		String fileName = null;
		if (args.length> 0) {
			/*if (args[0] != null) {
				ip = args[1];
				System.out.println(args[0]);
				System.out.println(ip);
			}

			if (args[2] != null) {
			 	serverPort = Integer.parseInt(args[3]);
			  	System.out.println(serverPort);
		  	}*/

			if (args[0].equals("-n")) {
				ip = args[1];
				System.out.println(ip);
			}

			if (args[2].equals("-p")) {
				serverPort = Integer.parseInt(args[3]);
				System.out.println(serverPort);
			}
		}
		while (true) {
			//String fileContent = readFile("file.txt", Charset.defaultCharset());
			Socket s = null; 
			Console console = System.console();
			String data = console.readLine() + "\n";
			try{ 
				if (data.equals("exit\n")) {
					break;
				}
				//processes the list keyword
				else if (data.equals("list\n")) {
					data = "LST\n";
				}
				//processes the request keyword and its attributes
				if (data.contains("request ")) {
					dataSplit = data.split(" ");
					dataSplit[0] = "REQ";
					fileName = dataSplit[2].replace("\n","");
					System.out.println(fileName);
					String fileContent = readFile(fileName, Charset.defaultCharset());
					System.out.println(fileContent);
					data = dataSplit[0] + " " + dataSplit[1] + " " + fileContent.length() + " " + fileContent;
				}
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
				  	try {
				  		s.shutdownOutput();
				  		s.close();
				  	} 
				  	catch (IOException e) {/*close failed*/}
				}
  		  }
	  }
	  public static String readFile(String path, Charset encoding) throws IOException {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
	    	return new String(encoded, encoding);
	  }
  }
