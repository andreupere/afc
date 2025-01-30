package cat.uib.secom.crypto.afc.integration.server;

import java.io.IOException;
import java.net.Socket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.utils.net.socket.Server;

public class ServerVM extends Server {

	private GeneralConfigXML gConfig;
	
	public ServerVM(Integer port) {
		super(port);
	}

	
	public void listen() throws IOException {
		Serializer serializer = new Persister();
		
		try {
			gConfig = serializer.read(GeneralConfigXML.class, this.getClass().getResourceAsStream("/general-config.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while (_listening) {
			Socket socket = _ss.accept();
			new ServerVMThread(socket, gConfig).run();
		}
	}

	
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Requires 2 arguments: HOST PORT");
			return;
		}
		String host = args[0];
		int port = new Integer( args[1] );
		ServerVM s = new ServerVM(port);
		try {
			System.out.println("Bind to port " + port);
			s.bind();
			System.out.println("Listening...");
			s.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	

}
