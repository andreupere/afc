package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="exit-gamma-user")
public class SystemExitGammaUserXML {

	@Element
	protected String dataEncrypted; // encrypted with symmetric cipher (AES)
	
	@Element
	protected String keyIvEncrypted; // encrypted with public  key cipher (RSA)
	
	
	

	
	
	public String getDataEncrypted() {
		return dataEncrypted;
	}

	public void setDataEncrypted(String dataEncrypted) {
		this.dataEncrypted = dataEncrypted;
	}

	
	
	public String getKeyIvEncrypted() {
		return keyIvEncrypted;
	}

	public void setKeyIvEncrypted(String keyIvEncrypted) {
		this.keyIvEncrypted = keyIvEncrypted;
	}
	
}
