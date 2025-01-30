package cat.uib.secom.crypto.afc.system;

import java.io.File;
import java.io.IOException;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cat.uib.secom.crypto.afc.server.provider.DestinationProviderServer;
import cat.uib.secom.crypto.afc.server.provider.SourceProviderServer;
import cat.uib.secom.crypto.afc.xml.config.ServerConfigXML;
import cat.uib.secom.crypto.afc.xml.config.ServerListXML;

import junit.framework.TestCase;

public class TestDeployServers extends TestCase {
	
	public void test(){
		
		File xmlConfig = new File("/home/apaspai/Desktop/", "servers_configuration.xml");
		Serializer serializer = new Persister();
		try {
			ServerListXML serverList = serializer.read(ServerListXML.class, xmlConfig);
			for (ServerConfigXML server : serverList.getServerList()) {
				if (server.getType().equals("source")) {
					SourceProviderServer s = new SourceProviderServer(server.getPort(), new Long(server.getPort() - 40000), "" );
					
					try {
						System.out.println("Bind to port " + server.getPort());
						s.bind();
						System.out.println("Listening...");
						s.listen();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				else {
//					DestinationProviderServer s = new DestinationProviderServer(server.getPort(), new Long(server.getPort() - 50000) );
//					try {
//						System.out.println("Bind to port " + server.getPort());
//						s.bind();
//						System.out.println("Listening...");
//						s.listen();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
