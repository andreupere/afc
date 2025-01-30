package cat.uib.secom.crypto.afc.server.groupttp;



import java.io.File;
import java.io.IOException;
import java.net.Socket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.utils.net.socket.Server;


public class GroupTTPServer extends Server {

	private String keyCertPassword;
	private GeneralConfigXML gConfig;
	
	public GroupTTPServer(Integer port, String keyCertPassword) {
		super(port);
		this.keyCertPassword = keyCertPassword;
	}
	
	public void listen() throws IOException {
		//File fConfig = new File("general-config.xml");
		Serializer serializer = new Persister();
		
		try {
			gConfig = serializer.read(GeneralConfigXML.class, this.getClass().getResourceAsStream("/general-config.xml"));
			gConfig.setPrivateKeyPassword(keyCertPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		while (_listening) {
			Socket socket = _ss.accept();
			new GroupTTPServerThread(socket, gConfig).run();
		}
	}

	
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Requires 3 arguments: HOST PORT KEY_PASS");
			return;
		}
		String host = args[0];
		int port = new Integer( args[1] );
		String keyCertPassword = args[2];
		GroupTTPServer s = new GroupTTPServer(port, keyCertPassword);
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
