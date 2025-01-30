package cat.uib.secom.crypto.afc.xml.config;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;



@Root(name="servers-configuration")
public class ServerListXML {
	
	@ElementList(name="server-list")
	private List<ServerConfigXML> servers;

	
	
	
	public List<ServerConfigXML> getServerList() {
		return servers;
	}

	public void setServerList(List<ServerConfigXML> serverList) {
		this.servers = serverList;
	}
	

}
