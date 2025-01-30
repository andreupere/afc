package cat.uib.secom.crypto.afc.servers;

import java.io.IOException;

import cat.uib.secom.crypto.afc.server.provider.SourceProviderServer;


import junit.framework.TestCase;

public class TestSourceProviderServer extends TestCase {

	public void test() {
		int port = 40000;
		Long stationId = new Long(1);
		SourceProviderServer s = new SourceProviderServer(port, stationId, "pere");
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
