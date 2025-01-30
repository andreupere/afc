package cat.uib.secom.crypto.afc.server.provider;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;


import cat.uib.secom.crypto.afc.xml.config.GeneralConfigXML;
import cat.uib.secom.utils.net.socket.Server;

public class SourceProviderServer extends Server {
	
	private String keyCertPassword;
	private Long stationId;
	private GeneralConfigXML gConfig;


	public SourceProviderServer(Integer port, Long stationId, String keyCertPassword) {
		super(port);
		this.stationId = stationId;
		this.keyCertPassword = keyCertPassword;
	}
	
	public void listen() throws IOException  {
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
			new SourceProviderServerThread(socket, this.stationId, gConfig).run();
			
			
		}
	}

	
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Requires 4 arguments: HOST PORT SOURCE_PROVIDER_ID KEY_PASS");
			return;
		}
		String host = args[0];
		int port = new Integer( args[1] );
		Long stationId = new Long( args[2] );
		String keyCertPassword = args[3];		                             
		SourceProviderServer s = new SourceProviderServer(port, stationId, keyCertPassword);
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
