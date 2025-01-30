package cat.uib.secom.crypto.afc.servers;

import java.io.IOException;

import cat.uib.secom.crypto.afc.server.payttp.PaymentTTPServer;
import junit.framework.TestCase;

public class TestPayTTPServer extends TestCase {

	
	public  void test() {
		int port = 30000;
		PaymentTTPServer s = new PaymentTTPServer(port, "pere");
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
