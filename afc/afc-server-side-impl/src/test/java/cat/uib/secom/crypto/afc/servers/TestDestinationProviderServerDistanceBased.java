package cat.uib.secom.crypto.afc.servers;

import java.io.IOException;

import cat.uib.secom.crypto.afc.server.provider.distancebased.DestinationProviderServer;
import junit.framework.TestCase;

public class TestDestinationProviderServerDistanceBased extends TestCase {

	public void test() {
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
