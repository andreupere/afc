package cat.uib.secom.crypto.afc.server.provider.distancebased;

import java.io.IOException;
import java.net.Socket;


import cat.uib.secom.utils.net.socket.Server;

public class DestinationProviderServer extends Server {

	protected Long stationId;
	
	
	public DestinationProviderServer(Integer port, Long stationId) {
		super(port);
		this.stationId = stationId;
	}
	
	public void listen() throws IOException {
		while (_listening) {
			Socket socket = _ss.accept();
			new DestinationProviderServerThread(socket, this.stationId).run();
		}
	}

	
	public static void main(String[] args) {
		int port = 50000;
		Long stationId = new Long(2);
		DestinationProviderServer s = new DestinationProviderServer(port, stationId);
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
