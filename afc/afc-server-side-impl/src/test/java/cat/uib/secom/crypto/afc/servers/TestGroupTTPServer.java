package cat.uib.secom.crypto.afc.servers;

import java.io.IOException;

import cat.uib.secom.crypto.afc.server.groupttp.GroupTTPServer;
import junit.framework.TestCase;

public class TestGroupTTPServer extends TestCase {
	
	
	public void test() {
		int port = 20000;
		GroupTTPServer s = new GroupTTPServer(port, "pere");
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
