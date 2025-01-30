package cat.uib.secom.crypto.afc.xml.config;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;



/**
 * General application configuration
 * 
 * @author Andreu Pere
 * */
@Root(name="general-config")
public class GeneralConfigXML {
	
	
	@Element(name="certificates-path")
	private String certsPath;
	
	@Element(name="private-key-password")
	private String privateKeyPassword;
	
	@Element(name="output-xml-path")
	private String outputXmlsPath;
	
	
	@Element(name="initial-balance")
	private Double initialBalance;
	
	
	@Element(name="validity-time")
	private Integer validityTime;
	
	
	@Element(name="max-serial-number")
	private Integer maxSerialNumber;
	
	
	@Element(name="elliptic-curve-filename")
	private String elipticCurveFileName;
	
	
	@Element(name="num-deployed-keys")
	private Integer numDeployedKeys;
	
	
	@Element(name="group-ttp-port")
	private Integer gttpPort;
	
	
	@Element(name="pay-ttp-port")
	private Integer pttpPort;
	
	
	@Element(name="source-provider-base-port")
	private Integer sourceProviderBasePort;
	
	
	@Element(name="destination-provider-base-port")
	private Integer destinationProviderBasePort;
	
	
	@Element(name="source-provider-1-port")
	private Integer sourceProvider1Port;
	
	
	@Element(name="destination-provider-1-port")
	private Integer destinationProvider1Port;
	
	
	@Element(name="destination-provider-2-port")
	private Integer destinationProvider2Port;
	
	
	@Element(name="destination-provider-3-port")
	private Integer destinationProvider3Port;
	
	
	@Element(name="destination-provider-4-port")
	private Integer destinationProvider4Port;

	
	
	
	
	public Integer getGttpPort() {
		return gttpPort;
	}

	public Integer getPttpPort() {
		return pttpPort;
	}

	public Integer getSourceProvider1Port() {
		return sourceProvider1Port;
	}

	public Integer getDestinationProvider1Port() {
		return destinationProvider1Port;
	}

	public Integer getDestinationProvider2Port() {
		return destinationProvider2Port;
	}

	public Integer getDestinationProvider3Port() {
		return destinationProvider3Port;
	}

	public Integer getDestinationProvider4Port() {
		return destinationProvider4Port;
	}

	public void setGttpPort(Integer gttpPort) {
		this.gttpPort = gttpPort;
	}

	public void setPttpPort(Integer pttpPort) {
		this.pttpPort = pttpPort;
	}

	public void setSourceProvider1Port(Integer sourceProvider1Port) {
		this.sourceProvider1Port = sourceProvider1Port;
	}

	public void setDestinationProvider1Port(Integer destinationProvider1Port) {
		this.destinationProvider1Port = destinationProvider1Port;
	}

	public void setDestinationProvider2Port(Integer destinationProvider2Port) {
		this.destinationProvider2Port = destinationProvider2Port;
	}

	public void setDestinationProvider3Port(Integer destinationProvider3Port) {
		this.destinationProvider3Port = destinationProvider3Port;
	}

	public void setDestinationProvider4Port(Integer destinationProvider4Port) {
		this.destinationProvider4Port = destinationProvider4Port;
	}

	public String getCertsPath() {
		return certsPath;
	}
	
	public String getPrivateKeyPassword() {
		return privateKeyPassword;
	}

	public String getOutputXmlsPath() {
		return outputXmlsPath;
	}

	public Double getInitialBalance() {
		return initialBalance;
	}

	public Integer getValidityTime() {
		return validityTime;
	}

	public Integer getMaxSerialNumber() {
		return maxSerialNumber;
	}

	public String getElipticCurveFileName() {
		return elipticCurveFileName;
	}

	public Integer getNumDeployedKeys() {
		return numDeployedKeys;
	}

	public void setCertsPath(String certsPath) {
		this.certsPath = certsPath;
	}
	
	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}

	public void setOutputXmlsPath(String outputXmlsPath) {
		this.outputXmlsPath = outputXmlsPath;
	}

	public void setInitialBalance(Double initialBalance) {
		this.initialBalance = initialBalance;
	}

	public void setValidityTime(Integer validityTime) {
		this.validityTime = validityTime;
	}

	public void setMaxSerialNumber(Integer maxSerialNumber) {
		this.maxSerialNumber = maxSerialNumber;
	}

	public void setElipticCurveFileName(String elipticCurveFileName) {
		this.elipticCurveFileName = elipticCurveFileName;
	}

	public void setNumDeployedKeys(Integer numDeployedKeys) {
		this.numDeployedKeys = numDeployedKeys;
	}

	public Integer getSourceProviderBasePort() {
		return sourceProviderBasePort;
	}

	public Integer getDestinationProviderBasePort() {
		return destinationProviderBasePort;
	}

	public void setSourceProviderBasePort(Integer sourceProviderBasePort) {
		this.sourceProviderBasePort = sourceProviderBasePort;
	}

	public void setDestinationProviderBasePort(Integer destinationProviderBasePort) {
		this.destinationProviderBasePort = destinationProviderBasePort;
	}
	
	
	
	

}
