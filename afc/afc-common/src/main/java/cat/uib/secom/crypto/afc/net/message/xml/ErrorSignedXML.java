package cat.uib.secom.crypto.afc.net.message.xml;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ErrorSignedXML {

	@Element
	private String signature;
	
	@Element
	private ErrorXML error;
	
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}

	
	public ErrorXML getError() {
		return error;
	}

	public void setError(ErrorXML error) {
		this.error = error;
	}
	
}
