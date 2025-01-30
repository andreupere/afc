package cat.uib.secom.crypto.afc.xml.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="server")
public class ServerConfigXML {

	@Element
	private String type;
	
	@Element
	private Integer port;
	
	@Element
	private String host;
	
	@Element
	private String identification;
	
	@Element
	private String description;

	
	
	
	public String getType() {
		return type;
	}

	public Integer getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public String getIdentification() {
		return identification;
	}

	public String getDescription() {
		return description;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
