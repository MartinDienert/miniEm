import java.io.*;
import java.net.*;

public class HttpRequest{
	String ip;
	int port;
	Socket socket;
	BufferedReader reader;
	PrintWriter writer;
	int fehler;
	
	public HttpRequest(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public String getResponse(String name, String req, String res, String con){
		try{
			socket = new Socket(ip, port);
			InputStream input = socket.getInputStream();
			reader = new BufferedReader(new InputStreamReader(input));
			OutputStream output = socket.getOutputStream();
			writer = new PrintWriter(new OutputStreamWriter(output));
			fehler = (socket.isConnected())? 0: 1;
			writer.printf("%s %s HTTP/1.1%n", req, res);
			writer.printf("Host: %s%n", ip);
			writer.printf("User-Agent: %s%n", name);
			writer.println("Accept: application/json");
			if(con != null){
				writer.println("Content-Type: application/json");
				writer.println("Content-Length: " + con.length());
			}
			writer.println();
			if(con != null)
				writer.println(con);
			writer.flush();
			String line;
			int pos;
			int laenge = 0;
			StringBuilder response = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.equals("")) {
					break;
				}
				pos = line.indexOf(":");
				if (pos > 0 && line.substring(0, pos).equals("Content-Length")) {
					laenge = Integer.parseInt(line.substring(pos + 1).trim());
				}
			}
			if(laenge > 0){
				char[] puffer = new char[laenge];
				laenge = reader.read(puffer, 0, laenge);
				if(laenge > 0)
					response.append(String.valueOf(puffer)).append("\n");
			}
			socket.close();
			return response.toString();
		}catch(Exception e){
			Log.err("HttpRequest.getResponse: " + e);
			return "";
		}
	}
}

